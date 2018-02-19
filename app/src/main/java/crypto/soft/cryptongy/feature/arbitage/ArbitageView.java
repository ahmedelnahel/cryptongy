package crypto.soft.cryptongy.feature.arbitage;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.json.market.Result;

/**
 * Created by tseringwongelgurung on 11/27/17.
 */

public interface ArbitageView extends MvpView {
    void initRecycler();

    void setAdapter(List<Result> results);

    void onSummaryDataLoad(MarketSummaries marketSummaries, String exchangeValue);

    void onSummaryLoadFailed();

    void setLevel(String s);

    void hideProgressBar();

    void showProgressBar();
    void setCoinInTable(List<Result> marketSummaries);
}
