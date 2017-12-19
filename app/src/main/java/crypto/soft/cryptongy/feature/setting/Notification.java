package crypto.soft.cryptongy.feature.setting;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by tseringwongelgurung on 11/21/17.
 */

public class Notification extends RealmObject {
    @PrimaryKey
    private int id = 0;
    private boolean vibrate;
    private boolean sound;
    private boolean automSync;
    private int syncInterval;

    public Notification() {
    }

    public Notification(boolean vibrate, boolean sound, boolean automSync, int syncInterval) {
        this.vibrate = vibrate;
        this.sound = sound;
        this.automSync = automSync;
        this.syncInterval = syncInterval;
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

    public boolean isAutomSync() {
        return automSync;
    }

    public void setAutomSync(boolean automSync) {
        this.automSync = automSync;
    }

    public int getSyncInterval() {
        return syncInterval;
    }

    public void setSyncInterval(int syncInterval) {
        this.syncInterval = syncInterval;
    }
}
