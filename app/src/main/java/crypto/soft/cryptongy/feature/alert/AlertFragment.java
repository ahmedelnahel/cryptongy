package crypto.soft.cryptongy.feature.alert;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.account.CustomDialog;
import crypto.soft.cryptongy.feature.home.CustomArrayAdapter;
import crypto.soft.cryptongy.feature.main.MainActivity;
import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.json.market.Result;
import crypto.soft.cryptongy.feature.shared.json.marketsummary.MarketSummary;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.feature.trade.TradeInteractor;
import crypto.soft.cryptongy.network.BittrexServices;

/**
 * Created by maiAjam on 11/20/2017.
 */

public class AlertFragment extends MvpFragment<AlertView, AlertPresenter> implements AlertView, View.OnClickListener {
    ProgressBar progressBar;

    //    TextView lastValuInfo_TXT, BidvalueInfo_TXT, Highvalue_Txt, ASKvalu_TXT, LowvalueInfo_TXT, VolumeValue_Txt, HoldingValue_Txt, lastComp_txt;
    EditText lowComp_txt, highValueComp_txt;
    Button save_b;
    RadioGroup radioGroup;
    RadioButton RB_oneTime, RB_everyTime;
    Double lastV, bidV, highV, askV, lowV, volumeV, holdingV;
    String coinName, exchangeName;
    Double HighValueEn, LowValueEn;
    int x = 1;
    RadioGroup alarm;
    RadioButton timeOneRB, everyTimeRB;
    CheckBox ch_higher, ch_lower;

    //    AutoCompleteTextView inputCoin;
//    CustomArrayAdapter adapterCoins;
////    Spinner spinner;
//    List<Result> coins;
    Result result;
    TableLayout tblMarketTradeAlert;

    // to check one time or every time
    int AlarmFreq = 1;
    private int reqCode, CoinId;
    private TextView lastValuInfo_Lab, BidvalueInfo_Lab, Highvalue_Lab, ASKvalu_Lab, LowvalueInfo_Lab,
            VolumeValue_Lab, coinNmae, txtEmpty, txtMarket;
    private ImageView imgSync, imgAccSetting;

    private View rootView;
    private HorizontalScrollView scrollView;
    private TextView lastValuInfo_TXT, BidvalueInfo_TXT, Highvalue_Txt, ASKvalu_TXT, LowvalueInfo_TXT,
            VolumeValue_Txt, HoldingValue_Txt, lastComp_txt;
    private Spinner spinner;

    private List<Result> coins;
    private AutoCompleteTextView inputCoin;
    private CustomArrayAdapter adapterCoins;

