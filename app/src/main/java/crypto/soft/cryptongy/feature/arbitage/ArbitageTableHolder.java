package crypto.soft.cryptongy.feature.arbitage;

import android.view.View;
import android.widget.TextView;

import crypto.soft.cryptongy.R;

/**
 * Created by tseringwongelgurung on 11/24/17.
 */

public class ArbitageTableHolder {
    public TextView txtTitleCoin,txtTitlePrice1,txtTitlePrice2,txtTitlePercentage;

    public ArbitageTableHolder(View view) {
        txtTitleCoin=view.findViewById(R.id.txtTitleCoin);
        txtTitlePrice1=view.findViewById(R.id.txtTitlePrice1);
        txtTitlePrice2=view.findViewById(R.id.txtTitlePrice2);
        txtTitlePercentage=view.findViewById(R.id.txtTitlePercentage);
    }
}
