package crypto.soft.cryptongy.feature.order;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.Timer;
import java.util.TimerTask;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.account.AccountActivity;
import crypto.soft.cryptongy.feature.account.CustomDialog;
import crypto.soft.cryptongy.feature.main.MainActivity;
import crypto.soft.cryptongy.feature.setting.Notification;
import crypto.soft.cryptongy.feature.setting.SettingActivity;
import crypto.soft.cryptongy.feature.shared.json.action.Cancel;
import crypto.soft.cryptongy.feature.shared.json.openorder.OpenOrder;
import crypto.soft.cryptongy.feature.shared.json.order.Order;
import crypto.soft.cryptongy.feature.shared.json.orderhistory.OrderHistory;
import crypto.soft.cryptongy.feature.shared.json.orderhistory.Result;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.feature.shared.module.Account;
import crypto.soft.cryptongy.utils.CoinApplication;
import crypto.soft.cryptongy.utils.GlobalUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by tseringwongelgurung on 11/23/17.
 */

public class OrderPresenter extends MvpBasePresenter<OrderView> {
    protected OrderInteractor interactor;
    private Context context;
    private Timer timer;
    private OpenOrder openOrder;

    public OrderPresenter(Context context) {
        this.context = context;
        interactor = new OrderInteractor();
    }

    public void onOptionItemClicked(int id) {
        switch (id) {
            case R.id.ic_setting:
                context.startActivity(new Intent(context, SettingActivity.class));
                break;
        }
    }

    public void onClicked(int id) {
        switch (id) {
            case R.id.imgSync:
                getData();
                break;
            case R.id.imgAccSetting:
                if (context instanceof MainActivity)
                    ((MainActivity) context).getPresenter().replaceAccountFragment();
                else
                    context.startActivity(new Intent(context, AccountActivity.class));
                break;
        }
    }

    public void getData() {
        CoinApplication application = (CoinApplication) context.getApplicationContext();
        Account account = application.getReadAccount();
        if (account != null) {
            if (getView() != null) {
                getView().resetView();
                getView().showLoading(context.getString(R.string.fetch_msg));
                getView().setLevel(account.getLabel());
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
                        if (getView() != null) {
                            openOrder = (OpenOrder) o;
                            getView().setOpenOrders(openOrder);
                        }
                    } else if (o instanceof OrderHistory) {
                        if (getView() != null) {
                            getView().setOrderHistory((OrderHistory) o);
                            calculateProfit((OrderHistory) o, 0);
                        }
                    }
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onComplete() {
                    if (getView() != null) {
                        getView().hideLoading();
                        if (count == 2) {
                            getView().showEmptyView();
                        } else {
                            startTimer();
                            getView().hideEmptyView();
                        }
                    }
                }
            };

            Observable.merge(getOpenOrders("", account), getOrderHistory("", account))
                    .subscribe(observer);
        } else {
            CustomDialog.showMessagePop(context, context.getString(R.string.noAPI), null);
            if (getView() != null) {
                getView().setLevel("No API");
                getView().showEmptyView();
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
            getView().showLoading(context.getString(R.string.cancle_msg));
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
                                    getView().setOpenOrders((OpenOrder) o);
                                    getView().hideEmptyView();
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
                                    getView().hideLoading();
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
                    getView().hideLoading();
                CustomDialog.showMessagePop(context, error, null);
            }
        });
    }

    protected void calculateProfit(OrderHistory history, double last) {
        if (history == null || history.getResult() == null || history.getResult().size() == 0)
            return;
        double sell = 0d, buy = 0d, sq = 0d, bq = 0d;
        double calculation = 0;
        int i = 0;
        for (Result data : history.getResult()) {
            if (i == 0)
                i++;

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

        double currentHolding = 0;
        if (bq >= sq) currentHolding = (bq - sq) * last;
        calculation = sell - buy + currentHolding;
        if (getView() != null) {
            getView().setCalculation(calculation);
        }
    }

    public void stopTimer() {
        if (timer != null)
            timer.cancel();
    }

    public void startTimer() {
        stopTimer();
        Notification notification = ((CoinApplication) context.getApplicationContext()).getNotification();
        if (notification.isAutomSync()) {
            int timerInterval = notification.getSyncInterval() * 1000;
            timer = new Timer();
            timer.scheduleAtFixedRate(new TickerTimer(), timerInterval,
                    timerInterval);
        }
    }

    public void startOpenOrder(String coinName, Account account) {
        getOpenOrders(coinName, account).subscribe(new Observer<OpenOrder>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(OpenOrder openOrder) {
                if (getView() != null) {
                    OrderPresenter.this.openOrder = openOrder;
                    getView().setOpenOrders(OrderPresenter.this.openOrder);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    class TickerTimer extends TimerTask {

        @Override
        public void run() {
            final Account account = ((CoinApplication) context.getApplicationContext()).getReadAccount();
            for (int i = 0; i < openOrder.getResult().size(); i++) {
                final crypto.soft.cryptongy.feature.shared.json.openorder.Result data = openOrder.getResult().get(i);
                final int finalI = i;
                interactor.getOrders(data.getOrderUuid(), account, new OnFinishListner<Order>() {

                    @Override
                    public void onComplete(Order result) {
                        if (getView() != null) {
                            crypto.soft.cryptongy.feature.shared.json.order.Result order = result.getResult();
                            if (!result.getResult().getIsOpen()) {
                                GlobalUtil.showNotification(context, "Open Order status", order.getExchange() + "(" + String.format("%.8f", order.getQuantity().doubleValue()) +
                                        ")" + "of " + order.getType() + " is now closed.", finalI);
                                startOpenOrder("", account);
                            } else if (result.getResult().getCancelInitiated()) {
                                GlobalUtil.showNotification(context, "Open Order status", order.getExchange() + "(" + String.format("%.8f", order.getQuantity().doubleValue()) +
                                        ")" + "of " + order.getType() + " is now cancelled.", finalI);
                                startOpenOrder("", account);
                            }
                        }
                    }

                    @Override
                    public void onFail(String error) {

                    }
                });
            }
        }
    }
}
