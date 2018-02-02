package crypto.soft.cryptongy.feature.wallet;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.account.CustomDialog;
import crypto.soft.cryptongy.feature.coinHome.CoinHomeActivity;
import crypto.soft.cryptongy.feature.main.MainActivity;
import crypto.soft.cryptongy.feature.setting.Notification;
import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.json.wallet.Result;
import crypto.soft.cryptongy.feature.shared.json.wallet.Wallet;
import crypto.soft.cryptongy.feature.shared.module.Account;
import crypto.soft.cryptongy.network.BinanceServices;
import crypto.soft.cryptongy.network.BittrexServices;
import crypto.soft.cryptongy.utils.AlertUtility;
import crypto.soft.cryptongy.utils.CoinApplication;
import crypto.soft.cryptongy.utils.GlobalConstant;
import crypto.soft.cryptongy.utils.GlobalUtil;
import crypto.soft.cryptongy.utils.HideKeyboard;
import crypto.soft.cryptongy.utils.ViewFontHelper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WalletFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WalletFragment extends Fragment implements OnRecyclerItemClickListener<Result,String>, View.OnClickListener {
    public String TAG = getClass().getSimpleName();
    private static Timer timer;
    MarketSummaries marketSummaries, bmarketSummaries = null;
    Wallet wallet = null;
    private BittrexServices bittrexServices ;
    private BinanceServices binanceServices;
    Disposable disposable;
    private static CountDownTimer countDownTimer;
    private boolean countDownTimerRunning =false;
    private TextView txtLevel, txtBtc, txtUsd, txtEmpty, txtProfit;
    private RecyclerView rvCoinList;
    private WalletAdapter coinAdapter;
    private Spinner spCurrency;
    private TextView txtCoinSearch;
    private ImageView ivRefresh, imgAccount;
    private List<Result> resultList;
    private double BTCSum = 0;
    private NestedScrollView baseView;
    private TextView tvHolding, tvPrice, txtPrice;
    private String spinnerValue;
    public static String EXCHANGE_VALUE="EXCHANGE_VALUE";
    boolean isComplete = true;

    public WalletFragment() {

    }

    public static WalletFragment newInstance(/*String param1*/) {
        WalletFragment fragment = new WalletFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);
        new HideKeyboard(getContext()).setupUI(view);
        setUpView(view);
        txtProfit.setText("Total");
        ViewFontHelper.setupTextViews(getActivity(), baseView);

        spinnerValue = getResources().getStringArray(R.array.exchange_value_array_wallet)[0];
        getData();
        setTitle();

        binanceServices=new BinanceServices();
        bittrexServices=new BittrexServices();

        defaultSpinnerValue();
        spinnerValue();

        return view;
    }

    private void defaultSpinnerValue() {

        CoinApplication application = (CoinApplication) getActivity().getApplicationContext();
        spinnerValue = application.getNotification().getDefaultExchange();
        if (spinnerValue != null) {

            if (spinnerValue.equalsIgnoreCase(getResources().getStringArray(R.array.exchange_value_array_wallet)[0])) {

                spCurrency.setSelection(0);
            } else {
                spCurrency.setSelection(1);

            }
        }
    }


    private void spinnerValue() {
        spCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (spCurrency.getItemAtPosition(position).toString().equalsIgnoreCase(getResources().getStringArray(R.array.exchange_value_array_wallet)[0])) {
                    spinnerValue = getResources().getStringArray(R.array.exchange_value_array_wallet)[0];


                }
                if (spCurrency.getItemAtPosition(position).toString().equalsIgnoreCase(getResources().getStringArray(R.array.exchange_value_array_wallet)[1])) {
                    spinnerValue = getResources().getStringArray(R.array.exchange_value_array_wallet)[1];

                }
                if (spCurrency.getItemAtPosition(position).toString().equalsIgnoreCase(getResources().getStringArray(R.array.exchange_value_array_wallet)[2])) {
                    spinnerValue = getResources().getStringArray(R.array.exchange_value_array_wallet)[2];
                }
                closeWebsocket();
                stopTimer();
                getData();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void getData() {
        CoinApplication application = (CoinApplication) getActivity().getApplication();
        Account account = application.getReadAccount();
        if (account == null) {
            CustomDialog.showMessagePop(getContext(), getActivity().getString(R.string.noAPI), null);
            setLevel("No API");
            txtEmpty.setVisibility(View.VISIBLE);
            if (resultList != null) {
                resultList.clear();
                coinAdapter.notifyDataSetChanged();
            }
        } else {
            txtPrice.setText("" + ((CoinApplication) getActivity().getApplication()).getUsdt_btc());
            setLevel(account.getLabel());
            new GetCoinDetails().execute();
        }
    }

    void setTitle() {
        TextView txtTitle = getActivity().findViewById(R.id.txtTitle);
        txtTitle.setText(R.string.wallet);
    }

    void setUpView(View view) {
        txtBtc = view.findViewById(R.id.txtBtc);
        txtUsd = view.findViewById(R.id.txtUsd);
        txtLevel = view.findViewById(R.id.txtLevel);
        txtEmpty = view.findViewById(R.id.txtEmpty);
        txtProfit = view.findViewById(R.id.txtProfit);
        txtPrice = view.findViewById(R.id.price);

        tvHolding = view.findViewById(R.id.tvHolding);
        tvHolding.setTag(R.id.tvHolding, Boolean.TRUE);
        tvHolding.setOnClickListener(this);
        tvPrice = view.findViewById(R.id.tvPrice);
        tvPrice.setTag(R.id.tvPrice, Boolean.TRUE);
        tvPrice.setOnClickListener(this);
        baseView = view.findViewById(R.id.baseView);

        spCurrency = view.findViewById(R.id.spCurrency);
        ivRefresh = view.findViewById(R.id.imgSync);
        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });

        imgAccount = view.findViewById(R.id.imgAccSetting);
        imgAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).getPresenter().replaceAccountFragment();
            }
        });

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.exchange_value_array_wallet)); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spCurrency.setAdapter(spinnerArrayAdapter);
        txtCoinSearch = view.findViewById(R.id.txtCoinSearch);
        txtCoinSearch.setTag(R.id.txtCoinSearch, Boolean.TRUE);
        txtCoinSearch.setOnClickListener(this);
        rvCoinList = view.findViewById(R.id.rvCoinList);
        rvCoinList.setNestedScrollingEnabled(false);
        coinAdapter = new WalletAdapter(new ArrayList<Result>(), getActivity(), this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvCoinList.setLayoutManager(mLayoutManager);
        rvCoinList.setItemAnimator(new DefaultItemAnimator());
        rvCoinList.setAdapter(coinAdapter);
    }

    @Override
    public void onCoinClickListener(Result result,String exchange) {

        if(result.getAdditionalProperties().get(EXCHANGE_VALUE)!=null){

            String localExchagevalue=result.getAdditionalProperties().get(EXCHANGE_VALUE).toString();
            Log.d(TAG, "onCoinClickListener: "+result.getAdditionalProperties().get(EXCHANGE_VALUE));



            String coinName = result.getCurrency();

            if (localExchagevalue.equalsIgnoreCase(GlobalConstant.Exchanges.BITTREX)) {


                if (coinName.equalsIgnoreCase("usdt"))
                    return;
                if (coinName.equalsIgnoreCase("btc"))
                    coinName = "usdt-" + coinName;
                else
                    coinName = "btc-" + coinName;
            }
            if (localExchagevalue.equalsIgnoreCase(GlobalConstant.Exchanges.BINANCE)) {
                if (coinName.equalsIgnoreCase("usdt"))
                    return;
                if (coinName.equalsIgnoreCase("btc"))
                    coinName = coinName + "usdt";
                else
                    coinName = coinName + "btc";
            }

            Intent intent = new Intent(getContext(), CoinHomeActivity.class);
            intent.putExtra("COIN_NAME", coinName.toUpperCase());
            intent.putExtra(EXCHANGE_VALUE, localExchagevalue);
            startActivity(intent);




        }



    }

    @Override
    public void onClick(View v) {
        List<Result> results = coinAdapter.getResultList();//new ArrayList<>();
//        results.addAll(resultList);


        switch (v.getId()) {
            case R.id.tvPrice:
                Collections.sort(results, new Result.PriceComparator((Boolean) v.getTag(v.getId())));
                break;
            case R.id.tvHolding:
                Collections.sort(results, new Result.HoldingComparator((Boolean) v.getTag(v.getId())));
                break;
            case R.id.txtCoinSearch:
                Collections.sort(results, new Result.CoinComparator((Boolean) v.getTag(v.getId())));
                break;

        }
//        coinAdapter.addResultList(results);
//        coinAdapter.setResultList();
        v.setTag(v.getId(), !(Boolean) v.getTag(v.getId()));

        coinAdapter.notifyDataSetChanged();
    }

    public void setLevel(String level) {
        txtLevel.setText(level);
    }

    private class GetCoinDetails extends AsyncTask<String, Void, Wallet> {


        @Override
        protected Wallet doInBackground(String... params) {

            try {
                CoinApplication application = (CoinApplication) getActivity().getApplication();

                Account account;
                if (spinnerValue.equalsIgnoreCase(getResources().getStringArray(R.array.exchange_value_array_wallet)[0])) {
                    account = application.getReadAccount(spinnerValue);
                    wallet = bittrexServices.getWallet(account);
                }
                if (spinnerValue.equalsIgnoreCase(getResources().getStringArray(R.array.exchange_value_array_wallet)[1])) {
                    account = application.getReadAccount(spinnerValue);
                    wallet = binanceServices.getWallet(account);
                }
                if (spinnerValue.equalsIgnoreCase(getResources().getStringArray(R.array.exchange_value_array_wallet)[2])) {
                    Wallet walletbinnace = null;
                    account = application.getReadAccount(GlobalConstant.Exchanges.BITTREX);
                    Account account2 = application.getReadAccount(GlobalConstant.Exchanges.BINANCE);

                    wallet = bittrexServices.getWallet(account);
                    walletbinnace = binanceServices.getWallet(account2);

                    HashMap<String, Result> coinsMapbitrix = wallet.getCoinsMap();
                    HashMap<String, Result> coinsMapBinance = walletbinnace.getCoinsMap();


                    /* check coin is same then add balance */
                    for (crypto.soft.cryptongy.feature.shared.json.wallet.Result r : walletbinnace.getResult()) {
                        if (r.getBalance() != 0){
                            Result bresult = coinsMapbitrix.get(r.getCurrency());
                            if(bresult !=null){
                                bresult.setBalance(bresult.getBalance()+r.getBalance());
                            }
                            else {
                                coinsMapbitrix.put(r.getCurrency(), r);
                            }
                        }
                    }
                }

                if (wallet != null && wallet.getSuccess() && wallet.getResult() != null) {
                    // List<Result> walletResults = wallet.getResult();

                    List<Result> filteredWalletResults = new ArrayList<Result>(wallet.getCoinsMap().values());


                    if (spinnerValue.equalsIgnoreCase(getResources().getStringArray(R.array.exchange_value_array_wallet)[0])) {
                        marketSummaries = bittrexServices.getMarketSummaries();
                    }
                    if (spinnerValue.equalsIgnoreCase(getResources().getStringArray(R.array.exchange_value_array_wallet)[1])) {

                         marketSummaries = binanceServices.getMarketSummaries();
                    }
                    if (spinnerValue.equalsIgnoreCase(getResources().getStringArray(R.array.exchange_value_array_wallet)[2])) {
                        marketSummaries = bittrexServices.getMarketSummaries();
                        MarketSummaries marketSummaries2 = binanceServices.getMarketSummaries();


                        HashMap<String, crypto.soft.cryptongy.feature.shared.json.market.Result> coinsMap = marketSummaries.getCoinsMap();
                        for (crypto.soft.cryptongy.feature.shared.json.market.Result r : marketSummaries2.getResult()) {

                            if(coinsMap.get(r.getMarketName()) == null)
                                coinsMap.put(r.getMarketName(), r);
                        }
                        marketSummaries.setCoinsMap(coinsMap);

                    }


                    if (marketSummaries != null && marketSummaries.getSuccess()) {

                        fillCoinPrice(filteredWalletResults, marketSummaries);
                        wallet.setResult(filteredWalletResults);
                        return wallet;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return wallet;
        }

        private void getFilteredCoinList(List<Result> walletResults, List<Result> filteredWalletResults) {
            for (Result walletResult : walletResults) {
                if (walletResult.getBalance() != 0)
                    filteredWalletResults.add(walletResult);
            }
        }



        @Override
        protected void onPostExecute(Wallet wallet) {
            AlertUtility.dismissDialog();

         postExecute(wallet);

            startWalletTimer();
        }


        @Override
        protected void onPreExecute() {
            AlertUtility.showLoadingDialog(getActivity());
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }


    }


    private void fillCoinPrice(List<Result> walletResults, MarketSummaries marketSummariesp) {
        BTCSum = 0;
        List<crypto.soft.cryptongy.feature.shared.json.market.Result> marketResults = marketSummariesp.getResult();
        for (Result walletResult : walletResults) {
            String coinName = walletResult.getCurrency();

            if (spinnerValue.equalsIgnoreCase(getResources().getStringArray(R.array.exchange_value_array_wallet)[0])) {

                if (walletResult.getCurrency().equals("USDT")) {
                    walletResult.setPrice(1.0);
                    crypto.soft.cryptongy.feature.shared.json.market.Result btcsummary  = marketSummariesp.getCoinsMap().get("USDT-BTC");
                    double bitcoinPrice = btcsummary != null ? btcsummary.getLast() : 1;
                    double balance = walletResult.getBalance();
                    BTCSum += (balance / bitcoinPrice);
                } else if (walletResult.getCurrency().equals("BTC")) {
                    walletResult.setPrice(1.0);
                    double balance = walletResult.getBalance();
                    BTCSum += balance;
                } else {
                    coinName = "BTC-" + coinName;
                    crypto.soft.cryptongy.feature.shared.json.market.Result marketSummary = marketSummariesp.getCoinsMap().get(coinName);
                    walletResult.setPrice(marketSummary != null ? marketSummary.getLast() : 0);

                    double balance = walletResult.getBalance();

                    double coinbitcoinPrice = walletResult.getPrice();

                    double totalBTC = (balance * coinbitcoinPrice);

                    BTCSum += totalBTC;

                }


            }


            if (spinnerValue.equalsIgnoreCase(getResources().getStringArray(R.array.exchange_value_array_wallet)[1])) {

                if (walletResult.getCurrency().equals("USDT")) {
                    walletResult.setPrice(1.0);
                    crypto.soft.cryptongy.feature.shared.json.market.Result btcsummary  = marketSummariesp.getCoinsMap().get("BTCUSDT");
                    double bitcoinPrice = btcsummary != null ? btcsummary.getLast() : 1;
                    double balance = walletResult.getBalance();
                    BTCSum += (balance / bitcoinPrice);
                } else if (walletResult.getCurrency().equals("BTC")) {
                    walletResult.setPrice(1.0);
                    double balance = walletResult.getBalance();
                    BTCSum += balance;
                } else {
                    coinName = coinName + "BTC";
                    crypto.soft.cryptongy.feature.shared.json.market.Result marketSummary = marketSummariesp.getCoinsMap().get(coinName);
                    //TODO null changes to zero
                    walletResult.setPrice(marketSummary != null ? marketSummary.getLast() : 0);

                    double balance = walletResult.getBalance();

                    double coinbitcoinPrice = walletResult.getPrice();

                    double totalBTC = (balance * coinbitcoinPrice);

                    BTCSum += totalBTC;

                }

            }


            if (spinnerValue.equalsIgnoreCase(getResources().getStringArray(R.array.exchange_value_array_wallet)[2])) {


                if (walletResult.getCurrency().equals("USDT")) {
                    walletResult.setPrice(1.0);
                    crypto.soft.cryptongy.feature.shared.json.market.Result btcsummary  = marketSummariesp.getCoinsMap().get("USDT-BTC");
                    double bitcoinPrice = btcsummary != null ? btcsummary.getLast() : 1;
                    double balance = walletResult.getBalance();
                    BTCSum += (balance / bitcoinPrice);
                } else if (walletResult.getCurrency().equals("BTC")) {
                    walletResult.setPrice(1.0);
                    double balance = walletResult.getBalance();
                    BTCSum += balance;
                } else {

                    String coinName1 = "BTC-" + coinName;
                    String coinName2 = coinName + "BTC";
                    crypto.soft.cryptongy.feature.shared.json.market.Result marketSummary = marketSummariesp.getCoinsMap().get(coinName1);
                    crypto.soft.cryptongy.feature.shared.json.market.Result marketSummary2 = marketSummariesp.getCoinsMap().get(coinName2);
                    double bitrixPrice = (marketSummary != null ? marketSummary.getLast() : 0);
                    double binaceprice = (marketSummary2 != null ? marketSummary2.getLast() : 0);
//                        Log.d(TAG, " market summary " + coinName1  + " " + coinName2 + " " + bitrixPrice + " " + binaceprice);

                    walletResult.setPrice(bitrixPrice != 0 ? bitrixPrice : binaceprice);

                    double balance = walletResult.getBalance();

                    double coinbitcoinPrice = walletResult.getPrice();

                    double totalBTC = (balance * coinbitcoinPrice);

                    BTCSum += totalBTC;

                }
            }

        }
    }
    public void startWalletTimer() {
        Notification notification = ((CoinApplication) getContext().getApplicationContext()).getNotification();
        if (notification.isAutomSync()) {
            Log.d(TAG, "startTicker: ");
            final int timerInterval = notification.getSyncInterval() * 1000;
            timer = new Timer();

            if(!countDownTimerRunning){

                countDownTimer = new CountDownTimer(timerInterval, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        countDownTimerRunning=true;
                    }

                    @Override
                    public void onFinish() {
                        try {
                            countDownTimerRunning=false;
                            Log.d(TAG, "onFinish: timeriscalled : "+timerInterval/1000);

                            new GetUpdatedCoinDetails().execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }.start();
            }

//
//            timer.scheduleAtFixedRate(new TimerTask() {
//
//                @Override
//                public void run() {
//                    r
//
//                    new GetCoinDetails().execute();
//                }
//            }, timerInterval, timerInterval);

        }
    }

    public void stopTimer() {
        Log.d(TAG, "stopTimer: ");
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimerRunning=false;
        }

        if (timer != null)
            timer.cancel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopTimer();
        closeWebsocket();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTimer();
        closeWebsocket();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private class GetUpdatedCoinDetails extends AsyncTask<String, Void, Wallet> {

        @Override
        protected Wallet doInBackground(String... strings) {
            if(isComplete) {
                isComplete = false;
                try {
                    if (wallet != null && wallet.getSuccess() && wallet.getResult() != null) {
                        // List<Result> walletResults = wallet.getResult();

                        List<Result> filteredWalletResults = new ArrayList<Result>(wallet.getCoinsMap().values());


                        if (spinnerValue.equalsIgnoreCase(getResources().getStringArray(R.array.exchange_value_array_wallet)[0])) {
                            marketSummaries = bittrexServices.getMarketSummaries();
                        }
                        if (spinnerValue.equalsIgnoreCase(getResources().getStringArray(R.array.exchange_value_array_wallet)[1])) {
                            //                        marketSummaries = binanceServices.getMarketSummaries();
                            binanceServices.getMarketSummariesWebsocket();

                            disposable = binanceServices.sourceMarketSummariesWebsocket.observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<MarketSummaries>() {

                                @Override
                                public void accept(MarketSummaries consumerMarketSummarries) throws Exception {
                                    if (disposable != null) {
                                        disposable.dispose();
                                    }
                                    binanceServices.closeWebSocket();
                                    marketSummaries = consumerMarketSummarries;


                                }
                            });


                        }
                        if (spinnerValue.equalsIgnoreCase(getResources().getStringArray(R.array.exchange_value_array_wallet)[2])) {

                            binanceServices.getMarketSummariesWebsocket();
                            disposable = binanceServices.sourceMarketSummariesWebsocket.observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<MarketSummaries>() {

                                @Override
                                public void accept(MarketSummaries consumerMarketSummarries) throws Exception {
                                    disposable.dispose();
                                    bmarketSummaries = consumerMarketSummarries;
                                    binanceServices.closeWebSocket();

                                }
                            });


                            if (bmarketSummaries != null) {
                                MarketSummaries marketSummaries2 = bittrexServices.getMarketSummaries();
                                HashMap<String, crypto.soft.cryptongy.feature.shared.json.market.Result> coinsMap = bmarketSummaries.getCoinsMap();
                                for (crypto.soft.cryptongy.feature.shared.json.market.Result r : marketSummaries2.getResult()) {

                                    if (coinsMap.get(r.getMarketName()) == null)
                                        coinsMap.put(r.getMarketName(), r);
                                }
                                marketSummaries.setCoinsMap(coinsMap);

                            }
                        }

                        isComplete = true;
                        if (marketSummaries != null && marketSummaries.getSuccess()) {

                            fillCoinPrice(filteredWalletResults, marketSummaries);
                            wallet.setResult(filteredWalletResults);

                            return wallet;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return wallet;
        }

        @Override
        protected void onPostExecute(Wallet wallet) {
           postExecute(wallet);
           startWalletTimer();

        }
    }




    public void postExecute(Wallet wallet){

        txtEmpty.setVisibility(View.GONE);
        if (!wallet.getSuccess() || wallet.getResult() == null) {
            String msg = wallet.getMessage() != null ? wallet.getMessage() : "Connection Error";
            Toast.makeText(getActivity(), wallet.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        resultList = new ArrayList<>();
        resultList.addAll(wallet.getResult());
        Collections.sort(wallet.getResult(), new Result.HoldingComparator(false));
        coinAdapter.setResultList(wallet.getResult());
        coinAdapter.notifyDataSetChanged();

        txtBtc.setText(String.valueOf(GlobalUtil.round(BTCSum, 9)) + "฿");
        double bitcoinPrice = ((CoinApplication) getActivity().getApplication()).getUsdt_btc();
        txtUsd.setText("$" + String.valueOf(GlobalUtil.round(BTCSum * bitcoinPrice, 4)));




    }

    public void closeWebsocket(){
        if(disposable!=null){
            disposable.dispose();
        }
        binanceServices.closeWebSocket();
    }
}