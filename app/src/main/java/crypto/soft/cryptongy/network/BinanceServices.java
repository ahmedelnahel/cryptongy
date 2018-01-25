package crypto.soft.cryptongy.network;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import crypto.soft.cryptongy.common.RESTUtil;
import crypto.soft.cryptongy.feature.coin.BnSocketOrders;
import crypto.soft.cryptongy.feature.shared.json.action.Cancel;
import crypto.soft.cryptongy.feature.shared.json.binance.cancel.BnCancel;
import crypto.soft.cryptongy.feature.shared.json.binance.marketsummary.BinanceMarket;
import crypto.soft.cryptongy.feature.shared.json.binance.order.BnNewOrder;
import crypto.soft.cryptongy.feature.shared.json.binance.order.BnOrders;
import crypto.soft.cryptongy.feature.shared.json.binance.time.BnTime;
import crypto.soft.cryptongy.feature.shared.json.limitorder.LimitOrder;
import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.json.market.Result;
import crypto.soft.cryptongy.feature.shared.json.marketsummary.MarketSummary;
import crypto.soft.cryptongy.feature.shared.json.openorder.OpenOrder;
import crypto.soft.cryptongy.feature.shared.json.order.Order;
import crypto.soft.cryptongy.feature.shared.json.orderhistory.OrderHistory;
import crypto.soft.cryptongy.feature.shared.json.ticker.Ticker;
import crypto.soft.cryptongy.feature.shared.json.wallet.Wallet;
import crypto.soft.cryptongy.feature.shared.module.Account;
import io.reactivex.subjects.PublishSubject;

import static android.content.ContentValues.TAG;


/**
 * Created by noni on 26/10/2017.
 * https://bittrex.com/api/v1.1/account/getbalances
 */

public class BinanceServices {


    public MarketSummaries getMarketSummaries() throws IOException {
        final String url = "https://api.binance.com/api/v1/ticker/24hr";  //"https://www.coinexchange.io/api/v1/getmarkets";
        String marketSummariesStr = new RESTUtil().callREST(url);
        MarketSummaries marketSummaries_ = new MarketSummaries();
        Log.d("Binance MarketSummaries", "response " + marketSummariesStr);
        if (marketSummariesStr == null || "".equals(marketSummariesStr)) {
            Log.d("Binance MarketSummaries", "response in error " + marketSummariesStr);
            marketSummaries_.setSuccess(false);
            marketSummaries_.setMessage("Connection Error");
        } else {
            if (marketSummariesStr != null && !marketSummariesStr.contains("msg"))
                marketSummariesStr = "{\"result\":" + marketSummariesStr + " }";
            ObjectMapper mapper = new ObjectMapper();
            BinanceMarket binanceMarket = mapper.readValue(marketSummariesStr, BinanceMarket.class);

            marketSummaries_.setJson(marketSummariesStr);
            marketSummaries_.setSuccess(true);


            if (binanceMarket.getMsg() == null || "".equals(binanceMarket.getMsg())) {
                HashMap<String, Result> coinsMap = new HashMap<>();
                ArrayList<Result> results = new ArrayList();
                for (crypto.soft.cryptongy.feature.shared.json.binance.marketsummary.Result r : binanceMarket.getResult()) {
                    Result mr = new Result(r);
                    results.add(mr);
                    coinsMap.put(r.getSymbol(), mr);
                }

                marketSummaries_.setCoinsMap(coinsMap);
                marketSummaries_.setResult(results);
            } else {
                marketSummaries_.setSuccess(false);
                marketSummaries_.setMessage(binanceMarket.getMsg());

            }
        }
        return marketSummaries_;
    }

    //


