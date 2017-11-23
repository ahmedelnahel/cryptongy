package crypto.soft.cryptongy.feature.shared.customView;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by tseringwongelgurung on 11/23/17.
 */

public class MspTextView extends AppCompatTextView {
    public MspTextView(Context context) {
        super(context);
        setFont(context);
    }

    public MspTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }

    public MspTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont(context);
    }

    private void setFont(Context context) {
        setTypeface(Typeface.createFromAsset(context.getAssets(), "myriad_pro_semibold.ttf"));
    }
}
