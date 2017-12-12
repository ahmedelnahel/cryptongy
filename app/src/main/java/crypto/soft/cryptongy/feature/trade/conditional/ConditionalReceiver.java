package crypto.soft.cryptongy.feature.trade.conditional;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.utils.GlobalUtil;

/**
 * Created by tseringwongelgurung on 12/11/17.
 */

public class ConditionalReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isServiceRunning = GlobalUtil.isServiceRunning(context, ConditionalService.class);
        if (isServiceRunning)
            context.stopService(new Intent(context, ConditionalService.class));

        context.startService(new Intent(context, ConditionalService.class));
        GlobalUtil.startAlarm(ConditionalReceiver.class,context.getResources().getInteger(R.integer.service_interval), context);
    }
}
