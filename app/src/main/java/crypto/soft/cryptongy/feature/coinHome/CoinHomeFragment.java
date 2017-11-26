package crypto.soft.cryptongy.feature.coinHome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.alert.AlertFragment;
import crypto.soft.cryptongy.feature.coin.CoinFragment;
import crypto.soft.cryptongy.feature.shared.adapter.MainPagerAdaptor;
import crypto.soft.cryptongy.utils.HideKeyboard;

/**
 * Created by tseringwongelgurung on 11/25/17.
 */

public class CoinHomeFragment extends MvpFragment<CoinHomeView, CoinHomePresenter> implements CoinHomeView {
    private View view;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_coin_home, container, false);
            new HideKeyboard(getContext()).setupUI(view);
            findViews();
            initTab();
        }
        setTitle();
        return view;
    }

    @Override
    public CoinHomePresenter createPresenter() {
        return new CoinHomePresenter();
    }

    @Override
    public void setTitle() {
        TextView txtTitle = getActivity().findViewById(R.id.txtTitle);
        txtTitle.setText(R.string.app_name);
    }

    @Override
    public void findViews() {
        tabLayout = view.findViewById(R.id.tabs);
        viewPager = view.findViewById(R.id.viewPager);
    }

    @Override
    public void initTab() {
        MainPagerAdaptor adapter = new MainPagerAdaptor(getActivity().getSupportFragmentManager());
        CoinFragment coinFragment=new CoinFragment();
        Bundle bundle=new Bundle();
        bundle.putString("COIN_NAME","BTC-XVG");
        coinFragment.setArguments(bundle);
        adapter.addFragment(coinFragment, "Coin");
        adapter.addFragment(new AlertFragment(), "Alert");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);
    }
}
