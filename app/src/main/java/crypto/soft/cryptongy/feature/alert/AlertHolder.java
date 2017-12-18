package crypto.soft.cryptongy.feature.alert;

import android.view.View;
import android.widget.TextView;

import crypto.soft.cryptongy.R;

public class AlertHolder {
    public TextView txtCoin, txtLowPrice, txtHighPrice, txtAction,edLow, edHigh;

    public AlertHolder(View view) {
        txtCoin = view.findViewById(R.id.txtCoin);
        txtLowPrice = view.findViewById(R.id.txtLowPrice);
        txtHighPrice = view.findViewById(R.id.txtHighPrice);
        txtAction = view.findViewById(R.id.txtAction);
        edLow = view.findViewById(R.id.LowValue_ED);
        edHigh = view.findViewById(R.id.HighValue_ED);
    }
}