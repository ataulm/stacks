package com.ataulm.stacks.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;

import com.ataulm.stacks.remove.RemovedActivity;
import com.ataulm.stacks.view.ViewActivity;

class TopLevelActivityNavigator {

    private final Context context;

    TopLevelActivityNavigator(Context context) {
        this.context = context;
    }

    public void navigateToViewStacks() {
        start(ViewActivity.class);
    }

    public void navigateToRemovedStacks() {
        start(RemovedActivity.class);
    }

    private void start(Class<? extends Activity> cls) {
        Intent intent = createIntent(cls);
        startActivityWithNoAnimations(intent);
    }

    private Intent createIntent(Class<? extends Activity> cls) {
        Intent intent = new Intent(context, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    private void startActivityWithNoAnimations(Intent intent) {
        Bundle options = noActivityAnimation();
        context.startActivity(intent, options);
    }

    private Bundle noActivityAnimation() {
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(context, 0, 0);
        return activityOptionsCompat.toBundle();
    }

}
