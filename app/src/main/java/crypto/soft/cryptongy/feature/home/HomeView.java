package crypto.soft.cryptongy.feature.home;

import com.hannesdorfmann.mosby.mvp.MvpView;

import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;

/**
 * Created by tseringwongelgurung on 11/27/17.
 */

public interface HomeView extends MvpView {
    void initRecycler();

    void setAdapter();

    void onSummaryDataLoad(MarketSummaries marketSummaries);

    void onSummaryLoadFailed();

    void setLevel(String s);

    void hideProgressBar();

    void showProgressBar();
}
