package crypto.soft.cryptongy.feature.home;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.framing.CloseFrame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.json.market.Result;
import crypto.soft.cryptongy.feature.shared.listner.OnMultiFinishListner;
import crypto.soft.cryptongy.network.BinanceServices;
import crypto.soft.cryptongy.network.BittrexServices;
import crypto.soft.cryptongy.utils.SharedPreference;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

import static crypto.soft.cryptongy.utils.SharedPreference.IS_COIN_ADDED_BINANCE;
import static crypto.soft.cryptongy.utils.SharedPreference.MOCK_VALUE_BINANCE;


/**
 * Created by Ajahar on 11/22/2017.
 */

public class HomeInteractor {
    public static String TAG = "HomeInteractor";
    BinanceServices binanceServices = new BinanceServices();


    public void loadSummary(Context context, OnMultiFinishListner<List<Result>, MarketSummaries> onBitrexLoadListener) {
        new AsyncSummaryLoader(context, onBitrexLoadListener).execute();
    }

    class AsyncSummaryLoader extends AsyncTask<AssetManager, Void, MarketSummaries> {
        OnMultiFinishListner<List<Result>, MarketSummaries> onBitrexLoadListener;
        List<Result> results = new ArrayList<>();
        private Context context;


        public AsyncSummaryLoader(Context context, OnMultiFinishListner<List<Result>, MarketSummaries> onBitrexLoadListener) {
            this.onBitrexLoadListener = onBitrexLoadListener;
            this.context = context;
        }

