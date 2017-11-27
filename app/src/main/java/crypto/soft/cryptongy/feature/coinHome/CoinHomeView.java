package crypto.soft.cryptongy.feature.coinHome;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by tseringwongelgurung on 11/25/17.
 */

public interface CoinHomeView extends MvpView {
    void setTitle();

    void initToolbar();

    void findViews();

    void initTab();
}
