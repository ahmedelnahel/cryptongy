package crypto.soft.cryptongy.feature.coin;

import com.hannesdorfmann.mosby.mvp.MvpView;

import crypto.soft.cryptongy.feature.order.OrderView;
import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.json.marketsummary.MarketSummary;

/**
 * Created by tseringwongelgurung on 11/25/17.
 */

public interface CoinView extends OrderView {
    void setMarketSummary(MarketSummary summary);

    void setMarketTrade(MarketSummaries marketSummary);
}
