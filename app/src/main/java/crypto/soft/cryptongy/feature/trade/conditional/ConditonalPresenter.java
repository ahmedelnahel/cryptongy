package crypto.soft.cryptongy.feature.trade.conditional;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.account.CustomDialog;
import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.feature.trade.TradePresenter;
import crypto.soft.cryptongy.utils.CoinApplication;
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

    public ConditonalPresenter(Context context) {
        super(context);
        conditionalInteractor = new ConditionalInteractor();
    }

    @Override
    public void getData() {
        CoinApplication application = (CoinApplication) context.getApplicationContext();
        if (application.getTradeAccount() != null) {
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

            Observable.merge(getCoins(), getConditionals())
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
                List<Conditional> conditionals = getView().getConditionals();
                if (conditionals != null) {
                    if (conditionals.size() > 0) {
                        int limit = context.getResources().getInteger(R.integer.order_limit);
                        conditionalInteractor.saveConditional(conditionals, limit, new OnFinishListner<Void>() {
                            @Override
                            public void onComplete(Void result) {
                                fetchConditionals();
                            }

                            @Override
                            public void onFail(String error) {
                                fetchConditionals();
                                CustomDialog.showMessagePop(context, error, null);
                            }
                        });
                    } else
                        CustomDialog.showMessagePop(context, "Please choose any option", null);
                }
                break;
            default:
                super.onClicked(id);
        }
    }

    public Observable<List<Conditional>> getConditionals() {
        return Observable.create(new ObservableOnSubscribe<List<Conditional>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<Conditional>> e) throws Exception {
                conditionalInteractor.getConditionals(new OnFinishListner<List<Conditional>>() {
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

    public void fetchConditionals() {
        getConditionals().subscribe(new Observer() {
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
        fetchConditionals();
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
}
