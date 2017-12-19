package crypto.soft.cryptongy.feature.trade;

import android.content.Context;
import android.text.TextUtils;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.account.CustomDialog;
import crypto.soft.cryptongy.feature.main.MainActivity;
import crypto.soft.cryptongy.feature.shared.json.limitorder.LimitOrder;
import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.json.marketsummary.MarketSummary;
import crypto.soft.cryptongy.feature.shared.json.wallet.Wallet;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.feature.shared.module.Account;
import crypto.soft.cryptongy.feature.shared.ticker.TickerPresenter;
import crypto.soft.cryptongy.feature.trade.limit.Limit;
import crypto.soft.cryptongy.utils.CoinApplication;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by tseringwongelgurung on 11/28/17.
 */

public class TradePresenter<T extends TradeView> extends TickerPresenter<T> {
    protected TradeInteractor tradeInteractor;

    public TradePresenter(Context context) {
        super(context);
        tradeInteractor = new TradeInteractor();
    }

    public void onClicked(int id) {
        switch (id) {
            case R.id.imgSync:
                String coin = getView().getCoin();
                if (!TextUtils.isEmpty(coin))
                    getData(coin);
                else
                    getData();
                break;
            case R.id.imgAccSetting:
                ((MainActivity) context).getPresenter().replaceAccountFragment();
                break;
            case R.id.txtMax:
                if (getView() != null)
                    getView().setMax();
                break;
        }
    }

    public void getData() {
        CoinApplication application = (CoinApplication) context.getApplicationContext();
        if (application.getTradeAccount() != null) {
            if (getView() != null) {
                getView().resetView();
                getView().showLoading(context.getString(R.string.fetch_msg));
                getView().setLevel(application.getTradeAccount().getLabel());
            }

            getCoins().subscribe(new Observer() {

                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Object o) {
                    if (o instanceof MarketSummaries)
                        if (getView() != null)
                            getView().onSummaryDataLoad((MarketSummaries) o);

                }

                @Override
                public void onError(Throwable e) {
                    CustomDialog.showMessagePop(context, e.getMessage(), null);
                    if (getView() != null) {
                        getView().hideLoading();
                        getView().showEmptyView();
                    }
                }

                @Override
                public void onComplete() {
                    if (getView() != null) {
                        getView().hideLoading();
                    }
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

    public Observable<MarketSummary> getMarketSummary(final String coinName) {
        return Observable.create(new ObservableOnSubscribe<MarketSummary>() {
            @Override
            public void subscribe(final ObservableEmitter<MarketSummary> e) throws Exception {
                tradeInteractor.getMarketSummary(coinName, new OnFinishListner<MarketSummary>() {
                    @Override
                    public void onComplete(MarketSummary result) {
                        startTicker(coinName);
                        e.onNext(result);
                        e.onComplete();
                    }

                    @Override
                    public void onFail(String error) {
                        e.onError(new Throwable(error));
                    }
                });
            }
        });
    }

    public Observable<Wallet> getWallet(final String coinName, final Account account) {
        return Observable.create(new ObservableOnSubscribe<Wallet>() {
            @Override
            public void subscribe(final ObservableEmitter<Wallet> e) throws Exception {
                tradeInteractor.getWalletSummary(coinName, account, new OnFinishListner<Wallet>() {
                    @Override
                    public void onComplete(Wallet result) {
                        e.onNext(result);
                        e.onComplete();
                    }

                    @Override
                    public void onFail(String error) {
                        e.onError(new Throwable(error));
                    }
                });
            }
        });
    }

    public Observable<MarketSummaries> getCoins() {
        return Observable.create(new ObservableOnSubscribe<MarketSummaries>() {
            @Override
            public void subscribe(final ObservableEmitter<MarketSummaries> e) throws Exception {
                tradeInteractor.loadSummary(new OnFinishListner<MarketSummaries>() {
                    @Override
                    public void onComplete(MarketSummaries marketSummaries) {
                        e.onNext(marketSummaries);
                        e.onComplete();
                    }

                    @Override
                    public void onFail(String error) {
                        e.onError(new Throwable(error));
                    }
                });
            }
        });
    }

    public void getData(String marketName) {
        if (getView() != null) {
            getView().showLoading(context.getString(R.string.fetch_msg));
            getView().resetView();
        }
        Observer observer = new Observer() {

            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Object o) {
                if (o instanceof MarketSummary) {
                    if (getView() != null)
                        getView().setMarketSummary((MarketSummary) o);
                } else if (o instanceof Wallet) {
                    if (getView() != null)
                        getView().setHolding((Wallet) o);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (getView() != null) {
                    getView().hideLoading();
                    CustomDialog.showMessagePop(context, e.getMessage(), null);
                    getView().showEmptyView();
                }
            }

            @Override
            public void onComplete() {
                if (getView() != null) {
                    getView().hideLoading();
                    getView().resetAll();
                    getView().hideEmptyView();
                }
            }
        };
        CoinApplication application = (CoinApplication) context.getApplicationContext();

        Observable.concat(getMarketSummary(marketName), getWallet(marketName, application.getTradeAccount()))
                .subscribe(observer);
    }

    public void buyLimit(Limit limit) {
        tradeInteractor.buyLimit(limit, new OnFinishListner<LimitOrder>() {
            @Override
            public void onComplete(LimitOrder limitOrder) {
                if (getView() != null) {
                    getView().resetAll();
                    getView().hideLoading();
                }
                CustomDialog.showMessagePop(context, "Buy order is placed successfully", null);
            }

            @Override
            public void onFail(String error) {
                CustomDialog.showMessagePop(context, error, null);
                if (getView() != null)
                    getView().hideLoading();
            }
        });
    }

    public void sellLimit(Limit limit) {
        tradeInteractor.sellLimit(limit, new OnFinishListner<LimitOrder>() {
            @Override
            public void onComplete(LimitOrder limitOrder) {
                if (getView() != null) {
                    getView().resetAll();
                    getView().hideLoading();
                }
                CustomDialog.showMessagePop(context, "Sell order is placed successfully", null);
            }

            @Override
            public void onFail(String error) {
                CustomDialog.showMessagePop(context, error, null);
                if (getView() != null)
                    getView().hideLoading();
            }
        });
    }
}
