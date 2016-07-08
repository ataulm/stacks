package com.ataulm.stacks.navigation;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;

import com.ataulm.Optional;
import com.ataulm.stacks.Log;
import com.ataulm.stacks.stack.Id;

public class Navigator {

    private final Activity activity;
    private final UriCreator uriCreator;

    public Navigator(Activity activity, UriCreator uriCreator) {
        this.activity = activity;
        this.uriCreator = uriCreator;
    }

    public void navigateTo(Screen screen) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uriCreator.createUriToView(screen));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (activity.getIntent().equals(intent)) {
            return;
        }

        Bundle options = ActivityOptionsCompat.makeCustomAnimation(activity, 0, 0).toBundle();
        activity.startActivity(intent, options);
    }

    public void navigateToStack(Id id) {
        Uri uri = uriCreator.createUriToView(Screen.STACKS, id);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(intent);
    }

    public void navigateUpToStack(Optional<Id> id) {
        if (id.isPresent()) {
            navigateUpToStack(id.get());
        } else {
            navigateUpToRootStack();
        }
    }

    // TODO: "activity" transitions on back vs up

    private void navigateUpToStack(Id id) {
        Log.d("navigating UP to stack: " + id);
        navigateToStack(id);
    }

    private void navigateUpToRootStack() {
        Log.d("navigating UP to root stack");
        navigateTo(Screen.STACKS);
    }

    public void navigateBackToStack(Optional<Id> id) {
        if (id.isPresent()) {
            navigateBackToStack(id.get());
        } else {
            navigateBackToRootStack();
        }
    }

    private void navigateBackToStack(Id id) {
        Log.d("navigating BACK to stack: " + id);
        navigateToStack(id);
    }

    private void navigateBackToRootStack() {
        Log.d("navigating BACK to root stack");
        navigateTo(Screen.STACKS);
    }
}
