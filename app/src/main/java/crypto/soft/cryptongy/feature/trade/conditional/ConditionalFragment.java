package crypto.soft.cryptongy.feature.trade.conditional;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.home.CustomArrayAdapter;
import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.json.market.Result;
import crypto.soft.cryptongy.feature.shared.json.marketsummary.MarketSummary;
import crypto.soft.cryptongy.feature.shared.json.wallet.Wallet;
import crypto.soft.cryptongy.utils.CoinApplication;
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
    private TextWatcher unitWatcher, totalWatcher;

    private ToggleButton tgbPrice, tgbLoss;
    private EditText edtPrice, edtLoss,edtProfit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_conditional, container, false);
            new HideKeyboard(getContext()).setupUI(view);
            findViews();
            init();
            setOnClickListner();
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

    public void initToggleListner() {
        tgbPrice.setOnCheckedChangeListener(this);
        tgbLoss.setOnCheckedChangeListener(this);
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
    public void setOnClickListner() {
        imgSync.setOnClickListener(this);
        imgAccSetting.setOnClickListener(this);
        txtMax.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        rdgPrice.setOnCheckedChangeListener(this);
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

        totalWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                try {
//                    String str = edtTotal.getText().toString();
//                    if (!TextUtils.isEmpty(str)) {
//                        String str2 = edtValue.getText().toString();
//                        if (!TextUtils.isEmpty(str2)) {
//                            Double total = GlobalUtil.formatNumber(Double.parseDouble(str) / Double.parseDouble(str2), "#.########");
//                            edtUnits.removeTextChangedListener(unitWatcher);
//                            edtUnits.setText(BigDecimal.valueOf(total).toPlainString());
//                            edtUnits.addTextChangedListener(unitWatcher);
//                        }
//                    }
//                } catch (NumberFormatException e) {
//                    e.printStackTrace();
//                }
            }
        };

        edtUnits.addTextChangedListener(unitWatcher);

//        edtTotal.addTextChangedListener(totalWatcher);

        edtProfit.addTextChangedListener(new TextWatcher() {
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
        });
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
            lastValuInfo_TXT.setText(String.valueOf(String.format("%.6f", result.getLast().doubleValue())));
            edtProfit.setText(String.valueOf(String.format("%.6f", result.getLast().doubleValue())));
            BidvalueInfo_TXT.setText(String.valueOf(String.format("%.6f", result.getBid().doubleValue())));
            ASKvalu_TXT.setText(String.valueOf(String.format("%.6f", result.getAsk().doubleValue())));
            Highvalue_Txt.setText(String.valueOf(String.format("%.6f", result.getHigh().doubleValue())));
            VolumeValue_Txt.setText(String.valueOf(String.format("%.6f", result.getVolume().doubleValue())));
            LowvalueInfo_TXT.setText(String.valueOf(String.format("%.6f", result.getLow().doubleValue())));
        }
    }

    @Override
    public void onSummaryDataLoad(MarketSummaries marketSummaries) {
        if (marketSummaries.getSuccess()) {
            for (crypto.soft.cryptongy.feature.shared.json.market.Result result : marketSummaries.getResult()) {
                if (result.getMarketName().equalsIgnoreCase("USDT-BTC")) {
                    ((CoinApplication) getActivity().getApplication()).setUsdt_btc(GlobalUtil.round(result.getBtcusdt(), 4));
                    txtBtc.setText("" + ((CoinApplication) getActivity().getApplication()).getUsdt_btc());
                }
            }
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
//        try {
//            String str = edtUnits.getText().toString();
//            if (!TextUtils.isEmpty(str)) {
//                String str2 = edtValue.getText().toString();
//                if (!TextUtils.isEmpty(str2)) {
//                    Double total = Double.parseDouble(str) * Double.parseDouble(str2);
//                    edtTotal.removeTextChangedListener(totalWatcher);
//
//                    edtTotal.setText(String.format("%.8f", total));
//                    edtTotal.addTextChangedListener(totalWatcher);
//                }
//            }
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        }
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
                calculateTotal();
                break;
            case R.id.rdbAsk:
                edtProfit.setText(ASKvalu_TXT.getText().toString());
                calculateTotal();
                break;
            case R.id.rdbBid:
                edtProfit.setText(BidvalueInfo_TXT.getText().toString());
                calculateTotal();
                break;
        }
    }

//    @Override
//    public Limit getLimit() {
//        String units = edtUnits.getText().toString();
//        if (TextUtils.isEmpty(units)) {
//            edtUnits.setError("Cannot be empty");
//            return null;
//        }
//
//        String total = edtTotal.getText().toString();
//        crypto.soft.cryptongy.feature.shared.json.wallet.Result result;
//        if (isBuy())
//            result = baseWallet;
//        else
//            result = coinWallet;
//        if (Double.parseDouble(total) <= result.getBalance()) {
//            Limit limit = new Limit();
//            limit.setMarket(txtVtc.getText().toString());
//            limit.setRate(txtBtc.getText().toString());
//            limit.setQuantity(edtTotal.getText().toString());
//            return limit;
//        } else {
//            CustomDialog.showMessagePop(getContext(), "Insufficient balance", null);
//            return null;
//        }
//    }

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
//        edtTotal.setText("");
    }
}
