package com.ataulm.stacks.stacks;

import com.ataulm.Event;
import com.ataulm.Optional;
import com.ataulm.Subscriptions;
import com.ataulm.stacks.ContentViewSetter;
import com.ataulm.stacks.LoggingObserver;
import com.ataulm.stacks.Presenter;
import com.ataulm.stacks.R;
import com.ataulm.stacks.jabber.Jabber;
import com.ataulm.stacks.jabber.Usecases;
import com.ataulm.stacks.navigation.Navigator;
import com.ataulm.stacks.navigation.Screen;
import com.ataulm.stacks.navigation.UriResolver;
import com.ataulm.stacks.stack.Id;
import com.ataulm.stacks.stack.Stack;
import com.ataulm.stacks.stack.Stacks;

import java.net.URI;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class StacksPresenter implements Presenter {

    private final ContentViewSetter contentViewSetter;
    private final UriResolver uriResolver;
    private final Usecases usecases;
    private final ClickActions clickActions;
    private final OnClickNavigationButtonListener onClickNavigationButtonListener;

    private StacksScreenLayout contentView;
    private Subscriptions subscriptions = new Subscriptions();

    public static StacksPresenter create(
            ContentViewSetter contentViewSetter,
            UriResolver uriResolver,
            Usecases usecases,
            final OnClickOpenNavigationDrawerListener navigationDrawerListener,
            final Navigator navigator
    ) {
        ClickActions clickActions = new StackClickActions(navigator, usecases.updateStacks(), usecases.removeStacks());
        OnClickNavigationButtonListener onClickNavigationButtonListener = new OnClickNavigationButtonListener() {
            @Override
            public void onClickNavigationButton(Optional<Stack> stack) {
                if (stack.isPresent()) {
                    Optional<Id> parentId = stack.get().parentId();
                    navigator.navigateUpToStack(parentId);
                } else {
                    navigationDrawerListener.onClickOpenNavigationDrawer();
                }
            }
        };

        return new StacksPresenter(
                contentViewSetter,
                uriResolver,
                usecases,
                clickActions,
                onClickNavigationButtonListener
        );
    }

    private StacksPresenter(
            ContentViewSetter contentViewSetter,
            UriResolver uriResolver,
            Usecases usecases,
            ClickActions clickActions,
            OnClickNavigationButtonListener onClickNavigationButtonListener
    ) {
        this.contentViewSetter = contentViewSetter;
        this.uriResolver = uriResolver;
        this.usecases = usecases;
        this.clickActions = clickActions;
        this.onClickNavigationButtonListener = onClickNavigationButtonListener;
    }

    @Override
    public void start(URI uri) {
        contentView = contentViewSetter.display(R.layout.view_stacks_screen);

        Optional<Id> id = uriResolver.extractIdFrom(uri);
        StackInputListener stackInputListener = createStackInputListener(id);
        contentView.set(stackInputListener);

        subscriptions.add(subscribeToStack(id));
        subscriptions.add(subscribeToChildren(id));
    }

    private StackInputListener createStackInputListener(final Optional<Id> id) {
        return new StackInputListener() {
            @Override
            public void onClickAddStack(String summary) {
                usecases.createStacks().createStack(id, summary);
            }
        };
    }

    private Subscription subscribeToStack(Optional<Id> id) {
        return usecases.fetchStacks().fetchStack(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new StackObserver());
    }

    private Subscription subscribeToChildren(Optional<Id> id) {
        return usecases.fetchStacks().fetchChildrenWithParent(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ChildrenObserver());

    }

    @Override
    public void stop() {
        usecases.persistStacks().persistStacks();
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
            contentView.updateToolbar(stack, onClickNavigationButtonListener);
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
