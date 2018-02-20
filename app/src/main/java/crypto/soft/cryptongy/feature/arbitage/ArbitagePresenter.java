package crypto.soft.cryptongy.feature.arbitage;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.setting.Notification;
import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.json.market.Result;
import crypto.soft.cryptongy.feature.shared.listner.OnMultiFinishListner;
import crypto.soft.cryptongy.network.BinanceServices;
import crypto.soft.cryptongy.network.BittrexServices;
import crypto.soft.cryptongy.utils.CoinApplication;
import crypto.soft.cryptongy.utils.GlobalConstant;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tseringwongelgurung on 11/27/17.
 */

public class ArbitagePresenter extends MvpBasePresenter<ArbitageView> implements OnMultiFinishListner<List<Result>, MarketSummaries, String> {
    public String TAG = getClass().getSimpleName();
    private static Timer timer;
    private ArbitageInteractor arbitageInteractor;
    private Context context;
    private List<Result> prevResults = new ArrayList<>();
    private List<Result> binancePrevResults = new ArrayList<>();
    private boolean isStarted = false;
    private String exchangeValue;


    public ArbitagePresenter(Context context) {
        this.arbitageInteractor = new ArbitageInteractor();
        this.context = context;
        isStarted = false;
    }

    public void loadSummaries() {
        if (getView() != null)
            getView().showProgressBar();

        exchangeValue = GlobalConstant.Exchanges.BITTREX;
        arbitageInteractor.loadSummary(context, this);
    }

    public void loadBinanceSummaries() {
        if (getView() != null)
            getView().showProgressBar();

        exchangeValue = GlobalConstant.Exchanges.BINANCE;
        arbitageInteractor.loadBinanceSummaryAPI(context, this);
    }

    @Override
    public void onComplete(List<Result> results, MarketSummaries marketSummaries, String exchange) {
        if (getView() != null) {
            //results = setDrawable(results);
            prevResults = results;
            //getView().setAdapter(results);
            getView().onSummaryDataLoad(marketSummaries, exchange);
            //isStarted = true;
//            startTimer();
        }
    }

