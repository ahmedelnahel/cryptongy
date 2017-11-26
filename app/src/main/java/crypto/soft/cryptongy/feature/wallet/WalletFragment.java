package crypto.soft.cryptongy.feature.wallet;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.shared.json.market.MarketSummaries;
import crypto.soft.cryptongy.feature.shared.json.wallet.Result;
import crypto.soft.cryptongy.feature.shared.json.wallet.Wallet;
import crypto.soft.cryptongy.network.BittrexServices;

import crypto.soft.cryptongy.utils.AlertUtility;
import crypto.soft.cryptongy.utils.CoinApplication;
import crypto.soft.cryptongy.utils.GlobalUtil;
import crypto.soft.cryptongy.utils.ViewFontHelper;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WalletFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WalletFragment extends Fragment implements OnRecyclerItemClickListener<Result>, View.OnClickListener
{
    private TextView tvBTCValue, tvDollarValue;

//    private AppBarLayout appBarLayout;
    private LinearLayout linlaySummary;
    private LinearLayout linlayOperationButton;
    private RecyclerView rvCoinList;
    private WalletAdapter coinAdapter;
    private Spinner spCurrency;
    private EditText editCoinSearch;
    private ImageView ivRefresh;
    private List<Result> resultList;
    private double BTCSum = 0;
    private LinearLayout baseView;
    private TextView tvHolding, tvPrice;
//    private static final String ARG_PARAM1 = "param1";
//    private String mParam1;

    public WalletFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WalletFragment.
     */
    public static WalletFragment newInstance(/*String param1*/)
    {
        WalletFragment fragment = new WalletFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }*/
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);
        setUpView(view);
        ViewFontHelper.setupTextViews(getActivity(), baseView);
        new GetCoinDetails().execute();

        return view;
    }

    void setUpView(View view)
    {
        tvHolding = view.findViewById(R.id.tvHolding);
        tvHolding.setTag(R.id.tvHolding,Boolean.TRUE);
        tvHolding.setOnClickListener(this);
        tvPrice = view.findViewById(R.id.tvPrice);
        tvPrice.setTag(R.id.tvPrice,Boolean.TRUE);
        tvPrice.setOnClickListener(this);
        baseView = view.findViewById(R.id.baseView);
        tvBTCValue = view.findViewById(R.id.tvBTCValue);
        tvDollarValue = view.findViewById(R.id.tvDollarValue);

        spCurrency = view.findViewById(R.id.spCurrency);
        ivRefresh = view.findViewById(R.id.ivRefresh);
        ivRefresh.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new GetCoinDetails().execute();
            }
        });

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_spinner_item, new String[]{"Bittrex"}); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spCurrency.setAdapter(spinnerArrayAdapter);
        editCoinSearch = view.findViewById(R.id.editCoinSearch);

        editCoinSearch.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    editCoinSearch.setCursorVisible(true);
                return false;
            }

        });


        editCoinSearch.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                //after the change calling the method and passing the search input
                filter(editable.toString());
                if(editable.toString().length()==0)
                    editCoinSearch.setCursorVisible(false);
                else
                    editCoinSearch.setCursorVisible(true);

            }
        });
