package crypto.soft.cryptongy.feature.trade.conditional;

import java.util.ArrayList;
import java.util.List;

import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.feature.trade.TradeInteractor;
import crypto.soft.cryptongy.utils.GlobalConstant;
import crypto.soft.cryptongy.utils.GlobalUtil;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by tseringwongelgurung on 12/2/17.
 */

public class ConditionalInteractor extends TradeInteractor {
    public void saveConditional(List<Conditional> conditionals, int limit, int sameLimit, OnFinishListner<Void> listner) {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        for (Conditional conditional : conditionals) {
            RealmResults<Conditional> sameConditional = realm.where(Conditional.class).equalTo("orderCoin", conditional.getOrderCoin())
                    .equalTo("orderStatus", GlobalConstant.Conditional.TYPE_OPEN).findAll();
            long count = realm.where(Conditional.class).equalTo("orderStatus", GlobalConstant.Conditional.TYPE_OPEN).count();
            if (sameConditional != null && sameConditional.size() >= sameLimit) {
                realm.commitTransaction();
                listner.onFail("Only "+sameLimit+" order per coin are permitted.");
                return;
            } else {
                if (count < limit) {
                    conditional.setId(GlobalUtil.getNextKey(realm, Conditional.class, "id"));
                    realm.copyToRealmOrUpdate(conditional);
                } else {
                    realm.commitTransaction();
                    listner.onFail("No more than " + limit + " open Orders are permitted.");
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
        RealmResults<Conditional> conditionalsDb = realm.where(Conditional.class).findAllSorted("orderStatus", Sort.DESCENDING);
        List<Conditional> list = new ArrayList<>();
        if (conditionalsDb != null)
            list.addAll(realm.copyFromRealm(conditionalsDb));
        realm.commitTransaction();

        if (list.size() == 0)
            listner.onFail("No data");
        else
            listner.onComplete(list);
    }
}
