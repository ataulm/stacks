package com.ataulm.stacks.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ataulm.stacks.R;
import com.ataulm.stacks.base.StacksBaseActivity;
import com.ataulm.stacks.fragment.ViewStackFragment;
import com.ataulm.stacks.model.Stack;

public class ViewStackActivity extends StacksBaseActivity {

    public static final String EXTRA_STACK = "com.ataulm.stacks.extra.EXTRA_PARENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stacks);

        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new ViewStackFragment()).commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != R.id.req_code_edit_stack) {
            throw new Error("Received result for a request I didn't make (request: " + requestCode + ")");
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        if (resultCode == Activity.RESULT_OK) {
            Stack editedStack = data.getParcelableExtra(EditStackActivity.EXTRA_UPDATED_STACK);
            getIntent().putExtra(ViewStackActivity.EXTRA_STACK, editedStack);
            toast(R.string.success_updating_stack);
        } else {
            toast(R.string.failed_saving_stack);
        }
    }

}
