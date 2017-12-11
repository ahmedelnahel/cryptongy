package crypto.soft.cryptongy.utils;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import crypto.soft.cryptongy.feature.trade.conditional.ConditionalReceiver;
import io.realm.Realm;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by tseringwongelgurung on 11/20/17.
 */

public class GlobalUtil {
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

    public static void startAlarm(int interval, Context context) {
        Intent intent = new Intent(context, ConditionalReceiver.class);
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
}
