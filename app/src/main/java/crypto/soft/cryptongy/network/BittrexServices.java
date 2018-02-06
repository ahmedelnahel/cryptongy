package crypto.soft.cryptongy.network;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;

import crypto.soft.cryptongy.common.RESTUtil;
import crypto.soft.cryptongy.feature.shared.json.action.Cancel;
import crypto.soft.cryptongy.feature.shared.json.limitorder.LimitOrder;
import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.json.market.Result;
import crypto.soft.cryptongy.feature.shared.json.markethistory.MarketHistory;
import crypto.soft.cryptongy.feature.shared.json.marketsummary.MarketSummary;
import crypto.soft.cryptongy.feature.shared.json.openorder.OpenOrder;
import crypto.soft.cryptongy.feature.shared.json.order.Order;
import crypto.soft.cryptongy.feature.shared.json.orderhistory.OrderHistory;
import crypto.soft.cryptongy.feature.shared.json.ticker.Ticker;
import crypto.soft.cryptongy.feature.shared.json.wallet.Wallet;
import crypto.soft.cryptongy.feature.shared.module.Account;
import crypto.soft.cryptongy.feature.wallet.WalletFragment;
import crypto.soft.cryptongy.utils.GlobalConstant;


/**
 * Created by noni on 26/10/2017.
 * https://bittrex.com/api/v1.1/account/getbalances
 */

public class BittrexServices {


    public  static String TAG="BittrexServices";
    public MarketSummaries getMarketSummaries() throws IOException {
        final String url = "https://bittrex.com/api/v1.1/public/getmarketsummaries";  //"https://www.coinexchange.io/api/v1/getmarkets";
        Log.d(TAG, "marketSummaries: "+ url);
        String marketSummariesStr = new RESTUtil().callREST(url);
        MarketSummaries marketSummaries_ = null;
        if(marketSummariesStr == null) {
            marketSummaries_ = new MarketSummaries();
            marketSummaries_.setSuccess(false);
            marketSummaries_.setMessage("Connection Error");
        }
        else {
            ObjectMapper mapper = new ObjectMapper();
            marketSummaries_ = mapper.readValue(marketSummariesStr, MarketSummaries.class);
            marketSummaries_.setJson(marketSummariesStr);
//        Log.i("MarketSummaries", marketSummariesStr);

            if (marketSummaries_.getSuccess()) {
                HashMap<String, Result> coinsMap = new HashMap<>();
                for (Result r : marketSummaries_.getResult()) {
                    coinsMap.put(r.getMarketName(), r);
                }
                marketSummaries_.setCoinsMap(coinsMap);
            }
        }
        return marketSummaries_;
    }

