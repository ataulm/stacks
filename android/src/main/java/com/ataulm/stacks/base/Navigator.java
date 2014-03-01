package com.ataulm.stacks.base;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.ataulm.stacks.model.Stack;
import com.ataulm.stacks.persistence.StacksProvider;

public class Navigator {

    private final Activity activity;

    public Navigator(Activity activity) {
        this.activity = activity;
    }

    public void stack(Stack stack) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.withAppendedPath(StacksProvider.URI_STACKS, stack.id));
        activity.startActivity(intent);
    }

    public void editStack(String stackId) {
        Intent intent = new Intent(Intent.ACTION_EDIT, Uri.withAppendedPath(StacksProvider.URI_STACKS, stackId));
        activity.startActivity(intent);
    }

    public void moveStack(String stackId) {
        Intent intent = new Intent(Intent.ACTION_PICK, Uri.withAppendedPath(StacksProvider.URI_STACKS, stackId));
        activity.startActivity(intent);
    }

}
