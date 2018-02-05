package crypto.soft.cryptongy.feature.order;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.io.IOException;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.account.AccountActivity;
import crypto.soft.cryptongy.feature.account.CustomDialog;
import crypto.soft.cryptongy.feature.main.MainActivity;
import crypto.soft.cryptongy.feature.setting.SettingActivity;
import crypto.soft.cryptongy.feature.shared.json.action.Cancel;
import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.json.openorder.OpenOrder;
import crypto.soft.cryptongy.feature.shared.json.orderhistory.OrderHistory;
import crypto.soft.cryptongy.feature.shared.json.orderhistory.Result;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.feature.shared.module.Account;
import crypto.soft.cryptongy.network.BinanceServices;
import crypto.soft.cryptongy.network.BittrexServices;
import crypto.soft.cryptongy.utils.CoinApplication;
import crypto.soft.cryptongy.utils.GlobalConstant;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by tseringwongelgurung on 11/23/17.
 */

public class OrderPresenter extends MvpBasePresenter<OrderView> {
    protected OrderInteractor interactor;
    private Context context;
    private String exchangeValue;
    private String orderHistoryExchangeValue;
    Disposable disposable;

    public OrderPresenter(Context context) {
        this.context = context;
        interactor = new OrderInteractor();
    }

    public void onOptionItemClicked(int id) {
        switch (id) {
            case R.id.ic_setting:
                context.startActivity(new Intent(context, SettingActivity.class));
                break;
        }
    }

    public void onClicked(int id) {
        switch (id) {
            case R.id.imgSync:
                getData(exchangeValue);
                break;
            case R.id.imgAccSetting:
                if (context instanceof MainActivity)
                    ((MainActivity) context).getPresenter().replaceAccountFragment();
                else
                    context.startActivity(new Intent(context, AccountActivity.class));
                break;
        }
    }

    public void getData(String exchangeValue) {
        this.exchangeValue=exchangeValue;
        if(TextUtils.isEmpty(this.exchangeValue)){
            this.exchangeValue= GlobalConstant.Exchanges.BITTREX;
        }

        CoinApplication application = (CoinApplication) context.getApplicationContext();
        Account account = application.getReadAccount(this.exchangeValue);
        if (account != null) {
            if (getView() != null) {
                getView().resetView();
                getView().showLoading(context.getString(R.string.fetch_msg));
                getView().setLevel(account.getLabel());
            }

            Observer observer = new Observer() {
                private int count = 2;

                @Override
                public void onSubscribe(Disposable d) {
                    count = 2;
                }

                @Override
                public void onNext(Object o) {
                    count--;
                    if (o instanceof OpenOrder) {
                        if (getView() != null) {
                            getView().setOpenOrders((OpenOrder) o);
                        }
                    } else if (o instanceof OrderHistory) {
                        if (getView() != null) {
                            getView().setOrderHistory((OrderHistory) o);
                            calculateProfit((OrderHistory) o, 0);
                        }
                    }
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onComplete() {
                    if (getView() != null) {
                        getView().hideLoading();
                        if (count == 2) {
                            getView().showEmptyView();
                        } else {
                            getView().hideEmptyView();
                        }
                    }
                }
            };

            Observable.merge(getOpenOrders("",exchangeValue, account), getOrderHistory("",exchangeValue, account))
                    .subscribe(observer);
        } else {
            CustomDialog.showMessagePop(context, context.getString(R.string.noAPI), null);
            if (getView() != null) {
                getView().setLevel("No API");
                getView().showEmptyView();
            }
        }
    }



    public void getOrderHistoryData(String exchangeValue,String coinName){
        this.exchangeValue=exchangeValue;
        if(TextUtils.isEmpty(this.exchangeValue)){
            this.exchangeValue= GlobalConstant.Exchanges.BITTREX;
        }

        CoinApplication application = (CoinApplication) context.getApplicationContext();
        Account account = application.getReadAccount(this.exchangeValue);
        if (account != null) {
            if (getView() != null) {
                getView().resetView();
                getView().showLoading(context.getString(R.string.fetch_msg));
                getView().setLevel(account.getLabel());
            }

            Observer observer = new Observer() {
                private int count = 2;

                @Override
                public void onSubscribe(Disposable d) {
                    count = 2;
                }

                @Override
                public void onNext(Object o) {
                    count--;
                    if (o instanceof OrderHistory) {
                        if (getView() != null) {
                            getView().setOrderHistory((OrderHistory) o);
                            calculateProfit((OrderHistory) o, 0);
                        }
                    }
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onComplete() {
                    if (getView() != null) {
                        getView().hideLoading();
                        if (count == 2) {
                            getView().showEmptyView();
                        } else {
                            getView().hideEmptyView();
                        }
                    }
                }
            };
            getOrderHistory(coinName,exchangeValue, account).subscribe(observer);
        } else {
            CustomDialog.showMessagePop(context, context.getString(R.string.noAPI), null);
            if (getView() != null) {
                getView().setLevel("No API");
                getView().showEmptyView();
            }
        }

    }

    public Observable<OrderHistory> getOrderHistory(final String coinName,final String exchangeValue, final Account account) {
        return Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(final ObservableEmitter e) throws Exception {
                interactor.getOrderHistory(coinName,exchangeValue, account, new OnFinishListner<OrderHistory>() {
                    @Override
                    public void onComplete(OrderHistory result) {
                        e.onNext(result);
                        e.onComplete();
                    }

                    @Override
                    public void onFail(String error) {
                        CustomDialog.showMessagePop(context, error, null);
                        e.onComplete();
                    }
                });
            }
        });
    }

