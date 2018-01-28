package crypto.soft.cryptongy.feature.coin;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import java.util.Arrays;
import java.util.List;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.account.AccountActivity;
import crypto.soft.cryptongy.feature.account.CustomDialog;
import crypto.soft.cryptongy.feature.main.MainActivity;
import crypto.soft.cryptongy.feature.setting.SettingActivity;
import crypto.soft.cryptongy.feature.shared.json.action.Cancel;
import crypto.soft.cryptongy.feature.shared.json.markethistory.MarketHistory;
import crypto.soft.cryptongy.feature.shared.json.marketsummary.MarketSummary;
import crypto.soft.cryptongy.feature.shared.json.openorder.OpenOrder;
import crypto.soft.cryptongy.feature.shared.json.orderhistory.OrderHistory;
import crypto.soft.cryptongy.feature.shared.json.orderhistory.Result;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.feature.shared.module.Account;
import crypto.soft.cryptongy.feature.shared.ticker.TickerPresenter;
import crypto.soft.cryptongy.utils.CoinApplication;
import crypto.soft.cryptongy.utils.GlobalConstant;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by tseringwongelgurung on 11/25/17.
 */

public class CoinPresenter extends TickerPresenter<CoinView> {
    private CoinInteractor coinInteractor;
    private Context context;
    private String exchangeValue;


    public CoinPresenter(Context context) {
        super(context);
        this.context=context;
        coinInteractor = new CoinInteractor();
        exchangeValue=GlobalConstant.Exchanges.BITTREX;
    }

