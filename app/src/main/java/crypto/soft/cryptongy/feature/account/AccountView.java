package crypto.soft.cryptongy.feature.account;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;


import crypto.soft.cryptongy.feature.shared.module.Account;

/**
 * Created by tseringwongelgurung on 11/19/17.
 */

public interface AccountView extends MvpView {
    void setTitle();

    void findViews();

    void initRecycleView();

    void setAdapter(List<Account> list);

    void addData(Account account);

    void showEmptyView(String msg);

    void hideEmptyView();
}
