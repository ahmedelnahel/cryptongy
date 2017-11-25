package crypto.soft.cryptongy.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class ViewFontHelper
{
    private static final Map<String, Typeface> mFontCache = new HashMap<>();

    private static Typeface getTypeface(Context context, String fontPath)
    {
        Typeface typeface;
        if (mFontCache.containsKey(fontPath))
        {
            typeface = mFontCache.get(fontPath);
        } else
        {
            typeface = Typeface.createFromAsset(context.getAssets(), fontPath);
            mFontCache.put(fontPath, typeface);
        }
        return typeface;
    }

    public static void setupTextViews(Context context, ViewGroup parent)
    {
        for (int i = parent.getChildCount() - 1; i >= 0; i--)
        {
            final View child = parent.getChildAt(i);
            if (child instanceof ViewGroup)
            {
                setupTextViews(context, (ViewGroup) child);
            } else
            {
                if (child != null)
                {
                    ViewFontHelper.setupTextView(context, child);
                }
            }
        }
    }

    public static void setupTextView(Context context, View view)
    {
        if (view instanceof TextView)
        {
            if (view.getTag() != null) // also inherited from TextView's style
            {
                TextView textView = (TextView) view;
                String fontPath = (String) textView.getTag();
                Typeface typeface = getTypeface(context, fontPath);
                if (typeface != null)
                {
                    textView.setTypeface(typeface);
                }
            }
        }
        if (view instanceof EditText)
        {
            if (view.getTag() != null) // also inherited from TextView's style
            {
                EditText editText = (EditText) view;
                String fontPath = (String) editText.getTag();
                Typeface typeface = getTypeface(context, fontPath);
                if (typeface != null)
                {
                    editText.setTypeface(typeface);
                }
            }
        }
    }

    public static TextView setupTextView(View rootView, int id)
    {
        TextView textView = (TextView) rootView.findViewById(id);
        setupTextView(rootView.getContext().getApplicationContext(), textView);
        return textView;
    }

    public static TextView setupTextView(Activity activity, int id)
    {
        TextView textView = (TextView) activity.findViewById(id);
        setupTextView(activity.getApplicationContext(), textView);
        return textView;
    }
}