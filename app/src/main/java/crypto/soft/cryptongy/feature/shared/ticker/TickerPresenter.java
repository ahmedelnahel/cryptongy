package crypto.soft.cryptongy.feature.shared.ticker;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.framing.CloseFrame;

import java.util.Timer;
import java.util.TimerTask;

import crypto.soft.cryptongy.feature.setting.Notification;
import crypto.soft.cryptongy.feature.shared.json.ticker.Ticker;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.network.BinanceServices;
import crypto.soft.cryptongy.utils.CoinApplication;
import crypto.soft.cryptongy.utils.GlobalConstant;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

import static android.content.ContentValues.TAG;

/**
 * Created by tseringwongelgurung on 12/18/17.
 */

public class TickerPresenter<T extends TickerView> extends MvpBasePresenter<T> {
    private static Timer timer;
    protected Context context;
    private String coinName;
    private String exchangeValue;
    private TickerInteractor tickerInteractor;
    BinanceServices binanceServices ;

    public TickerPresenter(Context context) {
        this.context = context;
        tickerInteractor = new TickerInteractor();

        if(binanceServices==null){
            binanceServices = new BinanceServices();
        }
    }

    public void startTicker(String coinName) {
        stopTimer();
        Notification notification = ((CoinApplication) context.getApplicationContext()).getNotification();
        if (notification.isAutomSync()) {
            int timerInterval = notification.getSyncInterval() * 1000;
            timer = new Timer();
            this.coinName = coinName;
            this.exchangeValue = exchangeValue;


            if (exchangeValue.equalsIgnoreCase(GlobalConstant.Exchanges.BINANCE)) {
                subscribeToWebsockett(coinName);
            }

            if (exchangeValue.equalsIgnoreCase(GlobalConstant.Exchanges.BITTREX)) {


                closeWebSocket();


                timer.scheduleAtFixedRate(new TickerTimer(), timerInterval,
                        timerInterval);

                tickerInteractor.getTicker(coinName, exchangeValue, new OnFinishListner<Ticker>() {
                    @Override
                    public void onComplete(Ticker result) {
                        if (getView() != null)
                            getView().setTicker(result);
                    }

                    @Override
                    public void onFail(String error) {

                    }
                });
            }


            if (TextUtils.isEmpty(exchangeValue)) {
                exchangeValue = GlobalConstant.Exchanges.BITTREX;
            }


        }
    }


    public void subscribeToWebsockett(String coinName) {


        try {
              binanceServices.getTickerConnectSocket(coinName);

            binanceServices.source.observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Ticker>() {
                @Override
                public void accept(Ticker ticker) throws Exception {

                    Log.d(TAG, "this is ticker  " + ticker);
                    if (getView() != null) {
                        getView().setTicker(ticker);
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void closeWebSocket(){


        binanceServices.sourceWebSocketClient.subscribe(new Consumer<WebSocketClient>() {
            @Override
            public void accept(WebSocketClient webSocketClient) throws Exception {

                if(webSocketClient!=null){
                    webSocketClient.closeConnection(CloseFrame.NORMAL,"its closeing time");
                }
            }
        });

//        if(binanceServices!=null){
//            if(binanceServices.mWebSocketClient!=null){
//                binanceServices.mWebSocketClient.closeConnection( CloseFrame.NEVER_CONNECTED,"its closing time");
//            }
//        }
    }

    public void startTicker(String coinName, String exchangeValue) {
        stopTimer();
        Notification notification = ((CoinApplication) context.getApplicationContext()).getNotification();
        if (notification.isAutomSync()) {
            int timerInterval = notification.getSyncInterval() * 1000;
            timer = new Timer();
            this.coinName = coinName;
            this.exchangeValue = exchangeValue;
            timer.scheduleAtFixedRate(new TickerTimer(), timerInterval,
                    timerInterval);
        }
    }


    public void stopTimer() {
        if (timer != null)
            timer.cancel();
    }

    class TickerTimer extends TimerTask {


        @Override
        public void run() {
            if(TextUtils.isEmpty(exchangeValue)){
                exchangeValue= GlobalConstant.Exchanges.BITTREX;
            }
            tickerInteractor.getTicker(coinName,exchangeValue, new OnFinishListner<Ticker>() {
                @Override
                public void onComplete(Ticker result) {
                    if (getView() != null)
                        getView().setTicker(result);
                }

                @Override
                public void onFail(String error) {

                }
            });
        }
    }
}
