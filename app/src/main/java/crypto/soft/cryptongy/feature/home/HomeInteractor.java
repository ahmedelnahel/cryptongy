package crypto.soft.cryptongy.feature.home;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.json.market.Result;
import crypto.soft.cryptongy.feature.shared.listner.OnMultiFinishListner;
import crypto.soft.cryptongy.network.BittrexServices;
import crypto.soft.cryptongy.utils.SharedPreference;


/**
 * Created by Ajahar on 11/22/2017.
 */

public class HomeInteractor {

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
                MarketSummaries marketSummaries = new BittrexServices().getMarketSummariesMock();
                boolean isFirst = SharedPreference.isFirst(context, "isCoinAdded");
                if (isFirst) {
                    if (marketSummaries != null && marketSummaries.getResult() != null &&  marketSummaries.getSuccess()) {

                        results.add(marketSummaries.getCoinsMap().get("USDT-BTC"));
                        results.add(marketSummaries.getCoinsMap().get("USDT-LTC"));
                        results.add(marketSummaries.getCoinsMap().get("USDT-ETH"));
                        if (results != null) {
                            SharedPreference.saveToPrefs(context, "isCoinAdded", false);
                            SharedPreference.saveToPrefs(context, "mockValue", new Gson().toJson(results));
                        }
                    }
                } else {
                    if (!SharedPreference.getFromPrefs(context, "mockValue").equals("")) {
                        List<Result> results = new Gson().fromJson(SharedPreference.getFromPrefs(context, "mockValue"), new TypeToken<List<Result>>() {
                        }.getType());
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
            if (marketSummaries != null && marketSummaries.getSuccess()) {
                onBitrexLoadListener.onComplete(results, marketSummaries);
            } else {
                onBitrexLoadListener.onFail("");
            }
        }
    }
}
