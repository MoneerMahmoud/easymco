package com.easymco.custom;

import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

public class FontLight  extends AppCompatTextView {
    public FontLight(Context context) {
        super(context);
        fontCustom();
    }

    public FontLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        fontCustom();
    }

    public FontLight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        fontCustom();
    }

    public void fontCustom(){
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/RobotoLight.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
