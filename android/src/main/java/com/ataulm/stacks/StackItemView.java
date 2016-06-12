package com.ataulm.stacks;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ataulm.stacks.stack.Stack;
import com.novoda.accessibility.AccessibilityServices;
import com.novoda.accessibility.Action;
import com.novoda.accessibility.Actions;
import com.novoda.accessibility.ActionsAccessibilityDelegate;
import com.novoda.accessibility.ActionsAlertDialogCreator;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StackItemView extends LinearLayout {

    @BindView(R.id.stack_item_check_completed)
    CheckBox completedCheckBox;

    @BindView(R.id.stack_item_text_summary)
    TextView summaryTextView;

    @BindView(R.id.stack_item_button_remove)
    View removeButton;

    private final AccessibilityServices accessibilityServices;

    public StackItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.accessibilityServices = AccessibilityServices.newInstance(getContext());
        setOrientation(HORIZONTAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_stack_item, this);
        ButterKnife.bind(this);
    }

    public void bind(Stack stack, StackItemListener listener) {
        summaryTextView.setText(stack.summary());
        setContentDescription(stack.summary());

        if (stack.completed()) {
            summaryTextView.setAlpha(0.54f);
        } else {
            summaryTextView.setAlpha(1f);
        }

        bindCompletedCheckBox(stack, listener);
        bindActions(stack, listener);
    }

    private void bindCompletedCheckBox(final Stack stack, final StackItemListener listener) {
        completedCheckBox.setOnCheckedChangeListener(null);
        completedCheckBox.setChecked(stack.completed());
        completedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !stack.completed()) {
                    listener.onClickMarkComplete(stack);
                } else if (!isChecked && stack.completed()) {
                    listener.onClickMarkNotComplete(stack);
                }
            }
        });
    }

    private void bindActions(Stack stack, StackItemListener listener) {
        Actions actions = createActions(stack, listener);
        AlertDialog alertDialog = new ActionsAlertDialogCreator(getContext(), R.string.stack_actions_title, actions).create();

        addOnClickToOpen(stack, listener);
        if (accessibilityServices.isSpokenFeedbackEnabled()) {
            ViewCompat.setAccessibilityDelegate(this, new ActionsAccessibilityDelegate(getResources(), actions));
            removeButton.setVisibility(GONE);
            addOnLongClickToShow(alertDialog);
        } else {
            removeButton.setVisibility(VISIBLE);
            addOnClickRemoveButtonToRemove(stack, listener);
        }
    }

    private Actions createActions(Stack stack, StackItemListener listener) {
        return new Actions(Arrays.asList(
                createViewActionFor(stack, listener),
                createRemoveActionFor(stack, listener)
        ));
    }

    private Action createViewActionFor(final Stack stack, final StackItemListener listener) {
        return new Action(R.id.stack_item_action_view, R.string.stack_item_view, new Runnable() {
            @Override
            public void run() {
                listener.onClick(stack);
            }
        });
    }

    private Action createRemoveActionFor(final Stack stack, final StackItemListener listener) {
        return new Action(R.id.stack_item_action_remove, R.string.stack_item_remove, new Runnable() {
            @Override
            public void run() {
                listener.onClickRemove(stack);
            }
        });
    }

    private void addOnLongClickToShow(final AlertDialog alertDialog) {
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                alertDialog.show();
                return true;
            }
        });
    }

    private void addOnClickToOpen(final Stack stack, final StackItemListener listener) {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(stack);
            }
        });
    }

    private void addOnClickRemoveButtonToRemove(final Stack stack, final StackItemListener listener) {
        removeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickRemove(stack);
            }
        });
    }

}
