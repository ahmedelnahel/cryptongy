package crypto.soft.cryptongy.feature.shared.ticker;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpView;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.shared.json.ticker.Ticker;

/**
 * Created by tseringwongelgurung on 12/18/17.
 */

public interface TickerView extends MvpView {
    void setTicker(Ticker ticker);

    void resetView();

    class TickerV {
        public void setData(Context context, Ticker ticker, TextView txtLast, TextView txtAsk, TextView txtBid) {
            if (ticker != null && ticker.getResult() != null) {
                crypto.soft.cryptongy.feature.shared.json.ticker.Result result = ticker.getResult();
                Double dblLast = Double.parseDouble(txtLast.getText().toString());
                Double dblAsk = Double.parseDouble(txtAsk.getText().toString());
                Double dblBid = Double.parseDouble(txtBid.getText().toString());

                if (dblLast != null && result.getLast() != null) {
                    txtLast.setText(String.format("%.8f", dblLast.doubleValue()));
                    if (dblLast.doubleValue() < result.getLast().doubleValue())
                        txtLast.setTextColor(ContextCompat.getColor(context, R.color.color_green));
                    else if (dblLast.doubleValue() > result.getLast().doubleValue())
                        txtLast.setTextColor(ContextCompat.getColor(context, R.color.color_red));
                }

                if (dblAsk != null && result.getAsk() != null) {
                    txtAsk.setText(String.format("%.8f", dblAsk.doubleValue()));
                    if (dblAsk.doubleValue() < result.getAsk().doubleValue())
                        txtAsk.setTextColor(ContextCompat.getColor(context, R.color.color_green));
                    else if (dblAsk.doubleValue() > result.getAsk().doubleValue())
                        txtAsk.setTextColor(ContextCompat.getColor(context, R.color.color_red));
                }

                if (dblBid != null && result.getBid() != null) {
                    txtBid.setText(String.format("%.8f", dblBid.doubleValue()));
                    if (dblBid.doubleValue() < result.getBid().doubleValue())
                        txtBid.setTextColor(ContextCompat.getColor(context, R.color.color_green));
                    else if (dblBid.doubleValue() > result.getBid().doubleValue())
                        txtBid.setTextColor(ContextCompat.getColor(context, R.color.color_red));
                }
            }
        }


        public void reset(int color, TextView txtLast, TextView txtAsk, TextView txtBid) {
            txtLast.setTextColor(color);
            txtAsk.setTextColor(color);
            txtBid.setTextColor(color);
        }
    }
}
