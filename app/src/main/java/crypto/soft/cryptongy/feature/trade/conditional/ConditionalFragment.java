package crypto.soft.cryptongy.feature.trade.conditional;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.util.ArrayList;
import java.util.List;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.home.CustomArrayAdapter;
import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.json.market.Result;
import crypto.soft.cryptongy.feature.shared.json.marketsummary.MarketSummary;
import crypto.soft.cryptongy.utils.CoinApplication;
import crypto.soft.cryptongy.utils.GlobalUtil;
import crypto.soft.cryptongy.utils.HideKeyboard;
import crypto.soft.cryptongy.utils.ProgressDialogFactory;

/**
 * Created by tseringwongelgurung on 11/28/17.
 */

public class ConditionalFragment extends MvpFragment<ConditionalView, ConditonalPresenter> implements
        ConditionalView, CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private View view;

    private TextView txtCoin, txtBtc, txtLevel, txtVtc;
    private ImageView imgSync, imgAccSetting;
    private LinearLayout lnlContainer;

    private HorizontalScrollView scrollView;
    private TextView lastValuInfo_TXT, BidvalueInfo_TXT, Highvalue_Txt, ASKvalu_TXT, LowvalueInfo_TXT, VolumeValue_Txt, HoldingValue_Txt, lastComp_txt;
    private Spinner spinner, spnCoin;

    private List<Result> coins;
    private AutoCompleteTextView inputCoin;
    private CustomArrayAdapter adapterCoins;

    private boolean isFirst = false;
    private Result result;

    private ToggleButton tgbPrice, tgbLoss;
    private EditText edtPrice, edtLoss;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_conditional, container, false);
            new HideKeyboard(getContext()).setupUI(view);
            findViews();
            init();
            setOnClickListner();
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
        txtVtc = view.findViewById(R.id.txtVtc);
        txtLevel = view.findViewById(R.id.txtLevel);
        imgSync = view.findViewById(R.id.imgSync);
        imgAccSetting = view.findViewById(R.id.imgAccSetting);

        txtVtc = view.findViewById(R.id.txtVtc);
        lastValuInfo_TXT = view.findViewById(R.id.LastValue_Id);
        BidvalueInfo_TXT = view.findViewById(R.id.BidValue_Id);
        Highvalue_Txt = view.findViewById(R.id.HighValue_Id);
        ASKvalu_TXT = view.findViewById(R.id.AskValue_Id);
        LowvalueInfo_TXT = view.findViewById(R.id.LowValue_Id);
        VolumeValue_Txt = view.findViewById(R.id.VolumeValue_Id);
        scrollView = view.findViewById(R.id.HorScrollView);

        lnlContainer = view.findViewById(R.id.lnlContainer);

        spinner = view.findViewById(R.id.spinner);
        spnCoin = view.findViewById(R.id.spnCoin);

        inputCoin = view.findViewById(R.id.inputCoin);

        tgbPrice = view.findViewById(R.id.tgbPrice);
        tgbLoss = view.findViewById(R.id.tgbLoss);

        edtPrice = view.findViewById(R.id.edtPrice);
        edtLoss = view.findViewById(R.id.edtLoss);
    }

    @Override
    public void init() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.coin_array, R.layout.drop_down_text);
        adapter.setDropDownViewResource(R.layout.drop_down_text);
        spinner.setAdapter(adapter);


        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),
                R.array.coin_array2, R.layout.drop_down_text);
        adapter.setDropDownViewResource(R.layout.drop_down_text);
        spnCoin.setAdapter(adapter2);
    }

    void setCoinAdapter() {
        coins = new ArrayList<>();
        adapterCoins = new CustomArrayAdapter(getContext(), coins);
        inputCoin.setThreshold(1);
        inputCoin.setAdapter(adapterCoins);

        inputCoin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                result = new Result();
                inputCoin.setText(((Result) ((CustomArrayAdapter) adapterView.getAdapter()).getItem(i)).getMarketName());
                inputCoin.setTextSize(12);
                Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
                        "fonts/calibri.ttf");
                inputCoin.setTypeface(face, Typeface.NORMAL);
                result = (Result) ((CustomArrayAdapter) adapterView.getAdapter()).getItem(i);

            }
        });
    }

    @Override
    public void setOnClickListner() {
        imgSync.setOnClickListener(this);
        imgAccSetting.setOnClickListener(this);
    }

    @Override
    public void setLevel(String level) {
        txtLevel.setText(level);
    }

    @Override
    public void setMarketSummary(MarketSummary summary) {
        txtVtc.setVisibility(View.VISIBLE);
        txtVtc.setText("Coin");
        scrollView.setVisibility(View.VISIBLE);

        if (summary != null) {
            crypto.soft.cryptongy.feature.shared.json.marketsummary.Result result = summary.getResult().get(0);
            lastValuInfo_TXT.setText(String.valueOf(String.format("%.6f", result.getLast().doubleValue())));
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
        lnlContainer.setVisibility(View.GONE);
    }

    @Override
    public void hideEmptyView() {
        lnlContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        presenter.onClicked(view.getId());
    }
}