        @Override
        protected MarketSummaries doInBackground(AssetManager... voids) {
            try {
                MarketSummaries marketSummaries = new BittrexServices().getMarketSummaries();

                boolean isFirst = SharedPreference.isFirst(context, "isCoinAdded");
                if (isFirst) {
                    if (marketSummaries != null && marketSummaries.getResult() != null && marketSummaries.getSuccess()) {


                        if (marketSummaries.getCoinsMap().get("BTC-LTC") != null)
                            results.add(marketSummaries.getCoinsMap().get("BTC-LTC"));
                        if (marketSummaries.getCoinsMap().get("BTC-ETH") != null)
                            results.add(marketSummaries.getCoinsMap().get("BTC-ETH"));
                        if (marketSummaries.getCoinsMap().get("BTC-VTC") != null)
                            results.add(marketSummaries.getCoinsMap().get("BTC-VTC"));
                        if (marketSummaries.getCoinsMap().get("BTC-SYS") != null)
                            results.add(marketSummaries.getCoinsMap().get("BTC-SYS"));
                        if (marketSummaries.getCoinsMap().get("BTC-XVG") != null)
                            results.add(marketSummaries.getCoinsMap().get("BTC-XVG"));


                        if (results != null && results.size() != 0) {
                            SharedPreference.saveToPrefs(context, "isCoinAdded", false);
                            SharedPreference.saveToPrefs(context, "mockValue", new Gson().toJson(results));
                        }
                    }
                } else {
                    if (!SharedPreference.getFromPrefs(context, "mockValue").equals("")) {
                        List<Result> results = new Gson().fromJson(SharedPreference.getFromPrefs(context, "mockValue"), new TypeToken<List<Result>>() {
                        }.getType());
                        if (marketSummaries != null && marketSummaries.getResult() != null && marketSummaries.getSuccess()) {
                            for (Result r : results) {
                                if (r != null) {
                                    Result ms = marketSummaries.getCoinsMap().get(r.getMarketName());
                                    if (ms != null) {

                                        r.setLast(ms.getLast());
                                        r.setVolume(ms.getVolume());
                                    }
                                }
                            }

                            if (results != null) {
                                SharedPreference.saveToPrefs(context, "mockValue", new Gson().toJson(results));
                            }
                        }
                        this.results.clear();
                        this.results.addAll(results);
                    }
                }
                return marketSummaries;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(MarketSummaries marketSummaries) {
            super.onPostExecute(marketSummaries);
            if (marketSummaries != null && marketSummaries.getSuccess() && marketSummaries.getResult() != null) {
                onBitrexLoadListener.onComplete(results, marketSummaries);
            } else {
                onBitrexLoadListener.onFail("");
            }
        }
    }



    /* Binannce */

    public void loadBinanceSummary(Context context, OnMultiFinishListner<List<Result>, MarketSummaries> onBinanceLoadListner) {
        new AsyncBinanceSummaryLoader(context, onBinanceLoadListner).execute();
    }

    class AsyncBinanceSummaryLoader extends AsyncTask<AssetManager, Void, MarketSummaries> {
        OnMultiFinishListner<List<Result>, MarketSummaries> onBinanceLoadListner;
        List<Result> results = new ArrayList<>();
        private Context context;
        MarketSummaries marketSummaries_ = null;


        public AsyncBinanceSummaryLoader(Context context, OnMultiFinishListner<List<Result>, MarketSummaries> onBinanceLoadListner) {
            this.onBinanceLoadListner = onBinanceLoadListner;
            this.context = context;
        }

        @Override
        protected MarketSummaries doInBackground(AssetManager... voids) {
            try {

                final boolean isFirst = SharedPreference.isFirst(context, IS_COIN_ADDED_BINANCE);

                binanceServices.getMarketSummariesWebsocket();
                binanceServices.sourceMarketSummariesWebsocket.observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<MarketSummaries>() {

                    @Override
                    public void accept(MarketSummaries marketSummaries) throws Exception {
                        marketSummaries_ = marketSummaries;

                        if (isFirst) {
                            if (marketSummaries != null && marketSummaries.getResult() != null && marketSummaries.getSuccess()) {

                                if (marketSummaries.getCoinsMap().get("LTCBTC") != null)
                                    results.add(marketSummaries.getCoinsMap().get("LTCBTC"));
                                if (marketSummaries.getCoinsMap().get("ETHBTC") != null)
                                    results.add(marketSummaries.getCoinsMap().get("ETHBTC"));
                                if (marketSummaries.getCoinsMap().get("APPCBTC") != null)
                                    results.add(marketSummaries.getCoinsMap().get("APPCBTC"));
                                if (marketSummaries.getCoinsMap().get("TRXBTC") != null)
                                    results.add(marketSummaries.getCoinsMap().get("TRXBTC"));
                                if (marketSummaries.getCoinsMap().get("ICXBTC") != null)
                                    results.add(marketSummaries.getCoinsMap().get("ICXBTC"));
                                if (results != null && results.size() != 0) {
                                    SharedPreference.saveToPrefs(context, IS_COIN_ADDED_BINANCE, false);
                                    SharedPreference.saveToPrefs(context, MOCK_VALUE_BINANCE, new Gson().toJson(results));
                                }
                            }

                        }


                        onBinanceLoadListner.onComplete(results, marketSummaries_);




                    }
                });


                if(marketSummaries_==null){
                    if(!isFirst){

                        if (!SharedPreference.getFromPrefs(context, MOCK_VALUE_BINANCE).equals("")) {
                            List<Result> resultsSharedPref = new Gson().fromJson(SharedPreference.getFromPrefs(context, MOCK_VALUE_BINANCE), new TypeToken<List<Result>>() {
                            }.getType());

                                marketSummaries_ = new MarketSummaries();
                                HashMap<String, Result> temp = new HashMap<>();
                                marketSummaries_.setCoinsMap(temp);
                                marketSummaries_.setSuccess(true);
                                marketSummaries_.setResult(resultsSharedPref);



                            results.clear();
                            results.addAll(resultsSharedPref);
                        }

                    }


                }

//                MarketSummaries marketSummaries12 = new BinanceServices().getMarketSummaries();
//
//                if (isFirst) {
//                    if (marketSummaries != null && marketSummaries.getResult() != null && marketSummaries.getSuccess()) {
//
//                        if (marketSummaries.getCoinsMap().get("LTCBTC") != null)
//                            results.add(marketSummaries.getCoinsMap().get("LTCBTC"));
//                        if (marketSummaries.getCoinsMap().get("ETHBTC") != null)
//                            results.add(marketSummaries.getCoinsMap().get("ETHBTC"));
//                        if (marketSummaries.getCoinsMap().get("APPCBTC") != null)
//                            results.add(marketSummaries.getCoinsMap().get("APPCBTC"));
//                        if (marketSummaries.getCoinsMap().get("TRXBTC") != null)
//                            results.add(marketSummaries.getCoinsMap().get("TRXBTC"));
//                        if (marketSummaries.getCoinsMap().get("ICXBTC") != null)
//                            results.add(marketSummaries.getCoinsMap().get("ICXBTC"));
//                        if (results != null && results.size() != 0) {
//                            SharedPreference.saveToPrefs(context, IS_COIN_ADDED_BINANCE, false);
//                            SharedPreference.saveToPrefs(context, MOCK_VALUE_BINANCE, new Gson().toJson(results));
//                        }
//                    }
//
//                } else {
//                    if (!SharedPreference.getFromPrefs(context, MOCK_VALUE_BINANCE).equals("")) {
//                        List<Result> resultsSharedPref = new Gson().fromJson(SharedPreference.getFromPrefs(context, MOCK_VALUE_BINANCE), new TypeToken<List<Result>>() {
//                        }.getType());
//                        if (marketSummaries != null && marketSummaries.getResult() != null && marketSummaries.getSuccess()) {
//                            for (Result r : resultsSharedPref) {
//                                if (r != null) {
//                                    Result ms = marketSummaries.getCoinsMap().get(r.getMarketName());
//                                    if (ms != null) {
//                                        r.setLast(ms.getLast());
//                                        r.setVolume(ms.getVolume());
//                                    }
//
//                                }
//                            }
//
//                            if (resultsSharedPref != null) {
//                                SharedPreference.saveToPrefs(context, MOCK_VALUE_BINANCE, new Gson().toJson(resultsSharedPref));
//                            }
//                        } else {
//                            marketSummaries = new MarketSummaries();
//                            HashMap<String, Result> temp = new HashMap<>();
//                            marketSummaries.setCoinsMap(temp);
//                            marketSummaries.setSuccess(false);
//                            marketSummaries.setResult(resultsSharedPref);
//                        }
//                        this.results.clear();
//                        this.results.addAll(resultsSharedPref);
//                    }
//                }
                return marketSummaries_;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return marketSummaries_;
        }


        @Override
        protected void onPostExecute(MarketSummaries marketSummaries) {
            super.onPostExecute(marketSummaries);
            onBinanceLoadListner.onComplete(results, marketSummaries);
            if (marketSummaries != null && marketSummaries.getSuccess() && marketSummaries.getResult() != null) {
//                onBinanceLoadListner.onComplete(results, marketSummaries);
            } else {
                onBinanceLoadListner.onFail("");
            }
        }
    }

    public void closeWebSocket() {
        binanceServices.sourceWebSocketClient.subscribe(new Consumer<WebSocketClient>() {
            @Override
            public void accept(WebSocketClient webSocketClient) throws Exception {

                if (webSocketClient != null) {
                    webSocketClient.closeConnection(CloseFrame.NORMAL, "its closeing time");
                }
            }
        });

    }

}
