package crypto.soft.cryptongy.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Owner on 22-11-2017.
 */

public class AlertUtility
{

    public static ProgressDialog dialog;

    public static void showLoadingDialog(Context context)
    {

        if (dialog != null && dialog.isShowing())
            return;
        dialog = new ProgressDialog(context); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    public static void dismissDialog()
    {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

}
