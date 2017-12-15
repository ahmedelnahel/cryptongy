package crypto.soft.cryptongy.feature.alert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.utils.GlobalUtil;

/**
 * Created by maiAjam on 11/20/2017.
 */

public class broadCastTicker extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, AlertService.class));
        GlobalUtil.startAlarm(broadCastTicker.class,context.getResources().getInteger(R.integer.service_interval), context);
    }
}
