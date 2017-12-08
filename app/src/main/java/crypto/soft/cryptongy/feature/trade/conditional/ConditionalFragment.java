package crypto.soft.cryptongy.feature.trade.conditional;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import crypto.soft.cryptongy.feature.home.CustomArrayAdapter;
import crypto.soft.cryptongy.feature.order.OpenOrderHolder;
import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.json.market.Result;
import crypto.soft.cryptongy.feature.shared.json.marketsummary.MarketSummary;
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
    private TextView txtCoin, txtBtc, txtLevel, txtVtc, txtEmpty, txtMax, txtAgainst;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_conditional, container, false);
            new HideKeyboard(getContext()).setupUI(view);
            findViews();
            init();
            setOnListner();
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

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = compoundButton.getId();
        EditText editText = null;
        switch (id) {
            case R.id.tgbLoss:
                editText = edtLoss;
                break;
            case R.id.tgbPrice:
                editText = edtPrice;
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
    public void findViews() {
        txtCoin = view.findViewById(R.id.txtCoin);
        txtBtc = view.findViewById(R.id.txtBtc);
        txtLevel = view.findViewById(R.id.txtLevel);
        txtEmpty = view.findViewById(R.id.txtEmpty);
        imgSync = view.findViewById(R.id.imgSync);
        imgAccSetting = view.findViewById(R.id.imgAccSetting);
        txtMax = view.findViewById(R.id.txtMax);
        txtAgainst = view.findViewById(R.id.txtAgainst);

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
        HoldingValue_Txt.setText(String.format("%.8f", baseWallet.getAvailable().doubleValue()));
        if (baseWallet.getBalance() > 0d)
            rdbBuy.setEnabled(true);
        else
            rdbBuy.setEnabled(false);

        if (coinWallet.getBalance() > 0d)
            rdbSell.setEnabled(true);
        else
            rdbSell.setEnabled(false);

        if (!rdbBuy.isEnabled() && !rdbSell.isEnabled())
            btnOk.setEnabled(false);
        else
            btnOk.setEnabled(true);
        String coin = txtVtc.getText().toString().split("-")[0];
        setAgaints(coin + "-" + BigDecimal.valueOf(baseWallet.getBalance()).toPlainString());
    }

    @Override
    public void setMax() {
        edtUnits.setText(coinWallet.getBalance().toString());
    }

    @Override
    public void setAgaints(String coin) {
        txtAgainst.setText(coin);
    }

    @Override
    public void calculateTotal() {
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
                chbTrailerLoss.setChecked(false);
                chbTrailerLoss.setEnabled(false);
                break;
            case R.id.rdbSell:
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
    public void setConditional(List<Conditional> conditionals) {
        tblConditional.removeAllViews();
        if (conditionals == null || conditionals.size() == 0) {
            return;
        }
        View title = getLayoutInflater().inflate(R.layout.table_conditional_title, null);
        tblConditional.addView(title);
        for (int i = 0; i < conditionals.size(); i++) {
            final Conditional conditional = conditionals.get(i);
            View sub = getLayoutInflater().inflate(R.layout.table_open_order_sub, null);
            OpenOrderHolder holder = new OpenOrderHolder(sub);
            holder.txtType.setText(conditional.getOrderType());
            holder.txtCoin.setText(String.format("%.8f", conditional.getUnits().doubleValue()));
            Double low = conditional.getLowPrice();
            if (low != null) {
                if (conditional.getConditionType().equalsIgnoreCase(GlobalConstant.Conditional.TYPE_PERCENTAGE))
                    holder.txtQuantity.setText(String.format("%.0f", low.doubleValue())+"%");
                else
                    holder.txtQuantity.setText(String.format("%.8f", low.doubleValue()));
            } else
                holder.txtQuantity.setText("NA");

            Double high = conditional.getHighPrice();
            if (high != null) {
                if (conditional.getConditionHighType().equalsIgnoreCase(GlobalConstant.Conditional.TYPE_PERCENTAGE))
                    holder.txtRate.setText(String.format("%.0f", high.doubleValue())+"%");
                else
                    holder.txtRate.setText(String.format("%.8f", high.doubleValue()));
            } else
                holder.txtRate.setText("NA");
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
            return null;
        }

        Double units, last, against;
        String orderType = GlobalConstant.Conditional.TYPE_SELL;
        String coin = txtVtc.getText().toString();

        units = Double.parseDouble(edtUnits.getText().toString());
        if (rdgUnits.getCheckedRadioButtonId() == R.id.rdbBuy)
            orderType = GlobalConstant.Conditional.TYPE_BUY;

        last = Double.parseDouble(lastValuInfo_TXT.getText().toString());
        against = baseWallet.getBalance();

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
}
