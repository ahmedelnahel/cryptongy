package crypto.soft.cryptongy.feature.home;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.aboutUs.AboutUsActivity;
import crypto.soft.cryptongy.feature.account.AccountFragment;
import crypto.soft.cryptongy.feature.coinHome.CoinHomeFragment;
import crypto.soft.cryptongy.feature.order.OrderFragment;
import crypto.soft.cryptongy.feature.setting.SettingActivity;
import crypto.soft.cryptongy.utils.GlobalUtil;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    public void openSetting(View view) {
        startActivity(new Intent(this, SettingActivity.class));
    }

    public void openAboutUs(View view) {
        startActivity(new Intent(this, AboutUsActivity.class));
    }

    public void openAccount(View view) {
        GlobalUtil.addFragment(this, new AccountFragment(), R.id.container, true);
    }

    public void openOrder(View view) {
        GlobalUtil.addFragment(this, new OrderFragment(), R.id.container, true);
    }

    public void openCoinHome(View view) {
        GlobalUtil.addFragment(this, new CoinHomeFragment(), R.id.container, true);
    }
}
