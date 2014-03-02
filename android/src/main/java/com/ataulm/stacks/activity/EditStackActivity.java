package com.ataulm.stacks.activity;

import android.os.Bundle;
import android.widget.EditText;

import com.ataulm.stacks.R;
import com.ataulm.stacks.base.StacksDoneDiscardActivity;
import com.ataulm.stacks.model.Stack;
import com.ataulm.stacks.persistence.task.UpdateTask;
import com.novoda.notils.caster.Views;

public class EditStackActivity extends StacksDoneDiscardActivity implements StacksDoneDiscardActivity.DoneDiscardListener {

    public static final String EXTRA_STACK = "com.ataulm.stacks.extra.EXTRA_PARENT";

    private EditText summary;
    private EditText description;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stack);
        summary = Views.findById(this, R.id.edittext_summary);
        description = Views.findById(this, R.id.edittext_description);

        updateStack();
    }

    private void updateStack() {
        Stack stack = getStack();
        summary.setText(stack.summary);
        description.setText(stack.description);
    }

    @Override
    public void onDoneClick() {
        String summaryText = summary.getText().toString().trim();
        if (summaryText.length() == 0) {
            toast(R.string.summary_cannot_be_blank);
            return;
        }
        String descriptionText = description.getText().toString().trim();
        Stack stack = Stack.Builder.from(getStack()).summary(summaryText).description(descriptionText).build();
        UpdateTask.newInstance(getContentResolver(), stack).execute();
        finish();
    }

    @Override
    public void onDiscardClick() {
        // TODO: persist in shared prefs with stack id in case of accidental discard
        finish();
    }

    private Stack getStack() {
        if (getIntent().hasExtra(EXTRA_STACK)) {
            return getIntent().getParcelableExtra(EXTRA_STACK);
        }
        return Stack.ZERO;
    }

}
