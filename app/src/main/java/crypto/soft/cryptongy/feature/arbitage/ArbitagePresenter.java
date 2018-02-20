package crypto.soft.cryptongy.feature.arbitage;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Callable;

import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.json.market.Result;
import crypto.soft.cryptongy.network.BinanceServices;
import crypto.soft.cryptongy.network.BittrexServices;
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

public class ArbitagePresenter extends MvpBasePresenter<ArbitageView> {
    public String TAG = getClass().getSimpleName();
    private static Timer timer;
    private Context context;
    private List<Result> prevResults = new ArrayList<>();
    private List<Result> binancePrevResults = new ArrayList<>();
    private boolean isStarted = false;
    private String exchangeValue;
    Subscription subscription;


    public ArbitagePresenter(Context context) {
        this.context = context;
        isStarted = false;
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

    public Observable<MarketSummaries> getBitrixMarket() {
        Log.d(TAG, "getBitrixMarket: ");
        final BittrexServices bittrexServices = new BittrexServices();
        Observable<MarketSummaries> marketSummariesObservable = Observable.defer(new Callable<ObservableSource<MarketSummaries>>() {
            @Override
            public ObservableSource<MarketSummaries> call() throws Exception {
                return Observable.just(bittrexServices.getMarketSummaries());
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return marketSummariesObservable;
    }

    public Observable<MarketSummaries> getBinanceMarket() {

        final BinanceServices binanceServices = new BinanceServices();
        Observable<MarketSummaries> marketSummariesObservable = Observable.defer(new Callable<ObservableSource<MarketSummaries>>() {
            @Override
            public ObservableSource<MarketSummaries> call() throws Exception {
                return Observable.just(binanceServices.getMarketSummaries());
            }
        });
        return marketSummariesObservable;
    }


    public void getArbitageTableResult(final String exchangeValue) {

        Log.d(TAG, "getArbitageTableResult: ");

        if (getView() != null)
            getView().showProgressBar();




       Observable.zip(getBitrixMarket(), getBinanceMarket(), new BiFunction<MarketSummaries, MarketSummaries, List<AribitaryTableResult>>() {
            @Override
            public List<AribitaryTableResult> apply(MarketSummaries marketSummaries, MarketSummaries marketSummaries2) throws Exception {

                List<AribitaryTableResult> aribitaryTableResultArrayList = new ArrayList<>();

                Log.d(TAG, "apply: zipcalled ");

                for (Result result : marketSummaries.getResult()) {
                    String coin = result.getMarketName().split("-")[0];
                    String base = result.getMarketName().split("-")[1];

                    if (marketSummaries2.getCoinsMap().get(base.toUpperCase() + coin.toUpperCase()) != null) {
                        Result tempResult = marketSummaries2.getCoinsMap().get(base.toUpperCase() + coin.toUpperCase());
                        if (exchangeValue.equalsIgnoreCase(GlobalConstant.Exchanges.BITTREX)) {

                            aribitaryTableResultArrayList.add(new AribitaryTableResult(result.getMarketName(), String.valueOf(result.getLast()), String.valueOf(tempResult.getLast()), getPercentage(result.getLast(), tempResult.getLast())+"%"));
                        }
                        else {
                            aribitaryTableResultArrayList.add(new AribitaryTableResult(tempResult.getMarketName(), String.valueOf(result.getLast()), String.valueOf(tempResult.getLast()), getPercentage(result.getLast(), tempResult.getLast())+"%"));

                        }

                    }

                }


                return aribitaryTableResultArrayList;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
             


                .subscribe(new Consumer<List<AribitaryTableResult>>() {
                    @Override
                    public void accept(List<AribitaryTableResult> aribitaryTableResult) throws Exception {


                        Log.d(TAG, "accept: " + aribitaryTableResult);
                        Log.d(TAG, "accept: " + aribitaryTableResult.size());
                        if (getView() != null)
                        {
                            getView().hideProgressBar();
                            getView().setList(aribitaryTableResult);

                        }

                    }
                })


        ;


    }




    public void sortList(final List<AribitaryTableResult> resultList, final boolean isAscend, final int type) {


       Observable.fromCallable(new Callable<List<AribitaryTableResult>>() {
           @Override
           public List<AribitaryTableResult> call() throws Exception {
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
               return resultList;
           }
       })
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new Consumer<List<AribitaryTableResult>>() {
                   @Override
                   public void accept(List<AribitaryTableResult> aribitaryTableResults) throws Exception {
                       if (getView() != null) {
                           if (!isAscend)
                               Collections.reverse(resultList);
                           getView().setCoinInTable(resultList);
                       }
                   }
               });




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

}