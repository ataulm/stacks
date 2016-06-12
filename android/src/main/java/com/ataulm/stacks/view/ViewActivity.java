package com.ataulm.stacks.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ataulm.Event;
import com.ataulm.Optional;
import com.ataulm.stacks.LoggingObserver;
import com.ataulm.stacks.R;
import com.ataulm.stacks.StackBundleConverter;
import com.ataulm.stacks.StackInputListener;
import com.ataulm.stacks.StackItemListener;
import com.ataulm.stacks.navigation.NavigationDrawerActivity;
import com.ataulm.stacks.stack.CreateStackUsecase;
import com.ataulm.stacks.stack.FetchStacksUsecase;
import com.ataulm.stacks.stack.Id;
import com.ataulm.stacks.stack.PersistStacksUsecase;
import com.ataulm.stacks.stack.RemoveStackUsecase;
import com.ataulm.stacks.stack.Stack;
import com.ataulm.stacks.stack.Stacks;
import com.ataulm.stacks.stack.UpdateStackUsecase;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static com.ataulm.stacks.StacksApplication.*;

public class ViewActivity extends NavigationDrawerActivity implements StackItemListener, StackInputListener {

    public static void start(Context context, Stack stack) {
        Intent intent = createIntent(context, stack);
        context.startActivity(intent);
    }

    private static Intent createIntent(Context context, Stack stack) {
        Intent intent = new Intent(context, ViewActivity.class);
        intent.putExtras(new StackBundleConverter().createBundleFrom(stack));
        return intent;
    }

    private final FetchStacksUsecase fetchStacksUsecase = createFetchStacksUsecase();
    private final CreateStackUsecase createStackUsecase = createCreateStackUsecase();
    private final RemoveStackUsecase removeStackUsecase = createRemoveStackUsecase();
    private final PersistStacksUsecase persistStacksUsecase = createPersistStacksUsecase();
    private final UpdateStackUsecase updateStacksUsecase = createUpdateStackUsecase();

    @BindView(R.id.content)
    ViewStackScreen viewStackScreen;

    private Subscription subscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view);
        ButterKnife.bind(this);

        Optional<Stack> stack = getStackFrom(getIntent());
        Optional<Id> parentId = getThisStacksParentId();
        if (stack.isPresent()) {
            viewStackScreen.setupToolbar(stack.get().summary(), parentId, this);
        } else {
            viewStackScreen.setupToolbar("Stacks", parentId, this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Optional<Id> parentId = getThisStacksParentId();

        subscription = fetchStacksUsecase.fetchStacks(parentId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new StacksEventObserver());
    }

    private Optional<Stack> getStackFrom(Intent intent) {
        Bundle extras = intent.getExtras();
        return extras == null ? Optional.<Stack>absent() : new StackBundleConverter().createStackFrom(extras);
    }

    @Override
    protected void onPause() {
        super.onPause();
        persistStacksUsecase.persistStacks();
        subscription.unsubscribe();
    }

    @Override
    public void onClick(Stack stack) {
        ViewActivity.start(this, stack);
    }

    @Override
    public void onClickMarkComplete(Stack stack) {
        updateStacksUsecase.markCompleted(stack);
    }

    @Override
    public void onClickMarkNotComplete(Stack stack) {
        updateStacksUsecase.markNotCompleted(stack);
    }

    @Override
    public void onClickRemove(Stack stack) {
        removeStackUsecase.markPendingRemove(stack);
    }

    @Override
    public void onClickAddStack(String summary) {
        Optional<Id> parentId = getThisStacksParentId();
        createStackUsecase.createStack(parentId, summary);
    }

    @Override
    public void onClickAddStackCompleted(String summary) {
        Optional<Id> parentId = getThisStacksParentId();
        createStackUsecase.createStackCompleted(parentId, summary);
    }

    private Optional<Id> getThisStacksParentId() {
        Optional<Stack> stack = getStackFrom(getIntent());
        return stack.isPresent() ? Optional.of(stack.get().id()) : Optional.<Id>absent();
    }

    private class StacksEventObserver extends LoggingObserver<Event<Stacks>> {

        @Override
        public void onNext(Event<Stacks> event) {
            Optional<Id> parent = getThisStacksParentId();
            if (event.getData().isPresent() && event.getData().get().size() > 0) {
                viewStackScreen.showData(event.getData().get(), parent, ViewActivity.this, ViewActivity.this, ViewActivity.this);
            } else {
                viewStackScreen.showEmptyScreen(parent, ViewActivity.this, ViewActivity.this);
            }
        }

    }

}