//        appBarLayout = view.findViewById(R.id.app_bar);
        linlaySummary = view.findViewById(R.id.linlaySummary);
        linlayOperationButton = view.findViewById(R.id.linlayOperationButton);
        rvCoinList = view.findViewById(R.id.rvCoinList);
        rvCoinList.setNestedScrollingEnabled(false);
        coinAdapter = new WalletAdapter(new ArrayList<Result>(), getActivity(), this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvCoinList.setLayoutManager(mLayoutManager);
        rvCoinList.setItemAnimator(new DefaultItemAnimator());
        rvCoinList.setAdapter(coinAdapter);

//        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener()
//        {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset)
//            {
//                /**
//                 * verticalOffset changes in diapason
//                 * from 0 - appBar is fully unwrapped
//                 * to -appBarLayout's height - appBar is totally collapsed
//                 * so in example we hide FAB when user folds half of the appBarLayout
//                 */
//                if (appBarLayout.getHeight() / 2 < -verticalOffset)
//                {
//                    linlaySummary.setVisibility(View.GONE);
//                    linlayOperationButton.setVisibility(View.GONE);
//                } else
//                {
//                    linlaySummary.setVisibility(View.VISIBLE);
//                    linlayOperationButton.setVisibility(View.VISIBLE);
//                }
//            }
//        });

    }

    private void filter(String text)
    {
        ArrayList<Result> filterdCurrency = new ArrayList<>();

        for (Result result : resultList)
        {
            //if the existing elements contains the search input
            if (result.getCurrency().toLowerCase().contains(text.toLowerCase()))
            {
                //adding the element to filtered list
                filterdCurrency.add(result);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        coinAdapter.setResultList(filterdCurrency);
        coinAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCoinClickListener(Result result)
    {
        Toast.makeText(getActivity(), "Coin Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v)
    {
        List<Result> results = coinAdapter.getResultList();//new ArrayList<>();
//        results.addAll(resultList);


        switch (v.getId())
        {
            case R.id.tvPrice:
                Collections.sort(results, new Result.PriceComparator((Boolean) v.getTag(v.getId())));
                break;
            case R.id.tvHolding:
                Collections.sort(results, new Result.HoldingComparator((Boolean) v.getTag(v.getId())));
                break;

        }
//        coinAdapter.addResultList(results);
//        coinAdapter.setResultList();
        v.setTag(v.getId(),!(Boolean) v.getTag(v.getId()));

        coinAdapter.notifyDataSetChanged();
    }


    private class GetCoinDetails extends AsyncTask<String, Void, List<Result>>
    {
        private BittrexServices bittrexServices = new BittrexServices();

        @Override
        protected List<Result> doInBackground(String... params)
        {
            try
            {
                Wallet wallet = bittrexServices.getWalletMock();

                if (wallet != null && wallet.getSuccess())
                {
                    List<Result> walletResults = wallet.getResult();

                    List<Result> filteredWalletResults = new ArrayList<>();

                    MarketSummaries marketSummaries = bittrexServices.getMarketSummariesMock();
                    if (marketSummaries != null && marketSummaries.getSuccess())
                    {

                        getFilteredCoinList(walletResults, filteredWalletResults);

                        fillCoinPrice(filteredWalletResults, marketSummaries);
                        return filteredWalletResults;
                    }
                }

            } catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        private void getFilteredCoinList(List<Result> walletResults, List<Result> filteredWalletResults)
        {
            for (Result walletResult : walletResults)
            {
                if (walletResult.getBalance() != 0)
                    filteredWalletResults.add(walletResult);
            }
        }


        private void fillCoinPrice(List<Result> walletResults, MarketSummaries marketSummaries)
        {
            BTCSum = 0;
            List<crypto.soft.cryptongy.feature.shared.json.market.Result> marketResults = marketSummaries.getResult();
            for (Result walletResult : walletResults)
            {

                for (crypto.soft.cryptongy.feature.shared.json.market.Result marketResult : marketResults)
                {
                    String currency = "BTC-" + walletResult.getCurrency();
                    if (currency.equals(marketResult.getMarketName()))
                    {
                        walletResult.setPrice(marketResult.getLast());
                        double balance = walletResult.getBalance();

                        double coinbitcoinPrice = walletResult.getPrice();

                        double totalBTC = (balance * coinbitcoinPrice);

                        BTCSum += totalBTC;
                        break;
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(List<Result> result)
        {
            AlertUtility.dismissDialog();

            if (result == null)
            {
                Toast.makeText(getActivity(), "Error fetching Coin details", Toast.LENGTH_SHORT).show();
                return;
            }
            resultList = new ArrayList<>();
            resultList.addAll(result);

            coinAdapter.setResultList(result);
            coinAdapter.notifyDataSetChanged();

            tvBTCValue.setText(String.valueOf(GlobalUtil.round(BTCSum, 9)));
            tvDollarValue.setText(String.valueOf(GlobalUtil.round(BTCSum * ((CoinApplication) getActivity().getApplication()).getUsdt_btc(), 4)));
        }

        @Override
        protected void onPreExecute()
        {
            AlertUtility.showLoadingDialog(getActivity());
        }

        @Override
        protected void onProgressUpdate(Void... values)
        {
        }
    }

}