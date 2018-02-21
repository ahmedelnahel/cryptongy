package crypto.soft.cryptongy.feature.arbitage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.shared.listner.AdapterItemClickListener;

/**
 * Created by vishalguptahmh on 02/20/18.
 */

public class ArbitageAdapter extends RecyclerView.Adapter<ArbitageAdapter.ArbitaryViewHolder>  {
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

}
