package crypto.soft.cryptongy.feature.account;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpActivity;

import java.util.ArrayList;
import java.util.List;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.account.module.Account;
import crypto.soft.cryptongy.feature.shared.listner.OnClickListner;
import crypto.soft.cryptongy.utils.HideKeyboard;

public class AccountActivity extends MvpActivity<AccountView, AccountePresenter> implements AccountView {
    private TextView txtEmpty;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;

    private List<Account> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        new HideKeyboard(this).setupUI(findViewById(android.R.id.content));
        initToolbar();
        findViews();
        initRecycleView();
        presenter.getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ic_menu_account, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        presenter.onOptionItemClicked(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public AccountePresenter createPresenter() {
        return new AccountePresenter(this);
    }

    @Override
    public void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        TextView txtTitle = toolbar.findViewById(R.id.txtTitle);
        txtTitle.setText(R.string.account);
    }

    @Override
    public void findViews() {
        recyclerView = findViewById(R.id.recycleView);
        txtEmpty = findViewById(R.id.txtEmpty);
    }

    @Override
    public void initRecycleView() {
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
    }

    @Override
    public void setAdapter(final List<Account> list) {
        txtEmpty.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        if (this.list == null) {
            this.list = list;
            adapter = new AccountAdapter(this.list, new OnClickListner() {

                @Override
                public void onItemClickedd(int position) {
                    Account account = list.get(position);
                    presenter.deleteAccount(account.getId());
                }
            }, new OnClickListner() {

                @Override
                public void onItemClickedd(int position) {
                    Account account = list.get(position);
                    presenter.editAccount(account);
                }
            });
            recyclerView.setAdapter(adapter);
        } else {
            this.list.clear();
            this.list.addAll(list);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void addData(Account account) {
        if (this.list == null) {
            List<Account> list = new ArrayList<>();
            list.add(account);
            setAdapter(list);
        } else {
            list.add(account);
            adapter.notifyItemInserted(list.size() - 1);
        }
    }

    @Override
    public void showEmptyView(String msg) {
        if (list != null) {
            list.clear();
            adapter.notifyDataSetChanged();
        }
        txtEmpty.setText(msg);
        txtEmpty.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void hideEmptyView() {
        txtEmpty.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
