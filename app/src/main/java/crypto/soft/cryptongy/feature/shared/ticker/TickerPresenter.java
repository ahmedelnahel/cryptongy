package crypto.soft.cryptongy.feature.shared.ticker;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.Timer;
import java.util.TimerTask;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.setting.Notification;
import crypto.soft.cryptongy.feature.shared.json.ticker.Ticker;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.utils.CoinApplication;

/**
 * Created by tseringwongelgurung on 12/18/17.
 */

public class TickerPresenter<T extends TickerView> extends MvpBasePresenter<T> {
    private static Timer timer = new Timer();
    protected Context context;
    private TickerInteractor tickerInteractor;

    public TickerPresenter(Context context) {
        this.context = context;
        tickerInteractor = new TickerInteractor();
    }

    public void startTicker(String coinName) {
        Notification notification = ((CoinApplication) context.getApplicationContext()).getNotification();
        if (notification.isAutomSync()) {
            int interval = context.getResources().getInteger(R.integer.sync_interval);
            timer.scheduleAtFixedRate(new TickerTimer(coinName), interval, interval);
        }
    }

    class TickerTimer extends TimerTask {
        private String coinName;

        public TickerTimer(String coinName) {
            this.coinName = coinName;
        }

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
}
