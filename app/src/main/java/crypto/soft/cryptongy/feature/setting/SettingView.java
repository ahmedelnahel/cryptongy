package crypto.soft.cryptongy.feature.setting;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by tseringwongelgurung on 11/21/17.
 */

public interface SettingView extends MvpView {
    void initToolbar();

    void findViews();

    void setListner();

    void setNotification(Notification notification);

    void intervalTextwatcher();
}
