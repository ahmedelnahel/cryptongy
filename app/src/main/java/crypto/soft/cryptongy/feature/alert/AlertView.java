package crypto.soft.cryptongy.feature.alert;

import com.hannesdorfmann.mosby.mvp.MvpView;

import crypto.soft.cryptongy.feature.shared.ticker.TickerView;

/**
 * Created by prajwal on 12/1/17.
 */

public interface AlertView extends MvpView, TickerView {
    void updateTable();
}
