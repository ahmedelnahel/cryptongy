package crypto.soft.cryptongy.feature.order;

import android.os.AsyncTask;
import android.text.TextUtils;

import java.io.IOException;
import java.util.Iterator;

import crypto.soft.cryptongy.feature.shared.json.action.Cancel;
import crypto.soft.cryptongy.feature.shared.json.openorder.OpenOrder;
import crypto.soft.cryptongy.feature.shared.json.openorder.Result;
import crypto.soft.cryptongy.feature.shared.json.orderhistory.OrderHistory;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.feature.shared.module.Account;
import crypto.soft.cryptongy.network.BittrexServiceComp;
import crypto.soft.cryptongy.network.BittrexServices;

/**
 * Created by tseringwongelgurung on 11/24/17.
 */

public class OrderInteractor {
    void getOpenOrder(final String coinName,  final Account account, final OnFinishListner<OpenOrder> listner) {
        new AsyncTask<Void, Void, OpenOrder>() {

            @Override
            protected OpenOrder doInBackground(Void... voids) {
                OpenOrder openOrder = null;
                try {
                    Thread.sleep(2000);
                    openOrder = new BittrexServices().getOpnOrders(account);
                    if (openOrder != null && openOrder.getSuccess()) {
                    if (TextUtils.isEmpty(coinName))
                        return openOrder;
                    else {

                            Iterator<Result> iterator = openOrder.getResult().iterator();
                            while (iterator.hasNext()) {
                                Result result = iterator.next();
                                if (!result.getExchange().equals(coinName))
                                    iterator.remove();
                            }
                            return openOrder;
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return openOrder;
            }

            @Override
            protected void onPostExecute(OpenOrder openOrder) {
                super.onPostExecute(openOrder);
                if (openOrder == null)
                    listner.onFail("Failed to fetch data");
                else
                    listner.onComplete(openOrder);
            }
        }.execute();
    }

    void getOrderHistory(final String coinName, final Account account, final OnFinishListner<OrderHistory> listner) {
        new AsyncTask<Void, Void, OrderHistory>() {

            @Override
            protected OrderHistory doInBackground(Void... voids) {
                OrderHistory orderHistory = null;
                try {
                    orderHistory = new BittrexServices().getOrderHistory(account);
                    if (orderHistory != null && orderHistory.getSuccess()) {
                    if (TextUtils.isEmpty(coinName))
                        return orderHistory;
                    else {

                            Iterator<crypto.soft.cryptongy.feature.shared.json.orderhistory.Result> iterator = orderHistory.getResult().iterator();
                            while (iterator.hasNext()) {
                                crypto.soft.cryptongy.feature.shared.json.orderhistory.Result result = iterator.next();
                                if (!result.getExchange().equals(coinName))
                                    iterator.remove();
                            }
                            return orderHistory;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return orderHistory;
            }

            @Override
            protected void onPostExecute(OrderHistory orderHistory) {
                super.onPostExecute(orderHistory);
                if (orderHistory == null)
                    listner.onFail("Failed to fetch data");
                else
                    listner.onComplete(orderHistory);
            }
        }.execute();
    }

    public void cancleOrder(final String uuid, final Account account, final OnFinishListner<Cancel> listner) {
        new AsyncTask<Void, Void, Cancel>() {

            @Override
            protected Cancel doInBackground(Void... voids) {
                try {
                    Thread.sleep(2000);
                    return new BittrexServices().cancelOrder(uuid, account);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Cancel cancel) {
                super.onPostExecute(cancel);
                if (cancel == null)
                    listner.onFail("Failed to cancle data");
                else
                    listner.onComplete(cancel);
            }
        }.execute();
    }
}