    public void getData(final String coinName,final String exchangeValue) {
        CoinApplication application = (CoinApplication) context.getApplicationContext();
        Account account = application.getReadAccount(exchangeValue);
        this.exchangeValue=exchangeValue;

        if (account != null) {
            if (getView() != null) {
                getView().showLoading(context.getString(R.string.fetch_msg));
                getView().setLevel(account.getLabel());
            }

            Observer observer = new Observer() {
                private int count = 4;
                OrderHistory orderhistory;
                double last = 0;
                @Override
                public void onSubscribe(Disposable d) {
                    count = 4;
                }

                @Override
                public void onNext(Object o) {
                    count--;
                    if (o instanceof OpenOrder) {
                        if (getView() != null)
                            getView().setOpenOrders((OpenOrder) o);
                    } else if (o instanceof MarketSummary) {
                        if (getView() != null)
                            getView().setMarketSummary((MarketSummary) o);
                        if( ((MarketSummary) o).getSuccess())
                            last = ((MarketSummary) o).getResult().get(0).getLast();
                    } else if (o instanceof OrderHistory) {
                        if (getView() != null) {
                            getView().setOrderHistory((OrderHistory) o);
                            orderhistory = (OrderHistory) o;
                        }
                    } else if (o instanceof MarketHistory) {
                        if (getView() != null)
                            getView().setMarketTrade((MarketHistory) o);
                    }
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onComplete() {
                    if (getView() != null) {
                        getView().hideLoading();
                        if (count == 4) {
                            getView().showEmptyView();
                        } else
                            getView().hideEmptyView();
                        calculateProfit(orderhistory, coinName, last);
                    }
                }
            };
            Observable.merge(getMarketSummary(coinName,exchangeValue), getOpenOrders(coinName,exchangeValue, account), getOrderHistory(coinName,exchangeValue, account))
                    .subscribe(observer);
        } else {
            CustomDialog.showMessagePop(context, context.getString(R.string.noAPI), null);
            if (getView() != null) {
                getView().setLevel("No API");
                getView().showEmptyView();
            }
        }
    }

//    public Observable getMarketHistory(final String coinName) {
//        return Observable.create(new ObservableOnSubscribe() {
//            @Override
//            public void subscribe(final ObservableEmitter e) throws Exception {
//                coinInteractor.getMarketHistory(coinName, new OnFinishListner<MarketHistory>() {
//                    @Override
//                    public void onComplete(MarketHistory result) {
//                        e.onNext(result);
//                        e.onComplete();
//                    }
//
//                    @Override
//                    public void onFail(String error) {
//                        CustomDialog.showMessagePop(context, error, null);
//                        e.onComplete();
//                    }
//                });
//            }
//        });
//    }

    public Observable<MarketSummary> getMarketSummary(final String coinName, final String exchangeValue) {
        return io.reactivex.Observable.create(new ObservableOnSubscribe<MarketSummary>() {
            @Override
            public void subscribe(final ObservableEmitter<MarketSummary> e) throws Exception {



                coinInteractor.getMarketSummary(coinName,exchangeValue, new OnFinishListner<MarketSummary>() {
                    @Override
                    public void onComplete(MarketSummary result) {
                        startTicker(coinName);
                        e.onNext(result);
                        e.onComplete();
                    }

                    @Override
                    public void onFail(String error) {

                        if(getView()!=null){

                            CustomDialog.showMessagePop(context, error, null);
                            e.onComplete();

                        }


                    }
                });



            }
        });
    }



    public void onOptionItemClicked(int id) {
        switch (id) {
            case R.id.ic_setting:
                context.startActivity(new Intent(context, SettingActivity.class));
                break;
        }
    }

    public void onClicked(int id, String coinName,String exchangeValue) {
        switch (id) {
            case R.id.imgSync:
                getData(coinName,exchangeValue);
                break;
            case R.id.imgAccSetting:
                if (context instanceof MainActivity)
                    ((MainActivity) context).getPresenter().replaceAccountFragment();
                else
                    context.startActivity(new Intent(context, AccountActivity.class));
                break;
        }
    }

    public Observable<OrderHistory> getOrderHistory(final String coinName, final String exchange, final Account account) {
        return Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(final ObservableEmitter e) throws Exception {
                coinInteractor.getOrderHistory(coinName,exchange, account, new OnFinishListner<OrderHistory>() {
                    @Override
                    public void onComplete(OrderHistory result) {
                        e.onNext(result);
                        e.onComplete();
                    }

                    @Override
                    public void onFail(String error) {
                        if(getView()!=null){

                            CustomDialog.showMessagePop(context, error, null);
                            e.onComplete();
                        }
                    }
                });
            }
        });
    }

    public Observable<OpenOrder> getOpenOrders(final String coinName, final String exchange, final Account account) {
        return Observable.create(new ObservableOnSubscribe<OpenOrder>() {
            @Override
            public void subscribe(final ObservableEmitter<OpenOrder> e) throws Exception {
                coinInteractor.getOpenOrder(coinName,exchange, account, new OnFinishListner<OpenOrder>() {
                    @Override
                    public void onComplete(OpenOrder result) {
                        e.onNext(result);
                        e.onComplete();
                    }

                    @Override
                    public void onFail(String error) {
                        if(getView()!=null){

                            CustomDialog.showMessagePop(context, error, null);
                            e.onComplete();
                        }
                    }
                });
            }
        });
    }

    public void cancleOrder(final String coinName, String orderUuid, final Account account) {
        if (getView() != null)
            getView().showLoading(context.getString(R.string.cancle_msg));
        coinInteractor.cancleOrder(orderUuid, coinName, account, new OnFinishListner<Cancel>() {

            @Override
            public void onComplete(Cancel result) {
                if (result.getSuccess()) {
                    if(((CoinApplication) context.getApplicationContext()).getOpenOrder() != null)
                        ((CoinApplication) context.getApplicationContext()).getOpenOrder().setChange(true);
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
                                        msg = "Cancel order is placed successfully.";
                                    CustomDialog.showMessagePop(context, msg, null);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                onFail(e.getMessage());
                            }

                            @Override
                            public void onComplete() {
                                if (getView() != null)
                                    getView().hideLoading();
                            }
                        };

                        getOpenOrders(coinName,exchangeValue, account).subscribe(observer);
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

    protected void calculateProfit(OrderHistory history, String coinName, double last) {
        if (history == null || history.getResult() == null || history.getResult().size() == 0)
            return;
        double sell = 0d, buy = 0d, sq= 0d, bq = 0d, limit = 0d;
        double calculation = 0;
        CoinApplication application = (CoinApplication) context.getApplicationContext();
        int i =0;
        for (Result data : history.getResult()) {
            if (i==0) {
                limit = data.getLimit();
                i++;
            }

            if (coinName.isEmpty() || data.getExchange().equals(coinName)) {
                if (data.getOrderType().toLowerCase().contains("sell")) {
                    if (data.getLimit() != null && data.getLimit() != 0) {
                        if (data.getQuantity() != null)
                            sell += data.getPrice();
                        sq += data.getQuantity()-data.getQuantityRemaining();

                    }
                } else if ((data.getOrderType().toLowerCase().contains("buy") )) {
                    if (data.getLimit() != null && data.getLimit() != 0) {
                        if (data.getQuantity() != null)
                            buy += data.getPrice();
                        bq += data.getQuantity()-data.getQuantityRemaining();

                    }
                }

            }

//            Log.d("Profit ", "Coin Name " + data.getExchange() + " sell " + sell + " sellq " + sq + " bq " + bq );

        }

        double currentHolding =0;
        if(bq>=sq)currentHolding = (bq-sq)*last;
//        Log.d("Profit ", "bq-sq  " +(bq-sq) + " currentHolding  " + currentHolding + " sell - buy " +  (sell - buy ));
        calculation = sell - buy + currentHolding;
        if (getView() != null) {
            getView().setCalculation(calculation);
        }
    }

    public void loadTradingView(WebView webView,String coinName,String exchangeValue)
    {


        String modifiedCoinName="LTCBTC";
        if(exchangeValue.equalsIgnoreCase(GlobalConstant.Exchanges.BITTREX)){
            List<String> stringList = Arrays.asList(coinName.split("-"));

            if(stringList!=null){
                if (stringList.size()>0){

                    modifiedCoinName=stringList.get(1)+stringList.get(0);
                }
                else {
                    modifiedCoinName=stringList.get(0);
                }
            }

        }
        else {
            modifiedCoinName=coinName;
        }



        int width=getView().displayWidth();
        int height=width*2;
        Log.d("CoinPresenter", "loadTradingView: w/h : "+width+"/"+height);
        String htmlPre = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"utf-8\"></head>" +
                "<body style='margin:0; pading:0; background-color: black;'>";
        String htmlCode = "<!-- TradingView Widget BEGIN -->\n" +
                "<script type=\"text/javascript\" src=\"https://s3.tradingview.com/tv.js\"></script>\n" +
                "<script type=\"text/javascript\">\n" +
                "new TradingView.widget({\n" +

                " \"width\": "+width+",\n"+
                "\"height\": "+height+",\n"+
             //   "  \"autosize\": true,\n" +
                "  \"symbol\": \""+exchangeValue.toUpperCase()+":"+modifiedCoinName.toUpperCase()+"\",\n" +
                "  \"interval\": \"D\",\n" +
                "  \"timezone\": \"Etc/UTC\",\n" +
                "  \"theme\": \"Light\",\n" +
                "  \"style\": \"1\",\n" +
                "  \"locale\": \"en\",\n" +
                "  \"toolbar_bg\": \"#f1f3f6\",\n" +
                "  \"enable_publishing\": false,\n" +
                "  \"hideideas\": true\n" +
                "});\n" +
                "</script>\n" +
                "<!-- TradingView Widget END -->\n";
        String htmlPost = "</body></html>";
        String baseUrl="https://www.tradingview.com/widget/advanced-chart/";

        webView.loadDataWithBaseURL(baseUrl,htmlPre+htmlCode+htmlPost, "text/html", "UTF-8", null);
        webView.setVisibility(View.VISIBLE);
    }
}