    @Override
    public void onFail(String error) {
        if (getView() != null) {
            getView().onSummaryLoadFailed();
            getView().hideProgressBar();
        }
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            closeWebSocket();
            prevResults = null;
        }
    }

    public void startTimer(String exchangeValue) {
        this.exchangeValue = exchangeValue;
        if (timer != null) {
            timer.cancel();
            closeWebSocket();
            prevResults = null;
        }
        Notification notification = ((CoinApplication) context.getApplicationContext()).getNotification();
        if (notification.isAutomSync()) {
            Log.d(TAG, "startTimer: synValue " + notification.getSyncInterval());
            int timerInterval = notification.getSyncInterval() * 1000;
            timer = new Timer();
            timer.scheduleAtFixedRate(new TickerTimer(), timerInterval,
                    timerInterval);
        }

    }

    private List<Result> setDrawable(List<Result> list) {
        for (int i = 0; i < list.size(); i++) {
            Result result = list.get(i);
            if (prevResults == null)
                result.setDrawable(R.drawable.seek_progress);
            else if (i < prevResults.size() && result != null) {
                Log.d(TAG, "setDrawable: previous result: " + prevResults.get(i).getLast().doubleValue() + " result last value : " + result.getLast().doubleValue());
                if (result.getLast().doubleValue() < prevResults.get(i).getLast().doubleValue())
                    result.setDrawable(R.drawable.seek_progress_red);
                else if (result.getLast().doubleValue() > prevResults.get(i).getLast().doubleValue())
                    result.setDrawable(R.drawable.seek_progress_green);
            }
        }
        return list;
    }

    public void filter(final String txtSearch, final List<AribitaryTableResult> results) {

        Observable.fromCallable(new Callable<List<AribitaryTableResult>>() {
            @Override
            public List<AribitaryTableResult> call() throws Exception {

                List<AribitaryTableResult> newResultList = new ArrayList<>();
                if (txtSearch.length() > 0) {
                    for (AribitaryTableResult result : results) {
                        if (result.getCoinName().contains(txtSearch.toUpperCase())) {
                            newResultList.add(result);
                        }
                    }
                }
                return newResultList;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<AribitaryTableResult>>() {
                               @Override
                               public void accept(List<AribitaryTableResult> resultList) throws Exception {


                                   if (getView() != null && resultList != null && resultList.size() > 0) {
                                       getView().setCoinInTable(resultList);
                                   } else {
                                       getView().setCoinInTable(results);
                                   }

                               }
                           }


                )
        ;


    }

    public Observable<MarketSummaries> getBitrixMarket(){
        final BittrexServices bittrexServices = new BittrexServices();
       Observable<MarketSummaries> marketSummariesObservable= Observable.defer(new Callable<ObservableSource<MarketSummaries>>() {
            @Override
            public ObservableSource<MarketSummaries> call() throws Exception {
                return Observable.just(bittrexServices.getMarketSummaries());
            }
        });
       return marketSummariesObservable;
    }

    public Observable<MarketSummaries> getBinanceMarket(){

        final BinanceServices binanceServices = new BinanceServices();
        Observable<MarketSummaries> marketSummariesObservable= Observable.defer(new Callable<ObservableSource<MarketSummaries>>() {
            @Override
            public ObservableSource<MarketSummaries> call() throws Exception {
                return Observable.just(binanceServices.getMarketSummaries());
            }
        });
        return marketSummariesObservable;
    }


    public void getArbitageTableResult() {

        if (getView() != null)
            getView().showProgressBar();


        Observable.zip(getBitrixMarket(), getBinanceMarket(), new BiFunction<MarketSummaries, MarketSummaries,    List<AribitaryTableResult>>() {
            @Override
            public    List<AribitaryTableResult> apply(MarketSummaries marketSummaries, MarketSummaries marketSummaries2) throws Exception {

                List<AribitaryTableResult> aribitaryTableResultArrayList=new ArrayList<>();

                for(Result result: marketSummaries.getResult()){
                    String coin=result.getMarketName().split("-")[0];
                    String base=result.getMarketName().split("-")[1];

                    if(marketSummaries2.getCoinsMap().get(base.toUpperCase()+coin.toUpperCase())!=null){
                        Result tempResult=marketSummaries2.getCoinsMap().get(base.toUpperCase()+coin.toUpperCase());

                        aribitaryTableResultArrayList.add(new AribitaryTableResult(base,String.valueOf(result.getLast()),String.valueOf(tempResult.getLast()),getPercentage(result.getLast(),tempResult.getLast())));

                    }

                }


                return aribitaryTableResultArrayList;
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<   List<AribitaryTableResult>>() {
            @Override
            public void accept(   List<AribitaryTableResult> aribitaryTableResult) throws Exception {
                Log.d(TAG, "accept: "+aribitaryTableResult);
                Log.d(TAG, "accept: "+aribitaryTableResult.size());
                if (getView() != null)
                    getView().hideProgressBar();

                getView().setList(aribitaryTableResult);

            }
        });



    }


    class TickerTimer extends TimerTask {
        boolean isComplete = true;

        @Override
        public void run() {

            Notification notification = ((CoinApplication) context.getApplicationContext()).getNotification();
            if (notification.isAutomSync() && isComplete) {
                isComplete = false;
                if (exchangeValue.equalsIgnoreCase(GlobalConstant.Exchanges.BITTREX)) {
                    arbitageInteractor.loadSummary(context, new OnMultiFinishListner<List<Result>, MarketSummaries, String>() {
                        @Override
                        public void onComplete(List<Result> results, MarketSummaries summaries, String exchange) {
                            if (getView() != null) {
                                results = setDrawable(results);
                                getView().setAdapter(results);
                                getView().onSummaryDataLoad(summaries, exchange);
                                prevResults = results;
                                isComplete = true;
                            }
                        }

                        @Override
                        public void onFail(String error) {

                        }
                    });
                }
                if (exchangeValue.equalsIgnoreCase(GlobalConstant.Exchanges.BINANCE)) {
                    arbitageInteractor.loadBinanceSummary(context, new OnMultiFinishListner<List<Result>, MarketSummaries, String>() {
                        @Override
                        public void onComplete(List<Result> results, MarketSummaries summaries, String exchange) {
                            if (getView() != null) {
                                results = setDrawable(results);
                                getView().setAdapter(results);
                                getView().onSummaryDataLoad(summaries, exchange);
                                prevResults = results;
                                isComplete = true;
                            }
                        }

                        @Override
                        public void onFail(String error) {

                        }
                    });
                }
            }

        }
    }


    public void sortList(final List<AribitaryTableResult> resultList, boolean isAscend, int type) {

        if (type == 0) {
            Collections.sort(resultList, new Comparator<AribitaryTableResult>() {
                @Override
                public int compare(AribitaryTableResult t1, AribitaryTableResult t2) {
                    return t1.getCoinName().compareTo(t2.getCoinName());
                }
            });
        }

        if (type == 1) {
            Collections.sort(resultList, new Comparator<AribitaryTableResult>() {
                @Override
                public int compare(AribitaryTableResult t1, AribitaryTableResult t2) {
                    return t1.getPrice1().compareTo(t2.getPrice1());
                }
            });
        }


        if (type == 2) {
            Collections.sort(resultList, new Comparator<AribitaryTableResult>() {
                @Override
                public int compare(AribitaryTableResult t1, AribitaryTableResult t2) {
                    return t1.getPrice2().compareTo(t2.getPrice2());
                }
            });
        }

        if (type == 3) {
            Collections.sort(resultList, new Comparator<AribitaryTableResult>() {
                @Override
                public int compare(AribitaryTableResult t1, AribitaryTableResult t2) {
                    return t1.getPercentage().compareTo(t2.getPercentage());
                }
            });
        }



        if (getView() != null) {
            if (!isAscend)
                Collections.reverse(resultList);
            getView().setCoinInTable(resultList);
        }
    }


    public String getPercentage(double d, double d2) {

        String tempString;
        if (d == 0 || d2 == 0) {
            return "";
        } else {
            if (d > d2) {
                tempString = String.valueOf(((d - d2) * 100) / d2);

            } else {
                tempString = String.valueOf(((d2 - d) * 100) / d);
            }

            if (tempString.length() > 4) {
                tempString = tempString.substring(0, 4);
            }


            return tempString;
        }

    }


    public void closeWebSocket() {
        arbitageInteractor.closeWebSocket();
    }
}