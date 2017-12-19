package crypto.soft.cryptongy.feature.setting;

import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import io.realm.Realm;

/**
 * Created by tseringwongelgurung on 11/21/17.
 */

public class SettingInteractor {
    public void updateNotification(Notification notification) {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(notification);
        realm.commitTransaction();
    }

    public void getNotification(OnFinishListner<Notification> listner) {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        Notification notificationDb = realm.where(Notification.class).equalTo("id", 0).findFirst();
        Notification notification;
        if (notificationDb == null) {
            notification = new Notification(true, true,true,15);
            realm.copyToRealmOrUpdate(notification);
        } else
            notification = realm.copyFromRealm(notificationDb);
        realm.commitTransaction();

        listner.onComplete(notification);
    }
}
