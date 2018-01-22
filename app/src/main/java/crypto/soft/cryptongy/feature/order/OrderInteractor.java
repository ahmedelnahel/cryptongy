package crypto.soft.cryptongy.feature.order;

import android.os.AsyncTask;
import android.text.TextUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import crypto.soft.cryptongy.feature.shared.json.action.Cancel;
import crypto.soft.cryptongy.feature.shared.json.openorder.OpenOrder;
import crypto.soft.cryptongy.feature.shared.json.openorder.Result;
import crypto.soft.cryptongy.feature.shared.json.order.Order;
import crypto.soft.cryptongy.feature.shared.json.orderhistory.OrderHistory;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.feature.shared.module.Account;
import crypto.soft.cryptongy.network.BittrexServices;
import crypto.soft.cryptongy.utils.GlobalConstant;

/**
 * Created by tseringwongelgurung on 11/24/17.
 */

public class OrderInteractor {
    public void getOpenOrder(final String coinName, final Account account, final OnFinishListner<OpenOrder> listner) {
        new AsyncTask<Void, Void, OpenOrder>() {

            @Override
            protected OpenOrder doInBackground(Void... voids) {
                OpenOrder openOrder = null;
//                try {
                    if(account.getExchange().equalsIgnoreCase(GlobalConstant.Exchanges.BITTREX)){

                        openOrder = getMockOpnOrders(account,coinName);
//                        openOrder = new BittrexServices().getOpnOrders(account);
                    }
                    if(account.getExchange().equalsIgnoreCase(GlobalConstant.Exchanges.BINANCE)){

                        openOrder = getMockOpnOrders(account,coinName);
//                        openOrder=new BinanceServices().getOpnOrders(account,coinName);
                    }

                    if (openOrder != null && openOrder.getSuccess()) {
                        if (TextUtils.isEmpty(coinName))
                            return openOrder;
                        else {

                            Iterator<Result> iterator = openOrder.getResult().iterator();
                            while (iterator.hasNext()) {
                                Result result = iterator.next();
                                if (!result.getExchange().equals(coinName))
                                    iterator.remove();
                            }
                            return openOrder;
                        }
                    }

//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                return openOrder;
            }


            @Override
            protected void onPostExecute(OpenOrder openOrder) {
                super.onPostExecute(openOrder);
                if (openOrder == null)
                    listner.onFail("Failed to fetch data");
                else if (openOrder.getSuccess().booleanValue())
                    listner.onComplete(openOrder);
                else
                    listner.onFail(openOrder.getMessage());
            }
        }.execute();
    }

    public void getOrderHistory(final String coinName, final Account account, final OnFinishListner<OrderHistory> listner) {
        new AsyncTask<Void, Void, OrderHistory>() {

            @Override
            protected OrderHistory doInBackground(Void... voids) {
                OrderHistory orderHistory = null;
//                try {

                    if(account.getExchange().equalsIgnoreCase(GlobalConstant.Exchanges.BITTREX)){

//                        orderHistory = new BittrexServices().getOrderHistory(account);
                        orderHistory = getMockOrderHistory(account,coinName);
                    }
                    if(account.getExchange().equalsIgnoreCase(GlobalConstant.Exchanges.BINANCE)){

                        orderHistory = getMockOrderHistory(account,coinName);
                    }




                    if (orderHistory != null && orderHistory.getSuccess()) {
                        if (TextUtils.isEmpty(coinName))
                            return orderHistory;
                        else {

                            Iterator<crypto.soft.cryptongy.feature.shared.json.orderhistory.Result> iterator = orderHistory.getResult().iterator();
                            while (iterator.hasNext()) {
                                crypto.soft.cryptongy.feature.shared.json.orderhistory.Result result = iterator.next();
                                if (!result.getExchange().equals(coinName))
                                    iterator.remove();
                            }
                            return orderHistory;
                        }
                    }
//                }
//                catch (IOException e) {
//                    e.printStackTrace();
//                }
                return orderHistory;
            }

            @Override
            protected void onPostExecute(OrderHistory orderHistory) {
                super.onPostExecute(orderHistory);
                if (orderHistory == null)
                    listner.onFail("Failed to fetch data");
                else if (orderHistory.getSuccess().booleanValue())
                    listner.onComplete(orderHistory);
                else
                    listner.onFail(orderHistory.getMessage());
            }
        }.execute();
    }

