package crypto.soft.cryptongy.feature.alert;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.shared.json.ticker.Ticker;
import crypto.soft.cryptongy.network.BinanceServices;
import crypto.soft.cryptongy.network.BittrexServices;
import crypto.soft.cryptongy.utils.CoinApplication;
import crypto.soft.cryptongy.utils.GlobalConstant;
import crypto.soft.cryptongy.utils.GlobalUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by maiAjam on 11/20/2017.
 */

public class AlertService extends IntentService {
    String TAG=getClass().getSimpleName().toString();
    Disposable disposable;
    boolean met;

    public AlertService() {
        super("AlerService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        startAlert();
    }

    public void startAlert() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<CoinInfo> coinInfoResult = realm.where(CoinInfo.class).equalTo("status",
                GlobalConstant.Conditional.TYPE_OPEN).findAll();
        List<CoinInfo> list = new ArrayList<>();

        if (coinInfoResult == null || coinInfoResult.size() == 0) {
            realm.commitTransaction();
            return;
        }
        list.addAll(realm.copyFromRealm(coinInfoResult));
        realm.commitTransaction();
        realm.close();
        BittrexServices Tickerservices = new BittrexServices();
        BinanceServices binanceServices=new BinanceServices();

        for (final CoinInfo coinInfo : list) {
             met = false;
            try {
                Ticker ticker=null;
                if(coinInfo.getSpinnerValue().equalsIgnoreCase(GlobalConstant.Exchanges.BITTREX)){

                    ticker = Tickerservices.getTicker(coinInfo.getCoinName());
                    notificationAlert(coinInfo,ticker,met);
                }
                if(coinInfo.getSpinnerValue().equalsIgnoreCase(GlobalConstant.Exchanges.BINANCE)){

                    binanceServices.getTickerConnectSocket(coinInfo.getCoinName());

                    disposable = binanceServices.sourceTickerWebsocket.observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Ticker>() {
                        @Override
                        public void accept(Ticker ticker) throws Exception {


                            Log.d(TAG, "accept: disposable disposed");
                            disposable.dispose();
                            Log.d(TAG, "ConditionService ticker  " + ticker);

                            notificationAlert(coinInfo,ticker,met);

                        }


                    });
//                    ticker=binanceServices.getTicker(coinInfo.getCoinName());
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public void notificationAlert(CoinInfo coinInfo,Ticker ticker,boolean met){
        if (coinInfo.isHigher()&& ticker!= null && ticker.getResult()!=null && ticker.getSuccess() && ticker.getResult().getLast().doubleValue() >= coinInfo.getHighValue().doubleValue() ) {
            showNotification("Alert", coinInfo.CoinName + " is above " + String.format("%.8f", coinInfo.getHighValue().doubleValue()), GlobalUtil.getUniqueID());
            met = true;
        }

        if (coinInfo.isLower() && ticker!= null&& ticker.getResult()!=null && ticker.getSuccess() && ticker.getResult().getLast().doubleValue() <= coinInfo.getLowValue().doubleValue() ) {
            showNotification("Alert", coinInfo.CoinName + " is below " + String.format("%.8f", coinInfo.getLowValue().doubleValue()), GlobalUtil.getUniqueID());
            met = true;
        }

        if (met && coinInfo.getAlarmFreq() == 1) {
            coinInfo.status = "Closed";
            update(coinInfo);
        }
    }
    private void update(CoinInfo coinInfo) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(coinInfo);
        realm.commitTransaction();
        realm.close();
    }

    private void showNotification(String title, String content, int id) {


        PendingIntent intent = PendingIntent.getActivity(this, 0,
                new Intent(this, AlertActivity.class), 0);

        Notification.Builder b = new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentIntent(intent)
                .setAutoCancel(true);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        crypto.soft.cryptongy.feature.setting.Notification globalSetting = ((CoinApplication) getApplication()).getSettings();
        if(globalSetting != null) {
            if (Boolean.valueOf(globalSetting.isVibrate())) {
                long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
                b.setVibrate(pattern);

            }
            if (Boolean.valueOf(globalSetting.isSound())) {


                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                b.setSound(alarmSound);

            }
        }
        notificationManager.notify(id, b.build());
    }

//    private void notifiyme(boolean notifyHigh, boolean notifyLow) {
//        NumberFormat formatter = new DecimalFormat("#0.00000000");
//        Toast.makeText(getBaseContext(), "notify", Toast.LENGTH_LONG).show();
//        NotificationCompat.Builder b = new NotificationCompat.Builder(getBaseContext());
//        b.setAutoCancel(true)
//                .setDefaults(NotificationCompat.DEFAULT_ALL)
//                .setWhen(System.currentTimeMillis())
//                .setSmallIcon(R.drawable.circle)
//
//                .setContentTitle(coinName);
//         String alarmText = "";
//        if (notifyLow) {
//            alarmText  = coinName + " is lower than " + formatter.format(LowValueEn);
//
//        }
//        if (notifyHigh) {
//            alarmText +=  " " + coinName + " is higher than " + formatter.format(HighValueEn);
//
//        }
//
//        b.setContentText(alarmText);
//        Intent resultIntent = new Intent(this, MainActivity.class);
//        resultIntent.putExtra("CoinName", coinName);
//        resultIntent.putExtra("requestCode", ReqCode);
//        resultIntent.putExtra("x", 2);
//
//
//
//        Notification globalSetting = ((CoinApplication) getApplication()).getSettings();
//        if (Boolean.valueOf(globalSetting.isVibrate())) {
//            long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
//            b.setVibrate(pattern);
//
//        }
//        if (Boolean.valueOf(globalSetting.isSound())) {
//
//
//            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            b.setSound(alarmSound);
//
//        }
//
//
//        int requestID = (int) System.currentTimeMillis();
//
//        resultIntent.setAction("dummy");
//        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getBaseContext());
//        stackBuilder.addParentStack(MainActivity.class);
//        stackBuilder.addNextIntent(resultIntent);
//
//        PendingIntent piResult = stackBuilder.getPendingIntent(requestID, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        b.addAction(R.drawable.circle, "See coin detials", piResult);
//
//        NotificationManager nm = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
//        nm.notify(1, b.build());
//
//
//        if (alarmFreq == 1) {
//            //one time so cancel alarm manger
//            Intent cancel = new Intent(getBaseContext(), broadCastTicker.class);
//
//            cancel.putExtra("coinName", coinName);
//            cancel.putExtra("exchangeName", exchangeName);
//            cancel.putExtra("high", HighValueEn);
//            cancel.putExtra("low", LowValueEn);
//            cancel.putExtra("reqCode", ReqCode);
//            cancel.putExtra("alarmFreq", alarmFreq);
//
//
//            PendingIntent opertaion = PendingIntent.getBroadcast(getBaseContext(), ReqCode, cancel, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            AlarmManager alarmManger = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//
//
//            alarmManger.cancel(opertaion);
//        }
//
//    }


}
