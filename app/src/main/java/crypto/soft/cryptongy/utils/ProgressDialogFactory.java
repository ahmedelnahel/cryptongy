package crypto.soft.cryptongy.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.KeyEvent;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.shared.listner.DialogListner;

public class ProgressDialogFactory {
    private static ProgressDialogFactory Instance = null;
    private static ProgressDialog pd;
    private static DialogListner listner;

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
        listner=null;
    }

    private void setOnKeyListner() {
        pd.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    pd.dismiss();
                    listner.onOkClicked();
                }
                return true;
            }
        });
    }

    public void setMessage(String msg) {
        pd.setMessage(msg);
    }

    public void show(DialogListner listner) {
        this.listner=listner;
        if (pd != null && !pd.isShowing()) {
            pd.show();
            setOnKeyListner();
        }
    }
}
