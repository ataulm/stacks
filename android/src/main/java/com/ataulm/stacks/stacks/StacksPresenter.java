package com.ataulm.stacks.stacks;

import com.ataulm.Optional;
import com.ataulm.Subscriptions;
import com.ataulm.stacks.ContentViewSetter;
import com.ataulm.stacks.Presenter;
import com.ataulm.stacks.R;
import com.ataulm.stacks.jabber.Usecases;
import com.ataulm.stacks.navigation.Navigator;
import com.ataulm.stacks.navigation.Screen;
import com.ataulm.stacks.navigation.UriResolver;
import com.ataulm.stacks.stack.Id;

import java.net.URI;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class StacksPresenter implements Presenter {

    private final ContentViewSetter contentViewSetter;
    private final UriResolver uriResolver;
    private final Usecases usecases;
    private final UserItemActions userItemActions;
    private final OnClickNavigationButtonListener onClickNavigationButtonListener;

    private StacksScreenLayout contentView;
    private Subscriptions subscriptions = new Subscriptions();

    public static StacksPresenter create(
            ContentViewSetter contentViewSetter,
            UriResolver uriResolver,
            Usecases usecases,
            Navigator navigator,
            OnClickNavigationButtonListener onClickNavigationButtonListener
    ) {
        UserItemActions userItemActions = new StackUserItemActions(navigator, usecases.updateStacks(), usecases.removeStacks());

        return new StacksPresenter(
                contentViewSetter,
                uriResolver,
                usecases,
                userItemActions,
                onClickNavigationButtonListener
        );
    }

    private StacksPresenter(
            ContentViewSetter contentViewSetter,
            UriResolver uriResolver,
            Usecases usecases,
            UserItemActions userItemActions,
            OnClickNavigationButtonListener onClickNavigationButtonListener
    ) {
        this.contentViewSetter = contentViewSetter;
        this.uriResolver = uriResolver;
        this.usecases = usecases;
        this.userItemActions = userItemActions;
        this.onClickNavigationButtonListener = onClickNavigationButtonListener;
    }

    @Override
    public void start(Optional<URI> uri) {
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
                .subscribe(new StackObserver(contentView, onClickNavigationButtonListener));
    }

    private Subscription subscribeToChildren(Optional<Id> id) {
        return usecases.fetchStacks().fetchChildrenWithParent(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ChildrenObserver(contentView, userItemActions));

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

}
