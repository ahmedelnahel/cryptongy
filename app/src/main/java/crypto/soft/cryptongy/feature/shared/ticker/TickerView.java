package crypto.soft.cryptongy.feature.shared.ticker;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
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
                if(TextUtils.isEmpty(txtLast.getText().toString())){
                    txtLast.setText("100");
                }
                if(TextUtils.isEmpty(txtAsk.getText().toString())){
                    txtAsk.setText("100");
                }
                if(TextUtils.isEmpty(txtBid.getText().toString())){
                    txtBid.setText("100");
                }

                Double dblLast = Double.parseDouble(txtLast.getText().toString());
                Double dblAsk = Double.parseDouble(txtAsk.getText().toString());
                Double dblBid = Double.parseDouble(txtBid.getText().toString());

                if (dblLast != null && result.getLast() != null) {
                    txtLast.setText(String.format("%.8f", result.getLast().doubleValue()));
                    if (dblLast.doubleValue() < result.getLast().doubleValue())
                        txtLast.setTextColor(ContextCompat.getColor(context, R.color.color_green));
                    else if (dblLast.doubleValue() > result.getLast().doubleValue())
                        txtLast.setTextColor(ContextCompat.getColor(context, R.color.color_red));
                    else
                        txtLast.setTextColor(ContextCompat.getColor(context, R.color.black_text));
                }

                if (dblAsk != null && result.getAsk() != null) {
                    txtAsk.setText(String.format("%.8f", result.getAsk().doubleValue()));
                    if (dblAsk.doubleValue() < result.getAsk().doubleValue())
                        txtAsk.setTextColor(ContextCompat.getColor(context, R.color.color_green));
                    else if (dblAsk.doubleValue() > result.getAsk().doubleValue())
                        txtAsk.setTextColor(ContextCompat.getColor(context, R.color.color_red));
                    else
                        txtAsk.setTextColor(ContextCompat.getColor(context, R.color.black_text));
                }

                if (dblBid != null && result.getBid() != null) {
                    txtBid.setText(String.format("%.8f", result.getBid().doubleValue()));
                    if (dblBid.doubleValue() < result.getBid().doubleValue())
                        txtBid.setTextColor(ContextCompat.getColor(context, R.color.color_green));
                    else if (dblBid.doubleValue() > result.getBid().doubleValue())
                        txtBid.setTextColor(ContextCompat.getColor(context, R.color.color_red));
                    else
                        txtBid.setTextColor(ContextCompat.getColor(context, R.color.black_text));
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
