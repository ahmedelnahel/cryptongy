package crypto.soft.cryptongy.feature.trade;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

import crypto.soft.cryptongy.R;

/**
 * Created by tseringwongelgurung on 11/28/17.
 */

public class FragmentLimit extends Fragment {
    private CheckBox chkImediate;
    private RadioGroup rdgValue;
    private EditText edtValue;

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_limit, container, false);
        }
        return view;
    }

    public void findViews() {
        chkImediate = view.findViewById(R.id.chkImediate);
        rdgValue = view.findViewById(R.id.rdgValue);
        edtValue = view.findViewById(R.id.edtValue);
    }
}
