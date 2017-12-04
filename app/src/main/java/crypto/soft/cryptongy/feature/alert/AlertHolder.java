package crypto.soft.cryptongy.feature.alert;

import android.view.View;
import android.widget.TextView;

import crypto.soft.cryptongy.R;

public class AlertHolder {
    public TextView txtCoin, txtLowPrice, txtHighPrice, txtAction;

    public AlertHolder(View view) {
        txtCoin = view.findViewById(R.id.txtCoin);
        txtLowPrice = view.findViewById(R.id.txtLowPrice);
        txtHighPrice = view.findViewById(R.id.txtHighPrice);
        txtAction = view.findViewById(R.id.txtAction);
    }
}