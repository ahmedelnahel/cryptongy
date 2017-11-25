package crypto.soft.cryptongy.feature.alert;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.shared.json.marketsummary.MarketSummary;
import crypto.soft.cryptongy.feature.shared.json.marketsummary.Result;
import crypto.soft.cryptongy.network.BittrexServices;

/**
 * Created by maiAjam on 11/20/2017.
 */

public class AlertFragment extends Fragment {
    ProgressBar progressBar;
    dbHandler db;
    TextView lastValuInfo_TXT, BidvalueInfo_TXT, Highvalue_Txt, ASKvalu_TXT, LowvalueInfo_TXT, VolumeValue_Txt, HoldingValue_Txt, lastComp_txt;
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
    // to check one time or every time
    int AlarmFreq = 1;
    int checkedHigher = 1;
    int CheckedLower = 1;
    private int reqCode, CoinId;
    private TextView lastValuInfo_Lab, BidvalueInfo_Lab, Highvalue_Lab, ASKvalu_Lab, LowvalueInfo_Lab, VolumeValue_Lab;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_alert, container, false);


        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar_cyclic);


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
        //

        //
        TextView coinNmae = (TextView) rootView.findViewById(R.id.vtc_txt);
        //
        lowComp_txt = (EditText) rootView.findViewById(R.id.LowValue_ED);
        lastComp_txt = (TextView) rootView.findViewById(R.id.lastvalue_txt);
        highValueComp_txt = (EditText) rootView.findViewById(R.id.HighValue_ED);
        //
        radioGroup = (RadioGroup) rootView.findViewById(R.id.RadioG);
        timeOneRB = (RadioButton) rootView.findViewById(R.id.radioOneTime);
        everyTimeRB = (RadioButton) rootView.findViewById(R.id.radioEvryTime);
        //

        ch_higher = (CheckBox) rootView.findViewById(R.id.ch_higher);
        ch_lower = (CheckBox) rootView.findViewById(R.id.ch_lower);
        //

        coinNmae.setText(coinName);


        db = new dbHandler(getContext());

        CoinInfo coinInfo = db.getCoinInfo(coinName);

        if (coinInfo.getCoinName() == null) {
            highValueComp_txt.setText("");
            lowComp_txt.setText("");

        } else {
            highValueComp_txt.setText(String.valueOf(coinInfo.getHighValue()));
            lowComp_txt.setText(String.valueOf(coinInfo.getLowValue()));

        }


        ch_higher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ch_higher.setChecked(isChecked);
                if (isChecked) {
                    checkedHigher = 2;
                } else {
                    checkedHigher = 1;
                }
            }
        });

        ch_lower.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ch_lower.setChecked(isChecked);
                if (isChecked) {
                    CheckedLower = 2;
                } else {
                    CheckedLower = 1;
                }
            }
        });
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

                LowValueEn = Double.parseDouble(lowComp_txt.getText().toString());
                HighValueEn = Double.parseDouble(highValueComp_txt.getText().toString());

                if (TextUtils.isEmpty(String.valueOf(LowValueEn))) {

                    Toast.makeText(getContext(), "Please Ener The Low Value", Toast.LENGTH_LONG).show();

                } else if (TextUtils.isEmpty(String.valueOf(HighValueEn))) {
                    Toast.makeText(getContext(), "Please Ener The High Value", Toast.LENGTH_LONG).show();

                } else {
                    //save the data and set the alarm manger
                    saveData(LowValueEn, HighValueEn, exchangeName, coinName, AlarmFreq);

                }
            }
        });
        sycCoinInfo sycCoinInfo = new sycCoinInfo();
        sycCoinInfo.execute();


        return rootView;
    }


    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle extra = getArguments();
        if (extra != null) {
            int x = getArguments().getInt("x");
            if (x == 1) {

                coinName = getArguments().getString("CoinName");
                exchangeName = getArguments().getString("exchangeName");
                reqCode = (int) System.currentTimeMillis();
            } else {

                coinName = getArguments().getString("CoinName");
                exchangeName = getArguments().getString("exchangeName");
                HighValueEn = getArguments().getDouble("high", 0);
                LowValueEn = getArguments().getDouble("low", 0);
                reqCode = getArguments().getInt("ReqCode", 0);

            }
        }

    }

    private void saveData(Double lowV, Double highV, String exchangeName, String coinName, int alarmFreq) {


        db = new dbHandler(getContext());
        CoinInfo coinInfo = new CoinInfo(coinName, exchangeName, HighValueEn, LowValueEn);
        db.AddCoinInfo(coinInfo);
        db.updateCoinInfo(coinInfo);
        Intent i = new Intent(getContext(), broadCastTicker.class);

        i.putExtra("coinName", coinName);
        i.putExtra("exchangeName", exchangeName);
        i.putExtra("high", HighValueEn);
        i.putExtra("low", LowValueEn);
        i.putExtra("reqCode", reqCode);
        i.putExtra("alarmFreq", alarmFreq);
        i.putExtra("higherCh", checkedHigher);
        i.putExtra("lowerCh", CheckedLower);


        PendingIntent opertaion = PendingIntent.getBroadcast(getContext(), reqCode, i, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManger = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);


        alarmManger.setRepeating(AlarmManager.RTC_WAKEUP, 30 * 1000,
                1000 * 30, opertaion);


        Toast.makeText(getContext(), "Your Values have been saved successfully", Toast.LENGTH_LONG).show();

    }


    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public class sycCoinInfo extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            //check network conntection

            progressBar.setVisibility(View.VISIBLE);
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

            List<Result> resultList = new ArrayList<>();


            MarketSummary marketSummary = new MarketSummary();
            BittrexServices bittrexServices = new BittrexServices();
            try {
                marketSummary = bittrexServices.getMarketSummaryMock("");
                if (marketSummary.getSuccess()) {

                    resultList = marketSummary.getResult();
                    Result resultItem = resultList.get(0);


                    lastV = resultItem.getLast();
                    bidV = resultItem.getBid();
                    askV = resultItem.getAsk();
                    // need to check it
                    holdingV = resultItem.getBaseVolume();
                    highV = resultItem.getHigh();
                    volumeV = resultItem.getVolume();
                    lowV = resultItem.getLow();


                } else {

                    Toast.makeText(getContext(), marketSummary.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            // set coin info

            progressBar.setVisibility(View.GONE);
            NumberFormat formatter = new DecimalFormat("#0.00000000");
            lastValuInfo_TXT.setText(String.valueOf(formatter.format((lastV))));
            BidvalueInfo_TXT.setText(String.valueOf(formatter.format(bidV)));
            ASKvalu_TXT.setText(String.valueOf(formatter.format(askV)));
            Highvalue_Txt.setText(String.valueOf(formatter.format(highV)));
            VolumeValue_Txt.setText(String.valueOf(formatter.format(volumeV)));
            LowvalueInfo_TXT.setText(String.valueOf(formatter.format(lowV)));

            //


            lastComp_txt.setText(String.valueOf(formatter.format(lastV)));


            //
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