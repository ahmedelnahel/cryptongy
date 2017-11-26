package crypto.soft.cryptongy.utils;

import android.app.Application;

import crypto.soft.cryptongy.common.Setting;
import crypto.soft.cryptongy.feature.shared.module.Account;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by tseringwongelgurung on 11/21/17.
 */

public class CoinApplication extends Application {
    private double usdt_btc;
    private double usdt_eth;
    private Account readAccount;
    private Account tradeAccount;
    private Account withdrawAccount;
    private Setting settings;

    public Setting getSettings() {
        return settings;
    }

    public void setSettings(Setting settings) {
        this.settings = settings;
    }

    public Account getTradeAccount() {
        return tradeAccount;
    }

    public void setTradeAccount(Account tradeAccount) {
        this.tradeAccount = tradeAccount;
    }

    public Account getReadAccount() {
        return readAccount;
    }

    public void setReadAccount(Account readAccount) {
        this.readAccount = readAccount;
    }

    public Account getWithdrawAccount() {
        return withdrawAccount;
    }

    public void setWithdrawAccount(Account withdrawAccount) {
        this.withdrawAccount = withdrawAccount;
    }

    public double getUsdt_eth() {
        return usdt_eth;
    }

    public void setUsdt_eth(double usdt_eth) {
        this.usdt_eth = usdt_eth;
    }

    public double getUsdt_btc() {
        return usdt_btc;
    }

    public void setUsdt_btc(double usdt_btc) {
        this.usdt_btc = usdt_btc;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        updateAccount();
    }

    public void updateAccount() {
        readAccount = null;
        tradeAccount = null;
        withdrawAccount = null;
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<Account> accounts = realm.where(Account.class).findAll();
        if (accounts != null) {
            for (Account account : accounts) {
                if (account.getLabel().equals("Read"))
                    setReadAccount(realm.copyFromRealm(account));
                else if (account.getLabel().equals("Trade"))
                    setTradeAccount(realm.copyFromRealm(account));
                else if (account.getLabel().equals("Withdraw"))
                    setWithdrawAccount(realm.copyFromRealm(account));
            }
        }
        realm.commitTransaction();
    }

    public Account getAccount() {
        if (getReadAccount() != null)
            return getReadAccount();
        else if (getTradeAccount() != null)
            return getTradeAccount();
        else if (getWithdrawAccount() != null)
            return getWithdrawAccount();
        return null;
    }
}
