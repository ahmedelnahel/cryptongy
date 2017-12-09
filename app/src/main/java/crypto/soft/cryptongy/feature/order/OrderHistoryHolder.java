package crypto.soft.cryptongy.feature.order;

import android.view.View;
import android.widget.TextView;

import crypto.soft.cryptongy.R;

/**
 * Created by tseringwongelgurung on 11/24/17.
 */

public class OrderHistoryHolder {
    public TextView txtType, txtQuantity, txtRate, txtTime,txtCoin;

    public OrderHistoryHolder(View view) {
        txtType = view.findViewById(R.id.txtType);
        txtCoin = view.findViewById(R.id.txtCoin);
        txtQuantity = view.findViewById(R.id.txtQuantity);
        txtRate = view.findViewById(R.id.txtRate);
        txtTime = view.findViewById(R.id.txtTime);
    }
}
