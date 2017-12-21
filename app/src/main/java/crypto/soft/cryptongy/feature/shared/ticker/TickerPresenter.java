package crypto.soft.cryptongy.feature.shared.ticker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.Timer;
import java.util.TimerTask;

import crypto.soft.cryptongy.feature.setting.Notification;
import crypto.soft.cryptongy.feature.shared.json.ticker.Ticker;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.utils.CoinApplication;

/**
 * Created by tseringwongelgurung on 12/18/17.
 */

public class TickerPresenter<T extends TickerView> extends MvpBasePresenter<T> {
    private static Timer timer;
    protected Context context;
    private String coinName;
    private TickerBroadcastReceiver receiver;
    private TickerInteractor tickerInteractor;

    public TickerPresenter(Context context) {
        this.context = context;
        tickerInteractor = new TickerInteractor();
    }

    public void startTicker(String coinName) {
        Notification notification = ((CoinApplication) context.getApplicationContext()).getNotification();
        if (notification.isAutomSync()) {
            int timerInterval = notification.getSyncInterval() * 1000;
            timer = new Timer();
            this.coinName = coinName;
            timer.scheduleAtFixedRate(new TickerTimer(), timerInterval,
                    timerInterval);
            registerReceiver();
        }
    }

    public void stopTimer() {
        if (timer != null)
            timer.cancel();
    }

    public void unregisterReceiver(){
        if (receiver != null)
            context.unregisterReceiver(receiver);
    }

    public void restartTimer(int timerInterval) {
        if (timer != null) {
            timer.cancel();
            timer = new Timer();
            timerInterval *= 1000;
            timer.scheduleAtFixedRate(new TickerTimer(), timerInterval,
                    timerInterval);
        }
    }

    public void registerReceiver() {
        IntentFilter filter = new IntentFilter(".feature.shared.ticker.TickerPresenter$TickerBroadcastReceiver");

        receiver = new TickerBroadcastReceiver();
        context.registerReceiver(receiver, filter);
    }

    class TickerTimer extends TimerTask {

        @Override
        public void run() {
            tickerInteractor.getTicker(coinName, new OnFinishListner<Ticker>() {
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

    public class TickerBroadcastReceiver extends BroadcastReceiver {

        public TickerBroadcastReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isSyncEnabled = intent.getBooleanExtra("NOTI_SYNC", true);
            int timeInterval = intent.getIntExtra("NOTI_INTERVAL", 15);
            if (isSyncEnabled)
                restartTimer(timeInterval);
            else
                stopTimer();
        }
    }
}
