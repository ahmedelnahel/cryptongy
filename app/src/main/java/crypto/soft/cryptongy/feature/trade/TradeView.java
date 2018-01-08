package crypto.soft.cryptongy.feature.trade;

import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.json.marketsummary.MarketSummary;
import crypto.soft.cryptongy.feature.shared.json.wallet.Wallet;
import crypto.soft.cryptongy.feature.shared.ticker.TickerView;

/**
 * Created by tseringwongelgurung on 11/28/17.
 */

public interface TradeView extends TickerView {
    void setTitle();

    String getCoin();

    void findViews();

    void init();

    void setOnListner();

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

    void setValue(String value);
}
