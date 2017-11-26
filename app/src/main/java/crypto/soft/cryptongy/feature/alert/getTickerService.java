package crypto.soft.cryptongy.feature.alert;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.IOException;

import crypto.soft.cryptongy.feature.shared.json.ticker.Result;
import crypto.soft.cryptongy.feature.shared.json.ticker.Ticker;
import crypto.soft.cryptongy.network.BittrexServices;

/**
 * Created by maiAjam on 11/20/2017.
 */

public class getTickerService extends IntentService {

    Double lastValue;
    String coinName;
    String exchangeName;
    Double HighValueEn;
    Double LowValueEn;
    int ReqCode, alarmFreq, checlLower, checkHigher;
    Setting setting;
    Ticker ticker;

    public getTickerService() {
        super("getTickerService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {


        coinName = intent.getStringExtra("coinName");
        exchangeName = intent.getStringExtra("exchangeName");
        HighValueEn = intent.getDoubleExtra("high", 0);
        LowValueEn = intent.getDoubleExtra("low", 0);
        ReqCode = intent.getIntExtra("reqCode", 0);
        alarmFreq = intent.getIntExtra("alarmFreq", 1);
        checkHigher = intent.getIntExtra("higherCh", 1);
        checlLower = intent.getIntExtra("lowerCh", 1);


        BittrexServices Tickerservices = new BittrexServices();


        try {
            ticker = Tickerservices.getTickerMock("");
            if (!ticker.getSuccess()) {
                Handler mHandler = new Handler(getMainLooper());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), ticker.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });


            } else {

                Result result = ticker.getResult();
                boolean notifyHigh, notifyLow;

                lastValue = result.getLast();

                if (checlLower == 1) {

                    if (lastValue <= LowValueEn) {//lower than

                        notifyHigh = true;
                    }
                }
                if (checkHigher == 1) {
                    if (lastValue >= HighValueEn) {
                        // higher than
                        notifyLow = true;

                    }
                }


            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void notifiyme(boolean notifyHigh, boolean notifyLow) {
//
//        NumberFormat formatter = new DecimalFormat("#0.00000000");
//        Toast.makeText(getBaseContext(), "notify", Toast.LENGTH_LONG).show();
//        NotificationCompat.Builder b = new NotificationCompat.Builder(getBaseContext());
//        b.setAutoCancel(true)
//                .setDefaults(NotificationCompat.DEFAULT_ALL)
//                .setWhen(System.currentTimeMillis())
//                .setSmallIcon(R.drawable.ic_stat_name)
//
//                .setContentTitle(coinName);
//
//        if (notifyLow) {
//            b.setContentText(coinName + " is lower than " + formatter.format(LowValueEn));
//        }
//        if (notifyHigh) {
//            b.setContentText(coinName + " is higher than " + formatter.format(HighValueEn));
//        }
//
//
//        Intent resultIntent = new Intent(this, MainActivity.class);
//        resultIntent.putExtra("CoinName", coinName);
//        resultIntent.putExtra("requestCode", ReqCode);
//        resultIntent.putExtra("x", 2);
//
//        CryptongyApp globalSetting = new CryptongyApp();
//        //for test
//        setting = new Setting();
//        setting.sound = true;
//        setting.vibration = true;
//        globalSetting.setSettings(setting);
//        ((CryptongyApp) getApplication()).getSettings();
//        if (Boolean.valueOf(globalSetting.getSettings().vibration)) {
//            long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
//            b.setVibrate(pattern);
//
//        }
//        if (Boolean.valueOf(globalSetting.getSettings().sound)) {
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
//        resultIntent.setAction("fdsfs");
//        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getBaseContext());
//        stackBuilder.addParentStack(FirstActivity.class);
//        stackBuilder.addNextIntent(resultIntent);
//
//        PendingIntent piResult = stackBuilder.getPendingIntent(requestID, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        b.addAction(R.drawable.ic_stat_name, "See coin detials", piResult);
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

    }


}
