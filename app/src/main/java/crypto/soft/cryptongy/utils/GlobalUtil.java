package crypto.soft.cryptongy.utils;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.main.MainActivity;
import crypto.soft.cryptongy.feature.setting.Notification;
import io.realm.Realm;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by tseringwongelgurung on 11/20/17.
 */

public class GlobalUtil {
    static  final  AtomicInteger c = new AtomicInteger(0);
    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(serviceClass.getName())) {
                return true;
            }
        }
        return false;
    }

    public static void startAlarm(Class cls, int interval, Context context) {
        Intent intent = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context.getApplicationContext(), 234324243, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + interval, pendingIntent);
    }

    public static void showToast(String msg, Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static int getNextKey(Realm realm, Class cls, String id) {
        Number max = realm.where(cls).max(id);
        if (max == null)
            return 1;
        return max.intValue() + 1;
    }

    public static void addFragment(Context context, Fragment fragment, int id, boolean addToBackstack) {
        FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager()
                .beginTransaction()
                .replace(id, fragment);

        if (addToBackstack)
            transaction.addToBackStack(null);

        transaction.commit();
    }

    public static double formatNumber(double longval, String pattern) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern(pattern);
        double data = Double.parseDouble(formatter.format(longval));
        return data;
    }


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static int getUniqueID() {
        return c.incrementAndGet();
    }

    public static void showNotification(Context context,String title, String content, int id) {
        Intent intent = new Intent(new Intent()).setClass(context, MainActivity.class);
        intent.putExtra("OPEN", "Conditional");
        intent.setAction("notification");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, 0);
        android.app.Notification.Builder b = new android.app.Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setAutoCancel(true);


        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        crypto.soft.cryptongy.feature.setting.Notification globalSetting = ((CoinApplication) context.getApplicationContext()).getSettings();
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
}
