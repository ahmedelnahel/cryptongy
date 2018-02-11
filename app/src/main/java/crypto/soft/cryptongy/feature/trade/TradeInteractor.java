package crypto.soft.cryptongy.feature.trade;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;

import crypto.soft.cryptongy.feature.shared.json.limitorder.LimitOrder;
import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.json.marketsummary.MarketSummary;
import crypto.soft.cryptongy.feature.shared.json.wallet.Result;
import crypto.soft.cryptongy.feature.shared.json.wallet.Wallet;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.feature.shared.module.Account;
import crypto.soft.cryptongy.feature.trade.limit.Limit;
import crypto.soft.cryptongy.network.BinanceServices;
import crypto.soft.cryptongy.network.BittrexServices;
import crypto.soft.cryptongy.utils.GlobalConstant;
import crypto.soft.cryptongy.utils.GlobalUtil;

/**
 * Created by tseringwongelgurung on 11/28/17.
 */

public class TradeInteractor {
    public String TAG=getClass().getSimpleName().toString();
    public void getMarketSummary(final String coinName, final String exchangeValue, final OnFinishListner<MarketSummary> listner) {

        new AsyncTask<Void, Void, MarketSummary>() {

            @Override
            protected MarketSummary doInBackground(Void... voids) {
                try {
                    if (exchangeValue.equalsIgnoreCase(GlobalConstant.Exchanges.BITTREX)) {

                        return new BittrexServices().getMarketSummary(coinName);
                    }
                    if (exchangeValue.equalsIgnoreCase(GlobalConstant.Exchanges.BINANCE)) {

                        return new BinanceServices().getMarketSummary(coinName);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(MarketSummary marketSummary) {
                super.onPostExecute(marketSummary);
                if (marketSummary == null)
                    listner.onFail("Failed to fetch data");
                else if (marketSummary.getSuccess().booleanValue())
                    listner.onComplete(marketSummary);
                else
                    listner.onFail(marketSummary.getMessage());
            }
        }.execute();
    }


    public void getWalletSummary(final String coin, final Account account, final OnFinishListner<Wallet> listner) {

        new AsyncTask<Void, Void, Wallet>() {

            @Override
            protected Wallet doInBackground(Void... voids) {
                try {

                    Wallet wallet = null;

                    if (account.getExchange().equalsIgnoreCase(GlobalConstant.Exchanges.BITTREX)) {
                        wallet = new BittrexServices().getWallet(account);
                    }

                    if (account.getExchange().equalsIgnoreCase(GlobalConstant.Exchanges.BINANCE)) {
                        wallet = new BinanceServices().getWallet(account);
                    }


                    if (wallet != null && wallet.getSuccess()) {
                        Iterator iterator = wallet.getResult().iterator();
                        String base="";
                        String coinName="";
                        if (account.getExchange().equalsIgnoreCase(GlobalConstant.Exchanges.BITTREX)) {
                            String[] ar = coin.split("-");
                            base = ar[0];
                            coinName = ar[1];
                        }
                        if (account.getExchange().equalsIgnoreCase(GlobalConstant.Exchanges.BINANCE)) {

                            if(coin.endsWith("USDT")){

                                base = coin.substring(coin.length() - 4, coin.length());
                                coinName = coin.substring(0, coin.length() - 4);
                            }
                            else {

                                base = coin.substring(coin.length() - 3, coin.length());
                                coinName = coin.substring(0, coin.length() - 3);
                            }
                        }


                        while (iterator.hasNext()) {
                            Result result = (Result) iterator.next();
                            if (!result.getCurrency().equalsIgnoreCase(base) && !result.getCurrency().equalsIgnoreCase(coinName))
                                iterator.remove();
                        }
                    }
                    return wallet;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Wallet wallet) {
                super.onPostExecute(wallet);
                if (wallet == null)
                    listner.onFail("Failed to fetch data");
                else if (wallet.getSuccess().booleanValue()) {
//                    if (wallet.getResult().size() < 2)
//                        listner.onFail("No coin match found");
//                    else
                    listner.onComplete(wallet);
                } else
                    listner.onFail(wallet.getMessage());
            }
        }.execute();
    }

    public void loadSummary(final OnFinishListner<MarketSummaries> listner) {
        new AsyncTask<Void, Void, MarketSummaries>() {

            @Override
            protected MarketSummaries doInBackground(Void... voids) {
                try {
                    return new BittrexServices().getMarketSummaries();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(MarketSummaries summaries) {
                super.onPostExecute(summaries);
                if (summaries == null)
                    listner.onFail("Failed to fetch data");
                else if (summaries.getSuccess().booleanValue())
                    listner.onComplete(summaries);
                else
                    listner.onFail(summaries.getMessage());
            }
        }.execute();
    }

    public void loadSummary(final String exchange, final OnFinishListner<MarketSummaries> listner) {
        new AsyncTask<Void, Void, MarketSummaries>() {

            @Override
            protected MarketSummaries doInBackground(Void... voids) {
                try {
                    if (exchange.equalsIgnoreCase(GlobalConstant.Exchanges.BITTREX)) {

                        return new BittrexServices().getMarketSummaries();
                    }
                    if (exchange.equalsIgnoreCase(GlobalConstant.Exchanges.BINANCE)) {

                        return new BinanceServices().getMarketSummaries();


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(MarketSummaries summaries) {
                super.onPostExecute(summaries);
                if (summaries == null)
                    listner.onFail("Failed to fetch data");
                else if (summaries.getSuccess().booleanValue())
                    listner.onComplete(summaries);
                else
                    listner.onFail(summaries.getMessage());
            }
        }.execute();
    }

    public void buyLimit(final String exchangeValue, final Limit limit, final OnFinishListner<LimitOrder> listner) {
        new AsyncTask<Void, Void, LimitOrder>() {

            @Override
            protected LimitOrder doInBackground(Void... voids) {
                try {
                    if(exchangeValue.equalsIgnoreCase(GlobalConstant.Exchanges.BITTREX)){

                        return new BittrexServices().buyLimit(limit.getMarket(), String.valueOf(limit.getQuantity()),  BigDecimal.valueOf(limit.getRate()).toPlainString(), limit.getAccount());

                    }
                    if(exchangeValue.equalsIgnoreCase(GlobalConstant.Exchanges.BINANCE)){
                        Log.d(TAG, "doInBackground: buy "+ BigDecimal.valueOf(limit.getRate()).toPlainString());
                        Log.d(TAG, "doInBackground: buy2 "+ limit.getRate());
                        Log.d(TAG, "doInBackground: buy3 "+ GlobalUtil.formatNumber(limit.getRate(),"#.000000"));

                        String limitRate=BigDecimal.valueOf(limit.getRate()).toPlainString();
                        return new BinanceServices().newOrder(limit.getMarket(), String.valueOf(limit.getQuantity()),limitRate,"BUY", limit.getAccount());
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
                else if (limitOrder.getSuccess().booleanValue())
                    listner.onComplete(limitOrder);
                else
                    listner.onFail(limitOrder.getMessage());
            }
        }.execute();
    }

    public void sellLimit(final String exchangeValue, final Limit limit, final OnFinishListner<LimitOrder> listner) {
        new AsyncTask<Void, Void, LimitOrder>() {

            @Override
            protected LimitOrder doInBackground(Void... voids) {
                try {
                    if(exchangeValue.equalsIgnoreCase(GlobalConstant.Exchanges.BITTREX)){

                        return new BittrexServices().sellLimit(limit.getMarket(), String.valueOf(limit.getQuantity()), String.valueOf(limit.getRate()), limit.getAccount());
                    }
                    if(exchangeValue.equalsIgnoreCase(GlobalConstant.Exchanges.BINANCE)){
                        String limitRate=BigDecimal.valueOf(limit.getRate()).toPlainString();

                        return new BinanceServices().newOrder(limit.getMarket(), String.valueOf(limit.getQuantity()),limitRate, "SELL",limit.getAccount());

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
                else if (limitOrder.getSuccess().booleanValue())
                    listner.onComplete(limitOrder);
                else
                    listner.onFail(limitOrder.getMessage());
            }
        }.execute();
    }
}
