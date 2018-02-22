package crypto.soft.cryptongy.feature.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.ArrayList;
import java.util.List;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.order.OrderReceiver;
import crypto.soft.cryptongy.feature.setting.Notification;
import crypto.soft.cryptongy.feature.setting.SettingActivity;
import crypto.soft.cryptongy.utils.CoinApplication;
import crypto.soft.cryptongy.utils.GlobalUtil;
import crypto.soft.cryptongy.utils.HideKeyboard;
import crypto.soft.cryptongy.utils.ProgressDialogFactory;

public class MainActivity extends MvpActivity<MainView, MainPresenter> implements MainView {
    List<MenuItem> menuItems = new ArrayList<>();
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private RecyclerView listMenu;
    private MenuItemAdapter menuItemAdapter;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new HideKeyboard(this).setupUI(findViewById(android.R.id.content));
        data = getIntent().getStringExtra("OPEN");
        if (TextUtils.isEmpty(data))
            data = "Home";
        initToolbar();
        findViews();
        initSideMenu();
        setAdapter();
        presenter.onItemClicked(data);
        GlobalUtil.startAlarm(OrderReceiver.class, 60000, this);
    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    public void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ic_menu_order, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == R.id.ic_setting) {
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public String getFragTitle() {
        TextView txtTitle = findViewById(R.id.txtTitle);
        return txtTitle.getText().toString();
    }

    public void findViews() {
        drawerLayout = findViewById(R.id.drawerLayout);
        listMenu = findViewById(R.id.list_menu);
    }

    public void initSideMenu() {
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                drawerView.bringToFront();
                drawerView.requestLayout();
            }
        };
        drawerLayout.addDrawerListener(toggle);
    }

    @Override
    public void setAdapter() {
        if (data.equalsIgnoreCase("home")) {
            menuItems.add(new MenuItem(R.drawable.ic_home_a, "Home", true));
        } else
            menuItems.add(new MenuItem(R.drawable.ic_house, "Home", false));

        menuItems.add(new MenuItem(R.drawable.ic_wallet, "Wallet", false));
        menuItems.add(new MenuItem(R.drawable.ic_orders, "Orders", false));
        menuItems.add(new MenuItem(R.drawable.ic_trade, "Trade", false));
        if (data.equalsIgnoreCase("Conditional")) {
            menuItems.add(new MenuItem(R.drawable.ic_portfolio_a, "Conditional", true));
        } else
            menuItems.add(new MenuItem(R.drawable.ic_portfolio, "Conditional", false));
        menuItems.add(new MenuItem(R.drawable.ic_alert, "Alert", false));
        menuItems.add(new MenuItem(R.drawable.ic_account, "Accounts", false));
        menuItems.add(new MenuItem(R.drawable.ic_bitcoin, "Donate", false));
        menuItems.add(new MenuItem(R.drawable.ic_about, "About Us", false));


        if (data.equalsIgnoreCase("Arbitrage")) {
            menuItems.add(new MenuItem(R.drawable.market_data_icon1, "Arbitrage", true));
        } else
            menuItems.add(new MenuItem(R.drawable.market_data_icon_white, "Arbitrage", false));


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listMenu.setLayoutManager(linearLayoutManager);
        listMenu.setHasFixedSize(true);
        listMenu.setItemAnimator(new DefaultItemAnimator());
        menuItemAdapter = new MenuItemAdapter(this, menuItems);
        listMenu.setAdapter(menuItemAdapter);
        menuItemAdapter.setOnItemClickListener(new MenuItemClickListener() {
            @Override
            public void onItemClicked(MenuItem menuItem, int position) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                presenter.onItemClicked(menuItem.getItemName());
            }
        });
    }

    @Override
    public void notifyMenu(String menu) {
        menuItems.clear();
        if (menu.equalsIgnoreCase("Home"))
            menuItems.add(new MenuItem(R.drawable.ic_home_a, "Home", true));
        else
            menuItems.add(new MenuItem(R.drawable.ic_house, "Home", false));
        if (menu.equalsIgnoreCase("Wallet"))
            menuItems.add(new MenuItem(R.drawable.ic_wallet_a, "Wallet", true));
        else
            menuItems.add(new MenuItem(R.drawable.ic_wallet, "Wallet", false));
        if (menu.equalsIgnoreCase("Orders"))
            menuItems.add(new MenuItem(R.drawable.ic_orders_a, "Orders", true));
        else
            menuItems.add(new MenuItem(R.drawable.ic_orders, "Orders", false));
        if (menu.equalsIgnoreCase("Trade"))
            menuItems.add(new MenuItem(R.drawable.ic_trade_a, "Trade", true));
        else
            menuItems.add(new MenuItem(R.drawable.ic_trade, "Trade", false));
        if (menu.equalsIgnoreCase("Conditional"))
            menuItems.add(new MenuItem(R.drawable.ic_portfolio_a, "Conditional", true));
        else
            menuItems.add(new MenuItem(R.drawable.ic_portfolio, "Conditional", false));
        if (menu.equalsIgnoreCase("Alert"))
            menuItems.add(new MenuItem(R.drawable.ic_alert_a, "Alert", true));
        else
            menuItems.add(new MenuItem(R.drawable.ic_alert, "Alert", false));
        if (menu.equalsIgnoreCase("Accounts"))
            menuItems.add(new MenuItem(R.drawable.ic_accounts_a, "Accounts", true));
        else
            menuItems.add(new MenuItem(R.drawable.ic_account, "Accounts", false));
        if (menu.equalsIgnoreCase("Donate"))
            menuItems.add(new MenuItem(R.drawable.ic_bitcoin_a, "Donate", true));
        else
            menuItems.add(new MenuItem(R.drawable.ic_bitcoin, "Donate", false));

        if (menu.equalsIgnoreCase("About Us"))
            menuItems.add(new MenuItem(R.drawable.ic_about_a, "About Us", true));
        else
            menuItems.add(new MenuItem(R.drawable.ic_about, "About Us", false));


        if (menu.equalsIgnoreCase("Arbitrage"))
            menuItems.add(new MenuItem(R.drawable.market_data_icon1, "Arbitrage", true));
        else
            menuItems.add(new MenuItem(R.drawable.market_data_icon_white, "Arbitrage", false));

        menuItemAdapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count > 0) {
            super.onBackPressed();
            String string = getFragTitle();
            if (string.equalsIgnoreCase("conditional trade"))
                string = "Conditional";
            else if (string.equalsIgnoreCase("Watch List"))
                string = "Home";
            notifyMenu(string);
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            if (drawerLayout.isDrawerOpen(Gravity.LEFT))
                drawerLayout.closeDrawer(Gravity.LEFT);
            else {
                if (getFragTitle().equalsIgnoreCase(getString(R.string.home)))
                    super.onBackPressed();
                else {
                    presenter.onItemClicked("Home");
                    notifyMenu("Home");
                }
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        ProgressDialogFactory.dismiss();
        GlobalUtil.stopAlarm(OrderReceiver.class, this);
//        Log.d("Cryptongy", "alarm stoped");
    }

    @Override
    protected void onStop() {
        super.onStop();
        ProgressDialogFactory.dismiss();
        GlobalUtil.stopAlarm(OrderReceiver.class, this);
//        Log.d("Cryptongy", "alarm stoped");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Notification notification = ((CoinApplication) this.getApplication()).getSettings();

        GlobalUtil.startAlarm(OrderReceiver.class, 30000, this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        GlobalUtil.startAlarm(OrderReceiver.class, 30000, this);
        Log.d("Cryptongy", "alarm started");
    }

    @Override
    public void onDestroy() {
        ProgressDialogFactory.dismiss();
        GlobalUtil.stopAlarm(OrderReceiver.class, this);
        super.onDestroy();

    }
}
