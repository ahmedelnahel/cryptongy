package crypto.soft.cryptongy.feature.alert;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.List;

import crypto.soft.cryptongy.BuildConfig;
import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.shared.ticker.TickerPresenter;
import crypto.soft.cryptongy.utils.GlobalConstant;
import crypto.soft.cryptongy.utils.GlobalUtil;
import io.realm.Realm;

/**
 * Created by prajwal on 12/1/17.
 */

public class AlertPresenter extends TickerPresenter<AlertView> {
    public AlertPresenter(Context context) {
        super(context);
    }

    public void saveData(Context context, Double LowValueEn, Double HighValueEn, String exchangeName,
                         String coinName, int alarmFreq, int reqCode, CheckBox ch_higher, CheckBox ch_lower) {
        CoinInfo coinInfo = new CoinInfo(coinName, exchangeName, HighValueEn, LowValueEn, alarmFreq, reqCode,
                ch_higher.isChecked(), ch_lower.isChecked(), GlobalConstant.Conditional.TYPE_OPEN);
        Realm realm = Realm.getDefaultInstance();

            if (getCoinInfo() != null && getCoinInfo().size() < BuildConfig.MAX_ALERT) {
                realm.beginTransaction();
                coinInfo.setId(GlobalUtil.getNextKey(realm, CoinInfo.class, "Id"));
                realm.copyToRealmOrUpdate(coinInfo);
                realm.commitTransaction();
                realm.close();
            } else {
                Toast.makeText(context, "Lite version max " + BuildConfig.MAX_ALERT +" alerts, please try the pro version", Toast.LENGTH_LONG).show();
                return;
            }

        boolean isServiceRunning = GlobalUtil.isServiceRunning(context, broadCastTicker.class);
        if (!isServiceRunning)
            GlobalUtil.startAlarm(broadCastTicker.class, context.getResources().getInteger(R.integer.service_interval), context);
//
//        Intent i = new Intent(context, broadCastTicker.class);
//
//        i.putExtra("coinName", coinName);
//        i.putExtra("exchangeName", exchangeName);
//        i.putExtra("high", HighValueEn);
//        i.putExtra("low", LowValueEn);
//        i.putExtra("reqCode", reqCode);
//        i.putExtra("alarmFreq", alarmFreq);
//        i.putExtra("higherCh", ch_higher.isChecked());
//        i.putExtra("lowerCh", ch_lower.isChecked());
//
//
//        PendingIntent opertaion = PendingIntent.getBroadcast(context, reqCode, i, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        AlarmManager alarmManger = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//
//
//        alarmManger.setRepeating(AlarmManager.RTC_WAKEUP, 30 * 1000,
//                1000 * 30, opertaion);
//

        Toast.makeText(context, "Alert is saved successfully", Toast.LENGTH_LONG).show();

        if (isViewAttached()) {
            getView().updateTable();
        }
    }

    public void deleteCoinInfo(String coinName) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        CoinInfo coinInfo = realm.where(CoinInfo.class).equalTo("CoinName", coinName).findFirst();
        coinInfo.deleteFromRealm();
        realm.commitTransaction();
        realm.close();

        if (isViewAttached()) {
            getView().updateTable();
        }
    }

    public List<CoinInfo> getCoinInfo() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(CoinInfo.class).findAll();
    }
}
