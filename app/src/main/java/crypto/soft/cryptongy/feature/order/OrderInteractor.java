package crypto.soft.cryptongy.feature.order;

import android.os.AsyncTask;

import java.io.IOException;

import crypto.soft.cryptongy.feature.shared.json.openorder.OpenOrder;
import crypto.soft.cryptongy.feature.shared.json.orderhistory.OrderHistory;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.feature.shared.module.Account;
import crypto.soft.cryptongy.network.BittrexServices;
import io.realm.Realm;

/**
 * Created by tseringwongelgurung on 11/24/17.
 */

public class OrderInteractor {
    void getOpenOrder(final OnFinishListner<OpenOrder> listner) {
        new AsyncTask<Void, Void, OpenOrder>() {

            @Override
            protected OpenOrder doInBackground(Void... voids) {
                try {
                    return new BittrexServices().getOpnOrdersMock();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
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

    void getOrderHistory(final OnFinishListner<OrderHistory> listner) {
        new AsyncTask<Void, Void, OrderHistory>() {

            @Override
            protected OrderHistory doInBackground(Void... voids) {
                try {
                    return new BittrexServices().getOrderHistoryMock();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
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

    public void getAccount(OnFinishListner<Account> listner) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Account accountDb = realm.where(Account.class).equalTo("label", "Read").findFirst();
        if (accountDb != null) {
            Account account = realm.copyFromRealm(accountDb);
            listner.onComplete(account);
            realm.commitTransaction();
        } else {
            realm.commitTransaction();
            listner.onFail("No api key available");
        }
    }
}
