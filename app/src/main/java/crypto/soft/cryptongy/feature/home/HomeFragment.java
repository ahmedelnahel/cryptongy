package crypto.soft.cryptongy.feature.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.account.CustomDialog;
import crypto.soft.cryptongy.feature.coinHome.CoinHomeActivity;
import crypto.soft.cryptongy.feature.main.MainActivity;
import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.json.market.Result;
import crypto.soft.cryptongy.feature.shared.listner.AdapterItemClickListener;
import crypto.soft.cryptongy.utils.CoinApplication;
import crypto.soft.cryptongy.utils.GlobalUtil;
import crypto.soft.cryptongy.utils.HideKeyboard;
import crypto.soft.cryptongy.utils.SharedPreference;

/**
 * Created by tseringwongelgurung on 11/27/17.
 */

public class HomeFragment extends MvpFragment<HomeView, HomePresenter> implements HomeView, AdapterItemClickListener, TextWatcher {
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
    @BindView(R.id.img_search)
    ImageView imgSearch;
    @BindView(R.id.list_currency)
    RecyclerView listCurrency;
    @BindView(R.id.imgAdd)
    ImageView imgAdd;
    @BindView(R.id.imgRefresh)
    ImageView imgRefresh;
    @BindView(R.id.imgDelete)
    ImageView imgDelete;
    @BindView(R.id.imgKey)
    ImageView imgKey;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.inputCoin)
    AutoCompleteTextView inputCoin;
    CurrencyAdapter currencyAdapter;
    List<Result> mock = new ArrayList<>();
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.search)
    EditText search;
    CustomArrayAdapter adapterCoins;
    List<Result> coins;
    @BindView(R.id.img_pr)
    ImageView imgPr;
    @BindView(R.id.img_vol)
    ImageView imgVol;
    @BindView(R.id.volume)
    RelativeLayout volume;
    @BindView(R.id.relPr)
    RelativeLayout relPrice;
    @BindView(R.id.rltSearch)
    RelativeLayout rltSearch;
    boolean isVolumesorted = false;
    boolean isPricesorted = false;
    Result result;
    private View view;

    private boolean isFirst = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_watch_list, container, false);
            new HideKeyboard(getContext()).setupUI(view);
            ButterKnife.bind(this, view);
            coins = new ArrayList<>();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.coin_array, R.layout.drop_down_text);
            adapter.setDropDownViewResource(R.layout.drop_down_text);
            spinner.setAdapter(adapter);
            search.addTextChangedListener(this);
            adapterCoins = new CustomArrayAdapter(getContext(), coins);
            inputCoin.setThreshold(1);
            inputCoin.setAdapter(adapterCoins);

            inputCoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inputCoin.setText("");
                }
            });

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
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isFirst) {
            isFirst = false;
            getPresenter().loadSummaries();
        }
    }

    @Override
    public HomePresenter createPresenter() {
        return new HomePresenter(getContext());
    }

    public void setTitle() {
        TextView txtTitle = getActivity().findViewById(R.id.txtTitle);
        txtTitle.setText(R.string.home);
    }

    @Override
    public void initRecycler() {
        listCurrency.setLayoutManager(new LinearLayoutManager(getContext()));
        listCurrency.setHasFixedSize(true);
        listCurrency.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void setAdapter(List<Result> results) {
        results=restoreData(results);
        mock.clear();
        mock.addAll(results);
        if (currencyAdapter == null) {
            currencyAdapter = new CurrencyAdapter(mock);
            listCurrency.setAdapter(currencyAdapter);
            currencyAdapter.setAdapterItemClickListener(this);
        } else {
            String txtSearch = search.getText().toString();
            if (!TextUtils.isEmpty(txtSearch))
                currencyAdapter.getFilter().filter(txtSearch);
        }
    }

    private List<Result> restoreData(List<Result> results){
        for (Result result:mock){
            for (Result result1:results){
                if (result.getMarketName().equalsIgnoreCase(result1.getMarketName())){
                    result1.setSelected(result.isSelected());
                    break;
                }
            }
        }
        return results;
    }

    @Override
    public void onSummaryDataLoad(MarketSummaries marketSummaries) {
        if (marketSummaries.getSuccess()) {
            ((CoinApplication) getActivity().getApplication()).setUsdt_btc(GlobalUtil.round(marketSummaries.getCoinsMap().get("USDT-BTC").getLast(), 4));
            ((CoinApplication) getActivity().getApplication()).setbtc_eth(marketSummaries.getCoinsMap().get("BTC-ETH").getLast());
            price.setText("" + ((CoinApplication) getActivity().getApplication()).getUsdt_btc());
            coins.addAll(marketSummaries.getResult());
            adapterCoins.notifyDataSetChanged();
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
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.imgAdd, R.id.imgRefresh, R.id.imgDelete, R.id.imgKey})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgAdd:
                if (mock == null)
                    return;
                if (result != null) {
                    for (Result data : mock) {
                        if (data.getMarketName().equalsIgnoreCase(result.getMarketName())) {
                            CustomDialog.showMessagePop(getContext(), result.getMarketName() + " has been already added.", null);
                            return;
                        }
                    }
                    mock.add(result);
                    result = null;
                } else {
                    inputCoin.requestFocus();
                    CustomDialog.showMessagePop(getContext(), "Please select a coin first.", null);
                }
                inputCoin.setText("");
                SharedPreference.saveToPrefs(getContext(), "mockValue", new Gson().toJson(mock));
                currencyAdapter.notifyDataSetChanged();
                break;
            case R.id.imgRefresh:
                if (coins != null)
                    coins.clear();
                if (adapterCoins != null)
                    adapterCoins.notifyDataSetChanged();
                presenter.loadSummaries();
                break;
            case R.id.imgDelete:
                if (mock == null)
                    return;
                Iterator iterator=mock.iterator();
                while (iterator.hasNext()){
                    Result result= (Result) iterator.next();
                    if (result.isSelected())
                        iterator.remove();
                    currencyAdapter.notifyDataSetChanged();
                    SharedPreference.saveToPrefs(getContext(), "mockValue", new Gson().toJson(mock));
                }
                break;
            case R.id.imgKey:
                ((MainActivity) getActivity()).getPresenter().replaceAccountFragment();
                break;
        }
    }

    @Override
    public void onItemClicked(Result menuItem, int position) {
        Intent intent = new Intent(getContext(), CoinHomeActivity.class);
        intent.putExtra("COIN_NAME", menuItem.getMarketName());
        startActivity(intent);
    }

    @Override
    public void onItemLongClicked(Result menuItem, int position) {
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (currencyAdapter != null)
            currencyAdapter.getFilter().filter(charSequence);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @OnClick({R.id.relPr, R.id.volume})
    public void onSortView(View view) {
        switch (view.getId()) {
            case R.id.relPr:
                if (!isPricesorted) {
                    isPricesorted = true;
                    imgPr.setImageResource(R.drawable.ic_down);
                    Collections.sort(mock, new Comparator<Result>() {
                        @Override
                        public int compare(Result c1, Result c2) {
                            return Double.compare(c1.getLast(), c2.getLast());
                        }
                    });
                } else {
                    isPricesorted = false;
                    imgPr.setImageResource(R.drawable.ic_up);
                    Collections.sort(mock, new Comparator<Result>() {
                        @Override
                        public int compare(Result c1, Result c2) {
                            return Double.compare(c2.getLast(), c1.getLast());
                        }
                    });
                }
                currencyAdapter.notifyDataSetChanged();
                break;
            case R.id.volume:
                if (!isVolumesorted) {
                    isVolumesorted = true;
                    imgVol.setImageResource(R.drawable.ic_down);
                    Collections.sort(mock, new Comparator<Result>() {
                        @Override
                        public int compare(Result c1, Result c2) {
                            return Double.compare(c1.getVolume(), c2.getVolume());
                        }
                    });
                } else {
                    isVolumesorted = false;
                    imgVol.setImageResource(R.drawable.ic_up);
                    Collections.sort(mock, new Comparator<Result>() {
                        @Override
                        public int compare(Result c1, Result c2) {
                            return Double.compare(c2.getVolume(), c1.getVolume());
                        }
                    });
                }
                currencyAdapter.notifyDataSetChanged();
                break;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        presenter.startTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.stopTimer();
    }
}
