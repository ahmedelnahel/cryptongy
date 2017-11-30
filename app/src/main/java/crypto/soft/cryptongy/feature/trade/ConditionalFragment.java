package crypto.soft.cryptongy.feature.trade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import crypto.soft.cryptongy.R;
import crypto.soft.cryptongy.utils.HideKeyboard;

/**
 * Created by tseringwongelgurung on 11/28/17.
 */

public class ConditionalFragment extends Fragment implements CompoundButton.OnCheckedChangeListener{
    private View view;

    private ToggleButton tgbPrice,tgbLoss;
    private EditText edtPrice,edtLoss;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_conditional, container, false);
            new HideKeyboard(getContext()).setupUI(view);
            findViews();
            initToggleListner();
        }
        return view;
    }

    public void findViews(){
        tgbPrice=view.findViewById(R.id.tgbPrice);
        tgbLoss=view.findViewById(R.id.tgbLoss);

        edtPrice=view.findViewById(R.id.edtPrice);
        edtLoss=view.findViewById(R.id.edtLoss);
    }

    public void initToggleListner(){
        tgbPrice.setOnCheckedChangeListener(this);
        tgbLoss.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id=compoundButton.getId();
        EditText editText=null;
        switch (id){
            case R.id.tgbLoss:
                editText=edtLoss;
                break;
            case R.id.tgbPrice:
                editText=edtPrice;
                break;
        }
    }
}
