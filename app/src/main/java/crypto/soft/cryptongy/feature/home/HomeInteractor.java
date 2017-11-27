package crypto.soft.cryptongy.feature.home;

import android.content.res.AssetManager;
import android.os.AsyncTask;

import java.io.IOException;

import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.network.BittrexServices;


/**
 * Created by Ajahar on 11/22/2017.
 */

public class HomeInteractor {

    public void loadSummary(OnFinishListner<MarketSummaries> onBitrexLoadListener) {
        new AsyncSummaryLoader(onBitrexLoadListener).execute();
    }

    class AsyncSummaryLoader extends AsyncTask<AssetManager, Void, MarketSummaries> {
        OnFinishListner onBitrexLoadListener;

        public AsyncSummaryLoader(OnFinishListner onBitrexLoadListener) {
            this.onBitrexLoadListener = onBitrexLoadListener;
        }

        @Override
        protected MarketSummaries doInBackground(AssetManager... voids) {
            try {
                return new BittrexServices().getMarketSummariesMock();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(MarketSummaries marketSummaries) {
            super.onPostExecute(marketSummaries);
            if (marketSummaries != null && marketSummaries.getSuccess()) {
                onBitrexLoadListener.onComplete(marketSummaries);
            } else {
                onBitrexLoadListener.onFail("");
            }
        }
    }
}
