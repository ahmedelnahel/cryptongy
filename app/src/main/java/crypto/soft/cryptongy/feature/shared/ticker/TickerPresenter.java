package crypto.soft.cryptongy.feature.shared.ticker;

import android.content.Context;
import android.text.TextUtils;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.Timer;
import java.util.TimerTask;

import crypto.soft.cryptongy.feature.setting.Notification;
import crypto.soft.cryptongy.feature.shared.json.ticker.Ticker;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.network.BinanceServices;
import crypto.soft.cryptongy.utils.CoinApplication;
import crypto.soft.cryptongy.utils.GlobalConstant;

/**
 * Created by tseringwongelgurung on 12/18/17.
 */

public class TickerPresenter<T extends TickerView> extends MvpBasePresenter<T> {
    private static Timer timer;
    protected Context context;
    private String coinName;
    private String exchangeValue;
    private TickerInteractor tickerInteractor;
    BinanceServices binanceServices;

    public TickerPresenter(Context context) {
        this.context = context;
        tickerInteractor = new TickerInteractor();

        if (binanceServices == null) {
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

            if (exchangeValue.equalsIgnoreCase(GlobalConstant.Exchanges.BITTREX)) {
                timer.scheduleAtFixedRate(new TickerTimer(), timerInterval,
                        timerInterval);
            }

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
    }


    public void closeWebSocket() {
        tickerInteractor.closeWebSocket();

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
            if (TextUtils.isEmpty(exchangeValue)) {
                exchangeValue = GlobalConstant.Exchanges.BITTREX;
            }
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
    }
}
