package crypto.soft.cryptongy.feature.arbitage;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
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

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.shared.json.market.Result;
import crypto.soft.cryptongy.feature.shared.listner.AdapterItemClickListener;
import crypto.soft.cryptongy.utils.CoinApplication;
import crypto.soft.cryptongy.utils.GlobalConstant;
import crypto.soft.cryptongy.utils.HideKeyboard;

/**
 * Created by tseringwongelgurung on 11/27/17.
 */

public class ArbitageFragment extends MvpFragment<ArbitageView, ArbitagePresenter> implements ArbitageView, AdapterItemClickListener {

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

    @BindView(R.id.crvArbitage)
    CardView cardView;
    @BindView(R.id.arbitageRecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.imgRefresh)
    ImageView imgRefresh;

    @BindView(R.id.spinner1)
    Spinner spinner1;
    @BindView(R.id.spinner2)
    Spinner spinner2;
    @BindView(R.id.tblArbitage)
    TableLayout tblArbitage;

    @BindView(R.id.spinner1Cointainer)
    RelativeLayout spinner1Cointainer;

    @BindView(R.id.spinner2Cointainer)
    RelativeLayout spinner2Cointainer;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.search)
    EditText search;
    @BindView(R.id.rltSearch)
    RelativeLayout rltSearch;

    @BindView(R.id.img_coin)
    ImageView imgCoin;
    @BindView(R.id.img_Price1)
    ImageView img_Price1;
    @BindView(R.id.img_Price2)
    ImageView img_Price2;
    @BindView(R.id.img_Percent)
    ImageView img_Percent;

    boolean isCoinSorted = true;
    boolean isPrice1Sorted = true;
    boolean isPrice2Sorted = true;
    boolean isPercentsorted = true;

    private View view;
    boolean isspinnerListner = false;

    private boolean isFirst = true;
    private static String TAG;
    private static String spinnerValue1;
    private static String spinnerValue2;

    private boolean isCoinAscend = true;
    private boolean isPrice1Ascend = true;
    private boolean isPrice2Ascend = true;
    private boolean isPercentageAscend = true;
    private List<AribitaryTableResult> aribitaryTableResultList;

    long delay = 1000;
    long last_text_edit = 0;
    String strToSearch;
    Handler handler = new Handler();

    ArbitageAdapter arbitageAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TAG = getActivity().getClass().getSimpleName();
        if (view == null) {
            view = inflater.inflate(R.layout.arbitage_layout, container, false);
            new HideKeyboard(getContext()).setupUI(view);
            ButterKnife.bind(this, view);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.coin_array, R.layout.drop_down_text_aribitary);
            adapter.setDropDownViewResource(R.layout.drop_down_text);
            spinner1.setAdapter(adapter);
            spinner2.setAdapter(adapter);
            spinerListener();
            aribitaryTableResultList = new ArrayList<>();

            initRecycler();
            setAdapter(aribitaryTableResultList);
            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {


                    handler.removeCallbacks(input_finish_checker);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    strToSearch = s.toString();
                    last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(input_finish_checker, delay);


                }
            });
            spinnerValue1 = GlobalConstant.Exchanges.BITTREX;
            spinnerValue2 = GlobalConstant.Exchanges.BINANCE;
            spinner1.setSelection(0);
            spinner2.setSelection(1);

            price.setText("" + ((CoinApplication) getActivity().getApplication()).getUsdt_btc());


            rltSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    search.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(search, InputMethodManager.SHOW_IMPLICIT);
                }
            });

        }
        setTitle();


        return view;
    }

    private Runnable input_finish_checker = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                Log.d(TAG, "run: " + strToSearch);
                if (strToSearch.length() > 0) {

                    presenter.filter(strToSearch, aribitaryTableResultList);
                } else {
                    setCoinInTable(aribitaryTableResultList);
                }
            }
        }
    };

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
                colorChangeOfSpinners();

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

                colorChangeOfSpinners();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    public void colorChangeOfSpinners() {

        Log.d(TAG, "colorChangeOfSpinners: ");
        if (spinnerValue1.equalsIgnoreCase(spinnerValue2)) {
            spinner1Cointainer.setBackground(getResources().getDrawable(R.drawable.rect_left_right_red));
            spinner2Cointainer.setBackground(getResources().getDrawable(R.drawable.rect_left_right_red));
        } else {
            Log.d(TAG, "colorChangeOfSpinners: " + isspinnerListner);

            if (isspinnerListner) {

                getArbitageTableResult();
            }
            isspinnerListner = true;

            spinner1Cointainer.setBackground(getResources().getDrawable(R.drawable.rect_left_right));
            spinner2Cointainer.setBackground(getResources().getDrawable(R.drawable.rect_left_right));
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (isFirst) {
            isFirst = false;
            Log.d(TAG, "onViewCreated: " + isFirst);

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

    @OnClick({R.id.imgRefresh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgRefresh:
                presenter.getArbitageTableResult(spinnerValue1);
                break;
        }
    }


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
        //  presenter.stopTimer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //  presenter.stopTimer();

    }

    @Override
    public void onStop() {
        super.onStop();
        //presenter.stopTimer();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // presenter.closeWebSocket();

    }


    @Override
    public void setList(List<AribitaryTableResult> list) {
        aribitaryTableResultList = list;
        setCoinInTable(aribitaryTableResultList);
    }

    public ArbitageAdapter getAdapter() {
        if (arbitageAdapter == null) {
            arbitageAdapter = new ArbitageAdapter(aribitaryTableResultList);
        }
        return arbitageAdapter;
    }

    @Override
    public void setCoinInTable(final List<AribitaryTableResult> resultList) {

        arbitageAdapter.aribitaryTableResultList.clear();
        arbitageAdapter.aribitaryTableResultList.addAll(resultList);
        cardView.setVisibility(View.VISIBLE);
        arbitageAdapter.notifyDataSetChanged();
        hideProgressBar();


        //
//        tblArbitage.removeAllViews();
//
//        View title = getLayoutInflater().inflate(R.layout.table_arbitage_title, null);
//        TextView txtTitleCoin = title.findViewById(R.id.txtTitleCoin);
//        TextView txtTitlePrice1 = title.findViewById(R.id.txtTitlePrice1);
//        TextView txtTitlePrice2 = title.findViewById(R.id.txtTitlePrice2);
//        TextView txtTitlePercent = title.findViewById(R.id.txtTitlePercentage);
//
//        if (isCoinAscend)
//            txtTitleCoin.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
//        else
//            txtTitleCoin.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
//
//        if (isPrice1Ascend)
//            txtTitlePrice1.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
//        else
//            txtTitlePrice1.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
//
//
//        if (isPrice2Ascend)
//            txtTitlePrice2.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
//        else
//            txtTitlePrice2.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
//
//
//        if (isPercentageAscend)
//            txtTitlePercent.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
//        else
//            txtTitlePercent.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_up_float, 0);
//
//
//        txtTitleCoin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                isCoinAscend = !isCoinAscend;
//                showProgressBar();
//                presenter.sortList(resultList, isCoinAscend, 0);
//            }
//        });
//
//        txtTitlePrice1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                isPrice1Ascend = !isPrice1Ascend;
//                showProgressBar();
//                presenter.sortList(resultList, isPrice1Ascend, 1);
//
//            }
//        });
//        txtTitlePrice2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                isPrice2Ascend = !isPrice2Ascend;
//                showProgressBar();
//                presenter.sortList(resultList, isPrice2Ascend, 1);
//            }
//        });
//        txtTitlePercent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                isPercentageAscend = !isPercentageAscend;
//                showProgressBar();
//                presenter.sortList(resultList, isPercentageAscend, 1);
//            }
//        });
//
//        tblArbitage.addView(title);


//
//        if (resultList != null && resultList.size() > 0) {
//
//
//            int co = resultList.size();
//
//
//            if (co > 10) {
//                co = 10;
//            }
//
//            for (int i = 0; i < co; i++) {
//                AribitaryTableResult result = resultList.get(i);
//                View sub = getLayoutInflater().inflate(R.layout.table_arbitary_sub, null);
//
//                ArbitageTableHolder holder = new ArbitageTableHolder(sub);
//
//                holder.txtTitleCoin.setText(result.getCoinName());
//                holder.txtTitlePrice1.setText(String.valueOf(BigDecimal.valueOf(Double.valueOf(result.getPrice1()))));
//                holder.txtTitlePrice2.setText(String.valueOf(BigDecimal.valueOf(Double.valueOf(result.getPrice2()))));
//                holder.txtTitlePercentage.setText(result.getPercentage() + "%");
//
//                tblArbitage.addView(sub);
//
//                if (i < co - 1) {
//                    View line = getLayoutInflater().inflate(R.layout.table_line, null);
//                    tblArbitage.addView(line);
//                }
//
//
//            }
//        }


    }


    @Override
    public void onItemClicked(Result menuItem, int position) {

    }

    @Override
    public void onItemLongClicked(Result menuItem, int position) {

    }

    @Override
    public void setAdapter(List<AribitaryTableResult> results) {
        aribitaryTableResultList.clear();
        aribitaryTableResultList.addAll(results);
        if (arbitageAdapter == null) {
            arbitageAdapter = new ArbitageAdapter(aribitaryTableResultList);
            recyclerView.setAdapter(arbitageAdapter);
            arbitageAdapter.setAdapterItemClickListener(this);
        } else {
            String txtSearch = search.getText().toString();
            if (!TextUtils.isEmpty(txtSearch))
                arbitageAdapter.getFilter().filter(txtSearch);
        }
    }

    @Override
    public void initRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    @OnClick({R.id.relcoin, R.id.relPrice1, R.id.relPrice2, R.id.relPercent})
    public void onSortView(View view) {
        switch (view.getId()) {

            case R.id.relcoin:
                showProgressBar();
                Log.d(TAG, "onSortView: coin " + isCoinSorted);
                isCoinSorted = !isCoinSorted;
                imgCoin.setImageResource(isCoinSorted ? R.drawable.ic_down : R.drawable.ic_up);
                presenter.sortList(aribitaryTableResultList, isCoinSorted, 0);
                break;
            case R.id.relPrice1:
                Log.d(TAG, "onSortView: price1" + isPrice1Sorted);

                isPrice1Sorted = !isPrice1Sorted;
                img_Price1.setImageResource(isPrice1Sorted ? R.drawable.ic_down : R.drawable.ic_up);
                presenter.sortList(aribitaryTableResultList, isPrice1Sorted, 0);
                break;
            case R.id.relPrice2:
                Log.d(TAG, "onSortView: price2" + isPrice2Sorted);

                isPrice2Sorted = !isPrice2Sorted;
                img_Price2.setImageResource(isPrice2Sorted ? R.drawable.ic_down : R.drawable.ic_up);
                presenter.sortList(aribitaryTableResultList, isPrice2Sorted, 0);
                break;
            case R.id.relPercent:
                Log.d(TAG, "onSortView: percent" + isPercentsorted);
                isPercentsorted = !isPercentsorted;
                img_Percent.setImageResource(isPercentsorted ? R.drawable.ic_down : R.drawable.ic_up);
                presenter.sortList(aribitaryTableResultList, isPercentsorted, 0);
                break;


        }
    }

    public void getArbitageTableResult() {
        presenter.getArbitageTableResult(spinnerValue1);
    }
}