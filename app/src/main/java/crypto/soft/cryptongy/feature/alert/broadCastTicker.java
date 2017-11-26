package crypto.soft.cryptongy.feature.alert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by maiAjam on 11/20/2017.
 */

public class broadCastTicker extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        String coinName = intent.getStringExtra("coinName");
        String exchangeName = intent.getStringExtra("exchangeName");
        Double HighValueEn = intent.getDoubleExtra("high",0);
        Double LowValueEn = intent.getDoubleExtra("low",0);
        int alarmFreq = intent.getIntExtra("alarmFreq",1);
        int checkedHigher =  intent.getIntExtra("higherCh",1);
        int checkedLower =  intent.getIntExtra("lowerCh",1);



        Intent intenSer = new Intent(context,getTickerService.class);
        intenSer.putExtra("coinName",coinName);
        intenSer.putExtra("exchangeName",exchangeName);
        intenSer.putExtra("high",HighValueEn);
        intenSer.putExtra("low",LowValueEn);
        intenSer.putExtra("alarmFreq",alarmFreq);
        intenSer.putExtra("higherCh",checkedHigher);
        intenSer.putExtra("lowerCh",checkedLower);
        context.startService(intenSer);
    }
}
