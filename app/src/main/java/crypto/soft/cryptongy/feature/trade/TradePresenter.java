package crypto.soft.cryptongy.feature.trade;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.account.AccountFragment;
import crypto.soft.cryptongy.feature.account.CustomDialog;
import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.json.market.Result;
import crypto.soft.cryptongy.feature.shared.json.marketsummary.MarketSummary;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.feature.shared.listner.OnMultiFinishListner;
import crypto.soft.cryptongy.utils.CoinApplication;
import crypto.soft.cryptongy.utils.GlobalUtil;

/**
 * Created by tseringwongelgurung on 11/28/17.
 */

public class TradePresenter<T extends TradeView> extends MvpBasePresenter<T> {
    private Context context;
    private TradeInteractor tradeInteractor;

    public TradePresenter(Context context) {
        this.context = context;
        tradeInteractor = new TradeInteractor();
    }

    public void onClicked(int id) {
        switch (id) {
            case R.id.imgSync:
                getData();
                break;
            case R.id.imgAccSetting:
                GlobalUtil.addFragment(context, new AccountFragment(), R.id.container, true);
                break;
        }
    }

    public void getData() {
        CoinApplication application = (CoinApplication) context.getApplicationContext();
        if (application.getTradeAccount() != null) {
            if (getView() != null) {
                getView().showLoading(context.getString(R.string.fetch_msg));
                getView().setLevel(application.getTradeAccount().getLabel());
            }
            getMarketSummary();

            tradeInteractor.loadSummary(context, new OnMultiFinishListner<List<Result>, MarketSummaries>() {
                @Override
                public void onComplete(List<Result> results, MarketSummaries marketSummaries) {
                    if (getView() != null) {
//                        getView().setAdapter(results);
                        getView().onSummaryDataLoad(marketSummaries);
                    }
                }

                @Override
                public void onFail(String error) {

                }
            });
        } else {
            CustomDialog.showMessagePop(context, context.getString(R.string.noAPI), null);
            if (getView() != null) {
                getView().setLevel("No API");
                getView().showEmptyView();
            }
        }
    }

    public void getMarketSummary() {
        tradeInteractor.getMarketSummary(new OnFinishListner<MarketSummary>() {
            @Override
            public void onComplete(MarketSummary result) {
                if (getView() != null) {
                    getView().setMarketSummary(result);
                    getView().hideEmptyView();
                    getView().hideLoading();
                }
            }

            @Override
            public void onFail(String error) {
                CustomDialog.showMessagePop(context, error, null);
                if (getView() != null) {
                    getView().hideLoading();
                    getView().showEmptyView();
                }
            }
        });
    }
}
