package crypto.soft.cryptongy.utils;

import android.app.Application;
import android.text.TextUtils;

import java.util.List;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.alert.broadCastTicker;
import crypto.soft.cryptongy.feature.order.OrderReceiver;
import crypto.soft.cryptongy.feature.setting.Notification;
import crypto.soft.cryptongy.feature.shared.json.market.Result;
import crypto.soft.cryptongy.feature.shared.json.openorder.OpenOrder;
import crypto.soft.cryptongy.feature.shared.module.Account;
import crypto.soft.cryptongy.feature.trade.conditional.ConditionalReceiver;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by tseringwongelgurung on 11/21/17.
 */

public class CoinApplication extends Application {
    private double usdt_btc = 0;
    private double btc_eth;
    private Account readAccount;
    private Account tradeAccount;
    private Account withdrawAccount;
    private Notification settings;
    private OpenOrder openOrder;
    private OpenOrder openOrderBinance;
    private String READ="Read";
    private String TRADE="Trade";
    private String WITHDRAW="Withdraw";
    private List<Result> coins;

    public Notification getSettings() {
        return getNotification();
    }

    public void setSettings(Notification settings) {
        this.settings = settings;
    }


    public List<Result> getCoins() {
        return coins;
    }

    public void setCoins(List<Result> coins) {
        this.coins = coins;
    }

    /*  TRADE ACCOUNT GETTER SETTER */
    public Account getTradeAccount() {
        if (tradeAccount != null)
            return tradeAccount;
        else if (getWithdrawAccount() != null)
            return getWithdrawAccount();
        return null;
    }

    public void setTradeAccount(Account tradeAccount) {
        this.tradeAccount = tradeAccount;
    }

    public Account getTradeAccount(String exchangeValue) {
        setTradeAccount(exchangeValue);
        if (tradeAccount != null)
            return tradeAccount;
        else if (getWithdrawAccount(exchangeValue) != null)
            return getWithdrawAccount(exchangeValue);
        return null;
    }

    public void setTradeAccount(String exchangeValue)
    {
        setTradeAccount(getAccountFromReam(exchangeValue,TRADE));
    }


    /*  READ ACCOUNT GETTER SETTER */
    public Account getReadAccount() {
        if (readAccount != null)
            return readAccount;
        else if (getTradeAccount() != null)
            return getTradeAccount();
        else if (getWithdrawAccount() != null)
            return getWithdrawAccount();
        return null;
    }
    public void setReadAccount(Account readAccount) {
        this.readAccount = readAccount;
    }

    public Account getReadAccount(String exchangeValue) {
        readAccount = getAccountFromReam(exchangeValue,READ);
        if (readAccount != null)
            return readAccount;
        else if (getTradeAccount(exchangeValue) != null)
            return getTradeAccount(exchangeValue);
        else if (getWithdrawAccount(exchangeValue) != null)
            return getWithdrawAccount(exchangeValue);
        return null;
    }

    public void setReadAccount(String exchangeValue){
            setReadAccount(getAccountFromReam(exchangeValue,READ));
    }



    /* WITHDRAW ACCOUNT GETTER SETTER */
    public Account getWithdrawAccount() {
        return withdrawAccount;
    }

    public void setWithdrawAccount(Account withdrawAccount) {
        this.withdrawAccount = withdrawAccount;
    }

    public Account getWithdrawAccount(String exchangeValue) {
        setWithdrawAccount(exchangeValue);
        if (withdrawAccount != null)
            return withdrawAccount;
        return null;
    }

    public void setWithdrawAccount(String exchangeValue){
        setWithdrawAccount(getAccountFromReam(exchangeValue,WITHDRAW));
    }



    public double getbtc_eth() {
        return btc_eth;
    }

    public void setbtc_eth(double btc_eth) {
        this.btc_eth = btc_eth;
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

        getNotification();

        startService();
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


    public Notification getNotification() {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        Notification notificationDb = realm.where(Notification.class).equalTo("id", 0).findFirst();
        Notification notification = null;
        if (notificationDb == null) {
            notification = new Notification(true, true, true, 15);
            realm.copyToRealmOrUpdate(notification);
        } else
            notification = realm.copyFromRealm(notificationDb);
        realm.commitTransaction();
        settings=notification;
        return notification;
    }

    public void startService() {
        GlobalUtil.startAlarm(ConditionalReceiver.class, getResources().getInteger(R.integer.service_interval), this);
        GlobalUtil.startAlarm(broadCastTicker.class, getResources().getInteger(R.integer.service_interval), this);


    }

    public OpenOrder getOpenOrder() {
        return openOrder;
    }

    public void setOpenOrder(OpenOrder openOrder) {
        this.openOrder = openOrder;
    }

    public OpenOrder getOpenOrderBinance() {
        return openOrderBinance;
    }

    public void setOpenOrderBinance(OpenOrder openOrderBinance) {
        this.openOrderBinance = openOrderBinance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        GlobalUtil.stopAlarm(OrderReceiver.class, this);
    }


    public Account getAccountFromReam(String exchangeValue,String accountLabel){
        Account requiredAccount=null;
        Account defaultExchangeAccount=null;
        String defaultExchangeValue=getNotification().getDefaultExchange();


        if(TextUtils.isEmpty(exchangeValue)  ){
            exchangeValue = defaultExchangeValue;
        }

        if(TextUtils.isEmpty(accountLabel)) {
            return null;
        }

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<Account> accounts = realm.where(Account.class).findAll();
        if (accounts != null) {
            for (Account account : accounts) {

                if (account.getLabel().equals(accountLabel)){
                    if(account.getExchange().equalsIgnoreCase(exchangeValue)){
                        requiredAccount=realm.copyFromRealm(account);
                    }

                    if(account.getExchange().equalsIgnoreCase(defaultExchangeValue)){
                        defaultExchangeAccount=realm.copyFromRealm(account);
                    }

                }
            }
        }

        if(requiredAccount==null){
            requiredAccount=defaultExchangeAccount;
        }
        realm.commitTransaction();



        return requiredAccount;
    }

}
