package crypto.soft.cryptongy.feature.order;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;

import crypto.soft.cryptongy.feature.shared.json.action.Cancel;
import crypto.soft.cryptongy.feature.shared.json.openorder.OpenOrder;
import crypto.soft.cryptongy.feature.shared.json.orderhistory.OrderHistory;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.feature.shared.module.Account;
import crypto.soft.cryptongy.network.BittrexServices;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by tseringwongelgurung on 11/24/17.
 */

public class OrderInteractor {
    void getOpenOrder(final OnFinishListner<OpenOrder> listner) {
        new AsyncTask<Void, Void, OpenOrder>() {

            @Override
            protected OpenOrder doInBackground(Void... voids) {
                try {
                    Thread.sleep(2000);
                    return new BittrexServices().getOpnOrdersMock();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
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
                    Thread.sleep(2000);
                    return new BittrexServices().getOrderHistoryMock();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
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

    public void getAccount(OnFinishListner<List<Account>> listner) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<Account> list = realm.where(Account.class).findAll();
        if (list != null) {
            List<Account> data = realm.copyFromRealm(list);
            realm.commitTransaction();
            listner.onComplete(data);
        } else {
            realm.commitTransaction();
            listner.onFail("No api key available");
        }
//        Account accountDb = realm.where(Account.class).equalTo("label", "Read").findFirst();
//        if (accountDb != null) {
//            Account account = realm.copyFromRealm(accountDb);
//            realm.commitTransaction();
//            listner.onComplete(account);
//        } else {
//            accountDb = realm.where(Account.class).equalTo("label", "Trade").findFirst();
//            if (accountDb != null) {
//                Account account = realm.copyFromRealm(accountDb);
//                realm.commitTransaction();
//                listner.onComplete(account);
//            } else {
//                realm.commitTransaction();
//                listner.onFail("No api key available");
//            }
//        }
    }

    public void cancleOrder(final String uuid, final OnFinishListner<Cancel> listner) {
        new AsyncTask<Void, Void, Cancel>() {

            @Override
            protected Cancel doInBackground(Void... voids) {
                try {
                    Thread.sleep(2000);
                    return new BittrexServices().cancelOrderMock(uuid);
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
