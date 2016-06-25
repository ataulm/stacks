package com.ataulm.stacks.stacks;

import android.net.Uri;

import com.ataulm.Optional;
import com.ataulm.stacks.Presenter;
import com.ataulm.stacks.R;
import com.ataulm.stacks.navigation.ContentViewSetter;
import com.ataulm.stacks.navigation.Screen;
import com.ataulm.stacks.navigation.UriResolver;
import com.ataulm.stacks.stack.FetchStacksUsecase;
import com.ataulm.stacks.stack.Id;
import com.ataulm.stacks.stack.ViewStackScreenLayout;

import rx.Subscription;

public class StacksPresenter implements Presenter {

    private final ContentViewSetter contentViewSetter;
    private final UriResolver uriResolver;
    private final FetchStacksUsecase fetchStacksUsecase;

    private ViewStackScreenLayout contentView;
    private Subscription subscription;

    public StacksPresenter(
            ContentViewSetter contentViewSetter,
            UriResolver uriResolver,
            FetchStacksUsecase fetchStacksUsecase
    ) {
        this.contentViewSetter = contentViewSetter;
        this.fetchStacksUsecase = fetchStacksUsecase;
        this.uriResolver = uriResolver;
    }

    @Override
    public void start(Uri uri) {
        contentView = contentViewSetter.display(R.layout.view_stacks_screen);
        Optional<Id> id = uriResolver.extractIdFrom(uri);
        subscription = fetchStacksUsecase.fetchStacks(id).subscribe();
    }

    @Override
    public void stop() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public boolean isDisplaying(Screen screen) {
        return screen == Screen.STACKS;
    }

}
