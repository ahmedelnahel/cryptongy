package crypto.soft.cryptongy.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;

import crypto.soft.cryptongy.R;

public class ProgressDialogFactory {
    private static ProgressDialogFactory Instance = null;
    private static ProgressDialog pd;

    public ProgressDialogFactory(Context context, String msg) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            pd = new ProgressDialog(context, R.style.Progress_AppTheme);
        else
            pd = new ProgressDialog(context, R.style.Progress_Holo);
        setMessage(msg);
        pd.setIndeterminate(false);
        pd.setCancelable(false);
    }

    public static ProgressDialogFactory getInstance(Context context, String msg) {
        if (Instance == null) {
            Instance = new ProgressDialogFactory(context, msg);
        } else
            Instance.setMessage(msg);
        return Instance;
    }

    public static void dismiss() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
        pd = null;
        Instance = null;
    }

    public void setMessage(String msg) {
        pd.setMessage(msg);
    }

    public void show() {
        if (pd != null && !pd.isShowing()) {
            pd.show();
        }
    }
}