    private crypto.soft.cryptongy.feature.shared.json.binance.order.Result getResultMockValue(){
        crypto.soft.cryptongy.feature.shared.json.binance.order.Result result=new crypto.soft.cryptongy.feature.shared.json.binance.order.Result();
        result.setSymbol("LTCBTC");
        result.setOrderId(1L);
        result.setClientOrderId("myOrder1");
        result.setPrice("0.1");
        result.setOrigQty("1.0");
        result.setExecutedQty("0.0");
        result.setStatus("NEW");
        result.setTimeInForce("GTC");
        result.setType("LIMIT");
        result.setSide("BUY");
        result.setStopPrice("0.0");
        result.setIcebergQty("0.0");
        result.setTime(1499827319559L);
        return result;

    }
    private OrderHistory getMockOrderHistory(Account account, String coinName) {

        OrderHistory orderHistory=new OrderHistory();
        orderHistory.setSuccess(true);
        orderHistory.setMessage("mock object");

        ArrayList<crypto.soft.cryptongy.feature.shared.json.orderhistory.Result> resultsarray = new ArrayList();
        crypto.soft.cryptongy.feature.shared.json.orderhistory.Result result1= new crypto.soft.cryptongy.feature.shared.json.orderhistory.Result(getResultMockValue());
        resultsarray.add(result1);

        orderHistory.setResult(resultsarray);
        return orderHistory;
    }

    private OpenOrder getMockOpnOrders(Account account, String coinName) {

        OpenOrder openOrder=new OpenOrder();
        openOrder.setSuccess(true);
        openOrder.setMessage("mock object");

        ArrayList<crypto.soft.cryptongy.feature.shared.json.openorder.Result> resultsArray = new ArrayList();
        crypto.soft.cryptongy.feature.shared.json.openorder.Result or = new crypto.soft.cryptongy.feature.shared.json.openorder.Result(getResultMockValue());
        resultsArray.add(or);


        openOrder.setResult(resultsArray);
        return openOrder;
    }

    public void cancleOrder(final String uuid, final String coinName, final Account account, final OnFinishListner<Cancel> listner) {
        new AsyncTask<Void, Void, Cancel>() {

            @Override
            protected Cancel doInBackground(Void... voids) {
                try {

                    if(account.getExchange().equalsIgnoreCase(GlobalConstant.Exchanges.BITTREX)){

                        return new BittrexServices().cancelOrder(uuid, account);
                    }
//                    if(account.getExchange().equalsIgnoreCase(GlobalConstant.Exchanges.BINANCE)){
//
//                        return new BinanceServices().cancelOrder(uuid, account);
//                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Cancel cancel) {
                super.onPostExecute(cancel);
                if (cancel == null)
                    listner.onFail("Failed to cancle data");
                else if (cancel.getSuccess().booleanValue())

                    listner.onComplete(cancel);
                else
                    listner.onFail(cancel.getMessage());
            }
        }.execute();
    }

    public void getOrders(String orderUuid, Account account,  OnFinishListner< Order> listner) {
        try {

            Order order=null;
            if(account.getExchange().equalsIgnoreCase(GlobalConstant.Exchanges.BITTREX)){

                 order = new BittrexServices().getOrder(orderUuid, account);
            }
//            if(account.getExchange().equalsIgnoreCase(GlobalConstant.Exchanges.BINANCE)){
//                 order = new BinanceServices().getOrder(orderUuid, account);
//
//            }
//


            if (order == null || !order.getSuccess())
                listner.onFail(order.getMessage());
            else if (order.getSuccess().booleanValue())
                listner.onComplete(order);
            else
                listner.onFail(order.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            listner.onFail(e.getMessage());
        }
    }




}
