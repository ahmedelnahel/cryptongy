package crypto.soft.cryptongy.feature.trade.conditional;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import crypto.soft.cryptongy.feature.shared.json.ticker.Ticker;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.feature.trade.TradeInteractor;
import crypto.soft.cryptongy.network.BittrexServices;
import crypto.soft.cryptongy.utils.GlobalConstant;
import crypto.soft.cryptongy.utils.GlobalUtil;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by tseringwongelgurung on 12/2/17.
 */

public class ConditionalInteractor extends TradeInteractor {
    public void saveConditional(List<Conditional> conditionals, int limit, OnFinishListner<Void> listner) {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        for (Conditional conditional : conditionals) {
            Conditional sameConditional = realm.where(Conditional.class).equalTo("orderCoin", conditional.getOrderCoin()).findFirst();
            long count = realm.where(Conditional.class).equalTo("orderStatus", GlobalConstant.Conditional.TYPE_OPEN).count();
            if (sameConditional != null) {
                realm.commitTransaction();
                listner.onFail("Only one order per coin are permitted.");
                return;
            } else {
                if (count < limit) {
                    conditional.setId(GlobalUtil.getNextKey(realm, Conditional.class, "id"));
                    realm.copyToRealmOrUpdate(conditional);
                } else {
                    realm.commitTransaction();
                    listner.onFail("No more " + limit + " open Orders are permitted.");
                    return;
                }
            }
        }
        realm.commitTransaction();
        listner.onComplete(null);
    }

    public void deleletConditional(int id) {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        Conditional conditional = realm.where(Conditional.class).equalTo("id", id).findFirst();
        if (conditional != null)
            conditional.deleteFromRealm();
        realm.commitTransaction();
    }

    public void getConditionals(OnFinishListner<List<Conditional>> listner) {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        RealmResults<Conditional> conditionalsDb = realm.where(Conditional.class).findAll();
        List<Conditional> list = new ArrayList<>();
        if (conditionalsDb != null)
            list.addAll(realm.copyFromRealm(conditionalsDb));
        realm.commitTransaction();

        if (list.size() == 0)
            listner.onFail("No data");
        else
            listner.onComplete(list);
    }

    public void getTicker(final String marketName, final OnFinishListner<Ticker> listner) {
        new AsyncTask<Void, Void, Ticker>() {

            @Override
            protected Ticker doInBackground(Void... voids) {
                try {
                    Thread.sleep(2000);
                    return new BittrexServices().getTicker(marketName);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Ticker ticker) {
                super.onPostExecute(ticker);
                if (ticker == null || ticker.getResult() != null)
                    listner.onFail("Failed to fetch data");
                else {
                    if (ticker.getSuccess())
                        listner.onComplete(ticker);
                    else
                        listner.onFail(ticker.getMessage());
                }
            }
        }.execute();
    }
}
