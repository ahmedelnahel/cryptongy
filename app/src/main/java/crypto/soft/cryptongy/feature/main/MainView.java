package crypto.soft.cryptongy.feature.main;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by tseringwongelgurung on 11/27/17.
 */

public interface MainView extends MvpView {
    void initToolbar();

    void setTitle();

    void findViews();

    void initSideMenu();

    void setAdapter();
}
