package com.ataulm.stacks;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.view.ViewGroup;

public class ActivityContentViewSetter implements ContentViewSetter {

    private final Activity activity;

    public ActivityContentViewSetter(Activity activity) {
        this.activity = activity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T display(@LayoutRes int layout) {
        activity.setContentView(layout);
        return (T) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
    }

}
