package crypto.soft.cryptongy.feature.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.account.CustomDialog;
import crypto.soft.cryptongy.feature.shared.json.openorder.OpenOrder;
import crypto.soft.cryptongy.feature.shared.json.openorder.Result;
import crypto.soft.cryptongy.feature.shared.json.order.Order;
import crypto.soft.cryptongy.feature.shared.json.orderhistory.OrderHistory;
import crypto.soft.cryptongy.feature.shared.listner.DialogListner;
import crypto.soft.cryptongy.utils.CoinApplication;
import crypto.soft.cryptongy.utils.GlobalConstant;
import crypto.soft.cryptongy.utils.GlobalUtil;
import crypto.soft.cryptongy.utils.HideKeyboard;
import crypto.soft.cryptongy.utils.ProgressDialogFactory;

/**
 * Created by tseringwongelgurung on 11/23/17.
 */

public class OrderFragment extends MvpFragment<OrderView, OrderPresenter> implements OrderView,
        View.OnClickListener {
    private View view;

    private TableLayout tblOpenOrders, tblOrderHistory;
    private Spinner spinner;
    private String spinnerValue;

    private LinearLayout lnlContainer;
    private TextView txtLevel, txtOpenOrder, txtOrderHistory, txtEmpty, txtBtc, txtUsd, txtGap, txtProfit;
    private ImageView imgSync, imgAccSetting;

    private boolean isFirst = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_order, container, false);
            new HideKeyboard(getContext()).setupUI(view);
            findViews();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.coin_array, R.layout.drop_down_text);
            adapter.setDropDownViewResource(R.layout.drop_down_text);
            spinner.setAdapter(adapter);


            CoinApplication application = (CoinApplication) getActivity().getApplicationContext();
            spinnerValue = application.getNotification().getDefaultExchange();
            if (spinnerValue != null) {

                if (spinnerValue.equalsIgnoreCase(getResources().getStringArray(R.array.coin_array)[0])) {

                    spinner.setSelection(0);
                } else {
                    spinner.setSelection(1);

                }
            }

            setClickListner();
            spinerListener();
            isFirst = true;
        } else
            isFirst = false;
        hideTotal();
        setTitle();
        return view;
    }

    private void spinerListener() {

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //if at position zero bitrex and at position 1 binance is called
                if (spinner.getItemAtPosition(position).toString().equalsIgnoreCase(getResources().getStringArray(R.array.coin_array)[0])) {

                    spinnerValue = getResources().getStringArray(R.array.coin_array)[0];
                    getDataFromPresenter(spinnerValue);


                } else {
                    spinnerValue = getResources().getStringArray(R.array.coin_array)[1];
                    getDataFromPresenter(spinnerValue);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void getDataFromPresenter(String spinnerValue) {
        if (TextUtils.isEmpty(spinnerValue)) {
            spinnerValue = GlobalConstant.Exchanges.BITTREX;
        }
        presenter.getData(spinnerValue);
    }

    @Override
    public OrderPresenter createPresenter() {
        return new OrderPresenter(getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isFirst) {
            isFirst = false;

            if (spinnerValue.equalsIgnoreCase(getResources().getStringArray(R.array.coin_array)[0])) {
                getDataFromPresenter(spinnerValue);

            } else {
                getDataFromPresenter(spinnerValue);
            }
        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void setTitle() {
        TextView txtTitle = getActivity().findViewById(R.id.txtTitle);
        txtTitle.setText(R.string.order);
    }

    @Override
    public void findViews() {
        tblOpenOrders = view.findViewById(R.id.tblOpenOrders);
        tblOrderHistory = view.findViewById(R.id.tblOrderHistory);

        txtBtc = view.findViewById(R.id.txtBtc);
        txtUsd = view.findViewById(R.id.txtUsd);
        txtProfit = view.findViewById(R.id.txtProfit);
        txtGap = view.findViewById(R.id.txtGap);
        txtLevel = view.findViewById(R.id.txtLevel);

        imgSync = view.findViewById(R.id.imgSync);
        imgAccSetting = view.findViewById(R.id.imgAccSetting);

        txtOpenOrder = view.findViewById(R.id.txtOpen);
        txtOrderHistory = view.findViewById(R.id.txtHistory);

        txtEmpty = view.findViewById(R.id.txtEmpty);
        lnlContainer = view.findViewById(R.id.lnlContainer);

        spinner = view.findViewById(R.id.spinner);

    }

    public void hideTotal() {
        txtBtc.setVisibility(View.GONE);
        txtUsd.setVisibility(View.GONE);
//        txtGap.setVisibility(View.GONE);
        txtProfit.setVisibility(View.GONE);
    }

    @Override
    public void setClickListner() {
        imgSync.setOnClickListener(this);
        imgAccSetting.setOnClickListener(this);
    }

    @Override
    public void setCalculation(double calculation) {
        double priceInDollar = ((CoinApplication) getActivity().getApplication()).getUsdt_btc();
        txtUsd.setText("$" + String.valueOf(GlobalUtil.formatNumber(priceInDollar * calculation, "#.####")));
        txtBtc.setText(String.valueOf(GlobalUtil.formatNumber(calculation, "#.00000000") + "฿"));
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
            holder.txtType.setText(data.getOrderType().replace("LIMIT_", ""));
            holder.txtCoin.setText(data.getExchange());
            holder.txtQuantity.setText(String.format("%.2f", data.getQuantity()) + "\n" + String.format("%.2f", data.getQuantityRemaining()));
            holder.txtRate.setText(String.format("%.8f", data.getLimit()));
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
                    CustomDialog.showConfirmation(getContext(), getString(R.string.cancle_confirm), new DialogListner() {
                        @Override
                        public void onOkClicked() {
                            final CoinApplication application = (CoinApplication) getActivity().getApplicationContext();
                            presenter.cancleOrder(data.getExchange(), data.getOrderUuid(), application.getReadAccount());
                        }
                    });
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
            holder.txtType.setText(data.getOrderType().replace("LIMIT_", ""));
            holder.txtCoin.setText(data.getExchange());
            holder.txtQuantity.setText(String.format("%.2f", data.getQuantity()));
            holder.txtRate.setText(String.format("%.8f", data.getLimit()));
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
    public void showLoading(String msg) {
        ProgressDialogFactory.getInstance(getContext(), msg).show(new DialogListner() {
            @Override
            public void onOkClicked() {
                getActivity().onBackPressed();
            }
        });
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
    public void setOrder(Order order) {

    }

    @Override
    public void resetView() {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        presenter.onClicked(id);
    }
}
