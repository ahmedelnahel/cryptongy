package crypto.soft.cryptongy.feature.main;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.aboutUs.AboutUsActivity;
import crypto.soft.cryptongy.feature.account.AccountFragment;
import crypto.soft.cryptongy.feature.alert.AlertFragment;
import crypto.soft.cryptongy.feature.arbitage.ArbitageFragment;
import crypto.soft.cryptongy.feature.donate.DonateActivity;
import crypto.soft.cryptongy.feature.home.HomeFragment;
import crypto.soft.cryptongy.feature.order.OrderFragment;
import crypto.soft.cryptongy.feature.trade.conditional.ConditionalFragment;
import crypto.soft.cryptongy.feature.trade.limit.LimitTradeFragment;
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
    public void replaceAccountFragment() {
        getView().notifyMenu("Accounts");
        GlobalUtil.addFragment(context, new AccountFragment(), R.id.container, true);
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
                replaceFragment(new LimitTradeFragment());
                break;
            case "Conditional":
                replaceFragment(new ConditionalFragment());
                break;
            case "Alert":
                replaceFragment(new AlertFragment());
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
            case "Arbitrage":
                replaceFragment(new ArbitageFragment());
                break;
            case "Exit":
                ((AppCompatActivity)context).finish();
                break;
        }
    }
}
