package crypto.soft.cryptongy.feature.trade.conditional;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import crypto.soft.cryptongy.feature.shared.json.limitorder.LimitOrder;
import crypto.soft.cryptongy.feature.shared.json.ticker.Result;
import crypto.soft.cryptongy.feature.shared.json.ticker.Ticker;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.feature.shared.module.Account;
import crypto.soft.cryptongy.feature.trade.limit.Limit;
import crypto.soft.cryptongy.network.BinanceServices;
import crypto.soft.cryptongy.network.BittrexServices;
import crypto.soft.cryptongy.utils.CoinApplication;
import crypto.soft.cryptongy.utils.GlobalConstant;
import crypto.soft.cryptongy.utils.GlobalUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by tseringwongelgurung on 12/10/17.
 */

public class ConditionalService extends IntentService {

    String TAG=getClass().getSimpleName().toString();
    String exchangeBittrix=GlobalConstant.Exchanges.BITTREX;
    String exchangeBinance=GlobalConstant.Exchanges.BINANCE;
    Disposable disposable;
    Ticker localTicker;
    BinanceServices binanceServices;

    public ConditionalService() {
        super("ConditionalService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        startService();
    }

    private void startService() {

        binanceServices=new BinanceServices();
        if(disposable!=null){
            disposable.dispose();
        }
        binanceServices.closeWebSocket();


        List<Conditional> list = getConditionals(exchangeBittrix);
        List<Conditional> listBinance = getConditionals(exchangeBinance);
        Account account = ((CoinApplication) getApplicationContext()).getTradeAccount(exchangeBittrix);
        Account accountBinance = ((CoinApplication) getApplicationContext()).getTradeAccount(exchangeBinance);

        if (account != null && list != null) {
            for (int i = 0; i < list.size(); i++) {
                Conditional conditional = list.get(i);
                Ticker ticker = getTicker(conditional.getOrderCoin(),exchangeBittrix);
                if (ticker != null && ticker.getSuccess() && ticker.getResult() != null) {
                    if (conditional.getOrderType().equalsIgnoreCase(GlobalConstant.Conditional.TYPE_BUY)) {
                        checkBuy(conditional, ticker.getResult(), account, i);
                    } else {
                        checkSell(conditional, ticker.getResult(), account, i);
                    }
                }
            }
        }


        if (accountBinance != null && listBinance != null) {
            for (int i = 0; i < listBinance.size(); i++) {
                Conditional conditional = listBinance.get(i);
                Ticker ticker = getTicker(conditional.getOrderCoin(),exchangeBinance);
                if (ticker != null && ticker.getSuccess() && ticker.getResult() != null) {
                    if (conditional.getOrderType().equalsIgnoreCase(GlobalConstant.Conditional.TYPE_BUY)) {
                        checkBuy(conditional, ticker.getResult(), accountBinance, i);
                    } else {
                        checkSell(conditional, ticker.getResult(), accountBinance, i);
                    }
                }
            }
        }




    }

    public void onDestroy() {
        super.onDestroy();

    }

    public Ticker getTicker(final String marketName,String exchangeValue) {

       localTicker=null;
        try {
            if(exchangeValue.equalsIgnoreCase(GlobalConstant.Exchanges.BITTREX)){

                Log.d(TAG, " ConditionService getTicker: bitrix ");
                localTicker= new BittrexServices().getTicker(marketName);
            }
            else {



                    binanceServices.getTickerConnectSocket(marketName);
                    disposable=      binanceServices.sourceTickerWebsocket.observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Ticker>() {
                        @Override
                        public void accept(Ticker ticker) throws Exception {

                            Log.d(TAG, "ConditionService ticker  " + ticker);

                            localTicker=ticker;
                            disposable.dispose();
                            binanceServices.closeWebSocket();

                        }
                    });




//                return new BinanceServices().getTicker(marketName);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localTicker;
    }

    public List<Conditional> getConditionals(String exchangeValue) {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        RealmResults<Conditional> conditionalsDb = realm.where(Conditional.class).equalTo("orderStatus", GlobalConstant.Conditional.TYPE_OPEN).equalTo("exchangeValue",exchangeValue).findAll();
        List<Conditional> list = new ArrayList<>();
        if (conditionalsDb != null)
            list.addAll(realm.copyFromRealm(conditionalsDb));
        realm.commitTransaction();
        return list;
    }


    private void updateConditional(Conditional conditional) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(conditional);
        realm.commitTransaction();
    }

    private void checkSell(Conditional conditional, Result ticker, Account account, final int id) {
        String market = conditional.getOrderCoin();
        Double quantity = conditional.getUnits();
        Double rate = 0.0;

            if (conditional.isHigh()) {
                if (ticker.getLast().doubleValue() >= conditional.getHighCondition().doubleValue() && conditional.getPriceType()!= null) {
                    switch (conditional.getPriceType()) {
                        case GlobalConstant.Conditional.TYPE_BID:
                            rate = ticker.getBid().doubleValue();
                            break;
                        case GlobalConstant.Conditional.TYPE_ASK:
                            rate = ticker.getAsk().doubleValue();
                            break;
                        default:
                            rate = ticker.getLast().doubleValue();
                            break;
                    }
                } else return;
            } else {
                if (conditional.getStopLossType().equalsIgnoreCase(GlobalConstant.Conditional.TYPE_TRAILER)) {
                    double low = conditional.getLast().doubleValue() - ((conditional.getLowCondition().doubleValue() / 100) * conditional.getLast().doubleValue());
                    if (ticker.getLast().doubleValue() <= low)
                        rate = ticker.getLast().doubleValue() - (ticker.getLast().doubleValue() * (conditional.getLowPrice().doubleValue() / 100));
                    else if (ticker.getLast().doubleValue() > conditional.getLast()) {
                        conditional.setLast(ticker.getLast());
                        updateConditional(conditional);
                        return;
                    } else return;
                } else {
                    double low = conditional.getLowCondition().doubleValue();
                    if (conditional.getConditionType().equalsIgnoreCase(GlobalConstant.Conditional.TYPE_PERCENTAGE))
                        low = conditional.getLast().doubleValue() - ((low / 100) * conditional.getLast().doubleValue());

                    if (ticker.getLast().doubleValue() <= low) {
                        if (conditional.getPriceType().equalsIgnoreCase(GlobalConstant.Conditional.TYPE_PERCENTAGE))
                            rate = ticker.getLast().doubleValue() - (ticker.getLast().doubleValue() * (conditional.getLowPrice().doubleValue() / 100));
                        else
                            rate = ticker.getLast().doubleValue() - conditional.getLowPrice().doubleValue();
                    } else return;

            }
            Limit limit = new Limit(market, quantity, rate, account);

            sellLimit(conditional, limit, new OnFinishListner<String>() {

                @Override
                public void onComplete(String result) {
                    GlobalUtil.showNotification(getApplicationContext(), "Sell order was placed successfully", result, GlobalUtil.getUniqueID());
                }

                @Override
                public void onFail(String error) {
                    GlobalUtil.showNotification(getApplicationContext(), "Sell order was failed", error, GlobalUtil.getUniqueID());
                }
            });
        }
    }

    private void checkBuy(final Conditional conditional, Result ticker, Account account, final int id) {
        String market = conditional.getOrderCoin();
        Double quantity = conditional.getUnits();
        Double rate = 0.0;
        if (conditional.isHigh()) {
            if (ticker.getLast().doubleValue() >= conditional.getHighCondition().doubleValue()) {
                if (conditional.getPriceType() != null) {
                    switch (conditional.getPriceType()) {
                        case GlobalConstant.Conditional.TYPE_BID:
                            rate = ticker.getBid().doubleValue();
                            break;
                        case GlobalConstant.Conditional.TYPE_ASK:
                            rate = ticker.getAsk().doubleValue();
                            break;
                        default:
                            rate = ticker.getLast().doubleValue();
                            break;
                    }
                } else
                    rate = ticker.getLast().doubleValue();
            } else return;
        } else {
            double low = conditional.getLowCondition().doubleValue();
            if (conditional.getConditionType().equalsIgnoreCase(GlobalConstant.Conditional.TYPE_PERCENTAGE))
                low = conditional.getLast().doubleValue() - ((low / 100) * conditional.getLast().doubleValue());

            if (ticker.getLast().doubleValue() <= low) {
                if (conditional.getPriceType().equalsIgnoreCase(GlobalConstant.Conditional.TYPE_PERCENTAGE))
                    rate = ticker.getLast().doubleValue() + (ticker.getLast().doubleValue() * conditional.getLowPrice().doubleValue());
                else
                    rate = ticker.getLast().doubleValue() + conditional.getLowPrice().doubleValue();
            } else return;
        }
        Limit limit = new Limit(market, quantity, rate, account);

        buyLimit(conditional, limit, new OnFinishListner<String>() {

            @Override
            public void onComplete(String result) {
                GlobalUtil.showNotification(getApplicationContext(), "Buy order was placed successfully", result, GlobalUtil.getUniqueID());
            }

            @Override
            public void onFail(String error) {
                GlobalUtil.showNotification(getApplicationContext(), "Buy order was failed", error, GlobalUtil.getUniqueID());
            }
        });
    }

    public void buyLimit(final Conditional conditional, final Limit limit, final OnFinishListner<String> listner) {
        new AsyncTask<Void, Void, LimitOrder>() {

            @Override
            protected LimitOrder doInBackground(Void... voids) {
                try {
                    if(conditional.getExchangeValue().equalsIgnoreCase(GlobalConstant.Exchanges.BITTREX)){

                        return new BittrexServices().buyLimit(limit.getMarket(), String.valueOf(limit.getQuantity()), String.valueOf(limit.getRate()), limit.getAccount());
                    }
                    else {

                        return new BinanceServices().newOrder(limit.getMarket(), String.valueOf(limit.getQuantity()), String.valueOf(limit.getRate()),"BUY", limit.getAccount());

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(LimitOrder limitOrder) {
                super.onPostExecute(limitOrder);
                if (limitOrder == null)
                    listner.onFail("Failed to fetch data");
                else if (limitOrder.getSuccess().booleanValue()) {
                    conditional.setOrderStatus(GlobalConstant.Conditional.TYPE_CLOSED);
                    updateConditional(conditional);
                    String msg = conditional.getOrderType() + " of " + conditional.getOrderCoin() + "   is placed successfully with rate " +
                            String.format("%.8f", limit.getRate().doubleValue());
                    listner.onComplete(msg);
                } else {
                    conditional.setOrderStatus(GlobalConstant.Conditional.TYPE_ERROR);
                    conditional.setError(limitOrder.getMessage());
                    updateConditional(conditional);
                    String msg = conditional.getOrderType() + " of " + conditional.getOrderCoin() + "   is failed due to " +
                            limitOrder.getMessage();
                    listner.onFail(msg);
                }
            }
        }.execute();
    }

    public void sellLimit(final Conditional conditional, final Limit limit, final OnFinishListner<String> listner) {
        new AsyncTask<Void, Void, LimitOrder>() {

            @Override
            protected LimitOrder doInBackground(Void... voids) {
                try {

                    if(conditional.getExchangeValue().equalsIgnoreCase(GlobalConstant.Exchanges.BITTREX)){

                        return new BittrexServices().sellLimit(limit.getMarket(), String.valueOf(limit.getQuantity()), String.valueOf(limit.getRate()), limit.getAccount());
                    }
                    else {

                        return new BinanceServices().newOrder(limit.getMarket(), String.valueOf(limit.getQuantity()), String.valueOf(limit.getRate()),"SELL" ,limit.getAccount());

                    }



                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(LimitOrder limitOrder) {
                super.onPostExecute(limitOrder);
                if (limitOrder == null)
                    listner.onFail("Failed to fetch data");
                else if (limitOrder.getSuccess().booleanValue()) {
                    conditional.setOrderStatus(GlobalConstant.Conditional.TYPE_CLOSED);
                    updateConditional(conditional);
                    String msg = conditional.getOrderType() + " of " + conditional.getOrderCoin() + "   is placed successfully with rate " +
                            String.format("%.8f", limit.getRate().doubleValue());
                    listner.onComplete(msg);
                } else {
                    conditional.setOrderStatus(GlobalConstant.Conditional.TYPE_ERROR);
                    conditional.setError(limitOrder.getMessage());
                    updateConditional(conditional);
                    String msg = conditional.getOrderType() + " of " + conditional.getOrderCoin() + "   is failed due to " +
                            limitOrder.getMessage();
                    listner.onFail(msg);
                }
            }
        }.execute();
    }
}