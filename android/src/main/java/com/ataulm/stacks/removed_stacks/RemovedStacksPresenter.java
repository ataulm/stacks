package com.ataulm.stacks.removed_stacks;

import android.net.Uri;

import com.ataulm.stacks.Presenter;
import com.ataulm.stacks.R;
import com.ataulm.stacks.navigation.ContentViewSetter;
import com.ataulm.stacks.navigation.Screen;

public class RemovedStacksPresenter implements Presenter {

    private final ContentViewSetter contentViewSetter;
    private RemovedStacksScreenLayout contentView;

    public RemovedStacksPresenter(ContentViewSetter contentViewSetter) {
        this.contentViewSetter = contentViewSetter;
    }

    @Override
    public void start(Uri uri) {
        contentView = contentViewSetter.display(R.layout.view_removed_stacks_screen);
    }

    @Override
    public void stop() {
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public boolean isDisplaying(Screen screen) {
        return screen == Screen.REMOVED_STACKS;
    }

}
