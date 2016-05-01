package com.ataulm.stacks;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ataulm.Event;
import com.ataulm.Optional;
import com.ataulm.stacks.stack.CreateStackUsecase;
import com.ataulm.stacks.stack.FetchStacksUsecase;
import com.ataulm.stacks.stack.Id;
import com.ataulm.stacks.stack.PersistStacksUsecase;
import com.ataulm.stacks.stack.RemoveStackUsecase;
import com.ataulm.stacks.stack.Stack;
import com.ataulm.stacks.stack.Stacks;
import com.ataulm.stacks.view.ViewStackScreen;

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
        Optional<Stack> stack = getStackFrom(getIntent());
        Optional<Id> parentId = stack.isPresent() ? Optional.of(stack.get().id()) : Optional.<Id>absent();

        subscription = fetchStacksUsecase.fetchStacks(parentId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new StacksEventObserver());
    }

    private Optional<Stack> getStackFrom(Intent intent) {
        Bundle extras = intent.getExtras();
        return extras == null ? Optional.<Stack>absent() : new StackBundle().createStackFrom(extras);
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
        intent.putExtras(new StackBundle().createBundleFrom(stack));
        startActivity(intent);
    }

    @Override
    public void onClickEdit(Stack stack) {
        // TODO: edit
    }

    @Override
    public void onClickRemove(Stack stack) {
        removeStackUsecase.remove(stack);
    }

    @Override
    public void onClickAddStack(String summary) {
        Optional<Stack> stack = getStackFrom(getIntent());
        Optional<Id> id = stack.isPresent() ? Optional.of(stack.get().id()) : Optional.<Id>absent();
        createStackUsecase.createStack(id, summary);
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
