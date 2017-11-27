package crypto.soft.cryptongy.feature.shared.listner;

import crypto.soft.cryptongy.feature.shared.json.market.Result;

/**
 * Created by Ajahar on 11/23/2017.
 */

public interface AdapterItemClickListener {
    void onItemClicked(Result menuItem, int position);

    void onItemLongClicked(Result menuItem, int position);
}
