package crypto.soft.cryptongy.feature.coin;

import crypto.soft.cryptongy.feature.order.OrderView;
import crypto.soft.cryptongy.feature.shared.json.markethistory.MarketHistory;
import crypto.soft.cryptongy.feature.shared.json.marketsummary.MarketSummary;
import crypto.soft.cryptongy.feature.shared.json.openorder.OpenOrder;
import crypto.soft.cryptongy.feature.shared.json.orderhistory.OrderHistory;
import crypto.soft.cryptongy.feature.shared.ticker.TickerView;

/**
 * Created by tseringwongelgurung on 11/25/17.
 */

public interface CoinView extends TickerView {
    void setMarketSummary(MarketSummary summary);

    void setMarketTrade(MarketHistory marketHistory);
    void setTitle();

    void findViews();

    void setClickListner();

    void setCalculation(double calcualtion);

    void setLevel(String level);

    void setOpenOrders(OpenOrder openOrders);

    void setOrderHistory(OrderHistory orderHistory);

    void showLoading(String msg);

    void hideLoading();

    void showEmptyView();

    void hideEmptyView();
}
