package com.ataulm.stacks.stacks;

import android.net.Uri;

import com.ataulm.Event;
import com.ataulm.Optional;
import com.ataulm.Subscriptions;
import com.ataulm.stacks.ContentViewSetter;
import com.ataulm.stacks.LoggingObserver;
import com.ataulm.stacks.Presenter;
import com.ataulm.stacks.R;
import com.ataulm.stacks.jabber.Jabber;
import com.ataulm.stacks.navigation.Navigator;
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

public final class StacksPresenter implements Presenter {

    private final ContentViewSetter contentViewSetter;
    private final UriResolver uriResolver;
    private final FetchStacksUsecase fetchStacksUsecase;
    private final CreateStackUsecase createStackUsecase;
    private final PersistStacksUsecase persistStacksUsecase;
    private final ToolbarActions toolbarActions;
    private final ClickActions clickActions;

    private StacksScreenLayout contentView;
    private Subscriptions subscriptions = new Subscriptions();

    public static StacksPresenter create(
            ContentViewSetter contentViewSetter,
            UriResolver uriResolver,
            FetchStacksUsecase fetchStacksUsecase,
            CreateStackUsecase createStackUsecase,
            PersistStacksUsecase persistStacksUsecase,
            ToolbarActions toolbarActions,
            Navigator navigator
    ) {
        ClickActions clickActions = new StackClickActions(navigator);
        return new StacksPresenter(
                contentViewSetter,
                uriResolver,
                fetchStacksUsecase,
                createStackUsecase,
                persistStacksUsecase,
                toolbarActions,
                clickActions
        );
    }

    private StacksPresenter(
            ContentViewSetter contentViewSetter,
            UriResolver uriResolver,
            FetchStacksUsecase fetchStacksUsecase,
            CreateStackUsecase createStackUsecase,
            PersistStacksUsecase persistStacksUsecase,
            ToolbarActions toolbarActions,
            ClickActions clickActions
    ) {
        this.contentViewSetter = contentViewSetter;
        this.fetchStacksUsecase = fetchStacksUsecase;
        this.uriResolver = uriResolver;
        this.createStackUsecase = createStackUsecase;
        this.persistStacksUsecase = persistStacksUsecase;
        this.toolbarActions = toolbarActions;
        this.clickActions = clickActions;
    }

    @Override
    public void start(Uri uri) {
        contentView = contentViewSetter.display(R.layout.view_stacks_screen);
        final Optional<Id> id = uriResolver.extractIdFrom(uri);

        StackInputListener stackInputListener = createStackInputListener(id);
        contentView.set(stackInputListener);

        subscriptions.add(subscribeToStack(id));
        subscriptions.add(subscribeToChildren(id));
    }

    private StackInputListener createStackInputListener(final Optional<Id> id) {
        return new StackInputListener() {
            @Override
            public void onClickAddStack(String summary) {
                createStackUsecase.createStack(id, summary);
            }
        };
    }

    private Subscription subscribeToStack(Optional<Id> id) {
        return fetchStacksUsecase.fetchStack(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new StackObserver());
    }

    private Subscription subscribeToChildren(Optional<Id> id) {
        return fetchStacksUsecase.fetchChildrenWithParent(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ChildrenObserver());

    }

    @Override
    public void stop() {
        persistStacksUsecase.persistStacks();
        subscriptions.unsubscribe();
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public boolean isDisplaying(Screen screen) {
        return screen == Screen.STACKS;
    }

    private class StackObserver extends LoggingObserver<Event<Optional<Stack>>> {

        @Override
        public void onNext(Event<Optional<Stack>> event) {
            super.onNext(event);
            switch (event.getType()) {
                case LOADING:
                    break;
                case ERROR:
                    Jabber.toast("uh oh, error");
                    break;
                case IDLE:
                    update(event);
                    break;
                default:
                    throw new IllegalArgumentException("unknown type: " + event.getType());
            }
        }

        private void update(Event<Optional<Stack>> event) {
            if (event.getData().isPresent()) {
                Optional<Stack> stack = event.getData().get();
                updateToolbar(stack);
            }
        }

        private void updateToolbar(Optional<Stack> stack) {
            contentView.updateToolbar(stack, toolbarActions);
        }

    }

    private class ChildrenObserver extends LoggingObserver<Event<Stacks>> {

        @Override
        public void onNext(Event<Stacks> event) {
            super.onNext(event);
            switch (event.getType()) {
                case LOADING:
                    break;
                case ERROR:
                    Jabber.toast("uh oh, error");
                    break;
                case IDLE:
                    update(event);
                    break;
                default:
                    throw new IllegalArgumentException("unknown type: " + event.getType());
            }
        }

        private void update(Event<Stacks> event) {
            if (event.getData().isPresent()) {
                Stacks stacks = event.getData().get();
                contentView.update(stacks, clickActions);
            }
        }

    }
}
