package crypto.soft.cryptongy.feature.order;

import android.content.Context;
import android.content.Intent;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.setting.SettingActivity;
import crypto.soft.cryptongy.feature.shared.json.openorder.OpenOrder;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;

/**
 * Created by tseringwongelgurung on 11/23/17.
 */

public class OrderPresenter extends MvpBasePresenter<OrderView> {
    private Context context;
    private OrderInteractor interactor;

    public OrderPresenter(Context context) {
        this.context = context;
        interactor=new OrderInteractor();
    }

    public void onOptionItemClicked(int id) {
        switch (id) {
            case R.id.ic_setting:
                context.startActivity(new Intent(context, SettingActivity.class));
                break;
        }
    }

    public void onClicked(int id) {
        switch (id) {
            case R.id.imgSync:
                break;
            case R.id.imgAccSetting:
                break;
        }
    }

    public void getData() {
        interactor.getOpenOrder(new OnFinishListner<OpenOrder>() {
            @Override
            public void onComplete(OpenOrder result) {
                if (getView()!=null)
                    getView().setOpenOrders(result);
            }

            @Override
            public void onFail(String error) {

            }
        });
    }
}
