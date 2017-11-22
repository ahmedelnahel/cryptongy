package crypto.soft.cryptongy.feature.account;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import crypto.soft.cryptongy.feature.account.module.Account;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.utils.GlobalUtil;

/**
 * Created by tseringwongelgurung on 11/20/17.
 */

public class AccountInteractor {
    public void getAccounts(OnFinishListner<List<Account>> listner) {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        RealmResults<Account> realmResults = realm.where(Account.class).findAll();
        List<Account> list = new ArrayList<>();
        list.addAll(realm.copyFromRealm(realmResults));

        realm.commitTransaction();
        if (list.size() > 0)
            listner.onComplete(list);
        else
            listner.onFail("No data available");
    }

    public void add(Account account, OnFinishListner<Account> listner) {
        Realm realm = Realm.getDefaultInstance();

        if (account.getId() == null)
            account.setId(GlobalUtil.getNextKey(realm, Account.class, "id"));

        account.setId(GlobalUtil.getNextKey(realm, Account.class, "id"));
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(account);
        realm.commitTransaction();

        listner.onComplete(account);
    }

    public void update(Account account, OnFinishListner<Account> listner) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(account);
        realm.commitTransaction();

        listner.onComplete(account);
    }

    public void delete(int id) {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        Account account = realm.where(Account.class).equalTo("id", id).findFirst();
        if (account != null)
            account.deleteFromRealm();

        realm.commitTransaction();
    }
}