    public MarketSummary getMarketSummary(String market) throws IOException {
        final String url = "https://api.binance.com/api/v1/ticker/24hr?symbol=" + market;
        String marketSummaryStr = new RESTUtil().callREST(url);
        MarketSummary marketSummary_ = new MarketSummary();
        if (marketSummaryStr == null || "".equals(marketSummaryStr)) {
            marketSummary_.setSuccess(false);
            marketSummary_.setMessage("Connection Error");
        } else {
            ObjectMapper mapper = new ObjectMapper();
            crypto.soft.cryptongy.feature.shared.json.binance.marketsummary.Result r = mapper.readValue(marketSummaryStr, crypto.soft.cryptongy.feature.shared.json.binance.marketsummary.Result.class);
            if (r.getMsg() == null || "".equals(r.getMsg())) {
                crypto.soft.cryptongy.feature.shared.json.marketsummary.Result mr = new crypto.soft.cryptongy.feature.shared.json.marketsummary.Result(r);
                List rl = new ArrayList();
                rl.add(mr);
                marketSummary_.setResult(rl);
                marketSummary_.setJson(marketSummaryStr);
                marketSummary_.setSuccess(true);
            } else {
                marketSummary_.setSuccess(false);
                marketSummary_.setMessage(r.getMsg());

            }
            Log.i("MarketSummary", marketSummaryStr);
        }
        return marketSummary_;
    }


//    public Ticker getTickerConnectSocket() throws Exception {
//        String websocketEndPointUrl;
//        WebSocketClient mWebSocketClient;
//
//        URI uri;
//        try {
//
//            websocketEndPointUrl= "wss://stream.binance.com:9443/ws/!ticker@arr";
//            Log.i(TAG, " WSURL: " + websocketEndPointUrl);
//
//            uri = new URI(websocketEndPointUrl);
//        } catch (URISyntaxException e) {
//            Log.e(TAG, e.getMessage());
//            return null;
//        }
//
//        mWebSocketClient = new WebSocketClient(uri)
//        {
//            @Override
//            public void onOpen(ServerHandshake serverHandshake) {
//                Log.i("Websocket", "Opened");
//            }
//
//            @Override
//            public void onMessage(String s) {
//                Log.d(TAG, "onMessage: websocket : "+s);
//
//
//                Ticker ticker=new Ticker();
//                if(s == null || "".equals(s)) {
//                    ticker.setSuccess(false);
//                    ticker.setMessage("Connection Error");
//                }
//                else
//                {
//
//
//                    ObjectMapper mapper = new ObjectMapper();
//                    BnSocketOrders[]  r = null;
//                    try {
//                        r = mapper.readValue(s, BnSocketOrders[].class);
//
//                        Log.d(TAG, "onMessage: r"+ r[0].getSymbol());
//
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
////                    if (r.get() == null || "".equals(r.getMsg())) {
////                        ticker.setBinanceResult(r[]);
////                        ticker.setJson(tickerStr);
////                        ticker.setSuccess(true);
////                    }
////                    else
////                    {
////                        ticker.setSuccess(false);
////                        ticker.setMessage(r.getMsg());
////
////                    }
//                }
//
//                //final String message =s;
//
//            }
//
//            @Override
//            public void onClose(int i, String s, boolean b) {
//                Log.i("Websocket", "Closed " + s);
//            }
//
//            @Override
//            public void onError(Exception e) {
//                Log.i("Websocket", "Error " + e.getMessage());
//            }
//        };
//
//
//
//        mWebSocketClient.connect();
//    }


    URI uri;
    Ticker ticker;

    public WebSocketClient mWebSocketClient;

    public PublishSubject<WebSocketClient> sourceWebSocketClient = PublishSubject.create();
    public PublishSubject<Ticker> source = PublishSubject.create();

    public void getTickerConnectSocket(String market) throws Exception {
        String websocketEndPointUrl;

        ticker = new Ticker();


        try {

            websocketEndPointUrl = "wss://stream.binance.com:9443/ws/" + market.trim().toLowerCase() + "@ticker";
            Log.i(TAG, " WSURL: " + websocketEndPointUrl);

            uri = new URI(websocketEndPointUrl);
        } catch (URISyntaxException e) {
            Log.e(TAG, e.getMessage());
            return ;
        }

         mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
            }

