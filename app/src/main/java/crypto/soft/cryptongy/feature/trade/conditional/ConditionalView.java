package crypto.soft.cryptongy.feature.trade.conditional;

import java.util.List;

import crypto.soft.cryptongy.feature.trade.TradeView;

/**
 * Created by tseringwongelgurung on 12/2/17.
 */

public interface ConditionalView extends TradeView {
    List<Conditional> getConditionals();

    void setConditional(List<Conditional> conditionals);
}
