package crypto.soft.cryptongy.feature.trade.conditional;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.account.CustomDialog;
import crypto.soft.cryptongy.feature.home.CustomArrayAdapter;
import crypto.soft.cryptongy.feature.order.OpenOrderHolder;
import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.json.market.Result;
import crypto.soft.cryptongy.feature.shared.json.marketsummary.MarketSummary;
import crypto.soft.cryptongy.feature.shared.json.ticker.Ticker;
import crypto.soft.cryptongy.feature.shared.json.wallet.Wallet;
import crypto.soft.cryptongy.utils.CoinApplication;
import crypto.soft.cryptongy.utils.GlobalConstant;
import crypto.soft.cryptongy.utils.GlobalUtil;
import crypto.soft.cryptongy.utils.HideKeyboard;
import crypto.soft.cryptongy.utils.ProgressDialogFactory;

/**
 * Created by tseringwongelgurung on 11/28/17.
 */

public class ConditionalFragment extends MvpFragment<ConditionalView, ConditonalPresenter> implements
        ConditionalView, CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    private View view;
    private NestedScrollView nestedScroll;
    private TextView txtCoin, txtBtc, txtLevel, txtVtc, txtEmpty, txtMax, txtAgainst, txtTotal;
    private ImageView imgSync, imgAccSetting;
    private LinearLayout lnlContainer, lnlHolding;
    private EditText edtUnits;

    private RadioGroup rdgUnits;
    private RadioButton rdbSell, rdbBuy, rdbLast;

    private HorizontalScrollView scrollView;
    private TextView lastValuInfo_TXT, BidvalueInfo_TXT, Highvalue_Txt, ASKvalu_TXT, LowvalueInfo_TXT, VolumeValue_Txt, HoldingValue_Txt, lastComp_txt;
    private Spinner spinner;

    private List<Result> coins;
    private AutoCompleteTextView inputCoin;
    private CustomArrayAdapter adapterCoins;

    private boolean isFirst = false;
    private crypto.soft.cryptongy.feature.shared.json.wallet.Result baseWallet, coinWallet;

    //limit
    private RadioGroup rdgPrice;
    private Button btnOk;

    private ToggleButton tgbPrice, tgbLoss;
    private EditText edtPrice, edtLoss, edtProfit, edtTrailerLoss, edtTrailerPrice;

    private CheckBox chbLoss, chbTrailerLoss, chbProfit;
    private TableLayout tblConditional;
    private boolean isCoinAscend = true;
    private boolean isStatusAscend = true;
    private TextWatcher unitWatcher;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_conditional, container, false);
            new HideKeyboard(getContext()).setupUI(view);
            findViews();
            init();
            setOnListner();
            setToggleListner();
            setTextWatcher();
            setCoinAdapter();
            isFirst = true;

        }
        setTitle();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isFirst) {
            isFirst = false;
            presenter.getData();
        }
    }

    public void setToggleListner() {
        tgbLoss.setOnCheckedChangeListener(this);
        tgbPrice.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = compoundButton.getId();
        switch (id) {
            case R.id.tgbLoss:
                boolean isChecked = tgbLoss.isChecked();
                if (isChecked)
                    edtLoss.setText("5");
                else
                    edtLoss.setText(LowvalueInfo_TXT.getText());
                break;
            case R.id.tgbPrice:
                boolean isPChecked = tgbPrice.isChecked();
                if (isPChecked)
                    edtPrice.setText("5");
                else
                    edtPrice.setText(LowvalueInfo_TXT.getText());
                break;
            case R.id.chbLoss:
                if (b && chbTrailerLoss.isChecked())
                    chbTrailerLoss.setChecked(false);
                break;
            case R.id.chbTrailerLoss:
                if (b && chbLoss.isChecked())
                    chbLoss.setChecked(false);
                break;
        }
    }

    @Override
    public ConditonalPresenter createPresenter() {
        return new ConditonalPresenter(getContext());
    }

    @Override
    public void setTitle() {
        TextView txtTitle = getActivity().findViewById(R.id.txtTitle);
        txtTitle.setText(R.string.conditional_trade);
    }

    @Override
    public String getCoin() {
        return txtVtc.getText().toString();
    }

    @Override
    public void findViews() {
        nestedScroll = view.findViewById(R.id.nestedScroll);

        txtCoin = view.findViewById(R.id.txtCoin);
        txtBtc = view.findViewById(R.id.txtBtc);
        txtLevel = view.findViewById(R.id.txtLevel);
        txtEmpty = view.findViewById(R.id.txtEmpty);
        imgSync = view.findViewById(R.id.imgSync);
        imgAccSetting = view.findViewById(R.id.imgAccSetting);
        txtMax = view.findViewById(R.id.txtMax);
        txtAgainst = view.findViewById(R.id.txtAgainst);
        txtTotal = view.findViewById(R.id.txtTotal);
        edtUnits = view.findViewById(R.id.edtUnits);
        rdgUnits = view.findViewById(R.id.rdgUnits);
        rdbSell = view.findViewById(R.id.rdbSell);
        rdbBuy = view.findViewById(R.id.rdbBuy);
        rdbLast = view.findViewById(R.id.rdbLast);

        txtVtc = view.findViewById(R.id.txtVtc);
        lastValuInfo_TXT = view.findViewById(R.id.LastValue_Id);
        BidvalueInfo_TXT = view.findViewById(R.id.BidValue_Id);
        Highvalue_Txt = view.findViewById(R.id.HighValue_Id);
        ASKvalu_TXT = view.findViewById(R.id.AskValue_Id);
        LowvalueInfo_TXT = view.findViewById(R.id.LowValue_Id);
        VolumeValue_Txt = view.findViewById(R.id.VolumeValue_Id);
        HoldingValue_Txt = view.findViewById(R.id.HoldingValue_Id);
        scrollView = view.findViewById(R.id.HorScrollView);

        lnlContainer = view.findViewById(R.id.lnlContainer);
        lnlHolding = view.findViewById(R.id.lnlHolding);

        spinner = view.findViewById(R.id.spinner);

        inputCoin = view.findViewById(R.id.inputCoin);

        rdgPrice = view.findViewById(R.id.rdgPrice);
        edtProfit = view.findViewById(R.id.edtProfit);
        btnOk = view.findViewById(R.id.btnOk);

        chbLoss = view.findViewById(R.id.chbLoss);
        chbTrailerLoss = view.findViewById(R.id.chbTrailerLoss);
        chbProfit = view.findViewById(R.id.chbProfit);

        tblConditional = view.findViewById(R.id.tblConditional);

        tgbPrice = view.findViewById(R.id.tgbPrice);
        tgbLoss = view.findViewById(R.id.tgbLoss);
        edtPrice = view.findViewById(R.id.edtPrice);
        edtLoss = view.findViewById(R.id.edtLoss);

        edtTrailerLoss = view.findViewById(R.id.edtTrailerLoss);
        edtTrailerPrice = view.findViewById(R.id.edtTrailerPrice);
    }

    @Override
    public void init() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.coin_array, R.layout.drop_down_text);
        adapter.setDropDownViewResource(R.layout.drop_down_text);
        spinner.setAdapter(adapter);
    }

    void setCoinAdapter() {
        coins = new ArrayList<>();
        adapterCoins = new CustomArrayAdapter(getContext(), coins);
        inputCoin.setThreshold(1);
        inputCoin.setAdapter(adapterCoins);

        inputCoin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                inputCoin.setText(((Result) ((CustomArrayAdapter) adapterView.getAdapter()).getItem(i)).getMarketName());
                inputCoin.setTextSize(12);
                Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
                        "fonts/calibri.ttf");
                inputCoin.setTypeface(face, Typeface.NORMAL);
                Result result = (Result) ((CustomArrayAdapter) adapterView.getAdapter()).getItem(i);
                txtVtc.setText(result.getMarketName());
                presenter.getData(result.getMarketName());
            }
        });

        inputCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputCoin.setText("");
            }
        });
    }

    @Override
    public void setOnListner() {
        imgSync.setOnClickListener(this);
        imgAccSetting.setOnClickListener(this);
        txtMax.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        rdgPrice.setOnCheckedChangeListener(this);
        chbLoss.setOnCheckedChangeListener(this);
        chbTrailerLoss.setOnCheckedChangeListener(this);
        chbProfit.setOnCheckedChangeListener(this);
        rdgUnits.setOnCheckedChangeListener(this);
    }

    @Override
    public void setTextWatcher() {

        unitWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                calculateTotal();
            }
        };
        edtUnits.addTextChangedListener(unitWatcher);
    }

    @Override
    public void setLevel(String level) {
        txtLevel.setText(level);
    }

    @Override
    public void setMarketSummary(MarketSummary summary) {
        txtVtc.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.VISIBLE);

        if (summary != null) {
            crypto.soft.cryptongy.feature.shared.json.marketsummary.Result result = summary.getResult().get(0);
            lastValuInfo_TXT.setText(String.format("%.8f", result.getLast().doubleValue()));
            edtProfit.setText(String.format("%.8f", result.getLast().doubleValue()));
            BidvalueInfo_TXT.setText(String.format("%.8f", result.getBid().doubleValue()));
            ASKvalu_TXT.setText(String.format("%.8f", result.getAsk().doubleValue()));
            Highvalue_Txt.setText(String.format("%.8f", result.getHigh().doubleValue()));
            VolumeValue_Txt.setText(String.format("%.8f", result.getVolume().doubleValue()));
            LowvalueInfo_TXT.setText(String.format("%.8f", result.getLow().doubleValue()));
            edtLoss.setText("5");
        }
    }

    @Override
    public void onSummaryDataLoad(MarketSummaries marketSummaries) {
        if (marketSummaries.getSuccess()) {
            ((CoinApplication) getActivity().getApplication()).setUsdt_btc(GlobalUtil.round(marketSummaries.getCoinsMap().get("USDT-BTC").getLast(), 4));
            txtBtc.setText("" + ((CoinApplication) getActivity().getApplication()).getUsdt_btc());
            coins.clear();
            coins.addAll(marketSummaries.getResult());
            adapterCoins.notifyDataSetChanged();
        }
        hideLoading();
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
    public void setHolding(Wallet wallet) {
        String[] ar = txtVtc.getText().toString().split("-");
        for (crypto.soft.cryptongy.feature.shared.json.wallet.Result result : wallet.getResult()) {
            if (result.getCurrency().equalsIgnoreCase(ar[0]))
                baseWallet = result;
            else
                coinWallet = result;
        }
        lnlHolding.setVisibility(View.VISIBLE);
        if (coinWallet == null)
            HoldingValue_Txt.setText("0");
        else
            HoldingValue_Txt.setText(String.format("%.8f", baseWallet.getAvailable().doubleValue()));
        if (baseWallet.getBalance() > 0d)
            rdbBuy.setEnabled(true);
        else
            rdbBuy.setEnabled(false);

        if (coinWallet != null && coinWallet.getBalance() > 0d)
            rdbSell.setEnabled(true);
        else
            rdbSell.setEnabled(false);

        if (!rdbBuy.isEnabled() && !rdbSell.isEnabled())
            btnOk.setEnabled(false);
        else
            btnOk.setEnabled(true);
        String coin = txtVtc.getText().toString().split("-")[0];
        setAgaints(coin + "-" + BigDecimal.valueOf(baseWallet.getAvailable()).toPlainString());
    }

    @Override
    public void setMax() {
        if (isBuy()) {
            edtUnits.setText(String.format("%.8f", (baseWallet.getBalance().doubleValue() / Double.valueOf(lastValuInfo_TXT.getText().toString()))));
        } else {
            if (coinWallet == null)
                edtUnits.setText("0");
            else
                edtUnits.setText(String.format("%.8f", coinWallet.getBalance().doubleValue()));
        }
    }

    @Override
    public void setAgaints(String coin) {
        txtAgainst.setText(coin);
    }

    @Override
    public void calculateTotal() {


        try {
            String str = edtUnits.getText().toString();
            if (!TextUtils.isEmpty(str)) {
                String str2 = lastValuInfo_TXT.getText().toString();
                if (!TextUtils.isEmpty(str2)) {
                    Double total = Double.parseDouble(str) * Double.parseDouble(str2);
                    txtTotal.setText(String.format("%.8f", total));
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View view) {
        presenter.onClicked(view.getId());
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.rdbLast:
                edtProfit.setText(lastValuInfo_TXT.getText().toString());
                break;
            case R.id.rdbAsk:
                edtProfit.setText(ASKvalu_TXT.getText().toString());
                break;
            case R.id.rdbBid:
                edtProfit.setText(BidvalueInfo_TXT.getText().toString());
                break;
            case R.id.rdbBuy:
                txtTotal.setText("");
                chbTrailerLoss.setChecked(false);
                chbTrailerLoss.setEnabled(false);
                break;
            case R.id.rdbSell:
                txtTotal.setText("");
                chbTrailerLoss.setEnabled(true);
                break;
        }
    }

    @Override
    public boolean isBuy() {
        if (rdgUnits.getCheckedRadioButtonId() == R.id.rdbBuy)
            return true;
        else
            return false;
    }

    @Override
    public void resetAll() {
        edtUnits.setText("");
        rdbLast.setChecked(true);
        rdbBuy.setChecked(true);
    }

    @Override
    public void setConditional(final List<Conditional> conditionals) {
        tblConditional.removeAllViews();
        if (conditionals == null || conditionals.size() == 0) {
            return;
        }
        View title = getLayoutInflater().inflate(R.layout.table_conditional_title, null);
        TextView txtTitleCoin = title.findViewById(R.id.txtTitleCoin);
        TextView txtTitleStatus = title.findViewById(R.id.txtTitleStatus);

        if (isCoinAscend)
            txtTitleCoin.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
        else
            txtTitleCoin.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);

        txtTitleCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCoinAscend = !isCoinAscend;
                presenter.sortList(conditionals, isCoinAscend);
            }
        });
        if (isStatusAscend)
            txtTitleStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
        else
            txtTitleStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);

        txtTitleStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStatusAscend = !isStatusAscend;
                presenter.sortListByStatus(conditionals, isStatusAscend);
            }
        });
        tblConditional.addView(title);
        for (int i = 0; i < conditionals.size(); i++) {
            final Conditional conditional = conditionals.get(i);
            View sub = getLayoutInflater().inflate(R.layout.table_open_order_sub, null);
            OpenOrderHolder holder = new OpenOrderHolder(sub);
            holder.txtType.setText(conditional.getOrderType());
            holder.txtCoin.setText(conditional.getOrderCoin());
            holder.txtQuantity.setText(String.format("%.2f", conditional.getUnits().doubleValue()));

            Double conditionPrice = conditional.getLowCondition();
            String conditionType = conditional.getConditionType();
            if (conditionPrice == null)
                conditionPrice = conditional.getHighCondition();
            if (TextUtils.isEmpty(conditionType))
                conditionType = conditional.getConditionHighType();

            if (conditionPrice != null && conditionType != null) {
                if (conditionType.equalsIgnoreCase(GlobalConstant.Conditional.TYPE_PERCENTAGE))
                    holder.txtRate.setText(String.format("%.0f", conditionPrice.doubleValue()) + "%");
                else
                    holder.txtRate.setText(String.format("%.8f", conditionPrice.doubleValue()));
            }

            holder.txtTime.setText(conditional.getOrderStatus());

            if (conditional.getOrderStatus().equalsIgnoreCase(GlobalConstant.Conditional.TYPE_OPEN))
                holder.txtAction.setText("Cancel");
            else
                holder.txtAction.setText("NA");
            tblConditional.addView(sub);

            holder.txtAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((TextView) view).getText().toString().equalsIgnoreCase("cancel"))
                        presenter.delete(conditional.getId());
                }
            });

            if (i < conditionals.size() - 1) {
                View line = getLayoutInflater().inflate(R.layout.table_line, null);
                tblConditional.addView(line);
            }
        }
    }

    @Override
    public List<Conditional> getConditionals() {
        if (TextUtils.isEmpty(edtUnits.getText().toString())) {
            edtUnits.setError("Cannot be empty");
            nestedScroll.smoothScrollTo(0, 0);
            return null;
        }

        String total = edtUnits.getText().toString();
        if (!isBuy()) {
            if (coinWallet == null || Double.parseDouble(total) > coinWallet.getBalance()) {
                CustomDialog.showMessagePop(getContext(), "Insufficient balance", null);
                return null;
            }
        }

        Double units, last, against;
        String orderType = GlobalConstant.Conditional.TYPE_SELL;
        String coin = txtVtc.getText().toString();

        units = Double.parseDouble(edtUnits.getText().toString());
        if (rdgUnits.getCheckedRadioButtonId() == R.id.rdbBuy)
            orderType = GlobalConstant.Conditional.TYPE_BUY;

        last = Double.parseDouble(lastValuInfo_TXT.getText().toString());
        against = baseWallet.getAvailable();

        List<Conditional> conditional = new ArrayList<>();
        if (chbLoss.isChecked()) {
            conditional.add(getStop(coin, units, last, against, orderType));
        } else if (chbTrailerLoss.isChecked())
            conditional.add(getTrailerStop(coin, units, last, against, orderType));

        if (chbProfit.isChecked())
            conditional.add(getProfit(coin, units, last, against, orderType));
        return conditional;
    }

    public Conditional getStop(String coin, Double units, Double last, Double against, String orderType) {
        Double lowCondition, lowPrice;
        String conditionType, priceType, stopLossType, orderStatus;

        if (TextUtils.isEmpty(edtPrice.getText().toString())) {
            edtPrice.setError("Cannot be empty");
            return null;
        }
        if (TextUtils.isEmpty(edtLoss.getText().toString())) {
            edtLoss.setError("Cannot be empty");
            return null;
        }

        lowCondition = Double.parseDouble(edtLoss.getText().toString());

        if (tgbLoss.isChecked()) {
            conditionType = GlobalConstant.Conditional.TYPE_PERCENTAGE;
        } else
            conditionType = GlobalConstant.Conditional.TYPE_OTHER;
        if (tgbPrice.isChecked())
            priceType = GlobalConstant.Conditional.TYPE_PERCENTAGE;
        else
            priceType = GlobalConstant.Conditional.TYPE_OTHER;
        lowPrice = Double.parseDouble(edtPrice.getText().toString());

        stopLossType = GlobalConstant.Conditional.TYPE_FIXED;
        orderStatus = GlobalConstant.Conditional.TYPE_OPEN;

        return new Conditional(false, orderType, coin, units, last, against, lowCondition, conditionType,
                lowPrice, priceType, stopLossType, orderStatus);
    }

    public Conditional getTrailerStop(String coin, Double units, Double last, Double against, String orderType) {
        Double lowCondition, lowPrice;
        String conditionType, priceType, stopLossType, orderStatus;

        if (TextUtils.isEmpty(edtTrailerLoss.getText().toString())) {
            edtTrailerLoss.setError("Cannot be empty");
            return null;
        }
        if (TextUtils.isEmpty(edtTrailerPrice.getText().toString())) {
            edtTrailerPrice.setError("Cannot be empty");
            return null;
        }

        lowCondition = Double.parseDouble(edtTrailerLoss.getText().toString());

        conditionType = GlobalConstant.Conditional.TYPE_PERCENTAGE;
        priceType = GlobalConstant.Conditional.TYPE_PERCENTAGE;

        lowPrice = Double.parseDouble(edtTrailerPrice.getText().toString());

        stopLossType = GlobalConstant.Conditional.TYPE_TRAILER;
        orderStatus = GlobalConstant.Conditional.TYPE_OPEN;

        return new Conditional(false, orderType, coin, units, last, against, lowCondition, conditionType,
                lowPrice, priceType, stopLossType, orderStatus);
    }

    public Conditional getProfit(String coin, Double units, Double last, Double against, String orderType) {
        Double highCondition, highPrice;
        String conditionType, priceType, stopLossType, orderStatus;

        if (TextUtils.isEmpty(edtProfit.getText().toString())) {
            edtProfit.setError("Cannot be empty");
            return null;
        }

        highCondition = Double.parseDouble(edtProfit.getText().toString());
        conditionType = GlobalConstant.Conditional.TYPE_OTHER;

        switch (rdgPrice.getCheckedRadioButtonId()) {
            case R.id.rdbAsk:
                priceType = GlobalConstant.Conditional.TYPE_ASK;
                break;
            case R.id.rdbBid:
                priceType = GlobalConstant.Conditional.TYPE_BID;
                break;
            default:
                priceType = GlobalConstant.Conditional.TYPE_LAST;
        }
        highPrice = null;

        stopLossType = GlobalConstant.Conditional.TYPE_NA;
        orderStatus = GlobalConstant.Conditional.TYPE_OPEN;

        return new Conditional(true, orderType, coin, units, last, against, highCondition, conditionType,
                highPrice, priceType, stopLossType, orderStatus);
    }

    @Override
    public void setTicker(Ticker ticker) {
        new TickerV().setData(getContext(), ticker, lastValuInfo_TXT, ASKvalu_TXT, BidvalueInfo_TXT);
    }
}
