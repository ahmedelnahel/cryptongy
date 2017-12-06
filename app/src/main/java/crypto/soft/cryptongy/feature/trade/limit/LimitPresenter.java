package crypto.soft.cryptongy.feature.trade.limit;

import android.content.Context;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.trade.TradePresenter;
import crypto.soft.cryptongy.utils.CoinApplication;

/**
 * Created by tseringwongelgurung on 12/2/17.
 */

public class LimitPresenter extends TradePresenter<LimitView> {
    public LimitPresenter(Context context) {
        super(context);
    }

    @Override
    public void onClicked(int id) {
        switch (id) {
            case R.id.btnOk:
                if (getView() != null) {
                    Limit limit = ((LimitView) getView()).getLimit();
                    if (limit != null) {
                        CoinApplication application = (CoinApplication) context.getApplicationContext();
                        limit.setAccount(application.getTradeAccount());
                        getView().showLoading("Please wait.");
                        if (getView().isBuy())
                            buyLimit(limit);
                        else
                            sellLimit(limit);
                    }
                }
                break;
            default:
                super.onClicked(id);
        }
    }
}
