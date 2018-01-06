package crypto.soft.cryptongy.feature.order;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import crypto.soft.cryptongy.feature.shared.json.openorder.OpenOrder;
import crypto.soft.cryptongy.feature.shared.json.order.Order;
import crypto.soft.cryptongy.feature.shared.json.order.Result;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
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
        OpenOrder order = application.getOpenOrder();
        if (order == null)
            getOpenOrder(application, false);
        else
            checkOrder(order, application);

    }

    private void getOpenOrder(final CoinApplication application, final boolean check) {
        interactor.getOpenOrder("", application.getReadAccount(), new OnFinishListner<OpenOrder>() {
            @Override
            public void onComplete(OpenOrder result) {
                if(result != null && result.getSuccess()&& result.getResult()!= null) {
                    application.setOpenOrder(result);
                    if (check)
                        checkOrder(result, application);
                }
            }

            @Override
            public void onFail(String error) {
                Log.e("orderService", error);
            }
        });
    }

    private void checkOrder(final OpenOrder openOrder, final CoinApplication application) {

        for (int i = 0; i < openOrder.getResult().size(); i++) {
            final crypto.soft.cryptongy.feature.shared.json.openorder.Result data = openOrder.getResult().get(i);
            final int finalI = i;
            interactor.getOrders(data.getOrderUuid(), application.getReadAccount(),  new OnFinishListner<Order>() {

                @Override
                public void onComplete(Order result) {
                    if(result!=null && result.getSuccess() && result.getResult()!= null) {
                        Result order = result.getResult();
                         if (result.getResult().getCancelInitiated() && !result.getResult().getIsOpen()) {
                            GlobalUtil.showNotification(OrderService.this, "Order status", order.getType() + "(" + String.format("%.8f", order.getQuantity().doubleValue()) +
                                    ")" + "of " + order.getExchange() + " is now cancelled.", finalI);
                        openOrder.setChange(true);

                        } else if (!result.getResult().getIsOpen() && !result.getResult().getCancelInitiated()) {
                             GlobalUtil.showNotification(OrderService.this, "Order Status", order.getType() + "(" + String.format("%.8f", order.getQuantity().doubleValue()) +
                                     ")" + "of " + order.getExchange() + " is now closed.", finalI);
                             openOrder.setChange(true);

                         }
                    }

                }

                @Override
                public void onFail(String error) {

                }
            });
        }
        if(openOrder.isChange()) {
            getOpenOrder(application, false);
            openOrder.setChange(false);
        }
    }
}
