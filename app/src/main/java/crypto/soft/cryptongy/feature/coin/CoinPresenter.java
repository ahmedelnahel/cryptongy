package crypto.soft.cryptongy.feature.coin;

import android.content.Context;

import java.util.List;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.account.CustomDialog;
import crypto.soft.cryptongy.feature.order.OrderPresenter;
import crypto.soft.cryptongy.feature.shared.json.markethistory.MarketHistory;
import crypto.soft.cryptongy.feature.shared.json.marketsummary.MarketSummary;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.feature.shared.module.Account;

/**
 * Created by tseringwongelgurung on 11/25/17.
 */

public class CoinPresenter extends OrderPresenter<CoinView> {
    private boolean marketSummary = false,
            marketHistory = false;
    ;
    private CoinInteractor coinInteractor;

    public CoinPresenter(Context context) {
        super(context);
        coinInteractor = new CoinInteractor();
    }

    @Override
    public void getData() {
        interactor.getAccount(new OnFinishListner<List<Account>>() {

            @Override
            public void onComplete(List<Account> result) {
                if (getView() != null) {
                    getV().showLoading(context.getString(R.string.fetch_msg));
                    setAccounts(result);
                }
                openOrder = false;
                orderHistory = false;
                marketSummary = false;
                marketHistory = false;
                getMarketSummary();
                getMarketHistory();
                getOpenOrders(false);
                getOrderHistory();
            }

            @Override
            public void onFail(String error) {
                CustomDialog.showMessagePop(context, error, null);
                if (getView() != null)
                    getV().showEmptyView();
            }
        });
    }

    public void getMarketHistory() {
        coinInteractor.getMarketHistory(new OnFinishListner<MarketHistory>() {
            @Override
            public void onComplete(MarketHistory result) {
                if (getView() != null) {
                    marketHistory = true;
                    getView().hideLoading();
                    getView().setMarketTrade(result);
                    getView().hideEmptyView();
                }
            }

            @Override
            public void onFail(String error) {
                if (getView() != null)
                    getView().hideLoading();
                isDataAvailable();
            }
        });
    }

    public void getMarketSummary() {
        coinInteractor.getMarketSummary(new OnFinishListner<MarketSummary>() {
            @Override
            public void onComplete(MarketSummary result) {
                if (getView() != null) {
                    marketSummary = true;
                    getView().hideLoading();
                    getView().setMarketSummary(result);
                    getView().hideEmptyView();
                }
            }

            @Override
            public void onFail(String error) {
                if (getView() != null)
                    getView().hideLoading();
                isDataAvailable();
            }
        });
    }

    @Override
    protected void isDataAvailable() {
        if (!openOrder && !orderHistory && !marketSummary & !marketHistory) {
            if (getView() != null)
                getV().showEmptyView();
        }
    }
}
