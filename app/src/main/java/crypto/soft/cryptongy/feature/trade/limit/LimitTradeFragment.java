package crypto.soft.cryptongy.feature.trade.limit;

import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.account.CustomDialog;
import crypto.soft.cryptongy.feature.home.CustomArrayAdapter;
import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.json.market.Result;
import crypto.soft.cryptongy.feature.shared.json.marketsummary.MarketSummary;
import crypto.soft.cryptongy.feature.shared.json.ticker.Ticker;
import crypto.soft.cryptongy.feature.shared.json.wallet.Wallet;
import crypto.soft.cryptongy.feature.shared.listner.DialogListner;
import crypto.soft.cryptongy.utils.CoinApplication;
import crypto.soft.cryptongy.utils.GlobalConstant;
import crypto.soft.cryptongy.utils.GlobalUtil;
import crypto.soft.cryptongy.utils.HideKeyboard;
import crypto.soft.cryptongy.utils.ProgressDialogFactory;

/**
 * Created by tseringwongelgurung on 11/28/17.
 */

public class LimitTradeFragment extends MvpFragment<LimitView, LimitPresenter> implements LimitView, View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private View view;
    private TextView txtCoin, txtBtc, txtLevel, txtVtc, txtEmpty, txtMax, txtAgainst;
    private ImageView imgSync, imgAccSetting;
    private LinearLayout lnlContainer, lnlLast, lnlBid, lnlAsk, lnlLow, lnlHigh, lnlHolding;
    private EditText edtUnits;

    private RadioGroup rdgUnits;
    private RadioButton rdbSell, rdbBuy, rdbLast;

    private HorizontalScrollView scrollView;
    private TextView lastValuInfo_TXT, BidvalueInfo_TXT, Highvalue_Txt, ASKvalu_TXT, LowvalueInfo_TXT, VolumeValue_Txt, HoldingValue_Txt, lastComp_txt;
    private Spinner spinner;
    private String spinnerValue;

    private List<Result> coins;
    private AutoCompleteTextView inputCoin;
    private CustomArrayAdapter adapterCoins;

    private boolean isFirst = false;
    private crypto.soft.cryptongy.feature.shared.json.wallet.Result baseWallet, coinWallet;

    //limit
    private RadioGroup rdgValue;
    private EditText edtValue, edtTotal;
    private Button btnOk;
    private TextWatcher unitWatcher, totalWatcher;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_trade, container, false);
            new HideKeyboard(getContext()).setupUI(view);
            findViews();
            init();
            setOnListner();
            setTextWatcher();
            setCoinAdapter();
            setSpinnerDefaultValue();
            spinerListener();
            isFirst = true;
        }
        setTitle();
        return view;
    }

    private void setSpinnerDefaultValue() {
        CoinApplication application = (CoinApplication) getActivity().getApplicationContext();
        spinnerValue = application.getNotification().getDefaultExchange();
        if ( spinnerValue!= null) {

            if (spinnerValue.equalsIgnoreCase(getResources().getStringArray(R.array.coin_array)[0])) {

                spinner.setSelection(0);
                spinnerValue=getResources().getStringArray(R.array.coin_array)[0];
            } else {
                spinner.setSelection(1);
                spinnerValue=getResources().getStringArray(R.array.coin_array)[1];

            }
        }
    }


    private void spinerListener() {

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //if at position zero bitrex and at position 1 binance is called
                if (spinner.getItemAtPosition(position).toString().equalsIgnoreCase(getResources().getStringArray(R.array.coin_array)[0])) {
                    spinnerValue = getResources().getStringArray(R.array.coin_array)[0];
                } else {
                    spinnerValue = getResources().getStringArray(R.array.coin_array)[1];
//
                }
                presenter.getDataForTrade(spinnerValue);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    @Override
    public LimitPresenter createPresenter() {
        return new LimitPresenter(getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isFirst) {
            isFirst = false;

          if(spinnerValue==null){
              spinnerValue=getResources().getStringArray(R.array.coin_array)[0];
          }

          presenter.getDataForTrade(spinnerValue);
           // presenter.getData();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void setTitle() {
        TextView txtTitle = getActivity().findViewById(R.id.txtTitle);
        txtTitle.setText(R.string.trade);
    }

    @Override
    public String getCoin() {
        return txtVtc.getText().toString();
    }

    @Override
    public String getExchangeValue() {
        if (spinnerValue==null){
            spinnerValue= GlobalConstant.Exchanges.BITTREX;
        }
        return spinnerValue;
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
        lnlLast = view.findViewById(R.id.lnlLast);
        lnlBid = view.findViewById(R.id.lnlBid);
        lnlAsk = view.findViewById(R.id.lnlAsk);
        lnlLow = view.findViewById(R.id.lnlLow);
        lnlHigh = view.findViewById(R.id.lnlHigh);

        spinner = view.findViewById(R.id.spinner);

        inputCoin = view.findViewById(R.id.inputCoin);

        rdgValue = view.findViewById(R.id.rdgValue);
        edtValue = view.findViewById(R.id.edtValue);
        edtTotal = view.findViewById(R.id.edtTotal);
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
                presenter.getDataForTrade(result.getMarketName(),spinnerValue);
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
        rdgValue.setOnCheckedChangeListener(this);

        lnlLast.setOnClickListener(this);
        lnlAsk.setOnClickListener(this);
        lnlBid.setOnClickListener(this);
        lnlHigh.setOnClickListener(this);
        lnlLow.setOnClickListener(this);
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
                try {
                    String str = edtTotal.getText().toString();
                    if (!TextUtils.isEmpty(str)) {
                        String str2 = edtValue.getText().toString();
                        if (!TextUtils.isEmpty(str2)) {
                            Double total = GlobalUtil.formatNumber(Double.parseDouble(str) / Double.parseDouble(str2), "#.00000000");
                            edtUnits.removeTextChangedListener(unitWatcher);
                            edtUnits.setText(BigDecimal.valueOf(total).toPlainString());
                            edtUnits.addTextChangedListener(unitWatcher);
                        }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        };

        edtUnits.addTextChangedListener(unitWatcher);

        edtTotal.addTextChangedListener(totalWatcher);

        edtValue.addTextChangedListener(new TextWatcher() {
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
            lastValuInfo_TXT.setText(String.format("%.8f", result.getLast().doubleValue()));
            edtValue.setText(String.format("%.8f", result.getLast().doubleValue()));
            BidvalueInfo_TXT.setText(String.format("%.8f", result.getBid().doubleValue()));
            ASKvalu_TXT.setText(String.format("%.8f", result.getAsk().doubleValue()));
            Highvalue_Txt.setText(String.format("%.8f", result.getHigh().doubleValue()));
            VolumeValue_Txt.setText(String.format("%.4f", result.getVolume().doubleValue()));
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
            HoldingValue_Txt.setText(String.format("%.4f", coinWallet.getBalance().doubleValue()));
        if (baseWallet.getAvailable() > 0d)
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
        edtUnits.setError(null);
        if (isBuy()) {
            String value = edtValue.getText().toString();
            if (!TextUtils.isEmpty(value)) {
                if (baseWallet.getAvailable() > 0.0025) {
                    double units = (baseWallet.getAvailable() - (baseWallet.getAvailable() * 0.0025)) / Double.valueOf(value);
                    edtUnits.setText("" + units);
                } else edtUnits.setText("0");
            }
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
                String str2 = edtValue.getText().toString();
                if (!TextUtils.isEmpty(str2)) {
                    Double total = Double.parseDouble(str) * Double.parseDouble(str2);
                    edtTotal.removeTextChangedListener(totalWatcher);
                    edtTotal.setText(String.format("%.8f", total));
                    edtTotal.addTextChangedListener(totalWatcher);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.lnlLast:
                setValue(lastValuInfo_TXT.getText().toString());
                break;
            case R.id.lnlBid:
                setValue(BidvalueInfo_TXT.getText().toString());
                break;
            case R.id.lnlHigh:
                setValue(Highvalue_Txt.getText().toString());
                break;
            case R.id.lnlAsk:
                setValue(ASKvalu_TXT.getText().toString());
                break;
            case R.id.lnlLow:
                setValue(LowvalueInfo_TXT.getText().toString());
                break;
            default:
                presenter.onClicked(view.getId());
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.rdbLast:
                edtValue.setText(lastValuInfo_TXT.getText().toString());
                calculateTotal();
                break;
            case R.id.rdbAsk:
                edtValue.setText(ASKvalu_TXT.getText().toString());
                calculateTotal();
                break;
            case R.id.rdbBid:
                edtValue.setText(BidvalueInfo_TXT.getText().toString());
                calculateTotal();
                break;
        }
    }

    @Override
    public Limit getLimit() {
        String units = edtUnits.getText().toString();
        if (TextUtils.isEmpty(units)) {
            edtUnits.setError("Cannot be empty");
            return null;
        }

        String total = edtTotal.getText().toString();
        crypto.soft.cryptongy.feature.shared.json.wallet.Result result;
        if (isBuy())
            result = baseWallet;
        else {
            total = edtUnits.getText().toString();
            result = coinWallet;
        }

        if (result != null && Double.parseDouble(total) <= result.getBalance()) {
            Limit limit = new Limit();
            limit.setMarket(txtVtc.getText().toString());
            limit.setRate(Double.parseDouble(edtValue.getText().toString()));
            limit.setQuantity(Double.parseDouble(edtUnits.getText().toString()));
            return limit;
        } else {
            CustomDialog.showMessagePop(getContext(), "Insufficient balance", null);
            return null;
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
        edtTotal.setText("");
    }

    @Override
    public void setValue(String value) {
        if (!TextUtils.isEmpty(value) && edtValue.isFocused())
            edtValue.setText(value);
    }

    @Override
    public void setTicker(Ticker ticker) {
        new TickerV().setData(getContext(), ticker, lastValuInfo_TXT, ASKvalu_TXT, BidvalueInfo_TXT);
    }

    @Override
    public void resetView() {
        new TickerV().reset(ContextCompat.getColor(getContext(), R.color.setting_text), lastValuInfo_TXT, ASKvalu_TXT, BidvalueInfo_TXT);
    }


    @Override
    public void onStart() {
        super.onStart();
        String coinNam = inputCoin.getText().toString();
        if (!TextUtils.isEmpty(coinNam))
            presenter.startTicker(coinNam,spinnerValue);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.stopTimer();
    }
}
