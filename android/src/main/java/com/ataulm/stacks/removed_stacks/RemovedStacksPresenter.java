package com.ataulm.stacks.removed_stacks;

import com.ataulm.Optional;
import com.ataulm.stacks.ContentViewSetter;
import com.ataulm.stacks.Presenter;
import com.ataulm.stacks.R;
import com.ataulm.stacks.navigation.Screen;

import java.net.URI;

public class RemovedStacksPresenter implements Presenter {

    private final ContentViewSetter contentViewSetter;
    private RemovedStacksScreenLayout contentView;

    public RemovedStacksPresenter(ContentViewSetter contentViewSetter) {
        this.contentViewSetter = contentViewSetter;
    }

    @Override
    public void start(Optional<URI> uri) {
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
