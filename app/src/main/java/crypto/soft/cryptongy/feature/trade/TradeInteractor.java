package crypto.soft.cryptongy.feature.trade;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.Iterator;

import crypto.soft.cryptongy.feature.shared.json.limitorder.LimitOrder;
import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.json.marketsummary.MarketSummary;
import crypto.soft.cryptongy.feature.shared.json.wallet.Result;
import crypto.soft.cryptongy.feature.shared.json.wallet.Wallet;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.feature.trade.limit.Limit;
import crypto.soft.cryptongy.network.BittrexServices;

/**
 * Created by tseringwongelgurung on 11/28/17.
 */

public class TradeInteractor {
    public void getMarketSummary(final String coinName, final OnFinishListner<MarketSummary> listner) {

        new AsyncTask<Void, Void, MarketSummary>() {

            @Override
            protected MarketSummary doInBackground(Void... voids) {
                try {
                    Thread.sleep(2000);
                    return new BittrexServices().getMarketSummaryMock(coinName);
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


    public void getWalletSummary(final String coin, final OnFinishListner<Wallet> listner) {

        new AsyncTask<Void, Void, Wallet>() {

            @Override
            protected Wallet doInBackground(Void... voids) {
                try {
                    Thread.sleep(2000);
                    Wallet wallet=new BittrexServices().getWalletMock();
                    Iterator iterator=wallet.getResult().iterator();
                    while (iterator.hasNext()){
                        Result result= (Result) iterator.next();
                        if (!result.getCurrency().equalsIgnoreCase(coin))
                            iterator.remove();
                    }
                    return wallet;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Wallet wallet) {
                super.onPostExecute(wallet);
                if (wallet == null)
                    listner.onFail("Failed to fetch data");
                else
                    listner.onComplete(wallet);
            }
        }.execute();
    }

    public void loadSummary(final OnFinishListner<MarketSummaries> listner) {
        new AsyncTask<Void, Void, MarketSummaries>() {

            @Override
            protected MarketSummaries doInBackground(Void... voids) {
                try {
                    Thread.sleep(2000);
                    return new BittrexServices().getMarketSummariesMock();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(MarketSummaries summaries) {
                super.onPostExecute(summaries);
                if (summaries == null)
                    listner.onFail("Failed to fetch data");
                else
                    listner.onComplete(summaries);
            }
        }.execute();
    }

    public void buyLimit(final Limit limit, final OnFinishListner<LimitOrder> listner) {
        new AsyncTask<Void, Void, LimitOrder>() {

            @Override
            protected LimitOrder doInBackground(Void... voids) {
                try {
                    Thread.sleep(2000);
                    return new BittrexServices().buyLimit(limit.getMarket(),limit.getQuantity(),limit.getRate(),limit.getAccount());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(LimitOrder limitOrder) {
                super.onPostExecute(limitOrder);
                if (limitOrder == null)
                    listner.onFail("Failed to fetch data");
                else
                    listner.onComplete(limitOrder);
            }
        }.execute();
    }
    public void sellLimit(final Limit limit, final OnFinishListner<LimitOrder> listner) {
        new AsyncTask<Void, Void, LimitOrder>() {

            @Override
            protected LimitOrder doInBackground(Void... voids) {
                try {
                    Thread.sleep(2000);
                    return new BittrexServices().sellLimit(limit.getMarket(),limit.getQuantity(),limit.getRate(),limit.getAccount());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(LimitOrder limitOrder) {
                super.onPostExecute(limitOrder);
                if (limitOrder == null)
                    listner.onFail("Failed to fetch data");
                else
                    listner.onComplete(limitOrder);
            }
        }.execute();
    }
}
