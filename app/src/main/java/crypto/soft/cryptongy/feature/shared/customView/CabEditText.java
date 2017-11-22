package crypto.soft.cryptongy.feature.shared.customView;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

/**
 * Created by tseringwongelgurung on 11/22/17.
 */

public class CabEditText extends AppCompatEditText {
    public CabEditText(Context context) {
        super(context);
        setFont(context);
    }

    public CabEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }

    public CabEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont(context);
    }

    private void setFont(Context context) {
        setTypeface(Typeface.createFromAsset(context.getAssets(), "calibrib.ttf"));
    }
}
