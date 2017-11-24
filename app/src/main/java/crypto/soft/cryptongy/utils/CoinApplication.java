package crypto.soft.cryptongy.utils;

import android.app.Application;

import crypto.soft.cryptongy.common.Setting;
import crypto.soft.cryptongy.feature.shared.module.Account;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by tseringwongelgurung on 11/21/17.
 */

public class CoinApplication extends Application {
    double usdt_btc;
    double usdt_eth;
    Account readAccount;
    Account tradeAccount;
    Account withdrawAccount;
    Setting settings;

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
    }
}
