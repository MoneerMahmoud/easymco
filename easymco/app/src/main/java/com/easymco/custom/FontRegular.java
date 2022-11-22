package com.easymco.custom;

import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

public class FontRegular extends AppCompatTextView {
    public FontRegular(Context context) {
        super(context);
        fontCustom();
    }

    public FontRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        fontCustom();
    }

    public FontRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        fontCustom();
    }

    public void fontCustom(){
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/RobotoRegular.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
