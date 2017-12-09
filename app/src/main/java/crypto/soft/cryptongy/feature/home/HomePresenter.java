package crypto.soft.cryptongy.feature.home;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.json.market.Result;
import crypto.soft.cryptongy.feature.shared.listner.OnMultiFinishListner;

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
        homeInteractor.loadSummary(context, this);
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