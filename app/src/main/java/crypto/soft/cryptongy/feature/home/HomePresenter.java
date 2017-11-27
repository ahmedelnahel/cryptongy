package crypto.soft.cryptongy.feature.home;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.account.CustomDialog;
import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.feature.shared.module.Account;
import crypto.soft.cryptongy.utils.CoinApplication;

/**
 * Created by tseringwongelgurung on 11/27/17.
 */

public class HomePresenter extends MvpBasePresenter<HomeView> implements OnFinishListner<MarketSummaries> {
    private HomeInteractor homeInteractor;
    private Context context;

    public HomePresenter(Context context) {
        this.homeInteractor = new HomeInteractor();
        this.context = context;
    }

    public void loadSummaries() {
        if (getView() != null)
            getView().showProgressBar();
        CoinApplication application = (CoinApplication) context.getApplicationContext();
        Account account = application.getAccount();
        if (account != null) {
            if (getView() != null)
                getView().setAdapter();
            homeInteractor.loadSummary(this);
        } else {
            CustomDialog.showMessagePop(context, context.getString(R.string.noAPI), null);
            if (getView() != null) {
                getView().setLevel("No API");
                getView().hideProgressBar();
            }
        }
    }

    @Override
    public void onComplete(MarketSummaries result) {
        if (getView() != null)
            getView().onSummaryDataLoad(result);
    }

    @Override
    public void onFail(String error) {
        if (getView() != null) {
            getView().onSummaryLoadFailed();
            getView().hideProgressBar();
        }
    }
}