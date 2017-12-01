package crypto.soft.cryptongy.feature.alert;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.CheckBox;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

/**
 * Created by prajwal on 12/1/17.
 */

public class AlertPresenter extends MvpBasePresenter<AlertView> {
    public void saveData(Context context, Double LowValueEn, Double HighValueEn, String exchangeName,
                          String coinName, int alarmFreq, int reqCode, CheckBox ch_higher, CheckBox ch_lower) {
        dbHandler db = new dbHandler(context);
        CoinInfo coinInfo = new CoinInfo(coinName, exchangeName, HighValueEn, LowValueEn);
        db.AddCoinInfo(coinInfo);
        db.updateCoinInfo(coinInfo);
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


        Toast.makeText(context, "Your Values have been saved successfully", Toast.LENGTH_LONG).show();

    }
}
