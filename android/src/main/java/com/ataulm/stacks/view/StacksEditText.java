package com.ataulm.stacks.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class StacksEditText extends EditText {

    private final TypefaceFactory typeFaceFactory;

    public StacksEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.typeFaceFactory = new TypefaceFactory();
        initTypeface(context, attrs);
    }

    private void initTypeface(Context context, AttributeSet attrs) {
        Typeface typeface = typeFaceFactory.createFrom(context, attrs);
        setTypeface(typeface);
    }

}
