package crypto.soft.cryptongy.feature.trade;

import android.os.AsyncTask;

import java.io.IOException;

import crypto.soft.cryptongy.feature.home.HomeInteractor;
import crypto.soft.cryptongy.feature.shared.json.marketsummary.MarketSummary;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.network.BittrexServices;

/**
 * Created by tseringwongelgurung on 11/28/17.
 */

public class TradeInteractor extends HomeInteractor{
    public void getMarketSummary(final OnFinishListner<MarketSummary> listner) {

        new AsyncTask<Void, Void, MarketSummary>() {

            @Override
            protected MarketSummary doInBackground(Void... voids) {
                try {
                    Thread.sleep(2000);
                    return new BittrexServices().getMarketSummaryMock("");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(MarketSummary marketSummary) {
                super.onPostExecute(marketSummary);
                if (marketSummary == null)
                    listner.onFail("Failed to fetch data");
                else
                    listner.onComplete(marketSummary);
            }
        }.execute();
    }
}
