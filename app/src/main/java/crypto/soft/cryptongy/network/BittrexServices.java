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
import crypto.soft.cryptongy.feature.shared.json.orderhistory.OrderHistory;
import crypto.soft.cryptongy.feature.shared.json.ticker.Ticker;
import crypto.soft.cryptongy.feature.shared.json.wallet.Wallet;
import crypto.soft.cryptongy.feature.shared.module.Account;


/**
 * Created by noni on 26/10/2017.
 * https://bittrex.com/api/v1.1/account/getbalances
 */

public class BittrexServices {


    public MarketSummaries getMarketSummaries() throws IOException {
        final String url = "https://bittrex.com/api/v1.1/public/getmarketsummaries";  //"https://www.coinexchange.io/api/v1/getmarkets";
        String marketSummariesStr = new RESTUtil().callREST(url);
        ObjectMapper mapper = new ObjectMapper();
        MarketSummaries marketSummaries_ = mapper.readValue(marketSummariesStr, MarketSummaries.class);
        marketSummaries_.setJson(marketSummariesStr);
        Log.i("MarketSummaries", marketSummariesStr);

        if (marketSummaries_.getSuccess())
        {
            HashMap<String, Result> coinsMap = new HashMap<>();
            for (Result r : marketSummaries_.getResult()) {
                coinsMap.put(r.getMarketName(), r) ;
            }
            marketSummaries_.setCoinsMap(coinsMap);
        }
        return marketSummaries_;
    }

    //
    public Wallet getWallet(Account account) throws IOException {

        final String url = "https://bittrex.com/api/v1.1/account/getbalances";
        String walletStr = new RESTUtil().callRestHttpClient(url, account.getApiKey(), account.getSecret());
        ObjectMapper mapper = new ObjectMapper();
        Wallet wallet = mapper.readValue(walletStr, Wallet.class);
        wallet.setJson(walletStr);
//        Log.i("response " , wallet.getSuccess() + wallet.getJson());
        if (wallet.getSuccess())
        {
            HashMap<String, crypto.soft.cryptongy.feature.shared.json.wallet.Result> coinsMap = new HashMap<>();
            for (crypto.soft.cryptongy.feature.shared.json.wallet.Result r : wallet.getResult()) {
                if(r.getBalance() != 0)
                    coinsMap.put(r.getCurrency(), r) ;
            }
            wallet.setCoinsMap(coinsMap);
        }
        return wallet;
    }

    public MarketSummary getMarketSummary(String market) throws IOException {
        final String url = "https://bittrex.com/api/v1.1/public/getmarketsummary?market="+market;
        String marketSummaryStr = new RESTUtil().callREST(url);
        ObjectMapper mapper = new ObjectMapper();
        MarketSummary marketSummary_ = mapper.readValue(marketSummaryStr, MarketSummary.class);
        marketSummary_.setJson(marketSummaryStr);
        Log.i("MarketSummary", marketSummaryStr);

        return marketSummary_;
    }

    public OpenOrder getOpnOrders( Account account) throws IOException {

        final String url = "https://bittrex.com/api/v1.1/market/getopenorders";
        String ordersStr = new RESTUtil().callRestHttpClient(url, account.getApiKey(), account.getSecret());
        ObjectMapper mapper = new ObjectMapper();
        OpenOrder openOrder = mapper.readValue(ordersStr, OpenOrder.class);
        openOrder.setJson(ordersStr);
//        Log.i("response " , wallet.getSuccess() + wallet.getJson());
        return openOrder;
    }


    public Ticker getTicker(String market) throws IOException {
        final String url = "https://bittrex.com/api/v1.1/public/getticker?market="+market;
        String tickerStr = new RESTUtil().callREST(url);
        ObjectMapper mapper = new ObjectMapper();
        Ticker ticker = mapper.readValue(tickerStr, Ticker.class);
        ticker.setJson(tickerStr);
        Log.i("MarketSummary", tickerStr);

        return ticker;
    }


    public OrderHistory getOrderHistory(Account account) throws IOException {

        final String url = "https://bittrex.com/api/v1.1/account/getorderhistory";
        String ordersStr = new RESTUtil().callRestHttpClient(url, account.getApiKey(), account.getSecret());
        ObjectMapper mapper = new ObjectMapper();
        OrderHistory orderHistory = mapper.readValue(ordersStr, OrderHistory.class);
        orderHistory.setJson(ordersStr);
        Log.i("order history " , ordersStr);
        return orderHistory;
    }


    public Cancel cancelOrder(String uuid, Account account) throws IOException
    {
        final String url = "https://bittrex.com/api/v1.1/market/cancel";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("uuid", uuid);
        String cancelStr = new RESTUtil().callRestHttpClient(url, account.getApiKey(), account.getSecret(), params);
        ObjectMapper mapper = new ObjectMapper();
        Cancel cancel = mapper.readValue(cancelStr, Cancel.class);

        return cancel;

    }

    public LimitOrder buyLimit(String market, String quantity, String rate, Account account) throws IOException
    {
        final String url = "https://bittrex.com/api/v1.1/market/buylimit";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("market", market);
        params.put("quantity", quantity);
        params.put("rate", rate);
        String buyLimitStr = new RESTUtil().callRestHttpClient(url, account.getApiKey(), account.getSecret(), params);
        ObjectMapper mapper = new ObjectMapper();
        LimitOrder limitOrder = mapper.readValue(buyLimitStr, LimitOrder.class);

        return limitOrder;

    }

    public LimitOrder sellLimit(String market, String quantity, String rate, Account account) throws IOException
    {
        final String url = "https://bittrex.com/api/v1.1/market/selllimit";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("market", market);
        params.put("quantity", quantity);
        params.put("rate", rate);
        String buyLimitStr = new RESTUtil().callRestHttpClient(url, account.getApiKey(), account.getSecret(), params);
        ObjectMapper mapper = new ObjectMapper();
        LimitOrder limitOrder = mapper.readValue(buyLimitStr, LimitOrder.class);

        return limitOrder;

    }

    public MarketHistory getMarketHistory(String market) throws IOException {
        final String url = "https://bittrex.com/api/v1.1/public/getmarkethistory?market=+market";
        String mhStr = new RESTUtil().callREST(url);
        ObjectMapper mapper = new ObjectMapper();
        MarketHistory mh = mapper.readValue(mhStr, MarketHistory.class);

        Log.i("MarketSummary", mhStr);

        return mh;
    }



}
