package com.ataulm.stacks.navigation;

import android.support.annotation.Nullable;

import com.ataulm.stacks.Presenter;

import java.net.URI;
import java.util.Collection;

class TopLevelPresenter implements Presenter {

    private final UriResolver uriResolver;
    private final Collection<Presenter> presenters;

    private Presenter activePresenter;

    TopLevelPresenter(UriResolver uriResolver, Collection<Presenter> presenters) {
        this.uriResolver = uriResolver;
        this.presenters = presenters;
    }

    @Override
    public void start(@Nullable URI uri) {
        activePresenter = getPresenterMatching(uri);
        activePresenter.start(uri);
    }

    private Presenter getPresenterMatching(@Nullable URI uri) {
        Screen screen = getScreenMatching(uri);
        for (Presenter presenter : presenters) {
            if (presenter.isDisplaying(screen)) {
                return presenter;
            }
        }
        throw new IllegalArgumentException("no presenters can display: " + uri);
    }

    private Screen getScreenMatching(@Nullable URI uri) {
        if (uri == null || uriResolver.matches(uri, Screen.STACKS)) {
            return Screen.STACKS;
        }

        if (uriResolver.matches(uri, Screen.REMOVED_STACKS)) {
            return Screen.REMOVED_STACKS;
        }

        throw new IllegalArgumentException("no screen matching uri: " + uri);
    }

    @Override
    public void stop() {
        activePresenter.stop();
    }

    @Override
    public boolean onBackPressed() {
        return activePresenter.onBackPressed();
    }

    @Override
    public boolean isDisplaying(Screen screen) {
        return activePresenter.isDisplaying(screen);
    }

}
