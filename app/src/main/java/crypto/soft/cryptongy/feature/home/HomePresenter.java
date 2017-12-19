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
    private static Timer timer = new Timer();
    private HomeInteractor homeInteractor;
    private Context context;
    private List<Result> prevResults = new ArrayList<>();

    public HomePresenter(Context context) {
        this.homeInteractor = new HomeInteractor();
        this.context = context;
    }

    public void loadSummaries() {
        if (getView() != null)
            getView().showProgressBar();
        homeInteractor.loadSummary(context, this);
    }

    @Override
    public void onComplete(List<Result> results, MarketSummaries marketSummaries) {
        if (getView() != null) {
            getView().setAdapter(results,getColors(results));
            prevResults = results;
            getView().onSummaryDataLoad(marketSummaries);
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

    public void startTimer() {
        Notification notification = ((CoinApplication) context.getApplicationContext()).getNotification();
        if (notification.isAutomSync()) {
            int timerInterval = notification.getSyncInterval() * 1000;
            timer.scheduleAtFixedRate(new TickerTimer(), timerInterval,
                    timerInterval);
        }
    }

    private List<Integer> getColors(List<Result> list) {
        List<Integer> colorList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Result result = list.get(i);
            if (prevResults == null)
                colorList.add(R.drawable.seek_progress);
            else if (i < prevResults.size()) {
                if (result.getVolume().doubleValue() < prevResults.get(i).getVolume().doubleValue())
                    colorList.add(R.drawable.seek_progress_red);
                else if (result.getVolume().doubleValue() > prevResults.get(i).getVolume().doubleValue())
                    colorList.add(R.drawable.seek_progress_green);
                else
                    colorList.add(R.drawable.seek_progress);
            } else
                colorList.add(R.drawable.seek_progress);
        }
        return colorList;
    }

    class TickerTimer extends TimerTask {

        @Override
        public void run() {
            homeInteractor.loadSummary(context, new OnMultiFinishListner<List<Result>, MarketSummaries>() {
                @Override
                public void onComplete(List<Result> results, MarketSummaries summaries) {
                    if (getView() != null) {
                        getView().setAdapter(results,getColors(results));
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