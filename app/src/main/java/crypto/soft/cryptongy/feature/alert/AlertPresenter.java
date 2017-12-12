package crypto.soft.cryptongy.feature.alert;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.CheckBox;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import io.realm.Realm;

/**
 * Created by prajwal on 12/1/17.
 */

public class AlertPresenter extends MvpBasePresenter<AlertView> {
    public void saveData(Context context, Double LowValueEn, Double HighValueEn, String exchangeName,
                         String coinName, int alarmFreq, int reqCode, CheckBox ch_higher, CheckBox ch_lower) {
        CoinInfo coinInfo = new CoinInfo(coinName, exchangeName, HighValueEn, LowValueEn);
        Realm realm = Realm.getDefaultInstance();
        CoinInfo coinInfoResult = realm.where(CoinInfo.class).equalTo("CoinName", coinName).findFirst();
        if (coinInfoResult == null) {
            if (getCoinInfo() != null && getCoinInfo().size() <= 5) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(coinInfo);
                realm.commitTransaction();
                realm.close();
            } else {
                Toast.makeText(context, "You can only add maximum of 5 alerts", Toast.LENGTH_LONG).show();
                return;
            }
        } else {
            Toast.makeText(context, "You can only add one alert for one coin", Toast.LENGTH_LONG).show();
            return;
        }

        Intent i = new Intent(context, broadCastTicker.class);

        i.putExtra("coinName", coinName);
        i.putExtra("exchangeName", exchangeName);
        i.putExtra("high", HighValueEn);
        i.putExtra("low", LowValueEn);
        i.putExtra("reqCode", reqCode);
        i.putExtra("alarmFreq", alarmFreq);
        i.putExtra("higherCh", ch_higher.isChecked());
        i.putExtra("lowerCh", ch_lower.isChecked());


        PendingIntent opertaion = PendingIntent.getBroadcast(context, reqCode, i, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManger = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


        alarmManger.setRepeating(AlarmManager.RTC_WAKEUP, 30 * 1000,
                1000 * 30, opertaion);


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
