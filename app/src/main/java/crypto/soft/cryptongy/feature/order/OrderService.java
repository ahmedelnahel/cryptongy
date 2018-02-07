package crypto.soft.cryptongy.feature.order;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import crypto.soft.cryptongy.feature.shared.json.openorder.OpenOrder;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.utils.CoinApplication;
import crypto.soft.cryptongy.utils.GlobalConstant;
import crypto.soft.cryptongy.utils.GlobalUtil;

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
            getOpenOrder(application, false,exchangeBittrix);
        }
        else {
            checkOrder(order,application.getOpenOrder(), application,exchangeBittrix);
        }

        if(orderBinance==null){
            getOpenOrder(application,false,exchangeBinance);
        }else {
            checkOrder(order,application.getOpenOrderBinance(),application,exchangeBinance);
        }


    }

    private void getOpenOrder(final CoinApplication application, final boolean check, final String exchangeValue) {

        if(application!=null){
            if(application.getReadAccount(exchangeValue)!=null){
//                exchangeValue=application.getReadAccount().getExchange();
            }
        }


        interactor.getOpenOrder("",exchangeValue, application.getReadAccount(exchangeValue), new OnFinishListner<OpenOrder>() {
            @Override
            public void onComplete(OpenOrder result) {
                if(result != null && result.getSuccess()&& result.getResult()!= null) {
                    if(exchangeValue.equalsIgnoreCase(GlobalConstant.Exchanges.BITTREX)){

                        if (check)
                        {
                            checkOrder(result, application.getOpenOrder(),application,exchangeValue);
                        }
                        else {
                            application.setOpenOrder(result);
                        }
                    }
                    if(exchangeValue.equalsIgnoreCase(GlobalConstant.Exchanges.BINANCE)){


                        if(check){

                            checkOrder(result,application.getOpenOrderBinance(),application,exchangeValue);
                        }
                        else {
                            application.setOpenOrderBinance(result);
                        }
                    }

                }
            }

            @Override
            public void onFail(String error) {
                Log.e("orderService", error);
            }
        });



    }


    private void checkOrder(final OpenOrder openOrder,final OpenOrder previousOpenOrder, final CoinApplication application, final String exchangeValue) {

        for (int i = 0; i < openOrder.getResult().size(); i++) {
            boolean findOrder = false;

            for (int j = 0; j < previousOpenOrder.getResult().size(); j++) {


                if (openOrder.getResult().get(i).getOrderUuid().equalsIgnoreCase(previousOpenOrder.getResult().get(j).getOrderUuid())) {
                    findOrder = true;
                    break;
                }

            }

            if(findOrder==false){
                GlobalUtil.showNotification(OrderService.this, "Order Status",  " Order is closed.", i);
                openOrder.setChange(true);

            }

        }

        if(openOrder.isChange()) {
            getOpenOrder(application, false,exchangeValue);
            openOrder.setChange(false);
        }
    }


}
