package crypto.soft.cryptongy.feature.account;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.shared.listner.OnClickListner;

/**
 * Created by tseringwongelgurung on 11/20/17.
 */

public class AccountVH extends RecyclerView.ViewHolder {
    public TextView edtExchange, edtLabel, edtApiKey;
    public ImageView imgDel, imgEdit;

    public AccountVH(View itemView, final OnClickListner deleteListner, final OnClickListner editListner) {
        super(itemView);
        edtExchange = itemView.findViewById(R.id.edtExchange);
        edtLabel = itemView.findViewById(R.id.edtLabel);
        edtApiKey = itemView.findViewById(R.id.edtApiKey);
        imgDel = itemView.findViewById(R.id.imgDelete);
        imgEdit = itemView.findViewById(R.id.imgEdit);

        imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteListner.onItemClickedd(getAdapterPosition());
            }
        });

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editListner.onItemClickedd(getAdapterPosition());
            }
        });
    }
}
