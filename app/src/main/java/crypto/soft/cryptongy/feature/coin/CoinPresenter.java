package crypto.soft.cryptongy.feature.coin;

import android.content.Context;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.account.CustomDialog;
import crypto.soft.cryptongy.feature.order.OrderPresenter;
import crypto.soft.cryptongy.feature.shared.json.markethistory.MarketHistory;
import crypto.soft.cryptongy.feature.shared.json.marketsummary.MarketSummary;
import crypto.soft.cryptongy.feature.shared.json.openorder.OpenOrder;
import crypto.soft.cryptongy.feature.shared.json.orderhistory.OrderHistory;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.feature.shared.module.Account;
import crypto.soft.cryptongy.utils.CoinApplication;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by tseringwongelgurung on 11/25/17.
 */

public class CoinPresenter extends OrderPresenter<CoinView> {
    private CoinInteractor coinInteractor;

    public CoinPresenter(Context context) {
        super(context);
        coinInteractor = new CoinInteractor();
    }

    @Override
    public void getData(final String coinName) {
        CoinApplication application = (CoinApplication) context.getApplicationContext();
        Account account = application.getReadAccount();
        if (account != null) {
            if (getView() != null) {
                getV().showLoading(context.getString(R.string.fetch_msg));
                getV().setLevel(account.getLabel());
            }

            Observer observer = new Observer() {
                private int count = 4;

                @Override
                public void onSubscribe(Disposable d) {
                    count = 4;
                }

                @Override
                public void onNext(Object o) {
                    count--;
                    if (o instanceof OpenOrder) {
                        if (getView() != null)
                            getV().setOpenOrders((OpenOrder) o);
                    } else if (o instanceof OrderHistory) {
                        if (getView() != null) {
                            getV().setOrderHistory((OrderHistory) o);
                            calculateProfit((OrderHistory) o, coinName);
                        }
                    } else if (o instanceof MarketSummary) {
                        if (getView() != null)
                            getView().setMarketSummary((MarketSummary) o);
                    } else if (o instanceof MarketHistory) {
                        if (getView() != null)
                            getView().setMarketTrade((MarketHistory) o);
                    }
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onComplete() {
                    if (getView() != null) {
                        getV().hideLoading();
                        if (count == 4) {
                            getV().showEmptyView();
                        } else
                            getView().hideEmptyView();
                    }
                }
            };
            Observable.merge(getMarketSummary(coinName), getOpenOrders(coinName, account), getOrderHistory(coinName, account), getMarketHistory(coinName))
                    .subscribe(observer);
        } else {
            CustomDialog.showMessagePop(context, context.getString(R.string.noAPI), null);
            if (getView() != null) {
                getV().setLevel("No API");
                getV().showEmptyView();
            }
        }
    }

    public Observable getMarketHistory(final String coinName) {
        return Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(final ObservableEmitter e) throws Exception {
                coinInteractor.getMarketHistory(coinName, new OnFinishListner<MarketHistory>() {
                    @Override
                    public void onComplete(MarketHistory result) {
                        e.onNext(result);
                        e.onComplete();
                    }

                    @Override
                    public void onFail(String error) {
                        CustomDialog.showMessagePop(context, error, null);
                        e.onComplete();
                    }
                });
            }
        });
    }

    public Observable<MarketSummary> getMarketSummary(final String coinName) {
        return io.reactivex.Observable.create(new ObservableOnSubscribe<MarketSummary>() {
            @Override
            public void subscribe(final ObservableEmitter<MarketSummary> e) throws Exception {
                coinInteractor.getMarketSummary(coinName, new OnFinishListner<MarketSummary>() {
                    @Override
                    public void onComplete(MarketSummary result) {
                        startTicker(coinName);
                        e.onNext(result);
                        e.onComplete();
                    }

                    @Override
                    public void onFail(String error) {
                        CustomDialog.showMessagePop(context, error, null);
                        e.onComplete();
                    }
                });
            }
        });
    }
}
