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

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.ArrayList;
import java.util.List;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.setting.SettingActivity;
import crypto.soft.cryptongy.utils.HideKeyboard;

public class MainActivity extends MvpActivity<MainView, MainPresenter> implements MainView {
    List<MenuItem> menuItems = new ArrayList<>();
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private RecyclerView listMenu;
    private MenuItemAdapter menuItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new HideKeyboard(this).setupUI(findViewById(android.R.id.content));
        initToolbar();
        setTitle();
        findViews();
        initSideMenu();
        setAdapter();
        String data=getIntent().getStringExtra("OPEN");
        if (TextUtils.isEmpty(data))
            data="Home";
        else
            Log.d("Ar",data);
        presenter.onItemClicked(data);
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

    public void setTitle() {
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
        menuItems.add(new MenuItem(R.drawable.ic_home_a, "Home", true));
        menuItems.add(new MenuItem(R.drawable.ic_wallet, "Wallet", false));
        menuItems.add(new MenuItem(R.drawable.ic_orders, "Orders", false));
        menuItems.add(new MenuItem(R.drawable.ic_trade, "Trade", false));
        menuItems.add(new MenuItem(R.drawable.ic_portfolio, "Conditional", false));
        menuItems.add(new MenuItem(R.drawable.ic_alert, "Alert", false));
        menuItems.add(new MenuItem(R.drawable.ic_account, "Accounts", false));
        menuItems.add(new MenuItem(R.drawable.ic_bitcoin, "Donate", false));
        menuItems.add(new MenuItem(R.drawable.ic_about, "About Us", false));
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
    public void notifyMenu() {
        menuItems.clear();
        menuItems.add(new MenuItem(R.drawable.ic_house, "Home", false));
        menuItems.add(new MenuItem(R.drawable.ic_wallet, "Wallet", false));
        menuItems.add(new MenuItem(R.drawable.ic_orders, "Orders", false));
        menuItems.add(new MenuItem(R.drawable.ic_trade, "Trade", false));
        menuItems.add(new MenuItem(R.drawable.ic_portfolio, "Conditional", false));
        menuItems.add(new MenuItem(R.drawable.ic_alert, "Alert", false));
        menuItems.add(new MenuItem(R.drawable.ic_accounts_a, "Accounts", true));
        menuItems.add(new MenuItem(R.drawable.ic_bitcoin, "Donate", false));
        menuItems.add(new MenuItem(R.drawable.ic_about, "About Us", false));
        menuItemAdapter.notifyDataSetChanged();
    }
}
