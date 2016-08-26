package com.ataulm.stacks.stacks.item;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.ataulm.stacks.R;
import com.ataulm.stacks.stack.Stack;
import com.ataulm.stacks.stacks.UserItemActions;

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

    public void bind(final Stack stack, final UserItemActions userItemActions) {
        bindSummary(stack, userItemActions);
        bindCompletedCheckBox(stack, userItemActions);
        applyTreatmentForCompletedState(stack);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                userItemActions.onClick(stack);
            }
        });
    }

    private void bindSummary(final Stack stack, final UserItemActions userItemActions) {
        resetSummaryVisibility();

        summaryTextView.bind(stack, new StacksItemSummaryView.Listener() {
            @Override
            public void onClickEdit() {
                setEditableSummaryVisible();
            }

            @Override
            public void onClickRemove() {
                userItemActions.onClickRemove(stack);
            }
        });

        summaryEditText.bind(stack, new StacksItemSummaryEditView.Listener() {
            @Override
            public void onCancelChanges() {
                resetSummaryVisibility();
                summaryEditText.bind(stack, this);
            }

            @Override
            public void onAcceptChanges(String summary) {
                userItemActions.onClickEdit(stack, summary);
            }
        });
    }

    private void resetSummaryVisibility() {
        summaryTextView.setVisibility(VISIBLE);
        summaryEditText.setVisibility(GONE);
    }

    private void setEditableSummaryVisible() {
        summaryTextView.setVisibility(GONE);
        summaryEditText.setVisibility(VISIBLE);
    }

    private void bindCompletedCheckBox(final Stack stack, final UserItemActions userItemActions) {
        completedCheckBox.setOnCheckedChangeListener(null);
        completedCheckBox.setChecked(stack.completed());
        completedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !stack.completed()) {
                    userItemActions.onClickMarkComplete(stack);
                } else if (!isChecked && stack.completed()) {
                    userItemActions.onClickMarkNotComplete(stack);
                }
            }
        });
    }

    private void applyTreatmentForCompletedState(Stack stack) {
        if (stack.completed()) {
            setAlpha(0.54f);
        } else {
            setAlpha(1f);
        }
    }

}
