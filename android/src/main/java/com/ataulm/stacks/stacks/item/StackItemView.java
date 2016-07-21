package com.ataulm.stacks.stacks.item;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.ataulm.stacks.R;
import com.ataulm.stacks.stack.Stack;
import com.ataulm.stacks.stacks.ItemClickActions;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StackItemView extends LinearLayout {

    @BindView(R.id.stack_item_check_completed)
    CheckBox completedCheckBox;

    @BindView(R.id.stack_item_text_summary)
    StacksItemSummaryView summaryTextView;

    @BindView(R.id.stack_item_edit_summary)
    StacksItemSummaryEditView summaryEditText;

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

    public void bind(final Stack stack, final ItemClickActions itemClickActions) {
        showSummary();

        if (stack.completed()) {
            setAlpha(0.54f);
        } else {
            setAlpha(1f);
        }

        summaryTextView.bind(stack, new StacksItemSummaryView.Listener() {
            @Override
            public void onClickEdit() {
                showEditableSummary();
            }

            @Override
            public void onClickRemove() {
                itemClickActions.onClickRemove(stack);
            }
        });

        summaryEditText.bind(stack, new StacksItemSummaryEditView.Listener() {
            @Override
            public void onCancelChanges() {
                showSummary();
                summaryEditText.bind(stack, this);
            }

            @Override
            public void onAcceptChanges(String summary) {
                itemClickActions.onClickEdit(stack, summary);
            }
        });

        bindCompletedCheckBox(stack, itemClickActions);
    }

    private void showSummary() {
        summaryTextView.setVisibility(VISIBLE);
        summaryEditText.setVisibility(GONE);
    }

    private void showEditableSummary() {
        summaryTextView.setVisibility(GONE);
        summaryEditText.setVisibility(VISIBLE);
    }

    private void bindCompletedCheckBox(final Stack stack, final ItemClickActions itemClickActions) {
        completedCheckBox.setOnCheckedChangeListener(null);
        completedCheckBox.setChecked(stack.completed());
        completedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !stack.completed()) {
                    itemClickActions.onClickMarkComplete(stack);
                } else if (!isChecked && stack.completed()) {
                    itemClickActions.onClickMarkNotComplete(stack);
                }
            }
        });
    }

}
