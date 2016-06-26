package com.ataulm.stacks.stacks;

import android.net.Uri;

import com.ataulm.Event;
import com.ataulm.Optional;
import com.ataulm.stacks.LoggingObserver;
import com.ataulm.stacks.Presenter;
import com.ataulm.stacks.R;
import com.ataulm.stacks.jabber.Jabber;
import com.ataulm.stacks.navigation.ContentViewSetter;
import com.ataulm.stacks.navigation.Screen;
import com.ataulm.stacks.navigation.UriResolver;
import com.ataulm.stacks.stack.FetchStacksUsecase;
import com.ataulm.stacks.stack.Id;
import com.ataulm.stacks.stack.Stacks;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class StacksPresenter implements Presenter {

    private final ContentViewSetter contentViewSetter;
    private final UriResolver uriResolver;
    private final FetchStacksUsecase fetchStacksUsecase;
    private final ToolbarActions toolbarActions;

    private StacksScreenLayout contentView;
    private Subscription subscription;

    public StacksPresenter(
            ContentViewSetter contentViewSetter,
            UriResolver uriResolver,
            FetchStacksUsecase fetchStacksUsecase,
            ToolbarActions toolbarActions) {
        this.contentViewSetter = contentViewSetter;
        this.fetchStacksUsecase = fetchStacksUsecase;
        this.uriResolver = uriResolver;
        this.toolbarActions = toolbarActions;
    }

    @Override
    public void start(Uri uri) {
        contentView = contentViewSetter.display(R.layout.view_stacks_screen);
        Optional<Id> id = uriResolver.extractIdFrom(uri);
        subscription = fetchStacksUsecase.fetchStacks(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new StacksObserver());
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

    private class StacksObserver extends LoggingObserver<Event<Stacks>> {

        @Override
        public void onNext(Event<Stacks> stacks) {
            super.onNext(stacks);
            switch (stacks.getType()) {
                case LOADING:
                    Jabber.toast("hang on, loading");
                    break;
                case ERROR:
                    Jabber.toast("uh oh, error");
                    break;
                case IDLE:
                    update(stacks);
                    Jabber.toast("up to date");
                    break;
            }
        }

        private void update(Event<Stacks> stacks) {
            StackInputListener inputListener = new StackInputListener() {
                @Override
                public void onClickAddStack(String summary) {
                    Jabber.toast("click add: " + summary);
                }

                @Override
                public void onClickAddStackCompleted(String summary) {
                    Jabber.toast("click add completed: " + summary);
                }
            };

            Optional<Stacks> data = stacks.getData();
            if (data.isPresent()) {
                contentView.update(data.get(), toolbarActions, inputListener);
            }
        }

    }

}
