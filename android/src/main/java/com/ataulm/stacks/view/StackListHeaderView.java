package com.ataulm.stacks.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ataulm.stacks.R;
import com.ataulm.stacks.model.Stack;
import com.novoda.notils.caster.Views;

public class StackListHeaderView extends RelativeLayout {

    private TextView summary;

    public StackListHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        summary = Views.findById(this, R.id.textview_summary);
    }

    public void updateWith(Stack stack) {
        if (Stack.ZERO.equals(stack)) {
            setVisibility(GONE);
            return;
        }
        summary.setText("summary:" + stack.summary);
    }
}
