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
    private final OnClickOpenNavigationDrawerListener navigationDrawerListener;
    private final ClickActions clickActions;
    private final BackAndUp backAndUpListener;
    private final PreviouslyViewedStacks previouslyViewedStacks;

    private StacksScreenLayout contentView;
    private Subscriptions subscriptions = new Subscriptions();

    public static StacksPresenter create(
            ContentViewSetter contentViewSetter,
            UriResolver uriResolver,
            Usecases usecases,
            OnClickOpenNavigationDrawerListener navigationDrawerListener, // TODO: resolve ToolbarActions with BackAndUp
            Navigator navigator,
            PreviouslyViewedStacks previouslyViewedStacks
    ) {
        ClickActions clickActions = new StackClickActions(navigator, usecases.updateStacks(), usecases.removeStacks());
        BackAndUp backAndUp = new BackAndUp(previouslyViewedStacks, navigator);

        return new StacksPresenter(
                contentViewSetter,
                uriResolver,
                usecases,
                navigationDrawerListener,
                clickActions,
                backAndUp,
                previouslyViewedStacks
        );
    }

    private StacksPresenter(
            ContentViewSetter contentViewSetter,
            UriResolver uriResolver,
            Usecases usecases,
            OnClickOpenNavigationDrawerListener navigationDrawerListener,
            ClickActions clickActions,
            BackAndUp backAndUpListener,
            PreviouslyViewedStacks previouslyViewedStacks
    ) {
        this.contentViewSetter = contentViewSetter;
        this.uriResolver = uriResolver;
        this.usecases = usecases;
        this.navigationDrawerListener = navigationDrawerListener;
        this.clickActions = clickActions;
        this.backAndUpListener = backAndUpListener;
        this.previouslyViewedStacks = previouslyViewedStacks;
    }

    @Override
    public void start(URI uri) {
        Optional<Id> id = uriResolver.extractIdFrom(uri);
        if (!previouslyViewedStacks.lastViewedStackIs(id)) {
            previouslyViewedStacks.add(id);
        }

        contentView = contentViewSetter.display(R.layout.view_stacks_screen);

        StackInputListener stackInputListener = createStackInputListener(id);
        contentView.set(stackInputListener);

        subscriptions.add(subscribeToStack(id));
        subscriptions.add(subscribeToChildren(id));
    }

    private static class BackAndUp implements OnClickNavigateUpToParentListener {

        private final PreviouslyViewedStacks previouslyViewedStacks;
        private final Navigator navigator;

        BackAndUp(PreviouslyViewedStacks previouslyViewedStacks, Navigator navigator) {
            this.previouslyViewedStacks = previouslyViewedStacks;
            this.navigator = navigator;
        }

        /**
         * @return true if back consumed
         */
        public boolean onClickBack() {
            if (previouslyViewedStacks.noPreviousStackIds()) {
                // this is either the root stack or arrived here via deeplink
                return false;
            }
            Optional<Id> previousStackId = previouslyViewedStacks.getPenultimateStackIdThenRemoveLastId();
            navigator.navigateBackToStack(previousStackId);
            return true;
        }

        private void navigateUpToParentOf(Stack stack) {
            navigator.navigateUpToStack(stack.parentId());
        }

        @Override
        public void onClickNavigateUpToParentOf(Stack stack) {
            navigateUpToParentOf(stack);
        }
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
        return backAndUpListener.onClickBack();
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
            contentView.updateToolbar(stack, backAndUpListener, navigationDrawerListener);
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
