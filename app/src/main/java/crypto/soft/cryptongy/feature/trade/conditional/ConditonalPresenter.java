package crypto.soft.cryptongy.feature.trade.conditional;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.account.CustomDialog;
import crypto.soft.cryptongy.feature.shared.json.marketsummary.MarketSummary;
import crypto.soft.cryptongy.feature.shared.json.wallet.Wallet;
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


    public void getData(String marketName) {
        if (getView() != null)
            getView().showLoading(context.getString(R.string.fetch_msg));
        Observer observer = new Observer() {

            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Object o) {
                if (getView() != null)
                    if (o instanceof MarketSummary)
                        getView().setMarketSummary((MarketSummary) o);
                    else if (o instanceof Wallet)
                        getView().setHolding((Wallet) o);
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
                if (getView() != null) {
                    getView().hideLoading();
                    getView().resetAll();
                    getView().hideEmptyView();
                }
            }
        };

        CoinApplication application = (CoinApplication) context.getApplicationContext();
        Observable.merge(getMarketSummary(marketName), getWallet(marketName,application.getTradeAccount()), getConditionals())
                .subscribe(observer);
    }

    @Override
    public void onClicked(int id) {
        switch (id) {
            case R.id.btnOk:
                List<Conditional> conditionals = getView().getConditionals();
                if (conditionals != null && conditionals.size() > 0) {
                    conditionalInteractor.saveConditional(conditionals);
                    fetchConditionals();
                } else
                    CustomDialog.showMessagePop(context, "Please choose any option", null);
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
}
