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
import com.ataulm.stacks.stack.CreateStackUsecase;
import com.ataulm.stacks.stack.FetchStacksUsecase;
import com.ataulm.stacks.stack.Id;
import com.ataulm.stacks.stack.PersistStacksUsecase;
import com.ataulm.stacks.stack.Stack;
import com.ataulm.stacks.stack.Stacks;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class StacksPresenter implements Presenter {

    private final ContentViewSetter contentViewSetter;
    private final UriResolver uriResolver;
    private final FetchStacksUsecase fetchStacksUsecase;
    private final CreateStackUsecase createStackUsecase;
    private final PersistStacksUsecase persistStacksUsecase;
    private final ToolbarActions toolbarActions;

    private StacksScreenLayout contentView;
    private Subscription subscription;

    public StacksPresenter(
            ContentViewSetter contentViewSetter,
            UriResolver uriResolver,
            FetchStacksUsecase fetchStacksUsecase,
            CreateStackUsecase createStackUsecase,
            PersistStacksUsecase persistStacksUsecase,
            ToolbarActions toolbarActions
    ) {
        this.contentViewSetter = contentViewSetter;
        this.fetchStacksUsecase = fetchStacksUsecase;
        this.uriResolver = uriResolver;
        this.createStackUsecase = createStackUsecase;
        this.persistStacksUsecase = persistStacksUsecase;
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
        persistStacksUsecase.persistStacks();
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

        private void update(Event<Stacks> stacksEvent) {
            Optional<Stacks> data = stacksEvent.getData();
            final Optional<Id> stackId = getStackIdFrom(data);

            StackInputListener inputListener = new StackInputListener() {
                @Override
                public void onClickAddStack(String summary) {
                    createStackUsecase.createStack(stackId, summary);
                }
            };

            if (data.isPresent()) {
                Stacks stacks = data.get();
                contentView.update(stacks, toolbarActions, inputListener);
            }
        }

        private Optional<Id> getStackIdFrom(Optional<Stacks> data) {
            if (data.isPresent()) {
                Optional<Stack> info = data.get().info();
                return info.isPresent() ? info.get().parentId() : Optional.<Id>absent();
            } else {
                return Optional.absent();
            }
        }

    }

}