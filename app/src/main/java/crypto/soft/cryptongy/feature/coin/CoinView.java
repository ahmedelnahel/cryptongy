package crypto.soft.cryptongy.feature.coin;

import crypto.soft.cryptongy.feature.order.OrderView;
import crypto.soft.cryptongy.feature.shared.json.markethistory.MarketHistory;
import crypto.soft.cryptongy.feature.shared.json.marketsummary.MarketSummary;

/**
 * Created by tseringwongelgurung on 11/25/17.
 */

public interface CoinView extends OrderView {
    void setMarketSummary(MarketSummary summary);

    void setMarketTrade(MarketHistory marketHistory);
}
