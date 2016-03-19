package com.ataulm.stacks.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ataulm.stacks.R;
import com.ataulm.stacks.model.AndroidStack;

public class BasicStackListItemView extends RelativeLayout {

    private TextView textViewSummary;

    public BasicStackListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        textViewSummary = (TextView) findViewById(R.id.textview_summary);
    }

    public void updateWith(AndroidStack stack) {
        textViewSummary.setText(stack.summary);
    }

}
