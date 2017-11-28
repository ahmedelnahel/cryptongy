package crypto.soft.cryptongy.feature.main;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.aboutUs.AboutUsActivity;
import crypto.soft.cryptongy.feature.account.AccountFragment;
import crypto.soft.cryptongy.feature.donate.DonateActivity;
import crypto.soft.cryptongy.feature.home.HomeFragment;
import crypto.soft.cryptongy.feature.order.OrderFragment;
import crypto.soft.cryptongy.feature.trade.TradeFragment;
import crypto.soft.cryptongy.feature.wallet.WalletFragment;
import crypto.soft.cryptongy.utils.GlobalUtil;

/**
 * Created by tseringwongelgurung on 11/27/17.
 */

public class MainPresenter extends MvpBasePresenter<MainView> {
    private Context context;

    public MainPresenter(Context context) {
        this.context = context;
    }

    public void replaceFragment(Fragment fragment) {
        GlobalUtil.addFragment(context, fragment, R.id.container, false);
    }

    public void onItemClicked(String name) {
        switch (name) {
            case "Home":
                replaceFragment(new HomeFragment());
                break;
            case "Wallet":
                replaceFragment(new WalletFragment());
                break;
            case "Orders":
                replaceFragment(new OrderFragment());
                break;
            case "Trade":
                replaceFragment(new TradeFragment());
                break;
            case "Portfolio":
                break;
            case "Accounts":
                replaceFragment(new AccountFragment());
                break;
            case "Donate":
                context.startActivity(new Intent(context, DonateActivity.class));
                break;
            case "About Us":
                context.startActivity(new Intent(context, AboutUsActivity.class));
                break;
        }
    }
}
