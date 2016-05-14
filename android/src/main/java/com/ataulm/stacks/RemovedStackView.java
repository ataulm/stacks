package com.ataulm.stacks;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ataulm.stacks.stack.Stack;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RemovedStackView extends LinearLayout {

    @Bind(R.id.removed_stack_text_summary)
    TextView summaryTextView;

    @Bind(R.id.removed_stack_button_restore)
    Button restoreButton;

    @Bind(R.id.removed_stack_button_delete)
    Button deleteButton;

    public RemovedStackView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_removed_stack, this);
        ButterKnife.bind(this);
    }

    public void bind(Stack stack) {
        summaryTextView.setText(stack.summary());
        // TODO: bind buttons to callbacks
    }

}
