package crypto.soft.cryptongy.feature.account;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.shared.module.Account;
import crypto.soft.cryptongy.feature.shared.listner.OnClickListner;

/**
 * Created by tseringwongelgurung on 11/20/17.
 */

public class AccountAdapter extends RecyclerView.Adapter<AccountVH> {
    private List<Account> list;
    private OnClickListner deleteListner, editListner;

    public AccountAdapter(List<Account> list, OnClickListner deleteListner, OnClickListner editListner) {
        this.list = list;
        this.deleteListner = deleteListner;
        this.editListner = editListner;
    }

    @Override
    public AccountVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_account, parent, false);
        return new AccountVH(view,deleteListner,editListner);
    }

    @Override
    public void onBindViewHolder(AccountVH holder, int position) {
        Account account = list.get(position);

        holder.edtExchange.setText(account.getExchange());
        holder.edtLabel.setText(account.getLabel());
        holder.edtApiKey.setText(account.getApiKey());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
