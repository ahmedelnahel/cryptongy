package crypto.soft.cryptongy.network;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import crypto.soft.cryptongy.common.RESTUtil;
import crypto.soft.cryptongy.feature.shared.json.action.Cancel;
import crypto.soft.cryptongy.feature.shared.json.binance.marketsummary.BinanceMarket;
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
import crypto.soft.cryptongy.utils.GlobalUtil;


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
        if(marketSummariesStr == null || "".equals(marketSummariesStr)) {
            Log.d("Binance MarketSummaries", "response in error " + marketSummariesStr);
            marketSummaries_.setSuccess(false);
            marketSummaries_.setMessage("Connection Error");
        }
        else {
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
            }
            else
            {
                marketSummaries_.setSuccess(false);
                marketSummaries_.setMessage(binanceMarket.getMsg());

            }
        }
        return marketSummaries_;
    }

    //


    public MarketSummary getMarketSummary(String market) throws IOException {
        final String url = "https://api.binance.com/api/v1/ticker/24hr?symbol="+market;
        String marketSummaryStr = new RESTUtil().callREST(url);
        MarketSummary marketSummary_= new MarketSummary();
        if(marketSummaryStr == null || "".equals(marketSummaryStr)) {
            marketSummary_.setSuccess(false);
            marketSummary_.setMessage("Connection Error");
        }
        else {
            ObjectMapper mapper = new ObjectMapper();
            crypto.soft.cryptongy.feature.shared.json.binance.marketsummary.Result r = mapper.readValue(marketSummaryStr, crypto.soft.cryptongy.feature.shared.json.binance.marketsummary.Result.class);
            if (r.getMsg() == null || "".equals(r.getMsg())) {
                crypto.soft.cryptongy.feature.shared.json.marketsummary.Result mr = new crypto.soft.cryptongy.feature.shared.json.marketsummary.Result(r);
                List rl = new ArrayList();
                rl.add(mr);
                marketSummary_.setResult(rl);
                marketSummary_.setJson(marketSummaryStr);
                marketSummary_.setSuccess(true);
            }
            else
            {
                marketSummary_.setSuccess(false);
                marketSummary_.setMessage(r.getMsg());

            }
        Log.i("MarketSummary", marketSummaryStr);
        }
        return marketSummary_;
    }

    public Ticker getTicker(String market) throws IOException {
        final String url = "https://api.binance.com/api/v1/ticker/24hr?symbol="+market;
        Ticker ticker = new Ticker();
        String tickerStr = new RESTUtil().callREST(url);
        if(tickerStr == null || "".equals(tickerStr)) {
            ticker.setSuccess(false);
            ticker.setMessage("Connection Error");
        }
        else
        {

            ObjectMapper mapper = new ObjectMapper();
            crypto.soft.cryptongy.feature.shared.json.binance.marketsummary.Result r = mapper.readValue(tickerStr, crypto.soft.cryptongy.feature.shared.json.binance.marketsummary.Result.class);
            if (r.getMsg() == null || "".equals(r.getMsg())) {
                ticker.setBinanceResult(r);
                ticker.setJson(tickerStr);
                ticker.setSuccess(true);
            }
            else
            {
                ticker.setSuccess(false);
                ticker.setMessage(r.getMsg());

            }
        }
        Log.i("MarketSummary", tickerStr);

        return ticker;
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
            String ordersStr = new RESTUtil().callRestHttpClient(url, account.getApiKey(), account.getSecret());

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


    public Wallet getWallet(Account account) throws IOException {
        Wallet wallet = null;

        if(account == null) {
            wallet = new Wallet();
            wallet.setSuccess(false);
            wallet.setMessage("Connection Error");
        }
        else {
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
            String ordersStr = new RESTUtil().callRestHttpClient(url, account.getApiKey(), account.getSecret());
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
