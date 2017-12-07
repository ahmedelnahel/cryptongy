package crypto.soft.cryptongy.feature.home;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.account.CustomDialog;
import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.json.market.Result;
import crypto.soft.cryptongy.feature.shared.listner.OnMultiFinishListner;
import crypto.soft.cryptongy.feature.shared.module.Account;
import crypto.soft.cryptongy.utils.CoinApplication;
import crypto.soft.cryptongy.utils.SharedPreference;

/**
 * Created by tseringwongelgurung on 11/27/17.
 */

public class HomePresenter extends MvpBasePresenter<HomeView> implements OnMultiFinishListner<List<Result>, MarketSummaries> {
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
        Account account = application.getReadAccount();
        if (account != null) {
            homeInteractor.loadSummary(context, this);
        } else {
            CustomDialog.showMessagePop(context, context.getString(R.string.noAPI), null);
            if (getView() != null) {
                getView().setLevel("No API");
                getView().hideProgressBar();
            }
        }
    }

    @Override
    public void onComplete(List<Result> results, MarketSummaries marketSummaries) {
        if (getView() != null) {
            getView().setAdapter(results);
            getView().onSummaryDataLoad(marketSummaries);
        }
    }

    @Override
    public void onFail(String error) {
        if (getView() != null) {
            getView().onSummaryLoadFailed();
            getView().hideProgressBar();
        }
    }
}