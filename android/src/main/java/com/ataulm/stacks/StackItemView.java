package com.ataulm.stacks;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ataulm.stacks.stack.Stack;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StackItemView extends LinearLayout {

    @Bind(R.id.stack_item_text_summary)
    TextView summaryTextView;

    @Bind(R.id.stack_item_text_id)
    TextView idTextView;

    @Bind(R.id.stack_item_text_parent_id)
    TextView parentIdTextView;

    @Bind(R.id.stack_item_button_remove)
    View removeButton;

    public StackItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_stack_item, this);
        ButterKnife.bind(this);
    }

    public void bind(final Stack stack, final StackItemListener listener) {
        summaryTextView.setText(stack.summary());
        idTextView.setText("id: " + stack.id().substring(0, 5));
        if (stack.parentId().isPresent()) {
            parentIdTextView.setText("parent: " + stack.parentId().get().substring(0, 5));
        } else {
            parentIdTextView.setText("parent: none");
        }

        removeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickRemove(stack);
            }
        });
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(stack);
            }
        });
    }

}
