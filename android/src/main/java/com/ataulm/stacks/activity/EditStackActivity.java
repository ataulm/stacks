package com.ataulm.stacks.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.ataulm.stacks.R;
import com.ataulm.stacks.base.StacksDoneDiscardActivity;
import com.ataulm.stacks.model.AndroidStack;
import com.ataulm.stacks.persistence.StackPersistCallback;
import com.ataulm.stacks.persistence.task.UpdateTask;

public class EditStackActivity extends StacksDoneDiscardActivity implements StacksDoneDiscardActivity.DoneDiscardListener, StackPersistCallback {

    public static final String EXTRA_STACK = "com.ataulm.stacks.extra.EXTRA_STACK";
    public static final String EXTRA_UPDATED_STACK = "com.ataulm.stacks.extra.EXTRA_UPDATED_STACK";
    public static final int RESULT_FAILURE = 456;

    private EditText summary;
    private EditText description;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stack);
        summary = (EditText) findViewById(R.id.edittext_summary);
        description = (EditText) findViewById(R.id.edittext_description);

        updateStack();
    }

    private void updateStack() {
        AndroidStack stack = getStack();
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
        AndroidStack stack = AndroidStack.Builder.from(getStack()).summary(summaryText).description(descriptionText).build();
        UpdateTask.newInstance(getContentResolver(), this, stack).execute();
    }

    @Override
    public void onDiscardClick() {
        // TODO: persist in shared prefs with stack id in case of accidental discard
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    private AndroidStack getStack() {
        if (getIntent().hasExtra(EXTRA_STACK)) {
            return getIntent().getParcelableExtra(EXTRA_STACK);
        }
        return AndroidStack.ZERO;
    }

    @Override
    public void onSuccessPersisting(AndroidStack stack) {
        setResult(Activity.RESULT_OK, new Intent().putExtra(EXTRA_UPDATED_STACK, stack));
        finish();
    }

    @Override
    public void onFailurePersisting(AndroidStack stack) {
        setResult(RESULT_FAILURE);
        finish();
    }

}
