package crypto.soft.cryptongy.feature.account;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.List;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.feature.shared.module.Account;
import crypto.soft.cryptongy.utils.CoinApplication;
import crypto.soft.cryptongy.utils.GlobalUtil;

/**
 * Created by tseringwongelgurung on 11/19/17.
 */

public class AccountePresenter extends MvpBasePresenter<AccountView> {
    private AccountInteractor interactor;
    private Context context;

    public AccountePresenter(Context context) {
        this.context = context;
        interactor = new AccountInteractor();
    }

    public void getData() {
        interactor.getAccounts(new OnFinishListner<List<Account>>() {
            @Override
            public void onComplete(List<Account> result) {
                if (getView() != null) {
                    getView().hideEmptyView();
                    getView().setAdapter(result);
                }
            }

            @Override
            public void onFail(String error) {
                if (getView() != null)
                    getView().showEmptyView(error);
            }
        });
    }

    public void onOptionItemClicked(int id) {
        switch (id) {
            case R.id.ic_add:
                showAccountDialog(new Account());
                break;
        }
    }

    private void showAccountDialog(final Account account) {
        CustomDialog.showAccountDialog(context, account, getList(context), new OnFinishListner<Account>() {
            @Override
            public void onComplete(Account result) {
                if (result.getId() == null)
                    addAccount(result);
                else
                    updateAccount(result);
            }

            @Override
            public void onFail(String error) {
                GlobalUtil.showToast(error, context);
            }
        });
    }

    public void editAccount(Account account) {
        showAccountDialog(account);
    }

    public void addAccount(final Account account) {
        interactor.add(account, new OnFinishListner<Account>() {
            @Override
            public void onComplete(Account result) {
                if (getView() != null) {
                    ((CoinApplication) context.getApplicationContext()).updateAccount();
                    getView().hideEmptyView();
                    getView().addData(result);
                }
            }

            @Override
            public void onFail(String error) {
                GlobalUtil.showToast(error, context);
            }
        });
    }

    public void updateAccount(final Account account) {
        interactor.update(account, new OnFinishListner<Account>() {
            @Override
            public void onComplete(Account result) {
                ((CoinApplication) context.getApplicationContext()).updateAccount();
                getData();
            }

            @Override
            public void onFail(String error) {
                GlobalUtil.showToast(error, context);
            }
        });
    }

    public List<String> getList(Context context) {
        List<String> list = new ArrayList<>();
      String[] coinArray=context.getResources().getStringArray(R.array.coin_array);
      for(int i=0;i<coinArray.length;i++){
       list.add(coinArray[i]);
      }
        return list;
    }

    public void deleteAccount(int id) {
        interactor.delete(id);
        getData();
        ((CoinApplication) context.getApplicationContext()).updateAccount();
    }
}
