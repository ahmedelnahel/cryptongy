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

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.shared.json.wallet.Result;
import crypto.soft.cryptongy.utils.CoinApplication;
import crypto.soft.cryptongy.utils.GlobalUtil;
import crypto.soft.cryptongy.utils.ViewFontHelper;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.MyViewHolder> {

    private List<Result> resultList;
    private Activity activity;
    private OnRecyclerItemClickListener<Result,String> onRecyclerItemClickListener;

    public WalletAdapter(List<Result> ResultsList, Activity activity, OnRecyclerItemClickListener<Result,String> onRecyclerItemClickListener) {
        this.resultList = ResultsList;
        this.activity = activity;
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    public void addResultList(List<Result> resultList) {
        this.resultList.clear();
        this.resultList.addAll(resultList);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_coin_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Result result = resultList.get(position);
        holder.tvCoin.setText(result.getCurrency());

        double balance = result.getBalance();

        double bitcoinPrice = result.getPrice();

        double totalBTC = (balance * bitcoinPrice);

        double priceInDollar = ((CoinApplication) activity.getApplication()).getUsdt_btc();
        double totalBTCInDollar = 0;

        totalBTCInDollar = (totalBTC * priceInDollar);

        String holding = "";
        if(result.getCurrency().equals("USDT"))
            holding = "$" + GlobalUtil.round(balance, 4);
        else if(result.getCurrency().equals("BTC"))
            holding =  "฿"+new DecimalFormat("0.00000000").format(balance);
        else
            holding =  "#" + GlobalUtil.round(balance, 4) + "\n฿"+  new DecimalFormat("0.00000000").format(totalBTC) + "\n$" + new DecimalFormat("#.#####").format(totalBTCInDollar);

        holder.tvHolding.setText(holding);

        double coininDollar = 0;
        String price = "";
        if(result.getCurrency().equals("USDT")) {
            coininDollar = balance;
            price = "$1";
        }
        else if(result.getCurrency().equals("BTC")) {
            coininDollar = priceInDollar;
            price = "฿1" + "\n$" + GlobalUtil.round(coininDollar, 4);
        }
        else {
            coininDollar = bitcoinPrice * priceInDollar;
            price = "฿" + new DecimalFormat("0.00000000").format(bitcoinPrice) + "\n$" + GlobalUtil.round(coininDollar, 4);
        }
        holder.tvPrice.setText(price);
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public List<Result> getResultList() {
        return resultList;
    }

    public void setResultList(List<Result> resultList) {
        this.resultList = resultList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCoin, tvHolding, tvPrice;
        private LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            tvCoin = view.findViewById(R.id.tvCoin);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRecyclerItemClickListener.onCoinClickListener(resultList.get(getAdapterPosition()),"");
                }
            });
            tvHolding = view.findViewById(R.id.tvHolding);
            tvPrice = view.findViewById(R.id.tvPrice);
            linearLayout = view.findViewById(R.id.linlay);
            ViewFontHelper.setupTextViews(activity, linearLayout);
        }
    }
}






