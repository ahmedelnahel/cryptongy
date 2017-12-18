package crypto.soft.cryptongy.feature.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import crypto.soft.cryptongy.R;

public class SettingActivity extends MvpActivity<SettingView, SettingPresenter> implements SettingView {
        private ToggleButton tgbSound, tgbVibration,tgbAutomaticSync;
    private Notification notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initToolbar();
        findViews();
        setListner();
        presenter.getNotification();
    }

    @NonNull
    @Override
    public SettingPresenter createPresenter() {
        return new SettingPresenter();
    }

    @Override
    public void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView txtTitle = toolbar.findViewById(R.id.txtTitle);
        txtTitle.setText(R.string.settings);
    }

    @Override
    public void findViews() {
        tgbSound = findViewById(R.id.tgbSound);
        tgbVibration = findViewById(R.id.tgbVibration);
        tgbAutomaticSync = findViewById(R.id.tgbAutomaticSync);
    }

    @Override
    public void setListner() {
        tgbSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                notification.setSound(b);
                presenter.updateNotification(notification);
            }
        });

        tgbVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                notification.setVibrate(b);
                presenter.updateNotification(notification);
            }
        });

        tgbAutomaticSync.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                notification.setAutomSync(b);
                presenter.updateNotification(notification);
            }
        });
    }

    @Override
    public void setNotification(Notification notification) {
        this.notification=notification;
        tgbSound.setChecked(notification.isSound());
        tgbVibration.setChecked(notification.isVibrate());
        tgbAutomaticSync.setChecked(notification.isAutomSync());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
