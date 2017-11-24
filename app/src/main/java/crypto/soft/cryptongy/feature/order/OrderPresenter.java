package crypto.soft.cryptongy.feature.order;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.account.CustomDialog;
import crypto.soft.cryptongy.feature.setting.SettingActivity;
import crypto.soft.cryptongy.feature.shared.json.action.Cancel;
import crypto.soft.cryptongy.feature.shared.json.openorder.OpenOrder;
import crypto.soft.cryptongy.feature.shared.json.orderhistory.OrderHistory;
import crypto.soft.cryptongy.feature.shared.json.orderhistory.Result;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.feature.shared.module.Account;
import crypto.soft.cryptongy.utils.GlobalUtil;

/**
 * Created by tseringwongelgurung on 11/23/17.
 */

public class OrderPresenter extends MvpBasePresenter<OrderView> {
    private Context context;
    private OrderInteractor interactor;
    private boolean openOrder = false, orderHistory = false;

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
                break;
        }
    }

    public void getData() {
        interactor.getAccount(new OnFinishListner<Account>() {
            @Override
            public void onComplete(Account result) {
                if (getView() != null)
                    getView().showLoading(context.getString(R.string.fetch_msg));
                openOrder = false;
                orderHistory = false;
                getOpenOrders();
                getOrderHistory();
            }

            @Override
            public void onFail(String error) {
                CustomDialog.showMessagePop(context, error, null);
                if (getView() != null)
                    getView().showEmptyView();
            }
        });
    }

    public void getOrderHistory() {
        interactor.getOrderHistory(new OnFinishListner<OrderHistory>() {
            @Override
            public void onComplete(OrderHistory result) {
                if (getView() != null) {
                    orderHistory = true;
                    getView().hideLoading();
                    getView().setOrderHistory(result);
                    calculateProfit(result);
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

    public void getOpenOrders() {
        interactor.getOpenOrder(new OnFinishListner<OpenOrder>() {
            @Override
            public void onComplete(OpenOrder result) {
                if (getView() != null) {
                    openOrder = true;
                    getView().hideLoading();
                    getView().setOpenOrders(result);
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

    private void isDataAvailable() {
        if (!openOrder && !orderHistory) {
            if (getView() != null)
                getView().showEmptyView();
        }
    }

    public void cancleOrder(String orderUuid) {
        if (getView() != null)
            getView().showLoading(context.getString(R.string.cancle_msg));
        interactor.cancleOrder(orderUuid, new OnFinishListner<Cancel>() {

            @Override
            public void onComplete(Cancel result) {
                if (result.getSuccess()) {
                    if (getView() != null) {
                        String msg = result.getMessage();
                        if (TextUtils.isEmpty(msg))
                            msg = "Order has been cancled successfully.";
                        CustomDialog.showMessagePop(context, msg, null);
                        getOpenOrders();
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

    private void calculateProfit(OrderHistory history) {
        if (history == null || history.getResult() == null || history.getResult().size() == 0)
            return;
        double sell = 0d, buy = 0d;

        for (Result data : history.getResult()) {
            if (data.getOrderType().toLowerCase().equals("limit_sell") ||
                    data.getOrderType().toLowerCase().equals("conditional_sell")) {
                if (data.getLimit() != null) {
                    if (data.getQuantity() != null)
                        sell += data.getQuantity().doubleValue() * data.getLimit().doubleValue();
                    else
                        sell = data.getLimit().doubleValue();

                }
            } else if (data.getOrderType().toLowerCase().equals("limit_buy") ||
                    data.getOrderType().toLowerCase().equals("conditional_buy")) {
                if (data.getLimit() != null) {
                    if (data.getQuantity() != null)
                        buy += data.getQuantity().doubleValue() * data.getLimit().doubleValue();
                    else
                        buy = data.getLimit().doubleValue();

                }
            }
        }
        double calculation = buy - sell;

        if (getView() != null)
            getView().setCalculation(String.valueOf(GlobalUtil.formatNumber(calculation, "#.########") + "฿"),
                    "$" + String.valueOf(GlobalUtil.formatNumber(GlobalUtil.convertBtcToUsd(calculation), "#.####")));
    }
}
