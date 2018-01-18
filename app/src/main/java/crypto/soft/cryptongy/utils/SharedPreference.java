package crypto.soft.cryptongy.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by apurva on 07/06/2017.
 */

public class SharedPreference {

    public static String IS_COIN_ADDED_BINANCE="isCoinAddedBinance";
    public static String MOCK_VALUE_BINANCE="mockValueBinance";

    public static void saveToPrefs(Context context, String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
        Log.v("ShpUtils saving", "key: " + key + "\n value:" + value);
    }

    public static void saveToPrefs(Context context, String key, Boolean value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
        Log.v("ShpUtils saving", "key: " + key + "\n value:" + value);
    }

    public static void clearall(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * Called to retrieve required value from shared preferences, identified by given key.
     * Default value will be returned of no value found or error occurred.
     *
     * @param context Context of caller activity
     * @param key     Key to find value against
     *                // * @param defaultValue Value to return if no data found against given key
     * @return Return the value found against given key, default if not found or any error occurs
     */
    public static String getFromPrefs(Context context, String key) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        String defaultValue = "";
        try {
            Log.v("ShpUtils for key: " + key, sharedPrefs.getString(key, defaultValue));
            return sharedPrefs.getString(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("ShpUtils for nothing", e.toString());
            return defaultValue;
        }
    }

    public static Boolean isFirst(Context context, String key) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        Boolean defaultValue = true;
        try {
            return sharedPrefs.getBoolean(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            Log.v("ShpUtils for nothing", e.toString());
            return defaultValue;
        }
    }

    /**
     * @param context Context of caller activity
     * @param key     Key to delete from SharedPreferences
     */
    public static void removeFromPrefs(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.commit();
    }
}
