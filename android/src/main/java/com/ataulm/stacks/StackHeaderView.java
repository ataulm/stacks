package com.ataulm.stacks;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ataulm.stacks.stack.Stack;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StackHeaderView extends LinearLayout {

    @Bind(R.id.stack_header_text_summary)
    TextView summaryTextView;

    @Bind(R.id.stack_header_text_description)
    TextView descriptionTextView;

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

    public void bind(final Stack stack) {
        summaryTextView.setText(stack.summary());
        if (stack.description().isPresent()) {
            descriptionTextView.setText(stack.description().get());
            descriptionTextView.setVisibility(VISIBLE);
        } else {
            descriptionTextView.setVisibility(GONE);
        }
    }

}
