package crypto.soft.cryptongy.feature.arbitage;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.LinearLayout;
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
import crypto.soft.cryptongy.feature.setting.Notification;
import crypto.soft.cryptongy.utils.CoinApplication;
import crypto.soft.cryptongy.utils.GlobalConstant;
import crypto.soft.cryptongy.utils.HideKeyboard;

/**
 * Created by vishalguptahmh on 02/20/18.
 */
public class ArbitageFragment extends MvpFragment<ArbitageView, ArbitagePresenter> implements ArbitageView {

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

    @BindView(R.id.llArbitageContainer)
    LinearLayout cardView;
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
    private List<AribitaryTableResult> aribitaryTableResultList;

    long delay = 1000;
    long last_text_edit = 0;
    String strToSearch;
    Handler handler = new Handler();

    ArbitageAdapter arbitageAdapter;
    private static CountDownTimer countDownTimer;
    private boolean countDownTimerRunning =false;


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

                stopTimer();
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
        startArbitageTimer();
        // presenter.startTimer(spinnerValue1);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTimer();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimer();

    }

    @Override
    public void onStop() {
        super.onStop();
        stopTimer();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopTimer();

    }


    @Override
    public void setList(List<AribitaryTableResult> list) {
        aribitaryTableResultList = list;
        startArbitageTimer();
        setCoinInTable(aribitaryTableResultList);
    }


    @Override
    public void setCoinInTable(final List<AribitaryTableResult> resultList) {

        arbitageAdapter.aribitaryTableResultList.clear();
        arbitageAdapter.aribitaryTableResultList.addAll(resultList);
        arbitageAdapter.notifyDataSetChanged();
        cardView.setVisibility(View.VISIBLE);
        hideProgressBar();

    }


    @Override
    public void setAdapter(List<AribitaryTableResult> results) {
        aribitaryTableResultList.clear();
        aribitaryTableResultList.addAll(results);
        if (arbitageAdapter == null) {
            arbitageAdapter = new ArbitageAdapter(aribitaryTableResultList);
            recyclerView.setAdapter(arbitageAdapter);
          //  arbitageAdapter.setAdapterItemClickListener(this);
        }
    }

    @Override
    public void initRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
//                linearLayoutManager.getOrientation());
//        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    @OnClick({R.id.relcoin, R.id.relPrice1, R.id.relPrice2, R.id.relPercent})
    public void onSortView(View view) {
        switch (view.getId()) {

            case R.id.relcoin:
               progressBar.setVisibility(View.VISIBLE);
                Log.d(TAG, "onSortView: coin " + isCoinSorted);
                isCoinSorted = !isCoinSorted;
                imgCoin.setImageResource(isCoinSorted ? R.drawable.ic_down : R.drawable.ic_up);
                presenter.sortList(aribitaryTableResultList, isCoinSorted, 0);
                break;
            case R.id.relPrice1:
                Log.d(TAG, "onSortView: price1" + isPrice1Sorted);
                progressBar.setVisibility(View.VISIBLE);
                isPrice1Sorted = !isPrice1Sorted;
                img_Price1.setImageResource(isPrice1Sorted ? R.drawable.ic_down : R.drawable.ic_up);
                presenter.sortList(aribitaryTableResultList, isPrice1Sorted, 0);
                break;
            case R.id.relPrice2:
                Log.d(TAG, "onSortView: price2" + isPrice2Sorted);
                progressBar.setVisibility(View.VISIBLE);
                isPrice2Sorted = !isPrice2Sorted;
                img_Price2.setImageResource(isPrice2Sorted ? R.drawable.ic_down : R.drawable.ic_up);
                presenter.sortList(aribitaryTableResultList, isPrice2Sorted, 0);
                break;
            case R.id.relPercent:
                Log.d(TAG, "onSortView: percent" + isPercentsorted);
                progressBar.setVisibility(View.VISIBLE);
                isPercentsorted = !isPercentsorted;
                img_Percent.setImageResource(isPercentsorted ? R.drawable.ic_down : R.drawable.ic_up);
                presenter.sortList(aribitaryTableResultList, isPercentsorted, 0);
                break;


        }
    }

    public void getArbitageTableResult() {
        presenter.getArbitageTableResult(spinnerValue1);
    }




    public void startArbitageTimer() {
        if (getContext()!= null && getContext().getApplicationContext() != null) {
            Notification notification = ((CoinApplication) getContext().getApplicationContext()).getNotification();
            if (notification.isAutomSync()) {
                Log.d(TAG, "startTicker: ");
                final int timerInterval = notification.getSyncInterval() * 1000;


                if (!countDownTimerRunning) {

                    countDownTimer = new CountDownTimer(timerInterval, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            countDownTimerRunning = true;
                        }

                        @Override
                        public void onFinish() {
                            try {
                                countDownTimerRunning = false;
                                Log.d(TAG, "onFinish: timeriscalled : " + timerInterval / 1000);

                               presenter.getArbitageTableResult(spinnerValue1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }.start();
                }

            }
        }
    }
    public void stopTimer() {
        Log.d(TAG, "stopTimer: ");
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimerRunning=false;
        }

    }

}
