package crypto.soft.cryptongy.utils;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by tseringwongelgurung on 11/21/17.
 */

public class CoinApplication extends Application {


    double usdt_btc = 100;

    public double getUsdt_btc()
    {
        return usdt_btc;
    }

    public void setUsdt_btc(double usdt_btc)
    {
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
