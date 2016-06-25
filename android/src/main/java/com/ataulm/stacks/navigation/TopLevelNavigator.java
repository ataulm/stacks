package com.ataulm.stacks.navigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;

class TopLevelNavigator {

    private final Activity activity;
    private final UriCreator uriCreator;

    TopLevelNavigator(Activity activity, UriCreator uriCreator) {
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

}
