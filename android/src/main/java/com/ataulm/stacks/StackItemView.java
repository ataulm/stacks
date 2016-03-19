package com.ataulm.stacks;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ataulm.stacks.stack.Stack;

public class StackItemView extends TextView {

    public StackItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void bind(Stack stack) {
        setText(stack.summary());
    }

}
