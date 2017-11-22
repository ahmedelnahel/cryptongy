package crypto.soft.cryptongy.feature.shared.customView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by tseringwongelgurung on 11/20/17.
 */

public class CalButton extends android.support.v7.widget.AppCompatButton {
    public CalButton(Context context) {
        super(context);
        setFont(context);
    }

    public CalButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }

    public CalButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont(context);
    }

    private void setFont(Context context) {
        setTypeface(Typeface.createFromAsset(context.getAssets(), "calibri.ttf"));
    }
}
