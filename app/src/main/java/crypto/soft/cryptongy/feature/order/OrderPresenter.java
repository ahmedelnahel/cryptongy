package crypto.soft.cryptongy.feature.order;

import android.content.Context;
import android.content.Intent;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.setting.SettingActivity;

/**
 * Created by tseringwongelgurung on 11/23/17.
 */

public class OrderPresenter extends MvpBasePresenter<OrderView> {
    private Context context;

    public OrderPresenter(Context context) {
        this.context = context;
    }

    public void onOptionItemClicked(int id) {
        switch (id){
            case R.id.ic_setting:
                context.startActivity(new Intent(context, SettingActivity.class));
                break;
        }
    }
}