    private RelativeLayout rllContainer;
    private boolean isDel = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_alert, container, false);

        setTitle();
        findViews();
        setOnClickListner();

        return rootView;
    }

    public void setTitle() {
        TextView txtTitle = getActivity().findViewById(R.id.txtTitle);
        txtTitle.setText(R.string.alert);
    }

    private void findViews() {
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar_cyclic);

        imgSync = rootView.findViewById(R.id.imgSync);
        imgAccSetting = rootView.findViewById(R.id.imgAccSetting);
        imgSync.setOnClickListener(this);
        imgAccSetting.setOnClickListener(this);

        txtEmpty = (TextView) rootView.findViewById(R.id.txtEmpty);
        rllContainer = rootView.findViewById(R.id.rllContainer);
        txtMarket = rootView.findViewById(R.id.txtMarket);

        // coin detials
        lastValuInfo_TXT = (TextView) rootView.findViewById(R.id.LastValue_Id);
        BidvalueInfo_TXT = (TextView) rootView.findViewById(R.id.BidValue_Id);
        Highvalue_Txt = (TextView) rootView.findViewById(R.id.HighValue_Id);
        ASKvalu_TXT = (TextView) rootView.findViewById(R.id.AskValue_Id);
        LowvalueInfo_TXT = (TextView) rootView.findViewById(R.id.LowValue_Id);
        VolumeValue_Txt = (TextView) rootView.findViewById(R.id.VolumeValue_Id);

        lastValuInfo_Lab = (TextView) rootView.findViewById(R.id.LabLast);
        BidvalueInfo_Lab = (TextView) rootView.findViewById(R.id.LabBid);
        Highvalue_Lab = (TextView) rootView.findViewById(R.id.LabHigh);
        ASKvalu_Lab = (TextView) rootView.findViewById(R.id.LabAsk);
        LowvalueInfo_Lab = (TextView) rootView.findViewById(R.id.LabLow);
        VolumeValue_Lab = (TextView) rootView.findViewById(R.id.LabVolume);

        inputCoin = rootView.findViewById(R.id.inputCoin);
        spinner = rootView.findViewById(R.id.spinner);
        txtMarket = rootView.findViewById(R.id.txtMarket);
        tblMarketTradeAlert = rootView.findViewById(R.id.tblMarketTradeAlert);

        coinNmae = (TextView) rootView.findViewById(R.id.vtc_txt);

        lowComp_txt = (EditText) rootView.findViewById(R.id.LowValue_ED);
        lastComp_txt = (TextView) rootView.findViewById(R.id.lastvalue_txt);
        highValueComp_txt = (EditText) rootView.findViewById(R.id.HighValue_ED);

        radioGroup = (RadioGroup) rootView.findViewById(R.id.RadioG);
        timeOneRB = (RadioButton) rootView.findViewById(R.id.radioOneTime);
        everyTimeRB = (RadioButton) rootView.findViewById(R.id.radioEvryTime);

        ch_higher = (CheckBox) rootView.findViewById(R.id.ch_higher);
        ch_lower = (CheckBox) rootView.findViewById(R.id.ch_lower);

        coinName = CoinName.coinName;
        coinNmae.setText(coinName);
    }

    private void getCoins() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.coin_array, R.layout.drop_down_text);
        adapter.setDropDownViewResource(R.layout.drop_down_text);
        spinner.setAdapter(adapter);

        TradeInteractor interactor = new TradeInteractor();
        progressBar.setVisibility(View.VISIBLE);
        txtEmpty.setVisibility(View.GONE);
        rllContainer.setVisibility(View.GONE);

        interactor.loadSummary(new OnFinishListner<MarketSummaries>() {
            @Override
            public void onComplete(MarketSummaries marketSummaries) {
                coins = new ArrayList<>();
                coins.addAll(marketSummaries.getResult());
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
                        coinNmae.setText(result.getMarketName());

                        sycCoinInfo sycCoinInfo = new sycCoinInfo();
                        sycCoinInfo.execute(coinNmae.getText().toString());
                    }
                });
                progressBar.setVisibility(View.GONE);
                rllContainer.setVisibility(View.GONE);
                if (tblMarketTradeAlert.getChildCount() > 0)
                    txtEmpty.setVisibility(View.GONE);
                else
                    txtEmpty.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFail(String error) {
                CustomDialog.showMessagePop(getContext(), error, null);
                progressBar.setVisibility(View.GONE);
                rllContainer.setVisibility(View.GONE);
                if (tblMarketTradeAlert.getChildCount() > 0)
                    txtEmpty.setVisibility(View.GONE);
                else
                    txtEmpty.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setOnClickListner() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radioOneTime:
                        AlarmFreq = 1;
                        break;
                    case R.id.radioEvryTime:
                        AlarmFreq = 2;
                        break;
                }
            }
        });
        save_b = (Button) rootView.findViewById(R.id.save_b);

        save_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ch_higher.isChecked() && !ch_lower.isChecked()) {
                    CustomDialog.showMessagePop(getContext(), "One should be checked", null);
                    return;
                }
                String last, high;
                last = lowComp_txt.getText().toString();
                high = highValueComp_txt.getText().toString();
                boolean error = false;
                if (ch_lower.isChecked() && TextUtils.isEmpty(last)) {
                    lowComp_txt.setError("Cannot be Empty.");
                    error = true;
                } else if (ch_lower.isChecked()) {
                    LowValueEn = Double.parseDouble(lowComp_txt.getText().toString());
                } else
                    LowValueEn = 0d;

                if (ch_higher.isChecked() && TextUtils.isEmpty(high)) {
                    highValueComp_txt.setError("Cannot be Empty.");
                    error = true;
                } else if (ch_higher.isChecked()) {
                    HighValueEn = Double.parseDouble(highValueComp_txt.getText().toString());
                } else
                    HighValueEn = 0d;

                if (error)
                    return;
                presenter.saveData(getContext(), LowValueEn, HighValueEn, exchangeName, coinNmae.getText().toString(),
                        AlarmFreq, reqCode, ch_higher, ch_lower);
            }
        });
    }


    @NonNull
    @Override
    public AlertPresenter createPresenter() {
        return new AlertPresenter();
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extra = getArguments();
        if (extra != null) {
            int x = getArguments().getInt("x");
            if (x == 1) {
                coinName = getArguments().getString("COIN_NAME");
                exchangeName = getArguments().getString("exchangeName");
                reqCode = (int) System.currentTimeMillis();
            } else {
                coinName = getArguments().getString("COIN_NAME");
                exchangeName = getArguments().getString("exchangeName");
                HighValueEn = getArguments().getDouble("high", 0);
                LowValueEn = getArguments().getDouble("low", 0);
                reqCode = getArguments().getInt("ReqCode", 0);
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateTable();
        getCoins();
    }

    @Override
    public void updateTable() {
        tblMarketTradeAlert.removeAllViews();
        List<CoinInfo> coinInfoList = presenter.getCoinInfo();
        if (coinInfoList != null && coinInfoList.size() > 0) {
            txtMarket.setVisibility(View.VISIBLE);
            View title = getLayoutInflater().inflate(R.layout.table_alert_title, null);
            tblMarketTradeAlert.addView(title);
            for (int i = 0; i < coinInfoList.size(); i++) {
                View sub = getLayoutInflater().inflate(R.layout.table_alert_sub, null);
                final AlertHolder holder = new AlertHolder(sub);
                holder.txtCoin.setText(coinInfoList.get(i).getCoinName());
                holder.txtLowPrice.setText(String.valueOf(coinInfoList.get(i).getLowValue()));
                holder.txtHighPrice.setText(String.valueOf(coinInfoList.get(i).getHighValue()));
                tblMarketTradeAlert.addView(sub);

                holder.txtAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isDel = true;
                        presenter.deleteCoinInfo(holder.txtCoin.getText().toString());
                    }
                });
            }
        } else {
            if (isDel && rllContainer.getVisibility() == View.GONE) {
                txtEmpty.setVisibility(View.VISIBLE);
            }
            txtMarket.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.imgSync:
                if (coins != null)
                    coins.clear();
                if (adapterCoins != null)
                    adapterCoins.notifyDataSetChanged();
                inputCoin.setText("");
                updateTable();
                getCoins();
                break;
            case R.id.imgAccSetting:
                if (getActivity() instanceof MainActivity)
                    ((MainActivity) getActivity()).getPresenter().replaceAccountFragment();
                else
                    startActivity(new Intent(getActivity(), AlertActivity.class));
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public class sycCoinInfo extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            //check network conntection

            ConnectivityManager cm =
                    (ConnectivityManager) getActivity().getSystemService(getContext().CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&

                    activeNetwork.isConnectedOrConnecting();


            if (isConnected) {

            } else {
                Toast.makeText(getContext(), "Check Your Network Connection..", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            List<crypto.soft.cryptongy.feature.shared.json.marketsummary.Result> resultList = new ArrayList<>();

            MarketSummary marketSummary;
            BittrexServices bittrexServices = new BittrexServices();
            try {
                marketSummary = bittrexServices.getMarketSummary(inputCoin.getText().toString());
                if (marketSummary.getSuccess()) {
                    resultList = marketSummary.getResult();
                    crypto.soft.cryptongy.feature.shared.json.marketsummary.Result resultItem = resultList.get(0);

                    lastV = resultItem.getLast();
                    bidV = resultItem.getBid();
                    askV = resultItem.getAsk();
                    // need to check it
                    holdingV = resultItem.getBaseVolume();
                    highV = resultItem.getHigh();
                    volumeV = resultItem.getVolume();
                    lowV = resultItem.getLow();
//                    for (Result result : marketSummary.getResult()) {
//                        if (result.getMarketName().equalsIgnoreCase("USDT-BTC")) {
//                            ((CoinApplication) getActivity().getApplication()).setUsdt_btc(GlobalUtil.round(result.getBtcusdt(), 4));
//                        }
//                    }
                    return "Success";
                } else {
                    Toast.makeText(getContext(), marketSummary.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (TextUtils.isEmpty(s)) {
                progressBar.setVisibility(View.GONE);
                txtEmpty.setVisibility(View.VISIBLE);
                rllContainer.setVisibility(View.GONE);
                return;
            }
            progressBar.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.GONE);
            rllContainer.setVisibility(View.VISIBLE);
            lastValuInfo_TXT.setText(String.valueOf(String.format("%.6f", lastV)));
            BidvalueInfo_TXT.setText(String.valueOf(String.format("%.6f", bidV)));
            ASKvalu_TXT.setText(String.valueOf(String.format("%.6f", askV)));
            Highvalue_Txt.setText(String.valueOf(String.format("%.6f", highV)));
            VolumeValue_Txt.setText(String.valueOf(String.format("%.6f", volumeV)));
            LowvalueInfo_TXT.setText(String.valueOf(String.format("%.6f", lowV)));

            lastComp_txt.setText(String.valueOf(String.format("%.6f", lastV)));

            Typeface typeFaceCalibri = Typeface.createFromAsset(getContext().getAssets(), "calibri.ttf");

            lastValuInfo_TXT.setTypeface(typeFaceCalibri);
            BidvalueInfo_TXT.setTypeface(typeFaceCalibri);
            ASKvalu_TXT.setTypeface(typeFaceCalibri);
            Highvalue_Txt.setTypeface(typeFaceCalibri);
            VolumeValue_Txt.setTypeface(typeFaceCalibri);
            LowvalueInfo_TXT.setTypeface(typeFaceCalibri);
            lastComp_txt.setTypeface(typeFaceCalibri);

            lastValuInfo_Lab.setTypeface(typeFaceCalibri);
            BidvalueInfo_Lab.setTypeface(typeFaceCalibri);
            Highvalue_Lab.setTypeface(typeFaceCalibri);
            ASKvalu_Lab.setTypeface(typeFaceCalibri);
            LowvalueInfo_Lab.setTypeface(typeFaceCalibri);
            VolumeValue_Lab.setTypeface(typeFaceCalibri);

            save_b.setTypeface(typeFaceCalibri);
        }
    }
}