package crypto.soft.cryptongy.feature.order;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import crypto.soft.cryptongy.feature.account.CustomDialog;
import crypto.soft.cryptongy.feature.shared.json.openorder.OpenOrder;
import crypto.soft.cryptongy.feature.shared.json.openorder.Result;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.feature.shared.module.Account;
import crypto.soft.cryptongy.utils.CoinApplication;
import crypto.soft.cryptongy.utils.GlobalConstant;
import crypto.soft.cryptongy.utils.GlobalUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by tseringwongelgurung on 12/25/17.
 */

public class OrderService extends IntentService {
    private OrderInteractor interactor = new OrderInteractor();
    String exchangeBittrix=GlobalConstant.Exchanges.BITTREX;
    String exchangeBinance=GlobalConstant.Exchanges.BINANCE;


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
        OpenOrder orderBinance=application.getOpenOrderBinance();


        if(order==null){
            getOpenOrder(application,exchangeBittrix);
        }
        else {
            checkOrder(order, application,exchangeBittrix);
        }

        if(orderBinance==null){
            getOpenOrder(application,exchangeBinance);
        }else {
            checkOrder(orderBinance,application,exchangeBinance);
        }


    }



    private void getOpenOrder(final CoinApplication application, final String exchangeValue) {

        interactor.getOpenOrder("",exchangeValue, application.getReadAccount(exchangeValue), new OnFinishListner<OpenOrder>() {
            @Override
            public void onComplete(OpenOrder result) {
                Log.d("orderService", "Order tracking json " + result + result.getSuccess() + result.getJson());
                if(result != null && result.getSuccess()&& result.getResult()!= null) {
                    if(exchangeValue.equalsIgnoreCase(GlobalConstant.Exchanges.BITTREX)){

                            application.setOpenOrder(result);

                    }
                    if(exchangeValue.equalsIgnoreCase(GlobalConstant.Exchanges.BINANCE)){

                        application.setOpenOrderBinance(result);
                    }

                }
            }

            @Override
            public void onFail(String error) {
                Log.e("orderService", error);
            }
        });


    }


    private void checkOrder(final OpenOrder previousOpenOrder, final CoinApplication application, final String exchangeValue) {
        interactor.getOpenOrder("",exchangeValue, application.getReadAccount(exchangeValue), new OnFinishListner<OpenOrder>() {
            @Override
            public void onComplete(OpenOrder openOrder) {
                Log.d("orderService", "Order tracking json " + openOrder + openOrder.getSuccess() + openOrder.getJson());
                if(openOrder != null && openOrder.getSuccess()&& openOrder.getResult()!= null) {
                    for (int i = 0; i < previousOpenOrder.getResult().size(); i++) {
                        boolean findOrder = false;

                        for (int j = 0; j < openOrder.getResult().size(); j++) {


                            if (openOrder.getResult().get(j).getOrderUuid().equalsIgnoreCase(previousOpenOrder.getResult().get(i).getOrderUuid())) {
                                findOrder = true;
                                break;
                            }

                        }

                        if (findOrder == false) {
                            Result result = previousOpenOrder.getResult().get(i);
                            GlobalUtil.showNotification(OrderService.this, " Order Status", result.getOrderType() + " Order for " + result.getExchange() + " with amount " + result.getQuantity() + " is closed.", i);


                        }
                    }
                    application.setOpenOrder(openOrder);

                }
            }

            @Override
            public void onFail(String error) {
                Log.e("orderService", error);
            }
        });
//        if (openOrder != null && previousOpenOrder != null && openOrder.getResult() != null && previousOpenOrder.getResult() != null)
//        {
//
//        }

    }

}
