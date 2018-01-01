package crypto.soft.cryptongy.feature.order;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import crypto.soft.cryptongy.feature.shared.json.openorder.OpenOrder;
import crypto.soft.cryptongy.feature.shared.json.order.Order;
import crypto.soft.cryptongy.feature.shared.json.order.Result;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.feature.shared.module.Account;
import crypto.soft.cryptongy.utils.CoinApplication;
import crypto.soft.cryptongy.utils.GlobalUtil;

/**
 * Created by tseringwongelgurung on 12/25/17.
 */

public class OrderService extends IntentService {
    private OrderInteractor interactor = new OrderInteractor();

    public OrderService() {
        super("OrderService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        startService();
    }

    private void startService() {
        final CoinApplication application = (CoinApplication) getApplication();
        GlobalUtil.showNotification(OrderService.this, "Error in Order Service","Order check started", 0);
        OpenOrder order = application.getOpenOrder();
        if (order == null)
            getOpenOrder(application, true);
        else
            checkOrder(order, application);

    }

    private void getOpenOrder(final CoinApplication application, final boolean check) {
        Account account = application.getReadAccount();
        if (account == null)
            return;
        else
            GlobalUtil.showNotification(OrderService.this, "Error in Order Service","No account available", 0);

        interactor.getOpenOrder("", account, new OnFinishListner<OpenOrder>() {
            @Override
            public void onComplete(OpenOrder result) {
                application.setOpenOrder(result);
                if (check)
                    checkOrder(result, application);
            }

            @Override
            public void onFail(String error) {
                GlobalUtil.showNotification(OrderService.this, "Error in Order Service",error, 0);
            }
        });
    }

    private void checkOrder(final OpenOrder openOrder, final CoinApplication application) {
        for (int i = 0; i < openOrder.getResult().size(); i++) {
            final crypto.soft.cryptongy.feature.shared.json.openorder.Result data = openOrder.getResult().get(i);
            interactor.getOrders(data.getOrderUuid(), application.getReadAccount(), new OnFinishListner<Order>() {
                private int j=0;

                @Override
                public void onComplete(Order result) {
                    Result order = result.getResult();
                    if (!result.getResult().getIsOpen()) {
                        GlobalUtil.showNotification(OrderService.this, "Order Status", order.getType() + "(" + String.format("%.8f", order.getQuantity().doubleValue()) +
                                ")" + "of " + order.getExchange() + " is filled.", j);
                    } else if (result.getResult().getCancelInitiated()) {
                        GlobalUtil.showNotification(OrderService.this, "Order status", order.getType() + "(" + String.format("%.8f", order.getQuantity().doubleValue()) +
                                ")" + "of " + order.getExchange() + " is now cancelled.", j);
                    }else
                        GlobalUtil.showNotification(OrderService.this, "Order status", order.getType() + "(" + String.format("%.8f", order.getQuantity().doubleValue()) +
                                ")" + "of " + order.getExchange() + " is still open.", j);
                    j++;
                    if (j == openOrder.getResult().size())
                        getOpenOrder(application, false);
                }

                @Override
                public void onFail(String error) {
                    GlobalUtil.showNotification(OrderService.this, "Error in Order Service",
                            error +" for order for "+openOrder.getResult().get(j), 0);
                }
            });
        }
    }
}
