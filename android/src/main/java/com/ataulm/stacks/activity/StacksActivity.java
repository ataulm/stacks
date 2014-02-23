package com.ataulm.stacks.activity;

import android.content.Intent;
import android.os.Bundle;

import com.ataulm.stacks.R;
import com.ataulm.stacks.base.StacksBaseActivity;
import com.ataulm.stacks.fragment.EditStackFragment;
import com.ataulm.stacks.fragment.ViewStackFragment;
import com.ataulm.stacks.model.Stack;

public class StacksActivity extends StacksBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stacks);

        String stackId = hasData() ? getStackId() : Stack.ZERO.id;

        if (Intent.ACTION_EDIT.equals(getIntent().getAction())) {
            getFragmentManager().beginTransaction().add(R.id.fragment_container, EditStackFragment.newInstance(stackId)).commit();
        } else {
            getFragmentManager().beginTransaction().add(R.id.fragment_container, ViewStackFragment.newInstance(stackId)).commit();
        }
    }

    private boolean hasData() {
        return getIntent().getData() != null;
    }

    private String getStackId() {
        return getIntent().getData().getLastPathSegment();
    }

}
