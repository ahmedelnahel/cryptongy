package crypto.soft.cryptongy.feature.setting;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;

/**
 * Created by tseringwongelgurung on 11/21/17.
 */

public class SettingPresenter extends MvpBasePresenter<SettingView> {
    private SettingInteractor interactor = new SettingInteractor();

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
    }
}
