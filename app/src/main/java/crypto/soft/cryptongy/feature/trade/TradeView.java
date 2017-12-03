package crypto.soft.cryptongy.feature.trade;

import com.hannesdorfmann.mosby.mvp.MvpView;

import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.json.marketsummary.MarketSummary;
import crypto.soft.cryptongy.feature.shared.json.wallet.Wallet;

/**
 * Created by tseringwongelgurung on 11/28/17.
 */

public interface TradeView extends MvpView {
    void setTitle();

    void findViews();

    void init();

    void setOnClickListner();

    void setTextWatcher();

    void setLevel(String level);

    void setMarketSummary(MarketSummary summary);

    void onSummaryDataLoad(MarketSummaries marketSummaries);

    void showLoading(String msg);

    void hideLoading();

    void showEmptyView();

    void hideEmptyView();

    void setHolding(Wallet o);

    void setMax();

    void setAgaints(String coin);

    void calculateTotal();

    boolean isBuy();

    void resetAll();
}
