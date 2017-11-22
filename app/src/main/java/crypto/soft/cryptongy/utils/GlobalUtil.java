package crypto.soft.cryptongy.utils;

import android.content.Context;
import android.widget.Toast;

import io.realm.Realm;

/**
 * Created by tseringwongelgurung on 11/20/17.
 */

public class GlobalUtil {
    public static void showToast(String msg, Context context){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    public static int getNextKey(Realm realm, Class cls, String id) {
        Number max = realm.where(cls).max(id);
        if (max == null)
            return 1;
        return max.intValue() + 1;
    }
}