            @Override
            public void onMessage(String s) {
                Log.d(TAG, "onMessage: websocket : " + s);

                if (s == null || "".equals(s)) {
                    ticker.setSuccess(false);
                    ticker.setMessage("Connection Error");
                } else {


                    ObjectMapper mapper = new ObjectMapper();
                    BnSocketOrders r = null;
                    try {
                        r = mapper.readValue(s, BnSocketOrders.class);

                        Log.d(TAG, "onMessage: " + r.getSymbol());

                        ticker.setBinanceResultWebSocket(r);
                        ticker.setJson(s);
                        ticker.setSuccess(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }


                source.onNext(ticker);
                sourceWebSocketClient.onNext(mWebSocketClient);
                //final String message =s;

            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };


        mWebSocketClient.connect();


    }


    public Ticker getTicker(String market) throws IOException {
        final String url = "https://api.binance.com/api/v1/ticker/24hr?symbol=" + market;
        Ticker ticker = new Ticker();
        String tickerStr = new RESTUtil().callREST(url);
        if (tickerStr == null || "".equals(tickerStr)) {
            ticker.setSuccess(false);
            ticker.setMessage("Connection Error");
        } else {

            ObjectMapper mapper = new ObjectMapper();
            crypto.soft.cryptongy.feature.shared.json.binance.marketsummary.Result r = mapper.readValue(tickerStr, crypto.soft.cryptongy.feature.shared.json.binance.marketsummary.Result.class);
            if (r.getMsg() == null || "".equals(r.getMsg())) {
                ticker.setBinanceResult(r);
                ticker.setJson(tickerStr);
                ticker.setSuccess(true);
            } else {
                ticker.setSuccess(false);
                ticker.setMessage(r.getMsg());

            }
        }
        Log.i("MarketSummary", tickerStr);

        return ticker;
    }

    public OpenOrder getOpnOrders(Account account, String coinName) throws IOException {
        OpenOrder openOrder = new OpenOrder();
        if (account == null) {
            openOrder.setSuccess(false);
            openOrder.setMessage("No API");
        } else {

            final String url = "https://api.binance.com/api/v3/openOrders";
            HashMap param = null;

            if (coinName != null && !StringUtils.isEmpty(coinName)) {
                param = new HashMap();
                param.put("symbol", coinName);
            }
            String ordersStr = new RESTUtil().callRestHttpClient(url, account.getApiKey(), account.getSecret(), param, "HmacSHA256", null);
            openOrder = new OpenOrder();
            if (ordersStr == null) {
                openOrder.setSuccess(false);
                openOrder.setMessage("Connection Error");
            } else {
                if (ordersStr != null && !ordersStr.contains("msg"))
                    ordersStr = "{\"result\":" + ordersStr + " }";
                ObjectMapper mapper = new ObjectMapper();
                BnOrders bnopenOrder = mapper.readValue(ordersStr, BnOrders.class);
                openOrder.setJson(ordersStr);
                Log.i("response ", openOrder.getSuccess() + openOrder.getJson());
                if (bnopenOrder != null && StringUtils.isEmpty(bnopenOrder.getMsg()) && bnopenOrder.getResult() != null) {
                    openOrder.setSuccess(true);
                    ArrayList<crypto.soft.cryptongy.feature.shared.json.openorder.Result> results = new ArrayList();
                    for (crypto.soft.cryptongy.feature.shared.json.binance.order.Result r : bnopenOrder.getResult()) {
                        crypto.soft.cryptongy.feature.shared.json.openorder.Result or = new crypto.soft.cryptongy.feature.shared.json.openorder.Result(r);
                        results.add(or);
                    }
                    openOrder.setResult(results);
                } else {
                    openOrder.setSuccess(false);
                    openOrder.setMessage("Error: " + bnopenOrder.getMsg());
                }
            }
        }
        return openOrder;
    }

    private BnTime getServerTime() throws IOException {
        BnTime time = null;
        String timeurl = "https://api.binance.com/api/v1/time";
        String timestr = new RESTUtil().callREST(timeurl);
        if (!StringUtils.isEmpty(timestr)) {
            ObjectMapper mapper = new ObjectMapper();
            time = mapper.readValue(timestr, BnTime.class);

        }
        return time;
    }


    public Wallet getWallet(Account account) throws IOException {
        Wallet wallet = null;

        if (account == null) {
            wallet = new Wallet();
            wallet.setSuccess(false);
            wallet.setMessage("Connection Error");
        } else {
            final String url = "https://bittrex.com/api/v1.1/account/getbalances";
            String walletStr = new RESTUtil().callRestHttpClient(url, account.getApiKey(), account.getSecret());

            if (walletStr == null) {
                wallet = new Wallet();
                wallet.setSuccess(false);
                wallet.setMessage("Connection Error");
            } else {
                ObjectMapper mapper = new ObjectMapper();
                wallet = mapper.readValue(walletStr, Wallet.class);
                wallet.setJson(walletStr);
//        Log.i("response " , wallet.getSuccess() + wallet.getJson());
                if (wallet.getSuccess()) {
                    HashMap<String, crypto.soft.cryptongy.feature.shared.json.wallet.Result> coinsMap = new HashMap<>();
                    for (crypto.soft.cryptongy.feature.shared.json.wallet.Result r : wallet.getResult()) {
                        if (r.getBalance() != 0)
                            coinsMap.put(r.getCurrency(), r);
                    }
                    wallet.setCoinsMap(coinsMap);
                }
            }
        }
        return wallet;
    }

    public OrderHistory getOrderHistory(Account account, String coinName) throws IOException {
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setSuccess(false);
        if (account == null) {
            orderHistory.setSuccess(false);
            orderHistory.setMessage("No API");
        } else {

            final String url = "https://api.binance.com/api/v3/allOrders";
            HashMap param = null;

            if (coinName != null && !StringUtils.isEmpty(coinName)) {
                param = new HashMap();
                param.put("symbol", coinName);

                String ordersStr = new RESTUtil().callRestHttpClient(url, account.getApiKey(), account.getSecret(), param, "HmacSHA256", null);
                orderHistory = new OrderHistory();
                if (ordersStr == null) {
                    orderHistory.setSuccess(false);
                    orderHistory.setMessage("Connection Error");
                } else {
                    if (ordersStr != null && !ordersStr.contains("msg"))
                        ordersStr = "{\"result\":" + ordersStr + " }";
                    ObjectMapper mapper = new ObjectMapper();
                    BnOrders bnopenOrder = mapper.readValue(ordersStr, BnOrders.class);
                    orderHistory.setJson(ordersStr);
                    Log.i("response ", orderHistory.getJson());
                    if (bnopenOrder != null && StringUtils.isEmpty(bnopenOrder.getMsg()) && bnopenOrder.getResult() != null) {
                        orderHistory.setSuccess(true);
                        ArrayList<crypto.soft.cryptongy.feature.shared.json.orderhistory.Result> results = new ArrayList();
                        for (crypto.soft.cryptongy.feature.shared.json.binance.order.Result r : bnopenOrder.getResult()) {
                            if (r.getStatus().equals("FILLED")) {
                                crypto.soft.cryptongy.feature.shared.json.orderhistory.Result or = new crypto.soft.cryptongy.feature.shared.json.orderhistory.Result(r);
                                results.add(or);
                            }
                        }
                        orderHistory.setResult(results);
                    } else {
                        orderHistory.setSuccess(false);
                        orderHistory.setMessage("Error: " + bnopenOrder.getMsg());
                    }
                }
            } else {
                orderHistory.setSuccess(true);
                orderHistory.setResult(new ArrayList());
            }
        }
        return orderHistory;
    }


    public Cancel cancelOrder(String uuid, String coinName, Account account) throws IOException {
        Cancel cancel = new Cancel();
        if (account == null) {
            cancel.setSuccess(false);
            cancel.setMessage("No API");
        } else {
            final String url = "https://api.binance.com/api/v3/order";
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("symbol", coinName);
            params.put("orderId", uuid);
            String cancelStr = new RESTUtil().callRestHttpClient(url, account.getApiKey(), account.getSecret(), params, "HmacSHA256", "DELETE");

            if (cancelStr == null) {
                cancel = new Cancel();
                cancel.setSuccess(false);
                cancel.setMessage("Connection Error");
            } else {
                ObjectMapper mapper = new ObjectMapper();
                BnCancel bnCancel = mapper.readValue(cancelStr, BnCancel.class);
                if (!StringUtils.isEmpty(bnCancel.getMsg())) {
                    cancel.setSuccess(false);
                    cancel.setMessage(bnCancel.getMsg());
                } else
                    cancel.setSuccess(true);
            }
        }
        return cancel;

    }

    /**
     * @param market
     * @param quantity
     * @param rate
     * @param orderType accepts BUY or SELL
     * @param account
     * @return
     * @throws IOException
     */
    public LimitOrder newOrder(String market, String quantity, String rate, String orderType, Account account) throws IOException {
        LimitOrder limitOrder = new LimitOrder();
        if (account == null) {
            limitOrder.setSuccess(false);
            limitOrder.setMessage("No API");
        } else {
            final String url = "https://api.binance.com/api/v3/order";
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("symbol", market);
            params.put("quantity", quantity);
            params.put("price", rate);
            params.put("side", orderType);
            params.put("type", "LIMIT");
            params.put("timeInForce", "GTC");
            String buyLimitStr = new RESTUtil().callRestHttpClient(url, account.getApiKey(), account.getSecret(), params, "HmacSHA256", "POST");

            if (buyLimitStr == null) {
                limitOrder.setSuccess(false);
                limitOrder.setMessage("Connection Error");
            } else {
                ObjectMapper mapper = new ObjectMapper();
                BnNewOrder bnNewOrder = mapper.readValue(buyLimitStr, BnNewOrder.class);
                if (!StringUtils.isEmpty(bnNewOrder.getMsg())) {
                    limitOrder.setSuccess(false);
                    limitOrder.setMessage(bnNewOrder.getMsg());
                } else
                    limitOrder.setSuccess(true);
            }
        }
        return limitOrder;

    }


    public Order getOrder(String orderUUID, String market, Account account) throws IOException {
        Order order = new Order();
        if (account == null) {
            order.setSuccess(false);
            order.setMessage("No API");
        } else {
            final String url = "https://api.binance.com/api/v3/order";
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("symbol", market);
            params.put("orderId", orderUUID);

            String orderStr = new RESTUtil().callRestHttpClient(url, account.getApiKey(), account.getSecret(), params, "HmacSHA256", "GET");

            if (orderStr == null) {
                order.setSuccess(false);
                order.setMessage("Connection Error");
            } else {
                ObjectMapper mapper = new ObjectMapper();
                BnOrders bnOrder = mapper.readValue(orderStr, BnOrders.class);
                if (!StringUtils.isEmpty(bnOrder.getMsg())) {
                    order.setSuccess(false);
                    order.setMessage(bnOrder.getMsg());
                } else {
                    order.setSuccess(true);

                    for (crypto.soft.cryptongy.feature.shared.json.binance.order.Result r : bnOrder.getResult()) {

                        crypto.soft.cryptongy.feature.shared.json.order.Result or = new crypto.soft.cryptongy.feature.shared.json.order.Result(r);
                        order.setResult(or);

                    }

                }

            }
        }
        return order;

    }


}
