package com.ataulm.stacks.navigation;

import android.support.annotation.LayoutRes;

import com.ataulm.stacks.R;

public enum Screen {

    STACKS(R.layout.view_stacks_screen, "stacks"),
    REMOVED_STACKS(R.layout.view_removed_stacks_screen, "removed");

    @LayoutRes
    private final int layout;
    private final String path;

    Screen(int layout, String path) {
        this.path = path;
        this.layout = layout;
    }

    @LayoutRes
    public int getLayout() {
        return layout;
    }

    public String getPath() {
        return path;
    }

}