    //
    public Wallet getWallet(Account account) throws IOException {
        Wallet wallet = null;

        if(account == null) {
            wallet = new Wallet();
            wallet.setSuccess(false);
            wallet.setMessage("Connection Error");
        }
        else {
            final String url = "https://bittrex.com/api/v1.1/account/getbalances";
            Log.d(TAG, "getWallet: "+url);
          String walletStr = new RESTUtil().callRestHttpClient(url, account.getApiKey(), account.getSecret());
//            String walletStr = " {\n" +
//                    "\t\"success\" : true,\n" +
//                    "\t\"message\" : \"\",\n" +
//                    "\t\"result\" : [{\n" +
//                    "\t\t\t\"Currency\" : \"DOGE\",\n" +
//                    "\t\t\t\"Balance\" : 0.00000000,\n" +
//                    "\t\t\t\"Available\" : 0.00000000,\n" +
//                    "\t\t\t\"Pending\" : 0.00000000,\n" +
//                    "\t\t\t\"CryptoAddress\" : \"DLxcEt3AatMyr2NTatzjsfHNoB9NT62HiF\",\n" +
//                    "\t\t\t\"Requested\" : false,\n" +
//                    "\t\t\t\"Uuid\" : null\n" +
//                    "\n" +
//                    "\t\t}, {\n" +
//                    "\t\t\t\"Currency\" : \"BTC\",\n" +
//                    "\t\t\t\"Balance\" : 14.21549076,\n" +
//                    "\t\t\t\"Available\" : 14.21549076,\n" +
//                    "\t\t\t\"Pending\" : 0.00000000,\n" +
//                    "\t\t\t\"CryptoAddress\" : \"1Mrcdr6715hjda34pdXuLqXcju6qgwHA31\",\n" +
//                    "\t\t\t\"Requested\" : false,\n" +
//                    "\t\t\t\"Uuid\" : null\n" +
//                    "\t\t}\n" +
//                    "\t]\n" +
//                    "}";

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
                            r.setAdditionalProperty(WalletFragment.EXCHANGE_VALUE, GlobalConstant.Exchanges.BITTREX);
                    }
                    wallet.setCoinsMap(coinsMap);
                }
            }
        }
        return wallet;
    }

    public MarketSummary getMarketSummary(String market) throws IOException {
        final String url = "https://bittrex.com/api/v1.1/public/getmarketsummary?market="+market;
        String marketSummaryStr = new RESTUtil().callREST(url);
        MarketSummary marketSummary_= null;
        if(marketSummaryStr == null) {
            marketSummary_ = new MarketSummary();
            marketSummary_.setSuccess(false);
            marketSummary_.setMessage("Connection Error");
        }
        else {
            ObjectMapper mapper = new ObjectMapper();
            marketSummary_ = mapper.readValue(marketSummaryStr, MarketSummary.class);
            marketSummary_.setJson(marketSummaryStr);
//        Log.i("MarketSummary", marketSummaryStr);
        }
        return marketSummary_;
    }

    public OpenOrder getOpnOrders( Account account) throws IOException {
        OpenOrder openOrder = null;

        if(account == null)
        {
            openOrder = new OpenOrder();
            openOrder.setSuccess(false);
            openOrder.setMessage("No API");
        }
        else {
            final String url = "https://bittrex.com/api/v1.1/market/getopenorders";
            Log.d(TAG, "getOpnOrders: " + url);
            String ordersStr = new RESTUtil().callRestHttpClient(url, account.getApiKey(), account.getSecret());
//            String ordersStr = "   {\n" +
//                    "\t\"success\" : true,\n" +
//                    "\t\"message\" : \"\",\n" +
//                    "\t\"result\" : [{\n" +
//                    "\t\t\t\"Uuid\" : null,\n" +
//                    "\t\t\t\"OrderUuid\" : \"09aa5bb6-8232-41aa-9b78-a5a1093e0211\",\n" +
//                    "\t\t\t\"Exchange\" : \"BTC-LTC\",\n" +
//                    "\t\t\t\"OrderType\" : \"LIMIT_SELL\",\n" +
//                    "\t\t\t\"Quantity\" : 5.00000000,\n" +
//                    "\t\t\t\"QuantityRemaining\" : 5.00000000,\n" +
//                    "\t\t\t\"Limit\" : 2.00000000,\n" +
//                    "\t\t\t\"CommissionPaid\" : 0.00000000,\n" +
//                    "\t\t\t\"Price\" : 0.00000000,\n" +
//                    "\t\t\t\"PricePerUnit\" : null,\n" +
//                    "\t\t\t\"Opened\" : \"2014-07-09T03:55:48.77\",\n" +
//                    "\t\t\t\"Closed\" : null,\n" +
//                    "\t\t\t\"CancelInitiated\" : false,\n" +
//                    "\t\t\t\"ImmediateOrCancel\" : false,\n" +
//                    "\t\t\t\"IsConditional\" : false,\n" +
//                    "\t\t\t\"Condition\" : null,\n" +
//                    "\t\t\t\"ConditionTarget\" : null\n" +
//                    "\t\t}, {\n" +
//                    "\t\t\t\"Uuid\" : null,\n" +
//                    "\t\t\t\"OrderUuid\" : \"8925d746-bc9f-4684-b1aa-e507467aaa99\",\n" +
//                    "\t\t\t\"Exchange\" : \"BTC-LTC\",\n" +
//                    "\t\t\t\"OrderType\" : \"LIMIT_BUY\",\n" +
//                    "\t\t\t\"Quantity\" : 100000.00000000,\n" +
//                    "\t\t\t\"QuantityRemaining\" : 100000.00000000,\n" +
//                    "\t\t\t\"Limit\" : 0.00000001,\n" +
//                    "\t\t\t\"CommissionPaid\" : 0.00000000,\n" +
//                    "\t\t\t\"Price\" : 0.00000000,\n" +
//                    "\t\t\t\"PricePerUnit\" : null,\n" +
//                    "\t\t\t\"Opened\" : \"2014-07-09T03:55:48.583\",\n" +
//                    "\t\t\t\"Closed\" : null,\n" +
//                    "\t\t\t\"CancelInitiated\" : false,\n" +
//                    "\t\t\t\"ImmediateOrCancel\" : false,\n" +
//                    "\t\t\t\"IsConditional\" : false,\n" +
//                    "\t\t\t\"Condition\" : null,\n" +
//                    "\t\t\t\"ConditionTarget\" : null\n" +
//                    "\t\t}\n" +
//                    "\t]\n" +
//                    "}\n";
            if (ordersStr == null) {
                openOrder = new OpenOrder();
                openOrder.setSuccess(false);
                openOrder.setMessage("Connection Error");
            } else {
                ObjectMapper mapper = new ObjectMapper();
                openOrder = mapper.readValue(ordersStr, OpenOrder.class);
                openOrder.setJson(ordersStr);
//        Log.i("response " , wallet.getSuccess() + wallet.getJson());
            }
        }
        return openOrder;
    }


    public Ticker getTicker(String market) throws IOException {
        final String url = "https://bittrex.com/api/v1.1/public/getticker?market="+market;
        Log.d(TAG, "getTicker: " + url);
        Ticker ticker = null;
        String tickerStr = new RESTUtil().callREST(url);
        if(tickerStr == null) {
            ticker = new Ticker();
            ticker.setSuccess(false);
            ticker.setMessage("Connection Error");
        }
        else
        {
            ObjectMapper mapper = new ObjectMapper();
            ticker = mapper.readValue(tickerStr, Ticker.class);
            ticker.setJson(tickerStr);
        }
//        Log.i("MarketSummary", tickerStr);

        return ticker;
    }


    public OrderHistory getOrderHistory(Account account) throws IOException {
        OrderHistory orderHistory = null;

        if(account == null)
        {
            orderHistory = new OrderHistory();
            orderHistory.setSuccess(false);
            orderHistory.setMessage("No API");
        }
        else {
            final String url = "https://bittrex.com/api/v1.1/account/getorderhistory";
            Log.d(TAG, "getOrderHistory: "+url);
            String ordersStr = new RESTUtil().callRestHttpClient(url, account.getApiKey(), account.getSecret());
//            String ordersStr = "" +
//                    "" +
//                    "" +
//                    "{\n" +
//                    "\t\"success\" : true,\n" +
//                    "\t\"message\" : \"\",\n" +
//                    "\t\"result\" : [{\n" +
//                    "\t\t\t\"OrderUuid\" : \"fd97d393-e9b9-4dd1-9dbf-f288fc72a185\",\n" +
//                    "\t\t\t\"Exchange\" : \"BTC-LTC\",\n" +
//                    "\t\t\t\"TimeStamp\" : \"2014-07-09T04:01:00.667\",\n" +
//                    "\t\t\t\"OrderType\" : \"LIMIT_BUY\",\n" +
//                    "\t\t\t\"Limit\" : 0.00000001,\n" +
//                    "\t\t\t\"Quantity\" : 100000.00000000,\n" +
//                    "\t\t\t\"QuantityRemaining\" : 100000.00000000,\n" +
//                    "\t\t\t\"Commission\" : 0.00000000,\n" +
//                    "\t\t\t\"Price\" : 0.00000000,\n" +
//                    "\t\t\t\"PricePerUnit\" : null,\n" +
//                    "\t\t\t\"IsConditional\" : false,\n" +
//                    "\t\t\t\"Condition\" : null,\n" +
//                    "\t\t\t\"ConditionTarget\" : null,\n" +
//                    "\t\t\t\"ImmediateOrCancel\" : false\n" +
//                    "\t\t}, {\n" +
//                    "\t\t\t\"OrderUuid\" : \"17fd64d1-f4bd-4fb6-adb9-42ec68b8697d\",\n" +
//                    "\t\t\t\"Exchange\" : \"BTC-ZS\",\n" +
//                    "\t\t\t\"TimeStamp\" : \"2014-07-08T20:38:58.317\",\n" +
//                    "\t\t\t\"OrderType\" : \"LIMIT_SELL\",\n" +
//                    "\t\t\t\"Limit\" : 0.00002950,\n" +
//                    "\t\t\t\"Quantity\" : 667.03644955,\n" +
//                    "\t\t\t\"QuantityRemaining\" : 0.00000000,\n" +
//                    "\t\t\t\"Commission\" : 0.00004921,\n" +
//                    "\t\t\t\"Price\" : 0.01968424,\n" +
//                    "\t\t\t\"PricePerUnit\" : 0.00002950,\n" +
//                    "\t\t\t\"IsConditional\" : false,\n" +
//                    "\t\t\t\"Condition\" : null,\n" +
//                    "\t\t\t\"ConditionTarget\" : null,\n" +
//                    "\t\t\t\"ImmediateOrCancel\" : false\n" +
//                    "\t\t}\n" +
//                    "\t]\n" +
//                    "}";
            if (ordersStr == null) {
                orderHistory = new OrderHistory();
                orderHistory.setSuccess(false);
                orderHistory.setMessage("Connection Error");
            } else {
                ObjectMapper mapper = new ObjectMapper();
                orderHistory = mapper.readValue(ordersStr, OrderHistory.class);
                orderHistory.setJson(ordersStr);
            }
        }
//        Log.i("response " , wallet.getSuccess() + wallet.getJson());
            return orderHistory;

    }


    public Cancel cancelOrder(String uuid, Account account) throws IOException
    {
        Cancel cancel = null;
        if(account == null) {
            cancel = new Cancel();
            cancel.setSuccess(false);
            cancel.setMessage("No API");
        }else
        {
            final String url = "https://bittrex.com/api/v1.1/market/cancel";
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("uuid", uuid);
            String cancelStr = new RESTUtil().callRestHttpClient(url, account.getApiKey(), account.getSecret(), params);

            if (cancelStr == null) {
                cancel = new Cancel();
                cancel.setSuccess(false);
                cancel.setMessage("Connection Error");
            } else {
                ObjectMapper mapper = new ObjectMapper();
                cancel = mapper.readValue(cancelStr, Cancel.class);
            }
        }
        return cancel;

    }

    public LimitOrder buyLimit(String market, String quantity, String rate, Account account) throws IOException
    {
        LimitOrder limitOrder = null;
        if(account == null) {
            limitOrder = new LimitOrder();
            limitOrder.setSuccess(false);
            limitOrder.setMessage("No API");
        }
        else {
            final String url = "https://bittrex.com/api/v1.1/market/buylimit";
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("market", market);
            params.put("quantity", quantity);
            params.put("rate", rate);
            String buyLimitStr = new RESTUtil().callRestHttpClient(url, account.getApiKey(), account.getSecret(), params);

            if (buyLimitStr == null) {
                limitOrder = new LimitOrder();
                limitOrder.setSuccess(false);
                limitOrder.setMessage("Connection Error");
            } else {
                ObjectMapper mapper = new ObjectMapper();
                limitOrder = mapper.readValue(buyLimitStr, LimitOrder.class);
            }
        }
        return limitOrder;

    }

    public LimitOrder sellLimit(String market, String quantity, String rate, Account account) throws IOException
    {
        LimitOrder limitOrder = null;
        if(account == null) {
            limitOrder = new LimitOrder();
            limitOrder.setSuccess(false);
            limitOrder.setMessage("No API");
        }
        else {
            final String url = "https://bittrex.com/api/v1.1/market/selllimit";
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("market", market);
            params.put("quantity", quantity);
            params.put("rate", rate);
            String sellLimitStr = new RESTUtil().callRestHttpClient(url, account.getApiKey(), account.getSecret(), params);

            if (sellLimitStr == null) {
                limitOrder = new LimitOrder();
                limitOrder.setSuccess(false);
                limitOrder.setMessage("Connection Error");
            } else {
                ObjectMapper mapper = new ObjectMapper();
                limitOrder = mapper.readValue(sellLimitStr, LimitOrder.class);
            }
        }
        return limitOrder;

    }

    public MarketHistory getMarketHistory(String market) throws IOException {
        final String url = "https://bittrex.com/api/v1.1/public/getmarkethistory?market="+market;
        String mhStr = new RESTUtil().callREST(url);
        MarketHistory mh = null;
        if(mhStr == null) {
            mh = new MarketHistory();
            mh.setSuccess(false);
            mh.setMessage("Connection Error");
        }
        else {
            ObjectMapper mapper = new ObjectMapper();
            mh = mapper.readValue(mhStr, MarketHistory.class);
        }
//        Log.i("MarketSummary", mhStr);

        return mh;
    }


    public Order getOrder(String orderUUID, Account account) throws IOException
    {
        Order order = null;
        if(account == null) {
            order = new Order();
            order.setSuccess(false);
            order.setMessage("No API");
//            Log.d("Order Status", "No API");
        }
        else {
            final String url = "https://bittrex.com/api/v1.1/account/getorder";
            Log.d(TAG, "getOrder: "+url);
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("uuid", orderUUID);
            String orderStr = new RESTUtil().callRestHttpClient(url, account.getApiKey(), account.getSecret(), params);
//            Log.d("Order Status", orderStr);
            if (orderStr == null) {
                order = new Order();
                order.setSuccess(false);
                order.setMessage("Connection Error");
            } else {
                ObjectMapper mapper = new ObjectMapper();
                order = mapper.readValue(orderStr, Order.class);
            }
        }
        return order;

    }


}
