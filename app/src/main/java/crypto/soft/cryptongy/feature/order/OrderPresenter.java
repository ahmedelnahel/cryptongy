package crypto.soft.cryptongy.feature.order;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpView;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.account.AccountActivity;
import crypto.soft.cryptongy.feature.account.CustomDialog;
import crypto.soft.cryptongy.feature.main.MainActivity;
import crypto.soft.cryptongy.feature.setting.SettingActivity;
import crypto.soft.cryptongy.feature.shared.json.action.Cancel;
import crypto.soft.cryptongy.feature.shared.json.openorder.OpenOrder;
import crypto.soft.cryptongy.feature.shared.json.orderhistory.OrderHistory;
import crypto.soft.cryptongy.feature.shared.json.orderhistory.Result;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.feature.shared.module.Account;
import crypto.soft.cryptongy.feature.shared.ticker.TickerPresenter;
import crypto.soft.cryptongy.feature.shared.ticker.TickerView;
import crypto.soft.cryptongy.utils.CoinApplication;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by tseringwongelgurung on 11/23/17.
 */

public class OrderPresenter<T extends MvpView & TickerView> extends TickerPresenter<T> {
    protected OrderInteractor interactor;
    public OrderPresenter(Context context) {
        super(context);
        interactor = new OrderInteractor();
    }

    public void onOptionItemClicked(int id) {
        switch (id) {
            case R.id.ic_setting:
                context.startActivity(new Intent(context, SettingActivity.class));
                break;
        }
    }

    public void onClicked(int id, String coinName) {
        switch (id) {
            case R.id.imgSync:
                getData(coinName);
                break;
            case R.id.imgAccSetting:
                if (context instanceof MainActivity)
                    ((MainActivity) context).getPresenter().replaceAccountFragment();
                else
                    context.startActivity(new Intent(context, AccountActivity.class));
                break;
        }
    }

    public void getData(final String coinName) {
        CoinApplication application = (CoinApplication) context.getApplicationContext();
        Account account = application.getReadAccount();
        if (account != null) {
            if (getView() != null) {
                getV().showLoading(context.getString(R.string.fetch_msg));
                getV().setLevel(account.getLabel());
            }

            Observer observer = new Observer() {
                private int count = 2;

                @Override
                public void onSubscribe(Disposable d) {
                    count = 2;
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
                            calculateProfit((OrderHistory) o, coinName, 0);
                        }
                    }
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onComplete() {
                    if (getView() != null) {
                        getV().hideLoading();
                        if (count == 2) {
                            getV().showEmptyView();
                        } else
                            getV().hideEmptyView();
                    }
                }
            };

            Observable.merge(getOpenOrders(coinName, account), getOrderHistory(coinName, account))
                    .subscribe(observer);
        } else {
            CustomDialog.showMessagePop(context, context.getString(R.string.noAPI), null);
            if (getView() != null) {
                getV().setLevel("No API");
                getV().showEmptyView();
            }
        }
    }

    public Observable<OrderHistory> getOrderHistory(final String coinName, final Account account) {
        return Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(final ObservableEmitter e) throws Exception {
                interactor.getOrderHistory(coinName, account, new OnFinishListner<OrderHistory>() {
                    @Override
                    public void onComplete(OrderHistory result) {
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

    public Observable<OpenOrder> getOpenOrders(final String coinName, final Account account) {
        return Observable.create(new ObservableOnSubscribe<OpenOrder>() {
            @Override
            public void subscribe(final ObservableEmitter<OpenOrder> e) throws Exception {
                interactor.getOpenOrder(coinName, account, new OnFinishListner<OpenOrder>() {
                    @Override
                    public void onComplete(OpenOrder result) {
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

    public void cancleOrder(final String coinName, String orderUuid, final Account account) {
        if (getView() != null)
            getV().showLoading(context.getString(R.string.cancle_msg));
        interactor.cancleOrder(orderUuid, account, new OnFinishListner<Cancel>() {

            @Override
            public void onComplete(Cancel result) {
                if (result.getSuccess()) {
                    if (getView() != null) {
                        Observer observer = new Observer() {
                            @Override
                            public void onSubscribe(Disposable d) {
                            }

                            @Override
                            public void onNext(Object o) {
                                if (getView() != null) {
                                    getV().setOpenOrders((OpenOrder) o);
                                    getV().hideEmptyView();
                                    String msg = ((OpenOrder) o).getMessage();
                                    if (TextUtils.isEmpty(msg))
                                        msg = "Order has been cancled successfully.";
                                    CustomDialog.showMessagePop(context, msg, null);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                onFail(e.getMessage());
                            }

                            @Override
                            public void onComplete() {
                                if (getView() != null)
                                    getV().hideLoading();
                            }
                        };

                        getOpenOrders(coinName, account).subscribe(observer);
                    } else
                        onFail(result.getMessage());
                }
            }

            @Override
            public void onFail(String error) {
                if (getView() != null)
                    getV().hideLoading();
                CustomDialog.showMessagePop(context, error, null);
            }
        });
    }

    protected void calculateProfit(OrderHistory history, String coinName, double last) {
        if (history == null || history.getResult() == null || history.getResult().size() == 0)
            return;
        double sell = 0d, buy = 0d, sq= 0d, bq = 0d, limit = 0d;
        double calculation = 0;
        CoinApplication application = (CoinApplication) context.getApplicationContext();
        int i =0;
        for (Result data : history.getResult()) {
            if (i==0) {
                limit = data.getLimit();
                i++;
            }

            if (coinName.isEmpty() || data.getExchange().equals(coinName)) {
                if (data.getOrderType().toLowerCase().equals("limit_sell") ||
                        data.getOrderType().toLowerCase().equals("conditional_sell")) {
                    if (data.getLimit() != null) {
                        if (data.getQuantity() != null)
                            sell += data.getPrice();
                            sq += data.getQuantity();

                    }
                } else if ((data.getOrderType().toLowerCase().equals("limit_buy") ||
                        data.getOrderType().toLowerCase().equals("conditional_buy"))) {
                    if (data.getLimit() != null) {
                        if (data.getQuantity() != null)
                            buy += data.getPrice();
                            bq += data.getQuantity();

                    }
                }

            }

//            Log.d("Profit ", "Coin Name " + data.getExchange() + " sell " + sell + " sellq " + sq + " bq " + bq );

        }

        double currentHolding =0;
        if(bq>=sq)currentHolding = (bq-sq)*last;
        Log.d("Profit ", "bq-sq  " +(bq-sq) + " currentHolding  " + currentHolding + " sell - buy " +  (sell - buy ));
        calculation = sell - buy + currentHolding;
        if (getView() != null) {
            getV().setCalculation(calculation);
        }

    }

    public OrderView getV() {
        T view = super.getView();
        if (view == null)
            return null;
        else
            return (OrderView) view;
    }
}
