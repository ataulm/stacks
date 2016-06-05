package com.ataulm.stacks.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;

import com.ataulm.stacks.remove.RemovedActivity;

class TopLevelActivityNavigator {

    private final Context context;
    private final UriCreator uriCreator;

    TopLevelActivityNavigator(Context context) {
        this.context = context;
        this.uriCreator = UriCreator.create(context);
    }

    public void navigateToViewStacks() {
        view(Screen.STACKS_LIST);
    }

    private void view(Screen screen) {
        Uri uri = uriCreator.createUriToView(screen);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityWithNoAnimations(intent);
    }

    public void navigateToRemovedStacks() {
        view(Screen.REMOVED_STACKS);
    }

    private void startActivityWithNoAnimations(Intent intent) {
        Bundle options = noActivityAnimation();
        context.startActivity(intent, options);
    }

    private Bundle noActivityAnimation() {
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(context, 0, 0);
        return activityOptionsCompat.toBundle();
    }

    private static class UriCreator {

        private final String authority;

        public static UriCreator create(Context context) {
            return new UriCreator(context.getPackageName());
        }

        UriCreator(String authority) {
            this.authority = authority;
        }

        public Uri createUriToView(Screen screen) {
            String uri = String.format("content://%1$s/screen/%2$s", authority, screen.getPath());
            return Uri.parse(uri);
        }

    }

}
