package com.ataulm.stacks.stacks;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ataulm.stacks.R;
import com.ataulm.stacks.stack.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StackHeaderView extends LinearLayout {

    @BindView(R.id.stack_header_text_summary)
    TextView summaryTextView;

    public StackHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_stack_header, this);
        ButterKnife.bind(this);
    }

    public void bind(Stack stack) {
        summaryTextView.setText(stack.summary());
    }

}
