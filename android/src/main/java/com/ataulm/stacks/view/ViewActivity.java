package com.ataulm.stacks.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ataulm.Event;
import com.ataulm.Optional;
import com.ataulm.stacks.LoggingObserver;
import com.ataulm.stacks.NavigationDrawerActivity;
import com.ataulm.stacks.R;
import com.ataulm.stacks.StackBundleConverter;
import com.ataulm.stacks.StackInputListener;
import com.ataulm.stacks.StackItemListener;
import com.ataulm.stacks.stack.CreateStackUsecase;
import com.ataulm.stacks.stack.FetchStacksUsecase;
import com.ataulm.stacks.stack.Id;
import com.ataulm.stacks.stack.PersistStacksUsecase;
import com.ataulm.stacks.stack.RemoveStackUsecase;
import com.ataulm.stacks.stack.Stack;
import com.ataulm.stacks.stack.Stacks;
import com.ataulm.stacks.stack.UpdateStackUsecase;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static com.ataulm.stacks.StacksApplication.*;

public class ViewActivity extends NavigationDrawerActivity implements StackItemListener, StackInputListener {

    private final FetchStacksUsecase fetchStacksUsecase = createFetchStacksUsecase();
    private final CreateStackUsecase createStackUsecase = createCreateStackUsecase();
    private final RemoveStackUsecase removeStackUsecase = createRemoveStackUsecase();
    private final PersistStacksUsecase persistStacksUsecase = createPersistStacksUsecase();
    private final UpdateStackUsecase updateStacksUsecase = createUpdateStackUsecase();

    @Bind(R.id.content)
    ViewStackScreen viewStackScreen;

    private Subscription subscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view);
        ButterKnife.bind(this);

        Optional<Stack> stack = getStackFrom(getIntent());
        if (stack.isPresent()) {
            viewStackScreen.setTitle(stack.get().summary());
        } else {
            viewStackScreen.setTitle("Stacks");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getStackFrom(getIntent()).isPresent()) {
            getMenuInflater().inflate(R.menu.view_stack, menu);
            return true;
        } else {
            return super.onCreateOptionsMenu(menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit:
                onClickEdit(getStackFrom(getIntent()).get());
                return true;
            case R.id.menu_remove:
                onClickRemove(getStackFrom(getIntent()).get());
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
        Intent intent = new Intent(this, ViewActivity.class);
        intent.putExtras(new StackBundleConverter().createBundleFrom(stack));
        startActivity(intent);
    }

    @Override
    public void onClickEdit(Stack stack) {
        // TODO: edit
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
            if (event.getData().isPresent() && event.getData().get().size() > 0) {
                viewStackScreen.showData(event.getData().get(), ViewActivity.this, ViewActivity.this);
            } else {
                viewStackScreen.showEmptyScreen(ViewActivity.this, ViewActivity.this);
            }
        }

    }

}
