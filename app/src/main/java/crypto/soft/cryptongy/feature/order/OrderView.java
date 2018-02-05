package crypto.soft.cryptongy.feature.order;

import com.hannesdorfmann.mosby.mvp.MvpView;

import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.json.openorder.OpenOrder;
import crypto.soft.cryptongy.feature.shared.json.order.Order;
import crypto.soft.cryptongy.feature.shared.json.orderhistory.OrderHistory;

/**
 * Created by tseringwongelgurung on 11/23/17.
 */

public interface OrderView extends MvpView {
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

    void setOrder(Order order);

    void resetView();
    void setCoins(MarketSummaries marketSummaries);
}