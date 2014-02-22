package com.ataulm.stacks.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ataulm.stacks.model.Stack;

public class StackListItemView extends TextView {

    public StackListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void updateWith(Stack stack) {
        setText(stack.summary);
    }

}