    public Observable<OpenOrder> getOpenOrders(final String coinName,final String exchangeValue, final Account account) {
        return Observable.create(new ObservableOnSubscribe<OpenOrder>() {
            @Override
            public void subscribe(final ObservableEmitter<OpenOrder> e) throws Exception {
                interactor.getOpenOrder(coinName,exchangeValue, account, new OnFinishListner<OpenOrder>() {
                    @Override
                    public void onComplete(OpenOrder result) {
                        e.onNext(result);
                        e.onComplete();
                    }

                    @Override
                    public void onFail(String error) {
                        CustomDialog.showMessagePop(context, error, null);
                        e.onComplete();
                    }
                });
            }
        });
    }

    public void cancleOrder(final String coinName, String orderUuid, final Account account) {
        if (getView() != null)
            getView().showLoading(context.getString(R.string.cancle_msg));
        interactor.cancleOrder(orderUuid, coinName, account, new OnFinishListner<Cancel>() {

            @Override
            public void onComplete(Cancel result) {
                if (result.getSuccess()) {
                    if (getView() != null) {
                        Observer observer = new Observer() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onNext(Object o) {
                                if (getView() != null) {
                                    getView().setOpenOrders((OpenOrder) o);
                                    getView().hideEmptyView();
                                    String msg = ((OpenOrder) o).getMessage();
                                    if (TextUtils.isEmpty(msg))
                                        msg = "Order has been cancled successfully.";
                                    CustomDialog.showMessagePop(context, msg, null);


                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                onFail(e.getMessage());
                            }

                            @Override
                            public void onComplete() {
                                if (getView() != null) {
                                    getView().hideLoading();

                                }
                                if(((CoinApplication) context.getApplicationContext()).getOpenOrder() != null)
                                    ((CoinApplication) context.getApplicationContext()).getOpenOrder().setChange(true);
                            }
                        };

                        getOpenOrders(null,account.getExchange(), account).subscribe(observer);
                    } else
                        onFail(result.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                if (getView() != null)
                    getView().hideLoading();
                CustomDialog.showMessagePop(context, error, null);
            }
        });
    }

    protected void calculateProfit(OrderHistory history, double last) {
        if (history == null || history.getResult() == null || history.getResult().size() == 0)
            return;
        double sell = 0d, buy = 0d, sq = 0d, bq = 0d;
        double calculation = 0;
        int i = 0;
        for (Result data : history.getResult()) {
            if (i == 0)
                i++;

            if (data.getOrderType().toLowerCase().equals("limit_sell") ||
                    data.getOrderType().toLowerCase().equals("conditional_sell")) {
                if (data.getLimit() != null) {
                    if (data.getQuantity() != null)
                        sell += data.getPrice();
                    sq += data.getQuantity();

                }
            } else if ((data.getOrderType().toLowerCase().equals("limit_buy") ||
                    data.getOrderType().toLowerCase().equals("conditional_buy"))) {
                if (data.getLimit() != null) {
                    if (data.getQuantity() != null)
                        buy += data.getPrice();
                    bq += data.getQuantity();

                }
            }
        }

        double currentHolding = 0;
        if (bq >= sq) currentHolding = (bq - sq) * last;
        calculation = sell - buy + currentHolding;
        if (getView() != null) {
            getView().setCalculation(calculation);
        }
    }


    public void getCoinList(String exchangeValue){
        this.orderHistoryExchangeValue=exchangeValue;

        new getCoinDetails().execute();
    }

    private class getCoinDetails extends AsyncTask<String, Void, MarketSummaries>{
        BittrexServices bittrexServices=new BittrexServices();
        BinanceServices binanceServices=new BinanceServices();
        MarketSummaries marketSummaries;

        @Override
        protected MarketSummaries doInBackground(String... strings) {

            try {
                if(orderHistoryExchangeValue.equalsIgnoreCase(GlobalConstant.Exchanges.BITTREX)){

                    marketSummaries = bittrexServices.getMarketSummaries();
                }

                if(orderHistoryExchangeValue.equalsIgnoreCase(GlobalConstant.Exchanges.BINANCE)){

                    binanceServices.getMarketSummariesWebsocket();

                    disposable = binanceServices.sourceMarketSummariesWebsocket.observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<MarketSummaries>() {

                        @Override
                        public void accept(MarketSummaries consumerMarketSummarries) throws Exception {
                            if (disposable != null) {
                                disposable.dispose();
                            }
                            binanceServices.closeWebSocket();
                            marketSummaries = consumerMarketSummarries;
                            if(marketSummaries!=null && marketSummaries.getSuccess()){
                                getView().setCoins(marketSummaries);
                            }


                        }
                    });

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return marketSummaries;
        }

        @Override
        protected void onPostExecute(MarketSummaries marketSummaries) {
            if(orderHistoryExchangeValue.equalsIgnoreCase(GlobalConstant.Exchanges.BITTREX)){

                if (marketSummaries != null && marketSummaries.getSuccess()) {
                    getView().setCoins(marketSummaries);
                }
            }



        }
    }
}