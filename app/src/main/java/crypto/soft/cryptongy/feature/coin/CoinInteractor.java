package crypto.soft.cryptongy.feature.coin;

import android.os.AsyncTask;

import java.io.IOException;

import crypto.soft.cryptongy.feature.order.OrderInteractor;
import crypto.soft.cryptongy.feature.shared.json.markethistory.MarketHistory;
import crypto.soft.cryptongy.feature.shared.json.marketsummary.MarketSummary;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.network.BinanceServices;
import crypto.soft.cryptongy.network.BittrexServices;

/**
 * Created by tseringwongelgurung on 11/25/17.
 */

public class CoinInteractor extends OrderInteractor{
    public void getMarketSummary(final String coinName, final OnFinishListner<MarketSummary> listner) {
        new AsyncTask<Void, Void, MarketSummary>() {

            @Override
            protected MarketSummary doInBackground(Void... voids) {
                try {
                    return new BinanceServices().getMarketSummary(coinName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(MarketSummary marketSummary) {
                super.onPostExecute(marketSummary);
                if (marketSummary == null || !marketSummary.getSuccess())
                    listner.onFail("Failed to fetch data");
                else if (marketSummary.getSuccess().booleanValue())
                    listner.onComplete(marketSummary);
                else
                    listner.onFail(marketSummary.getMessage());
            }
        }.execute();
    }

//    public void getMarketHistory(final String coinName, final OnFinishListner<MarketHistory> listner) {
//        new AsyncTask<Void, Void, MarketHistory>() {
//
//            @Override
//            protected MarketHistory doInBackground(Void... voids) {
//                try {
//                    return new BittrexServices().getMarketHistory(coinName);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(MarketHistory marketSummary) {
//                super.onPostExecute(marketSummary);
//                if (marketSummary == null || !marketSummary.getSuccess())
//                    listner.onFail("Failed to fetch data");
//                else if (marketSummary.getSuccess().booleanValue())
//                    listner.onComplete(marketSummary);
//                else
//                    listner.onFail(marketSummary.getMessage());
//
//            }
//        }.execute();
//    }
}
