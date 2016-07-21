package com.ataulm.stacks.stacks.item;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ataulm.stacks.R;
import com.ataulm.stacks.SimpleTextWatcher;
import com.ataulm.stacks.stack.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StacksItemSummaryEditView extends LinearLayout {

    @BindView(R.id.stacks_item_summary_edit)
    EditText summaryEditText;

    @BindView(R.id.stacks_item_summary_button_cancel)
    View cancelChangesButton;

    @BindView(R.id.stacks_item_summary_button_accept)
    View acceptChangesButton;

    public StacksItemSummaryEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_stacks_item_summary_edit, this);
        ButterKnife.bind(this);

        summaryEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                boolean textIsPresent = s.length() > 0;
                acceptChangesButton.setEnabled(textIsPresent);
            }
        });

        // TODO: add acceptChanges view states that include distinct states for enabled/disabled
    }

    public void bind(Stack stack, final Listener listener) {
        summaryEditText.setText(stack.summary());

        cancelChangesButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancelChanges();
            }
        });

        acceptChangesButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String summary = summaryEditText.getText().toString();
                listener.onAcceptChanges(summary);
            }
        });
    }

    interface Listener {

        void onCancelChanges();

        void onAcceptChanges(String summary);

    }

}
