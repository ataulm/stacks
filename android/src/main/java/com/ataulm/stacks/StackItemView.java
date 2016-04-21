package com.ataulm.stacks;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ataulm.stacks.stack.Stack;
import com.novoda.accessibility.AccessibilityServices;
import com.novoda.accessibility.Action;
import com.novoda.accessibility.Actions;
import com.novoda.accessibility.ActionsAccessibilityDelegate;
import com.novoda.accessibility.ActionsAlertDialogCreator;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StackItemView extends LinearLayout {

    @Bind(R.id.stack_item_text_summary)
    TextView summaryTextView;

    @Bind(R.id.stack_item_button_remove)
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
        setContentDescription(stack.summary());
        summaryTextView.setText(stack.summary());

        Actions actions = createActions(stack, listener);
        final AlertDialog alertDialog = new ActionsAlertDialogCreator(getContext(), R.string.stack_actions_title, actions).create();

        if (accessibilityServices.isSpokenFeedbackEnabled()) {
            ViewCompat.setAccessibilityDelegate(this, new ActionsAccessibilityDelegate(getResources(), actions));
            addOnClickToShow(alertDialog);
        } else {
            addOnClickToOpen(stack, listener);
            addOnClickToRemove(stack, listener);
        }
    }

    private static Actions createActions(Stack stack, StackItemListener listener) {
        return new Actions(Arrays.asList(
                createViewActionFor(stack, listener),
                createRemoveActionFor(stack, listener)
        ));
    }

    private static Action createViewActionFor(final Stack stack, final StackItemListener listener) {
        return new Action(R.id.stack_item_action_view, R.string.stack_item_view, new Runnable() {
            @Override
            public void run() {
                listener.onClick(stack);
            }
        });
    }

    private static Action createRemoveActionFor(final Stack stack, final StackItemListener listener) {
        return new Action(R.id.stack_item_action_remove, R.string.stack_item_remove, new Runnable() {
            @Override
            public void run() {
                listener.onClickRemove(stack);
            }
        });
    }

    private void addOnClickToShow(final AlertDialog alertDialog) {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
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

    private void addOnClickToRemove(final Stack stack, final StackItemListener listener) {
        removeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickRemove(stack);
            }
        });
    }

}
