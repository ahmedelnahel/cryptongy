package crypto.soft.cryptongy.feature.trade.conditional;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import crypto.soft.cryptongy.BuildConfig;
import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.account.CustomDialog;
import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.json.marketsummary.MarketSummary;
import crypto.soft.cryptongy.feature.shared.json.wallet.Wallet;
import crypto.soft.cryptongy.feature.shared.listner.DialogListner;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.feature.trade.TradePresenter;
import crypto.soft.cryptongy.utils.CoinApplication;
import crypto.soft.cryptongy.utils.GlobalUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by tseringwongelgurung on 12/2/17.
 */

public class ConditonalPresenter extends TradePresenter<ConditionalView> {
    private ConditionalInteractor conditionalInteractor;
    String exchangeValue=null;

    public ConditonalPresenter(Context context) {
        super(context);
        conditionalInteractor = new ConditionalInteractor();
    }
//
//    @Override
//    public void getData() {
//        CoinApplication application = (CoinApplication) context.getApplicationContext();
//        if (application.getTradeAccount() != null) {
//            if (getView() != null) {
//                getView().showLoading(context.getString(R.string.fetch_msg));
//                getView().setLevel(application.getTradeAccount().getLabel());
//                getView().showEmptyView();
//            }
//
//            Observer observer = new Observer() {
//
//                @Override
//                public void onSubscribe(Disposable d) {
//                }
//
//                @Override
//                public void onNext(Object o) {
//                    if (getView() != null)
//                        if (o instanceof MarketSummaries)
//                            getView().onSummaryDataLoad((MarketSummaries) o);
//                        else
//                            getView().setConditional((List<Conditional>) o);
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                    if (getView() != null) {
//                        getView().hideLoading();
//                        CustomDialog.showMessagePop(context, e.getMessage(), null);
//                        getView().showEmptyView();
//                    }
//                }
//
//                @Override
//                public void onComplete() {
//                    if (getView() != null)
//                        getView().hideLoading();
//                }
//            };
//
//            Observable.concat(getCoinsForTrade(getView().getExchangeValue()), getConditionals())
//                    .subscribe(observer);
//        } else {
//            CustomDialog.showMessagePop(context, context.getString(R.string.noAPI), null);
//            if (getView() != null) {
//                getView().setLevel("No API");
//                getView().showEmptyView();
//            }
//        }
//    }


    public void getDataForConditional(String exchangeValue) {
        this.exchangeValue=exchangeValue;
        CoinApplication application = (CoinApplication) context.getApplicationContext();
        if (application.getTradeAccount(exchangeValue) != null) {
            if (getView() != null) {
                getView().showLoading(context.getString(R.string.fetch_msg));
                getView().setLevel(application.getTradeAccount().getLabel());
                getView().showEmptyView();
            }

            Observer observer = new Observer() {

                @Override
                public void onSubscribe(Disposable d) {
                }

                @Override
                public void onNext(Object o) {
                    if (getView() != null)
                        if (o instanceof MarketSummaries)
                            getView().onSummaryDataLoad((MarketSummaries) o);
                        else
                            getView().setConditional((List<Conditional>) o);
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
                    if (getView() != null)
                        getView().hideLoading();
                }
            };

            Observable.concat(getCoinsForTrade(this.exchangeValue), getConditionals(exchangeValue))
                    .subscribe(observer);
        } else {
            CustomDialog.showMessagePop(context, context.getString(R.string.noAPI), null);
            if (getView() != null) {
                getView().setLevel("No API");
                getView().showEmptyView();
            }
        }
    }

    @Override
    public void onClicked(int id) {
        switch (id) {
            case R.id.btnOk:
                final List<Conditional> conditionals = getView().getConditionals();
                if (conditionals != null) {
                    if (conditionals.size() > 0) {

                        CustomDialog.showConfirmation(context, context.getString(R.string.limit_msg), new DialogListner() {
                            @Override
                            public void onOkClicked() {
                                int limit = BuildConfig.MAX_ORDER;
                                int sameLimit = BuildConfig.MAX_ORDER_COIN;
                                conditionalInteractor.saveConditional(conditionals,getView().getExchangeValue(), limit, sameLimit, new OnFinishListner<Void>() {
                                    @Override
                                    public void onComplete(Void result) {
                                        CustomDialog.showMessagePop(context, "Created sucessfully.", null);
                                        fetchConditionals(getView().getExchangeValue());
                                        if (!GlobalUtil.isServiceRunning(context, ConditionalService.class))
                                            GlobalUtil.startAlarm(ConditionalReceiver.class, context.getResources().getInteger(R.integer.service_interval), context);
                                    }

                                    @Override
                                    public void onFail(String error) {
                                        fetchConditionals(getView().getExchangeValue());
                                        CustomDialog.showMessagePop(context, error, null);
                                    }
                                });
                            }
                        });
                    } else
                        CustomDialog.showMessagePop(context, "Please choose atleast one option", null);
                }
                break;
            default:
                super.onClicked(id);
        }
    }

    public Observable<List<Conditional>> getConditionals(final String exchangeValue) {
        return Observable.create(new ObservableOnSubscribe<List<Conditional>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<Conditional>> e) throws Exception {
                conditionalInteractor.getConditionals(exchangeValue,new OnFinishListner<List<Conditional>>() {
                    @Override
                    public void onComplete(List<Conditional> result) {
                        e.onNext(result);
                        e.onComplete();
                    }

                    @Override
                    public void onFail(String error) {
                        e.onNext(new ArrayList<Conditional>());
                        e.onComplete();
                    }
                });
            }
        });
    }

    public void fetchConditionals(String exchangeValue) {
        getConditionals(exchangeValue).subscribe(new Observer() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {
                if (getView() != null)
                    getView().setConditional((List<Conditional>) o);

            }

            @Override
            public void onError(Throwable e) {
                if (getView() != null) {
                    getView().hideLoading();
                }
            }

            @Override
            public void onComplete() {
                if (getView() != null) {
                    getView().hideLoading();
                }
            }
        });
    }

    public void delete(int id) {
        if (getView() != null)
            getView().showLoading("Please wait.");
        conditionalInteractor.deleletConditional(id);
        fetchConditionals(getView().getExchangeValue());
    }

    public void sortList(List<Conditional> conditionals, boolean isCoinAscend) {
        Collections.sort(conditionals, new Comparator<Conditional>() {
            @Override
            public int compare(Conditional conditional, Conditional t1) {
                return conditional.getOrderCoin().compareTo(t1.getOrderCoin());
            }
        });
        if (getView() != null) {
            if (!isCoinAscend)
                Collections.reverse(conditionals);
            getView().setConditional(conditionals);
        }
    }

    public void sortListByStatus(List<Conditional> conditionals, boolean isStatusAscend) {
        Collections.sort(conditionals, new Comparator<Conditional>() {
            @Override
            public int compare(Conditional conditional, Conditional t1) {
                return conditional.getOrderStatus().compareTo(t1.getOrderStatus());
            }
        });
        if (getView() != null) {
            if (!isStatusAscend)
                Collections.reverse(conditionals);
            getView().setConditional(conditionals);
        }
    }

    public void getDataForCondition(String marketName,String exchangeValue) {
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

        Observable.concat(getMarketSummary(marketName,this.exchangeValue), getWallet(marketName, application.getTradeAccount(this.exchangeValue)))
                .subscribe(observer);
    }
}
