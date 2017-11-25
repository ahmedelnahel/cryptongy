package crypto.soft.cryptongy.feature.wallet;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import crypto.soft.cryptongy.feature.shared.json.wallet.Result;
import crypto.soft.cryptongy.network.BittrexServices;

import crypto.soft.cryptongy.utils.CoinApplication;
import crypto.soft.cryptongy.utils.GlobalUtil;
import crypto.soft.cryptongy.utils.ViewFontHelper;
import crypto.soft.cryptongy.R;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.MyViewHolder>
{

    private List<Result> resultList;
    private BittrexServices bittrexServices = new BittrexServices();
    private Activity activity;
    private OnRecyclerItemClickListener<Result> onRecyclerItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView tvCoin, tvHolding, tvPrice;
        private LinearLayout linearLayout;

        public MyViewHolder(View view)
        {
            super(view);
            tvCoin = view.findViewById(R.id.tvCoin);
            tvCoin.setOnClickListener(this);
            tvHolding = view.findViewById(R.id.tvHolding);
            tvPrice = view.findViewById(R.id.tvPrice);
            linearLayout = view.findViewById(R.id.linlay);
            ViewFontHelper.setupTextViews(activity, linearLayout);

        }

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.tvCoin:
                    onRecyclerItemClickListener.onCoinClickListener(resultList.get(getAdapterPosition()));
                    break;
            }
        }
    }


    public WalletAdapter(List<Result> ResultsList, Activity activity, OnRecyclerItemClickListener<Result> onRecyclerItemClickListener)
    {
        this.resultList = ResultsList;
        this.activity = activity;
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    public void setResultList(List<Result> resultList)
    {
        this.resultList = resultList;
    }

    public void addResultList(List<Result> resultList)
    {
        this.resultList.clear();
        this.resultList.addAll(resultList);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_coin_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        Result result = resultList.get(position);
        holder.tvCoin.setText(result.getCurrency());

        double balance = result.getBalance();

        double bitcoinPrice = result.getPrice();

        double totalBTC = (balance * bitcoinPrice);

        double priceInDollar = ((CoinApplication) activity.getApplication()).getUsdt_btc();
        double totalBTCInDollar = (totalBTC * priceInDollar);

        String holding = new DecimalFormat("#.#########").format(balance) + "\nB " + GlobalUtil.round(totalBTC, 9) + "\n$" + GlobalUtil.round(totalBTCInDollar, 4);
        holder.tvHolding.setText(holding);
        double coininDollar = bitcoinPrice * priceInDollar;
        String price = "" + GlobalUtil.round(bitcoinPrice, 9) + "s\n$" + GlobalUtil.round(coininDollar, 4);

        holder.tvPrice.setText(price);
    }

    @Override
    public int getItemCount()
    {
        return resultList.size();
    }

    public List<Result> getResultList()
    {
        return resultList;
    }
}






