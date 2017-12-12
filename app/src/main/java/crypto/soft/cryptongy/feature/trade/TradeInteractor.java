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
import crypto.soft.cryptongy.feature.shared.module.Account;
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
                    return new BittrexServices().getMarketSummary(coinName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(MarketSummary marketSummary) {
                super.onPostExecute(marketSummary);
                if (marketSummary == null)
                    listner.onFail("Failed to fetch data");
                else if (marketSummary.getSuccess().booleanValue())
                    listner.onComplete(marketSummary);
                else
                    listner.onFail(marketSummary.getMessage());
            }
        }.execute();
    }


    public void getWalletSummary(final String coin, final Account account, final OnFinishListner<Wallet> listner) {

        new AsyncTask<Void, Void, Wallet>() {

            @Override
            protected Wallet doInBackground(Void... voids) {
                try {
                    Wallet wallet = new BittrexServices().getWallet(account);
                    if (wallet != null && wallet.getSuccess()) {
                        Iterator iterator = wallet.getResult().iterator();
                        String[] ar = coin.split("-");
                        String base = ar[0];
                        String coinName = ar[1];
                        while (iterator.hasNext()) {
                            Result result = (Result) iterator.next();
                            if (!result.getCurrency().equalsIgnoreCase(base) && !result.getCurrency().equalsIgnoreCase(coinName))
                                iterator.remove();
                        }
                    }
                    return wallet;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Wallet wallet) {
                super.onPostExecute(wallet);
                if (wallet == null)
                    listner.onFail("Failed to fetch data");
                else if (wallet.getSuccess().booleanValue()) {
                    if (wallet.getResult().size() < 2)
                        listner.onFail("No coin match found");
                    else
                        listner.onComplete(wallet);
                } else
                    listner.onFail(wallet.getMessage());
            }
        }.execute();
    }

    public void loadSummary(final OnFinishListner<MarketSummaries> listner) {
        new AsyncTask<Void, Void, MarketSummaries>() {

            @Override
            protected MarketSummaries doInBackground(Void... voids) {
                try {
                    return new BittrexServices().getMarketSummaries();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(MarketSummaries summaries) {
                super.onPostExecute(summaries);
                if (summaries == null)
                    listner.onFail("Failed to fetch data");
                else if (summaries.getSuccess().booleanValue())
                    listner.onComplete(summaries);
                else
                    listner.onFail(summaries.getMessage());
            }
        }.execute();
    }

    public void buyLimit(final Limit limit, final OnFinishListner<LimitOrder> listner) {
        new AsyncTask<Void, Void, LimitOrder>() {

            @Override
            protected LimitOrder doInBackground(Void... voids) {
                try {
                    return new BittrexServices().buyLimit(limit.getMarket(), String.valueOf(limit.getQuantity()), String.valueOf(limit.getRate()), limit.getAccount());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(LimitOrder limitOrder) {
                super.onPostExecute(limitOrder);
                if (limitOrder == null)
                    listner.onFail("Failed to fetch data");
                else if (limitOrder.getSuccess().booleanValue())
                    listner.onComplete(limitOrder);
                else
                    listner.onFail(limitOrder.getMessage());
            }
        }.execute();
    }

    public void sellLimit(final Limit limit, final OnFinishListner<LimitOrder> listner) {
        new AsyncTask<Void, Void, LimitOrder>() {

            @Override
            protected LimitOrder doInBackground(Void... voids) {
                try {
                    return new BittrexServices().sellLimit(limit.getMarket(), String.valueOf(limit.getQuantity()), String.valueOf(limit.getRate()), limit.getAccount());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(LimitOrder limitOrder) {
                super.onPostExecute(limitOrder);
                if (limitOrder == null)
                    listner.onFail("Failed to fetch data");
                else if (limitOrder.getSuccess().booleanValue())
                    listner.onComplete(limitOrder);
                else
                    listner.onFail(limitOrder.getMessage());
            }
        }.execute();
    }
}
