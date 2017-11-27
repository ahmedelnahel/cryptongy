package crypto.soft.cryptongy.feature.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.shared.json.market.Result;
import crypto.soft.cryptongy.feature.shared.listner.AdapterItemClickListener;

/**
 * Created by Ajahar on 11/26/2017.
 */

public class CustomArrayAdapter extends BaseAdapter implements Filterable {
    Context context;
    List<Result> results = new ArrayList<>();
    List<Result> filteredResults = new ArrayList<>();
    private AdapterItemClickListener adapterItemClickListener;

    public CustomArrayAdapter(Context context, List<Result> results) {
        this.context = context;
        this.filteredResults = results;
        this.results = results;
    }


    @Override
    public int getCount() {
        return filteredResults.size();
    }

    @Override
    public Object getItem(int i) {
        return filteredResults.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.drop_down, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.coinName.setText(((Result) getItem(i)).getMarketName());
        //  viewHolder.coinName.setOnClickListener(new OnCurrencyItemClick(i,viewHolder,(Result)getItem(i)));
        return view;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                List<Result> filteredList = new ArrayList<>();
                if (charString.isEmpty()) {
                    filteredList = filteredResults;
                } else {

                    for (Result row : filteredResults) {
                        if (row.getMarketName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                filterResults.count = filteredList.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, final FilterResults filterResults) {
                List<Result> allMatching = (ArrayList<Result>) filterResults.values;
                if (allMatching != null && !allMatching.isEmpty()) {
                    filteredResults = allMatching;
                } else {
                    filteredResults = results;
                }
                notifyDataSetChanged();
            }
        };
    }

    public void setAdapterItemClickListener(AdapterItemClickListener adapterItemClickListener) {
        this.adapterItemClickListener = adapterItemClickListener;
    }

    static class ViewHolder {
        @BindView(R.id.coinName)
        TextView coinName;
        @BindView(R.id.parent)
        RelativeLayout parent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class OnCurrencyItemClick implements View.OnClickListener {
        int position;
        ViewHolder currencyViewHolder;
        Result item;

        public OnCurrencyItemClick(int position, ViewHolder currencyViewHolder, Result item) {
            this.position = position;
            this.currencyViewHolder = currencyViewHolder;
            this.item = item;
        }

        @Override
        public void onClick(View view) {
            if (adapterItemClickListener != null) {
                adapterItemClickListener.onItemClicked(item, position);
            }
        }

    }


}
