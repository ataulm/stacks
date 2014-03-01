package com.ataulm.stacks.base;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.ataulm.stacks.R;
import com.ataulm.stacks.activity.EditStackActivity;
import com.ataulm.stacks.activity.MoveStackActivity;
import com.ataulm.stacks.activity.ViewStackActivity;
import com.ataulm.stacks.model.Stack;
import com.ataulm.stacks.persistence.StacksProvider;

public class Navigator {

    private final Activity activity;

    public Navigator(Activity activity) {
        this.activity = activity;
    }

    public void stack(Stack stack) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.withAppendedPath(StacksProvider.URI_STACKS, stack.id));
        intent.putExtra(ViewStackActivity.EXTRA_STACK, stack);
        activity.startActivity(intent);
    }

    public void editStack(Stack stack) {
        Intent intent = new Intent(Intent.ACTION_EDIT, Uri.withAppendedPath(StacksProvider.URI_STACKS, stack.id));
        intent.putExtra(EditStackActivity.EXTRA_STACK, stack);
        activity.startActivity(intent);
    }

    public void pickNewParentForStack(String currentParent, String... stackId) {
        Intent intent = new Intent(Intent.ACTION_PICK).setType(StacksProvider.MIME_STACK);
        intent.putExtra(MoveStackActivity.EXTRA_MOVE_STACKS_TO_MOVE, stackId);
        intent.putExtra(MoveStackActivity.EXTRA_MOVE_CURRENT_PARENT, currentParent);

        activity.startActivityForResult(intent, R.id.req_code_move_stack);
    }

}
