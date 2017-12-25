package crypto.soft.cryptongy.feature.order;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import crypto.soft.cryptongy.feature.setting.Notification;
import crypto.soft.cryptongy.utils.CoinApplication;
import crypto.soft.cryptongy.utils.GlobalUtil;

/**
 * Created by tseringwongelgurung on 12/25/17.
 */

public class OrderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Notification notification = ((CoinApplication) context.getApplicationContext()).getNotification();
        if (notification.isAutomSync()) {
            context.startService(new Intent(context, OrderService.class));
            GlobalUtil.startAlarm(OrderReceiver.class, notification.getSyncInterval() * 1000, context);
        }
    }
}
