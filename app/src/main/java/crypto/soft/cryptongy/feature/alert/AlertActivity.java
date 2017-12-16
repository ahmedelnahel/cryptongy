package crypto.soft.cryptongy.feature.alert;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.account.AccountFragment;
import crypto.soft.cryptongy.utils.ProgressDialogFactory;

public class AlertActivity extends AppCompatActivity {
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        initToolbar();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, new AlertFragment(), "")
                .commit();
    }

    public void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        ProgressDialogFactory.dismiss();
        super.onDestroy();
    }
}