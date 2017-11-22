package crypto.soft.cryptongy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import crypto.soft.cryptongy.feature.aboutUs.AboutUsActivity;
import crypto.soft.cryptongy.feature.account.AccountActivity;
import crypto.soft.cryptongy.feature.setting.SettingActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void openSetting(View view){
        startActivity(new Intent(this, SettingActivity.class));
    }

    public void openAboutUs(View view){
        startActivity(new Intent(this, AboutUsActivity.class));
    }

    public void openAccount(View view){
        startActivity(new Intent(this, AccountActivity.class));
    }
}
