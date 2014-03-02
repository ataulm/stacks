package com.ataulm.stacks.base;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.ataulm.stacks.activity.EditStackActivity;
import com.ataulm.stacks.activity.MoveStacksActivity;
import com.ataulm.stacks.activity.ViewStackActivity;
import com.ataulm.stacks.model.Stack;
import com.ataulm.stacks.persistence.StacksProvider;

import java.util.ArrayList;
import java.util.Arrays;

public class Navigator {

    private final Activity activity;

    public Navigator(Activity activity) {
        this.activity = activity;
    }

    public void stack(Stack stack) {
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setData(Uri.withAppendedPath(StacksProvider.URI_STACKS, stack.id))
                .putExtra(ViewStackActivity.EXTRA_STACK, stack);

        activity.startActivity(intent);
    }

    public void editStack(Stack stack, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_EDIT)
                .setData(Uri.withAppendedPath(StacksProvider.URI_STACKS, stack.id))
                .putExtra(EditStackActivity.EXTRA_STACK, stack);

        activity.startActivityForResult(intent, requestCode);
    }

    public void pickNewParentForStack(Stack parent, Stack... stacks) {
        Intent intent = new Intent(Intent.ACTION_PICK)
                .setData(Uri.withAppendedPath(StacksProvider.URI_STACKS, parent.id))
                .putExtra(MoveStacksActivity.EXTRA_PARENT, parent)
                .putParcelableArrayListExtra(MoveStacksActivity.EXTRA_STACKS_TO_MOVE,
                        new ArrayList<Stack>(Arrays.asList(stacks)));

        activity.startActivity(intent);
    }

}
