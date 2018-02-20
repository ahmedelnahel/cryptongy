package crypto.soft.cryptongy.feature.arbitage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.account.CustomDialog;
import crypto.soft.cryptongy.feature.coinHome.CoinHomeActivity;
import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.json.market.Result;
import crypto.soft.cryptongy.feature.shared.listner.AdapterItemClickListener;
import crypto.soft.cryptongy.utils.CoinApplication;
import crypto.soft.cryptongy.utils.GlobalConstant;
import crypto.soft.cryptongy.utils.GlobalUtil;
import crypto.soft.cryptongy.utils.HideKeyboard;
import crypto.soft.cryptongy.utils.SharedPreference;

import static crypto.soft.cryptongy.utils.SharedPreference.WATCHLIST_BINANCE;
import static crypto.soft.cryptongy.utils.SharedPreference.WATCHLIST_BITTREX;

/**
 * Created by tseringwongelgurung on 11/27/17.
 */

public class ArbitageFragment extends MvpFragment<ArbitageView, ArbitagePresenter> implements ArbitageView, AdapterItemClickListener, TextWatcher {
    //    @BindView(R.id.txtLevel)
//    TextView txtLevel;
    @BindView(R.id.type)
    ImageView type;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.center)
    RelativeLayout center;
    @BindView(R.id.toolBar)
    RelativeLayout toolBar;
    @BindView(R.id.icon_search)
    ImageView iconSearch;


    @BindView(R.id.imgRefresh)
    ImageView imgRefresh;

    @BindView(R.id.spinner1)
    Spinner spinner1;
    @BindView(R.id.spinner2)
    Spinner spinner2;
    @BindView(R.id.tblArbitage)
    TableLayout tblArbitage;


    CurrencyAdapter currencyAdapter;
    List<Result> mock = new ArrayList<>();
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.search)
    EditText search;
    CustomArrayAdapter adapterCoins;
    List<Result> coins;
