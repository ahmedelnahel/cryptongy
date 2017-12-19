package crypto.soft.cryptongy.feature.coin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.alert.CoinName;
import crypto.soft.cryptongy.feature.order.OpenOrderHolder;
import crypto.soft.cryptongy.feature.order.OrderHistoryHolder;
import crypto.soft.cryptongy.feature.shared.json.markethistory.MarketHistory;
import crypto.soft.cryptongy.feature.shared.json.marketsummary.MarketSummary;
import crypto.soft.cryptongy.feature.shared.json.openorder.OpenOrder;
import crypto.soft.cryptongy.feature.shared.json.openorder.Result;
import crypto.soft.cryptongy.feature.shared.json.orderhistory.OrderHistory;
import crypto.soft.cryptongy.feature.shared.json.ticker.Ticker;
import crypto.soft.cryptongy.feature.shared.module.Account;
import crypto.soft.cryptongy.utils.CoinApplication;
import crypto.soft.cryptongy.utils.GlobalUtil;
import crypto.soft.cryptongy.utils.HideKeyboard;
import crypto.soft.cryptongy.utils.ProgressDialogFactory;

/**
 * Created by tseringwongelgurung on 11/25/17.
 */

public class CoinFragment extends MvpFragment<CoinView, CoinPresenter> implements CoinView
        , View.OnClickListener {
    private TextView lastValuInfo_TXT, BidvalueInfo_TXT, Highvalue_Txt, ASKvalu_TXT, LowvalueInfo_TXT, VolumeValue_Txt, HoldingValue_Txt, lastComp_txt;
    private View view;
    private TableLayout tblOpenOrders, tblOrderHistory, tblMarketTrade;
    private LinearLayout lnlContainer;
    private TextView txtLevel, txtOpenOrder, txtOrderHistory, txtEmpty, txtBtc, txtUsd, txtMarket, txtVtc,txtProfit;
    private ImageView imgSync, imgAccSetting;

    private HorizontalScrollView scrollView;

    private boolean isFirst = false;
    private String coinName = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_coin, container, false);
            new HideKeyboard(getContext()).setupUI(view);
            findViews();
            setClickListner();
            isFirst = true;
        } else
            isFirst = false;
        coinName = getArguments().getString("COIN_NAME", "");
        CoinName.coinName = coinName;
        setTitle();
       // hideTotal();
        return view;
    }
    public void hideTotal() {
        txtBtc.setVisibility(View.GONE);
        txtUsd.setVisibility(View.GONE);
        txtProfit.setVisibility(View.GONE);
    }
    @Override
    public CoinPresenter createPresenter() {
        return new CoinPresenter(getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
//        Log.d("Coin screen", "coinName " + coinName );
        double last = 0;
        if (!lastValuInfo_TXT.getText().toString().isEmpty()) last = Double.valueOf(lastValuInfo_TXT.getText().toString());
        Log.d("Coin screen", "last " + last );
        presenter.getData(coinName, last);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.ic_menu_order, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        presenter.onOptionItemClicked(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle() {
    }

    @Override
    public void findViews() {
        tblOpenOrders = view.findViewById(R.id.tblOpenOrders);
        tblOrderHistory = view.findViewById(R.id.tblOrderHistory);
        tblMarketTrade = view.findViewById(R.id.tblMarketTrade);

        txtBtc = view.findViewById(R.id.txtBtc);
        txtUsd = view.findViewById(R.id.txtUsd);
        txtProfit = view.findViewById(R.id.txtProfit);
        txtLevel = view.findViewById(R.id.txtLevel);

        imgSync = view.findViewById(R.id.imgSync);
        imgAccSetting = view.findViewById(R.id.imgAccSetting);

        txtOpenOrder = view.findViewById(R.id.txtOpen);
        txtOrderHistory = view.findViewById(R.id.txtHistory);
        txtMarket = view.findViewById(R.id.txtMarket);

        txtEmpty = view.findViewById(R.id.txtEmpty);
        lnlContainer = view.findViewById(R.id.lnlContainer);

        txtVtc = view.findViewById(R.id.txtVtc);
        lastValuInfo_TXT = view.findViewById(R.id.LastValue_Id);
        BidvalueInfo_TXT = view.findViewById(R.id.BidValue_Id);
        Highvalue_Txt = view.findViewById(R.id.HighValue_Id);
        ASKvalu_TXT = view.findViewById(R.id.AskValue_Id);
        LowvalueInfo_TXT = view.findViewById(R.id.LowValue_Id);
        VolumeValue_Txt = view.findViewById(R.id.VolumeValue_Id);
        scrollView = view.findViewById(R.id.HorScrollView);
    }

    @Override
    public void setClickListner() {
        imgSync.setOnClickListener(this);
        imgAccSetting.setOnClickListener(this);
    }

    @Override
    public void setCalculation(double calculation) {
        double priceInDollar = ((CoinApplication) getActivity().getApplication()).getUsdt_btc();
        txtUsd.setText( "$" + String.valueOf(GlobalUtil.formatNumber(priceInDollar*calculation, "#.####")));
        txtBtc.setText(String.valueOf(GlobalUtil.formatNumber(calculation, "0.00000000") + "฿"));
    }

    @Override
    public void setLevel(String level) {
        txtLevel.setText(level);
    }

    @Override
    public void setOpenOrders(OpenOrder openOrders) {
        tblOpenOrders.removeAllViews();
        if (openOrders == null || openOrders.getResult() == null || openOrders.getResult().isEmpty()) {
            txtOpenOrder.setVisibility(View.GONE);
            return;
        }

        txtOpenOrder.setVisibility(View.VISIBLE);
        View title = getLayoutInflater().inflate(R.layout.table_open_order_title, null);
        tblOpenOrders.addView(title);
        for (int i = 0; i < openOrders.getResult().size(); i++) {
            final Result data = openOrders.getResult().get(i);
            View sub = getLayoutInflater().inflate(R.layout.table_open_order_sub, null);
            OpenOrderHolder holder = new OpenOrderHolder(sub);
            holder.txtType.setText(data.getOrderType());
            holder.txtCoin.setText(data.getExchange());
            holder.txtQuantity.setText(String.valueOf(data.getQuantity()) + "\n" + String.valueOf(data.getQuantityRemaining()));
            holder.txtRate.setText(String.valueOf(data.getLimit()));
            String date = data.getOpened();
            if (!TextUtils.isEmpty(date)) {
                String[] arr = date.split("T");
                String d = arr[0];
                String t = "";
                if (arr.length > 1) {
                    t = arr[1];
                    holder.txtTime.setText(d + "\n" + t);
                } else
                    holder.txtTime.setText(d);
            } else
                holder.txtTime.setText("");
            holder.txtAction.setText("Cancel");
            tblOpenOrders.addView(sub);

            holder.txtAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final CoinApplication application = (CoinApplication) getActivity().getApplicationContext();
                    presenter.cancleOrder(coinName, data.getOrderUuid(), application.getReadAccount() );
                }
            });

            if (i < openOrders.getResult().size() - 1) {
                View line = getLayoutInflater().inflate(R.layout.table_line, null);
                tblOpenOrders.addView(line);
            }
        }
    }

    @Override
    public void setOrderHistory(OrderHistory orderHistory) {
        tblOrderHistory.removeAllViews();
        if (orderHistory == null || orderHistory.getResult() == null || orderHistory.getResult().isEmpty()) {
            txtOrderHistory.setVisibility(View.GONE);
            return;
        }

        txtOrderHistory.setVisibility(View.VISIBLE);
        View title = getLayoutInflater().inflate(R.layout.table_order_history_title, null);
        tblOrderHistory.addView(title);
        for (int i = 0; i < orderHistory.getResult().size(); i++) {
            crypto.soft.cryptongy.feature.shared.json.orderhistory.Result data = orderHistory.getResult().get(i);
            View sub = getLayoutInflater().inflate(R.layout.talbe_order_history_sub, null);
            OrderHistoryHolder holder = new OrderHistoryHolder(sub);
            holder.txtCoin.setText(data.getExchange());
            holder.txtType.setText(data.getOrderType());
            holder.txtQuantity.setText(String.valueOf(data.getQuantity()));
            holder.txtRate.setText(String.valueOf(data.getLimit()));
            String date = data.getClosed();
            if (!TextUtils.isEmpty(date)) {
                String[] arr = date.split("T");
                String d = arr[0];
                String t = "";
                if (arr.length > 1) {
                    t = arr[1];
                    holder.txtTime.setText(d + "\n" + t);
                } else
                    holder.txtTime.setText(d);
            } else
                holder.txtTime.setText("");
            tblOrderHistory.addView(sub);

            if (i < orderHistory.getResult().size() - 1) {
                View line = getLayoutInflater().inflate(R.layout.table_line, null);
                tblOrderHistory.addView(line);
            }
        }
    }

    @Override
    public void setMarketSummary(MarketSummary summary) {
        txtVtc.setVisibility(View.VISIBLE);
        txtVtc.setText(coinName);
        scrollView.setVisibility(View.VISIBLE);

        if (summary != null) {
            crypto.soft.cryptongy.feature.shared.json.marketsummary.Result result = summary.getResult().get(0);
            lastValuInfo_TXT.setText(String.valueOf(String.format("%.8f", result.getLast().doubleValue())));
            BidvalueInfo_TXT.setText(String.valueOf(String.format("%.8f", result.getBid().doubleValue())));
            ASKvalu_TXT.setText(String.valueOf(String.format("%.8f", result.getAsk().doubleValue())));
            Highvalue_Txt.setText(String.valueOf(String.format("%.8f", result.getHigh().doubleValue())));
            VolumeValue_Txt.setText(String.valueOf(String.format("%.6f", result.getVolume().doubleValue())));
            LowvalueInfo_TXT.setText(String.valueOf(String.format("%.8f", result.getLow().doubleValue())));
        }
    }

    @Override
    public void setMarketTrade(MarketHistory marketHistory) {
        tblMarketTrade.removeAllViews();
        if (marketHistory == null || marketHistory.getResult() == null || marketHistory.getResult().isEmpty()) {
            txtMarket.setVisibility(View.GONE);
            return;
        }

        txtMarket.setVisibility(View.VISIBLE);
        View title = getLayoutInflater().inflate(R.layout.table_market_trade_title, null);
        tblMarketTrade.addView(title);
        for (int i = 0; i < marketHistory.getResult().size(); i++) {
            crypto.soft.cryptongy.feature.shared.json.markethistory.Result data = marketHistory.getResult().get(i);
            View sub = getLayoutInflater().inflate(R.layout.talbe_order_history_sub, null);
            OrderHistoryHolder holder = new OrderHistoryHolder(sub);

            holder.txtType.setText(String.valueOf(GlobalUtil.formatNumber(data.getPrice().doubleValue(), "0.00000000")));
            holder.txtQuantity.setText(String.valueOf(data.getQuantity().doubleValue()));
            holder.txtRate.setText(String.valueOf(GlobalUtil.formatNumber(data.getTotal().doubleValue(), "#.####")));

            String date = data.getTimeStamp();
            if (!TextUtils.isEmpty(date)) {
                String[] arr = date.split("T");
                String d = arr[0];
                String t = "";
                if (arr.length > 1) {
                    t = arr[1];
                    holder.txtTime.setText(d + "\n" + t);
                } else
                    holder.txtTime.setText(d);
            } else
                holder.txtTime.setText("");
            tblMarketTrade.addView(sub);

            if (i < marketHistory.getResult().size() - 1) {
                View line = getLayoutInflater().inflate(R.layout.table_line, null);
                tblMarketTrade.addView(line);
            }
        }
    }

    @Override
    public void showLoading(String msg) {
        ProgressDialogFactory.getInstance(getContext(), msg).show();
    }

    @Override
    public void hideLoading() {
        ProgressDialogFactory.dismiss();
    }

    @Override
    public void showEmptyView() {
        txtEmpty.setVisibility(View.VISIBLE);
        lnlContainer.setVisibility(View.GONE);
    }

    @Override
    public void hideEmptyView() {
        txtEmpty.setVisibility(View.GONE);
        lnlContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
//        Log.d("Coin screen sync", "coinName  " + coinName );
        double last = 0;
        if (!lastValuInfo_TXT.getText().toString().isEmpty()) last = Double.valueOf(lastValuInfo_TXT.getText().toString());
        presenter.onClicked(id, coinName, last);
    }

    @Override
    public void setTicker(Ticker ticker) {
        new TickerV().setData(getContext(), ticker, lastValuInfo_TXT, ASKvalu_TXT, BidvalueInfo_TXT);
    }
}
