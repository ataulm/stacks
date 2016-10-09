package com.ataulm.stacks.navigation;

import com.ataulm.Optional;
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
    public void start(Optional<URI> uri) {
        activePresenter = getPresenterMatching(uri);
        activePresenter.start(uri);
    }

    private Presenter getPresenterMatching(Optional<URI> uri) {
        Screen screen = getScreenMatching(uri);
        for (Presenter presenter : presenters) {
            if (presenter.isDisplaying(screen)) {
                return presenter;
            }
        }
        throw new IllegalArgumentException("no presenters can display: " + uri);
    }

    private Screen getScreenMatching(Optional<URI> uriOptional) {
        if (!uriOptional.isPresent() || uriResolver.matches(uriOptional.get(), Screen.STACKS)) {
            return Screen.STACKS;
        }

        URI uriActual = uriOptional.get();
        if (uriResolver.matches(uriActual, Screen.REMOVED_STACKS)) {
            return Screen.REMOVED_STACKS;
        }

        throw new IllegalArgumentException("no screen matching uri: " + uriActual);
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
