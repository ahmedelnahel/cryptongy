package crypto.soft.cryptongy.feature.arbitage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.shared.json.market.Result;
import crypto.soft.cryptongy.feature.shared.listner.AdapterItemClickListener;

/**
 * Created by Ajahar on 11/21/2017.
 */

public class ArbitageAdapter extends RecyclerView.Adapter<ArbitageAdapter.ArbitaryViewHolder> implements Filterable {
    List<AribitaryTableResult> aribitaryTableResultList;
    private AdapterItemClickListener adapterItemClickListener;



    public ArbitageAdapter(List<AribitaryTableResult> aribitaryTableResultList) {
        this.aribitaryTableResultList = aribitaryTableResultList;

    }

    @Override
    public ArbitaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ArbitaryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.table_arbitary_sub2, parent, false));
    }

    @Override
    public void onBindViewHolder(ArbitaryViewHolder holder, final int position) {
        AribitaryTableResult result = aribitaryTableResultList.get(position);
        if (result != null) {
            holder.txtTitleCoin.setText(result.getCoinName());
            holder.txtTitlePrice1.setText(result.getPrice1());
            holder.txtTitlePrice2.setText(result.getPrice2());
            holder.txtTitlePercentage.setText(result.getPercentage());
        }
    }

    @Override
    public int getItemCount() {
        return aribitaryTableResultList.size();
    }

    public void setAdapterItemClickListener(AdapterItemClickListener adapterItemClickListener) {
        this.adapterItemClickListener = adapterItemClickListener;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                List<AribitaryTableResult> filteredList = new ArrayList<>();
                if (charString.isEmpty())
                    filteredList= aribitaryTableResultList;
                 else {
                    for (int i = 0; i < aribitaryTableResultList.size(); i++) {
                        AribitaryTableResult row = aribitaryTableResultList.get(i);
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCoinName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                aribitaryTableResultList = (List<AribitaryTableResult>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ArbitaryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtTitleCoin)
        TextView txtTitleCoin;
        @BindView(R.id.txtTitlePrice1)
        TextView txtTitlePrice1;
        @BindView(R.id.txtTitlePrice2)
        TextView txtTitlePrice2;
        @BindView(R.id.txtTitlePercentage)
        TextView txtTitlePercentage;


        public ArbitaryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class OnCurrencyItemClick implements View.OnLongClickListener {
        int position;
        ArbitaryViewHolder currencyViewHolder;
        Result item;

        public OnCurrencyItemClick(int position, ArbitaryViewHolder currencyViewHolder, Result item) {
            this.position = position;
            this.currencyViewHolder = currencyViewHolder;
            this.item = item;
        }


        @Override
        public boolean onLongClick(View view) {
            if (adapterItemClickListener != null) {
                AribitaryTableResult result=aribitaryTableResultList.get(position);
                adapterItemClickListener.onItemLongClicked(item, position);
            }
            return true;
        }
    }

}
