package crypto.soft.cryptongy.feature.setting;

import android.content.Context;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import crypto.soft.cryptongy.feature.order.OrderReceiver;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.utils.CoinApplication;
import crypto.soft.cryptongy.utils.GlobalUtil;

/**
 * Created by tseringwongelgurung on 11/21/17.
 */

public class SettingPresenter extends MvpBasePresenter<SettingView> {
    private Context context;
    private SettingInteractor interactor;

    public SettingPresenter(Context context) {
        this.context = context;
        interactor = new SettingInteractor();
    }

    public void getNotification() {
        interactor.getNotification(new OnFinishListner<Notification>() {
            @Override
            public void onComplete(Notification result) {
                if (getView() != null)
                    getView().setNotification(result);
            }

            @Override
            public void onFail(String error) {

            }
        });
    }

    public void updateNotification(Notification notification) {
        interactor.updateNotification(notification);
        ((CoinApplication) context.getApplicationContext()).setSettings(notification);

        if (notification.isAutomSync()) {
            GlobalUtil.startAlarm(OrderReceiver.class, notification.getSyncInterval() * 1000, context);
        }
    }
}
