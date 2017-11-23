package crypto.soft.cryptongy.feature.account;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.feature.shared.module.Account;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;

/**
 * Created by tseringwongelgurung on 11/20/17.
 */

public class CustomDialog {
    public static void showAccountDialog(final Context context, final Account account, List<String> list, final OnFinishListner<Account> listner) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        final View view = View.inflate(context, R.layout.dialog_account, null);
        alertDialogBuilder.setView(view);
        final Dialog dialog = alertDialogBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setDimAmount(0.7f);

        final Spinner spinner = view.findViewById(R.id.spinner);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, list);
        spinner.setAdapter(adapter);

        Button btnAdd = view.findViewById(R.id.btnAdd);
        final EditText edtApiKey = view.findViewById(R.id.edtApiKey);
        final EditText edtSecret = view.findViewById(R.id.edtSecret);
        final RadioGroup rdgLabel = view.findViewById(R.id.rdgLabel);

        if (account.getId() != null) {
            edtApiKey.setText(account.getApiKey());
            edtSecret.setText(account.getSecret());
            int id = R.id.rdbRead;
            switch (account.getLabel()) {
                case "Read":
                    id = R.id.rdbRead;
                    break;
                case "Trade":
                    id = R.id.rdbTrade;
                    break;
                case "Withdraw":
                    id = R.id.rdbWithdraw;
                    break;
            }
            rdgLabel.check(id);

            btnAdd.setText("Update");
        } else
            btnAdd.setText("Add");

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String apiKey = null;
                String secret = null;
                String label = null;
                String exchange = null;

                apiKey = edtApiKey.getText().toString();
                secret = edtSecret.getText().toString();
                exchange = (String) spinner.getSelectedItem();

                if (TextUtils.isEmpty(apiKey)) {
                    edtApiKey.setError("Cannot be Empty");
                    return;
                }

                if (TextUtils.isEmpty(secret)) {
                    edtSecret.setError("Cannot be Empty");
                    return;
                }

                RadioButton radioButton = rdgLabel.findViewById(rdgLabel.getCheckedRadioButtonId());
                label = radioButton.getText().toString();

                account.setLabel(label);
                account.setApiKey(apiKey);
                account.setSecret(secret);
                account.setExchange(exchange);

                if (isAccountUsed(account)){
                    Toast.makeText(context, "Should have different Account type for same Exchange", Toast.LENGTH_LONG).show();
                    return;
                }
                listner.onComplete(account);
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.show();
    }

    public static boolean isAccountUsed(Account acc) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        RealmQuery<Account> query = realm.where(Account.class)
                .equalTo("exchange", acc.getExchange()).equalTo("label", acc.getLabel());

        if (acc.getId() != null)
            query.notEqualTo("id", acc.getId());

        Account account = query.findFirst();
        realm.commitTransaction();

        if (account == null)
            return false;
        else
            return true;
    }
}
