package com.ataulm.stacks.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.design.widget.AppBarLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.ataulm.stacks.R;

public class AppBarWidget extends AppBarLayout {

    public AppBarWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomAttributes(context, attrs);
    }

    private void applyCustomAttributes(Context context, AttributeSet attrs) {
        View.inflate(getContext(), R.layout.merge_app_bar_widget, this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AppBarWidget);
        Drawable drawable = typedArray.getDrawable(R.styleable.AppBarWidget_appBarBackground);
        if (drawable != null) {
            setBackground(drawable);
        } else {
            int backgroundResource = typedArray.getColor(R.styleable.AppBarWidget_appBarBackground, getColorPrimary());
            setBackgroundColor(backgroundResource);
        }
        typedArray.recycle();

        getColorPrimary();
    }

    @ColorInt
    private int getColorPrimary() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getContext().getTheme();
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

}
