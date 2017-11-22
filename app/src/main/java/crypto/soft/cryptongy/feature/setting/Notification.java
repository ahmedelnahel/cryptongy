package crypto.soft.cryptongy.feature.setting;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by tseringwongelgurung on 11/21/17.
 */

public class Notification extends RealmObject {
    @PrimaryKey
    private int id=0;
    private boolean vibrate;
    private boolean sound;

    public Notification() {
    }

    public Notification(boolean vibrate, boolean sound) {
        this.vibrate = vibrate;
        this.sound = sound;
    }

    public boolean isVibrate() {
        return vibrate;
    }

    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
    }

    public boolean isSound() {
        return sound;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }
}
