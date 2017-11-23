package crypto.soft.cryptongy.feature.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.util.ArrayList;
import java.util.List;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.shared.module.Account;
import crypto.soft.cryptongy.feature.shared.listner.OnClickListner;
import crypto.soft.cryptongy.utils.HideKeyboard;

/**
 * Created by tseringwongelgurung on 11/20/17.
 */

public class AccountFragment extends MvpFragment<AccountView, AccountePresenter> implements AccountView {
    private View view;
    private TextView txtEmpty;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;

    private List<Account> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_account, container, false);
            new HideKeyboard(getContext()).setupUI(view);
            initToolbar();
            findViews();
            initRecycleView();
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        presenter.getData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.ic_menu_account, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        presenter.onOptionItemClicked(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public AccountePresenter createPresenter() {
        return new AccountePresenter(getContext());
    }

    @Override
    public void initToolbar() {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        TextView txtTitle = toolbar.findViewById(R.id.txtTitle);
        txtTitle.setText(R.string.account);
    }

    @Override
    public void findViews() {
        recyclerView = view.findViewById(R.id.recycleView);
        txtEmpty = view.findViewById(R.id.txtEmpty);
    }

    @Override
    public void initRecycleView() {
        manager = new LinearLayoutManager(getContext());
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
