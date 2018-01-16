package crypto.soft.cryptongy.feature.home;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.setting.Notification;
import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.json.market.Result;
import crypto.soft.cryptongy.feature.shared.listner.OnMultiFinishListner;
import crypto.soft.cryptongy.utils.CoinApplication;

/**
 * Created by tseringwongelgurung on 11/27/17.
 */

public class HomePresenter extends MvpBasePresenter<HomeView> implements OnMultiFinishListner<List<Result>, MarketSummaries> {
    private static Timer timer;
    private HomeInteractor homeInteractor;
    private Context context;
    private List<Result> prevResults = new ArrayList<>();
    private boolean isStarted = false;

    public HomePresenter(Context context) {
        this.homeInteractor = new HomeInteractor();
        this.context = context;
        isStarted = false;
    }

    public void loadSummaries() {
        if (getView() != null)
            getView().showProgressBar();
        homeInteractor.loadSummary(context, this);
    }

    @Override
    public void onComplete(List<Result> results, MarketSummaries marketSummaries) {
        if (getView() != null) {
            results = setDrawable(results);
            getView().setAdapter(results);
            prevResults = results;
            getView().onSummaryDataLoad(marketSummaries);
            isStarted=true;
            startTimer();
        }
    }

    @Override
    public void onFail(String error) {
        if (getView() != null) {
            getView().onSummaryLoadFailed();
            getView().hideProgressBar();
        }
    }

    public void stopTimer() {
        if (timer != null)
            timer.cancel();
    }

    public void startTimer() {
        if (timer != null)
            timer.cancel();
        Notification notification = ((CoinApplication) context.getApplicationContext()).getNotification();
        if (notification.isAutomSync()) {
            int timerInterval = notification.getSyncInterval() * 1000;
            timer = new Timer();
            timer.scheduleAtFixedRate(new TickerTimer(), timerInterval,
                    timerInterval);
        }

    }

    private List<Result> setDrawable(List<Result> list) {
        for (int i = 0; i < list.size(); i++) {
            Result result = list.get(i);
            if (prevResults == null)
                result.setDrawable(R.drawable.seek_progress);
            else if (i < prevResults.size() && result != null) {
                if (result.getLast().doubleValue() < prevResults.get(i).getLast().doubleValue())
                    result.setDrawable(R.drawable.seek_progress_red);
                else if (result.getLast().doubleValue() > prevResults.get(i).getLast().doubleValue())
                    result.setDrawable(R.drawable.seek_progress_green);
//                else
//                    result.setDrawable(R.drawable.seek_progress);
            }
            else if(result != null)
                result.setDrawable(R.drawable.seek_progress);
        }
        return list;
    }

    class TickerTimer extends TimerTask {

        @Override
        public void run() {
            homeInteractor.loadSummary(context, new OnMultiFinishListner<List<Result>, MarketSummaries>() {
                @Override
                public void onComplete(List<Result> results, MarketSummaries summaries) {
                    if (getView() != null) {
                        results = setDrawable(results);
                        getView().setAdapter(results);
                        getView().onSummaryDataLoad(summaries);
                        prevResults = results;
                    }
                }

                @Override
                public void onFail(String error) {

                }
            });
        }
    }
}