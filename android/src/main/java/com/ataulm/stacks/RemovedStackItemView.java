package com.ataulm.stacks;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ataulm.stacks.remove.RemovedStackItemListener;
import com.ataulm.stacks.stack.Stack;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RemovedStackItemView extends LinearLayout {

    @Bind(R.id.removed_stack_text_summary)
    TextView summaryTextView;

    @Bind(R.id.removed_stack_button_restore)
    Button restoreButton;

    @Bind(R.id.removed_stack_button_delete)
    Button deleteButton;

    public RemovedStackItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_removed_stack, this);
        ButterKnife.bind(this);
    }

    public void bind(final Stack stack, final RemovedStackItemListener listener) {
        summaryTextView.setText(stack.summary());

        restoreButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickRestore(stack);
            }
        });

        deleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickDelete(stack);
            }
        });
    }

    private void confirmRemove(final Stack stack, final RemovedStackItemListener listener) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete this stack?")
                .setMessage("This stack and all its children will be deleted.")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onClickDelete(stack);
                    }
                })
                .setNegativeButton("Cancel", new NoOpDialogOnClickListener())
                .create()
                .show();
    }

}