//    @BindView(R.id.img_pr)
//    ImageView imgPr;
//    @BindView(R.id.img_vol)
//    ImageView imgVol;
//    @BindView(R.id.volume)
////    RelativeLayout volume;
//    @BindView(R.id.relPr)
//    RelativeLayout relPrice;
    @BindView(R.id.rltSearch)
    RelativeLayout rltSearch;
    boolean isVolumesorted = false;
    boolean isPricesorted = false;
    Result result;
    private View view;

    private boolean isFirst = false;
    private static String TAG;
    private static String spinnerValue1;
    private static String spinnerValue2;
    public static String EXCHANGE_VALUE = "EXCHANGE_VALUE";

    private boolean isCoinAscend = true;
    private boolean isPrice1Ascend = true;
    private boolean isPrice2Ascend = true;
    private boolean isPercentageAscend = true;
    private List<AribitaryTableResult> aribitaryTableResultList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TAG = getActivity().getClass().getSimpleName();
        if (view == null) {
            view = inflater.inflate(R.layout.arbitage_layout, container, false);
            new HideKeyboard(getContext()).setupUI(view);
            ButterKnife.bind(this, view);
            coins = new ArrayList<>();

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.coin_array, R.layout.drop_down_text_aribitary);
            adapter.setDropDownViewResource(R.layout.drop_down_text);
            spinner1.setAdapter(adapter);
            spinner2.setAdapter(adapter);

            mock=new ArrayList<>();
            aribitaryTableResultList=new ArrayList<>();


            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    presenter.filter(s.toString(),aribitaryTableResultList);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            adapterCoins = new CustomArrayAdapter(getContext(), coins);

            CoinApplication application = (CoinApplication) getActivity().getApplicationContext();

            spinnerValue1 = GlobalConstant.Exchanges.BITTREX;
            spinnerValue2 = GlobalConstant.Exchanges.BINANCE;
            spinner1.setSelection(0);
            spinner2.setSelection(1);

            price.setText("" + ((CoinApplication) getActivity().getApplication()).getUsdt_btc());
            initRecycler();

            rltSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    search.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(search, InputMethodManager.SHOW_IMPLICIT);
                }
            });
            isFirst = true;
        }
        setTitle();


        spinerListener();
        return view;
    }

    private void spinerListener() {


        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //if at position zero bitrex and at position 1 binance is called
                if (spinner1.getItemAtPosition(position).toString().equalsIgnoreCase(getResources().getStringArray(R.array.coin_array)[0])) {
                    spinnerValue1 = getResources().getStringArray(R.array.coin_array)[0];
                } else {
                    spinnerValue1 = getResources().getStringArray(R.array.coin_array)[1];

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //if at position zero bitrex and at position 1 binance is called
                if (spinner2.getItemAtPosition(position).toString().equalsIgnoreCase(getResources().getStringArray(R.array.coin_array)[0])) {
                    spinnerValue2 = getResources().getStringArray(R.array.coin_array)[0];
                } else {
                    spinnerValue2 = getResources().getStringArray(R.array.coin_array)[1];

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if (spinnerValue1.equalsIgnoreCase(spinnerValue2)) {

        } else {

        }


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isFirst) {
            isFirst = false;
         //   presenter.closeWebSocket();
          // getPresenter().loadSummaries();
           getPresenter().getArbitageTableResult();

//            getPresenter().loadBinanceSummaries();



        }
    }

    @Override
    public ArbitagePresenter createPresenter() {
        return new ArbitagePresenter(getContext());
    }

    public void setTitle() {
        TextView txtTitle = getActivity().findViewById(R.id.txtTitle);
        txtTitle.setText(" ");
    }

    @Override
    public void initRecycler() {
//        listCurrency.setLayoutManager(new LinearLayoutManager(getContext()));
//        listCurrency.setHasFixedSize(true);
//        listCurrency.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void setAdapter(List<Result> results) {
        results = restoreData(results);
        mock.clear();
        mock.addAll(results);
        if (currencyAdapter == null) {
            currencyAdapter = new CurrencyAdapter(mock);
//            listCurrency.setAdapter(currencyAdapter);
           // currencyAdapter.setAdapterItemClickListener(this);
        } else {
            String txtSearch = search.getText().toString();
//            if (!TextUtils.isEmpty(txtSearch))
              //  presenter.filter(txtSearch,results);
              //  currencyAdapter.getFilter().filter(txtSearch);
        }
    }

    private List<Result> restoreData(List<Result> results) {
        for (Result result : mock) {
            for (Result result1 : results) {
                if (result != null && result.getMarketName().equalsIgnoreCase(result1.getMarketName())) {
                    result1.setSelected(result.isSelected());
                    break;
                }
            }
        }
        return results;
    }

    @Override
    public void onSummaryDataLoad(MarketSummaries marketSummaries, String exchangeValue) {

        if (marketSummaries != null ) {
            if (marketSummaries.getSuccess()) {
                mock.clear();
                mock.addAll(marketSummaries.getResult());

                List<Result> resultList=new ArrayList<>(marketSummaries.getResult().subList(0,10));


                if (spinnerValue1.equalsIgnoreCase(getResources().getStringArray(R.array.coin_array)[0])) {//Bitrix value comparing
                    if (marketSummaries.getCoinsMap().get("USDT-BTC") != null) {

                        ((CoinApplication) getActivity().getApplication()).setUsdt_btc(GlobalUtil.round(marketSummaries.getCoinsMap().get("USDT-BTC").getLast(), 4));
                        price.setText("" + ((CoinApplication) getActivity().getApplication()).getUsdt_btc());
                    }

                    if (marketSummaries.getCoinsMap().get("BTC-ETH") != null) {
                        ((CoinApplication) getActivity().getApplication()).setbtc_eth(marketSummaries.getCoinsMap().get("BTC-ETH").getLast());
                    }

                }
                if (spinnerValue1.equalsIgnoreCase(getResources().getStringArray(R.array.coin_array)[1])) {//Binance value comparing

                    if (marketSummaries.getCoinsMap().get("BTCUSDT") != null) {
                        ((CoinApplication) getActivity().getApplication()).setUsdt_btc(GlobalUtil.round(marketSummaries.getCoinsMap().get("BTCUSDT").getLast(), 4));
                        price.setText("" + ((CoinApplication) getActivity().getApplication()).getUsdt_btc());
                    }


                    if (marketSummaries.getCoinsMap().get("ETHBTC") != null) {
                        ((CoinApplication) getActivity().getApplication()).setbtc_eth(marketSummaries.getCoinsMap().get("ETHBTC").getLast());
                    }

                }

                coins.clear();
                coins.addAll(marketSummaries.getResult());
                adapterCoins.notifyDataSetChanged();
            } else {
                coins.clear();
                adapterCoins.notifyDataSetChanged();
            }

        }
        hideProgressBar();

    }

    @Override
    public void onSummaryLoadFailed() {
        CustomDialog.showMessagePop(getContext(), "Connection Error.", null);
        Log.v("Here", "" + "failed");
    }

    @Override
    public void setLevel(String s) {
//        txtLevel.setText(s);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    @OnClick({ R.id.imgRefresh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//
            case R.id.imgRefresh:
                if (spinnerValue1.equalsIgnoreCase(getResources().getStringArray(R.array.coin_array)[0])) {
                    presenter.getArbitageTableResult();
                break;
//
        }
        }
    }

    private void saveMockToSharedPrefrence(List<Result> mock) {
        if (spinnerValue1.equalsIgnoreCase(getResources().getStringArray(R.array.coin_array)[0])) {//bitrix check
            SharedPreference.saveToPrefs(getContext(), WATCHLIST_BITTREX, new Gson().toJson(mock));
        } else {
            SharedPreference.saveToPrefs(getContext(), WATCHLIST_BINANCE, new Gson().toJson(mock));
        }
    }

    @Override
    public void onItemClicked(Result menuItem, int position) {
        presenter.closeWebSocket();
        Intent intent = new Intent(getContext(), CoinHomeActivity.class);
        intent.putExtra("COIN_NAME", menuItem.getMarketName());
        intent.putExtra(EXCHANGE_VALUE, spinnerValue1);
        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        presenter.closeWebSocket();
//        if (spinnerValue1.equalsIgnoreCase(GlobalConstant.Exchanges.BINANCE)) {
//            presenter.loadBinanceSummaries();
//        }
//        if (spinnerValue1.equalsIgnoreCase(GlobalConstant.Exchanges.BITTREX)) {
//            presenter.loadSummaries();
//        }
    }

    @Override
    public void onItemLongClicked(Result menuItem, int position) {
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //if (currencyAdapter != null)
            //currencyAdapter.getFilter().filter(charSequence);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
//
//    @OnClick({R.id.relPr, R.id.volume})
//    public void onSortView(View view) {
//        switch (view.getId()) {
//            case R.id.relPr:
//                if (!isPricesorted) {
//                    isPricesorted = true;
//                    imgPr.setImageResource(R.drawable.ic_down);
//                    Collections.sort(mock, new Comparator<Result>() {
//                        @Override
//                        public int compare(Result c1, Result c2) {
//                            return Double.compare(c1.getLast(), c2.getLast());
//                        }
//                    });
//                } else {
//                    isPricesorted = false;
//                    imgPr.setImageResource(R.drawable.ic_up);
//                    Collections.sort(mock, new Comparator<Result>() {
//                        @Override
//                        public int compare(Result c1, Result c2) {
//                            return Double.compare(c2.getLast(), c1.getLast());
//                        }
//                    });
//                }
//                currencyAdapter.notifyDataSetChanged();
//                break;
//            case R.id.volume:
//                if (!isVolumesorted) {
//                    isVolumesorted = true;
//                    imgVol.setImageResource(R.drawable.ic_down);
//                    Collections.sort(mock, new Comparator<Result>() {
//                        @Override
//                        public int compare(Result c1, Result c2) {
//                            return Double.compare(c1.getVolume(), c2.getVolume());
//                        }
//                    });
//                } else {
//                    isVolumesorted = false;
//                    imgVol.setImageResource(R.drawable.ic_up);
//                    Collections.sort(mock, new Comparator<Result>() {
//                        @Override
//                        public int compare(Result c1, Result c2) {
//                            return Double.compare(c2.getVolume(), c1.getVolume());
//                        }
//                    });
//                }
//                if (currencyAdapter != null)
//                    currencyAdapter.notifyDataSetChanged();
//                break;
//        }
//    }


    @Override
    public void onStart() {
        super.onStart();
       /// presenter.startTimer(spinnerValue1);
    }

    @Override
    public void onResume() {
        super.onResume();
       // presenter.startTimer(spinnerValue1);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.stopTimer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.stopTimer();

    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.stopTimer();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.closeWebSocket();

    }


    @Override
    public void setList(List<AribitaryTableResult> list){
        aribitaryTableResultList=list;
        setCoinInTable(aribitaryTableResultList);
    }

    @Override
    public void setCoinInTable(final List<AribitaryTableResult> resultList) {



        tblArbitage.removeAllViews();
//        if (marketSummaries == null || marketSummaries.getCoinsMap().size() == 0) {
//            return;
//        }
        View title = getLayoutInflater().inflate(R.layout.table_arbitage_title, null);
        TextView txtTitleCoin = title.findViewById(R.id.txtTitleCoin);
        TextView txtTitlePrice1 = title.findViewById(R.id.txtTitlePrice1);
        TextView txtTitlePrice2 = title.findViewById(R.id.txtTitlePrice2);
        TextView txtTitlePercent = title.findViewById(R.id.txtTitlePercentage);

        if (isCoinAscend)
            txtTitleCoin.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
        else
            txtTitleCoin.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);

        if (isPrice1Ascend)
            txtTitlePrice1.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
        else
            txtTitlePrice1.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);


        if (isPrice2Ascend)
            txtTitlePrice2.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
        else
            txtTitlePrice2.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);


        if (isPercentageAscend)
            txtTitlePercent.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
        else
            txtTitlePercent.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);



        txtTitleCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCoinAscend = !isCoinAscend;
                 presenter.sortList(resultList, isCoinAscend,0);
            }
        });

        txtTitlePrice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPrice1Ascend = !isPrice1Ascend;
                presenter.sortList(resultList, isPrice1Ascend,1);

            }
        });
        txtTitlePrice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPrice2Ascend = !isPrice2Ascend;
                presenter.sortList(resultList, isPrice2Ascend,1);
            }
        });
        txtTitlePercent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPercentageAscend = !isPercentageAscend;
                presenter.sortList(resultList, isPercentageAscend,1);
            }
        });

        tblArbitage.addView(title);




        if(resultList!=null && resultList.size()>0){


            int co=resultList.size();


            for (int i = 0; i < co; i++) {


                AribitaryTableResult result=resultList.get(i);

                //   final Conditional conditional = conditionals.get(i);


                View sub = getLayoutInflater().inflate(R.layout.table_arbitary_sub, null);

                ArbitageTableHolder holder = new ArbitageTableHolder(sub);

                holder.txtTitleCoin.setText(result.getCoinName());
                holder.txtTitlePrice1.setText(String.valueOf(BigDecimal.valueOf(Double.valueOf(result.getPrice1()))));
                holder.txtTitlePrice2.setText(String.valueOf(BigDecimal.valueOf(Double.valueOf(result.getPrice2()))));
                holder.txtTitlePercentage.setText(result.getPercentage()+ "$");
//
//            Double conditionPrice = conditional.getLowCondition();
//            String conditionType = conditional.getConditionType();
//            if (conditionPrice == null)
//                conditionPrice = conditional.getHighCondition();
//            if (TextUtils.isEmpty(conditionType))
//                conditionType = conditional.getConditionHighType();
//
//            if (conditionPrice != null && conditionType != null) {
//                if (conditionType.equalsIgnoreCase(GlobalConstant.Conditional.TYPE_PERCENTAGE))
//                    holder.txtRate.setText(String.format("%.0f", conditionPrice.doubleValue()) + "%");
//                else
//                    holder.txtRate.setText(String.format("%.8f", conditionPrice.doubleValue()));
//            }
//
//            holder.txtTime.setText(conditional.getOrderStatus());


                tblArbitage.addView(sub);

//            holder.txtAction.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(final View view) {
//                    CustomDialog.showConfirmation(getContext(), getString(R.string.cancle_confirm), new DialogListner() {
//                        @Override
//                        public void onOkClicked() {
//                            if (((TextView) view).getText().toString().equalsIgnoreCase("cancel"))
//                                presenter.delete(conditional.getId());
//                        }
//                    });
//                }
//            });

                if (i < co- 1) {
                    View line = getLayoutInflater().inflate(R.layout.table_line, null);
                    tblArbitage.addView(line);
                }



            }
        }


    }


}
