package com.ataulm.stacks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.ataulm.Event;
import com.ataulm.Optional;
import com.ataulm.stacks.stack.CreateStackUsecase;
import com.ataulm.stacks.stack.FetchStacksUsecase;
import com.ataulm.stacks.stack.PersistStacksUsecase;
import com.ataulm.stacks.stack.RemoveStackUsecase;
import com.ataulm.stacks.stack.Stack;
import com.ataulm.stacks.stack.Stacks;

import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class ViewActivity extends AppCompatActivity implements StackItemListener {

    private final FetchStacksUsecase fetchStacksUsecase;
    private final CreateStackUsecase createStackUsecase;
    private final RemoveStackUsecase removeStackUsecase;
    private final PersistStacksUsecase persistStacksUsecase;

    @Bind(R.id.view_recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.view_stack_edit_text)
    EditText stackEditText;

    @Bind(R.id.view_stack_button_add)
    View addStackButton;

    private Subscription subscription;

    public ViewActivity() {
        this.fetchStacksUsecase = StacksApplication.createFetchStacksUsecase();
        this.createStackUsecase = StacksApplication.createCreateStackUsecase();
        this.removeStackUsecase = StacksApplication.createRemoveStackUsecase();
        this.persistStacksUsecase = StacksApplication.createPersistStacksUsecase();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Optional<Stack> stack = getStackFrom(getIntent());
        if (stack.isPresent()) {
            setTitle(stack.get().summary());
        }

        addStackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String summary = stackEditText.getText().toString();
                if (summary.isEmpty()) {
                    return;
                }
                Optional<Stack> stack = getStackFrom(getIntent());
                Optional<String> id = stack.isPresent() ? Optional.of(stack.get().id()) : Optional.<String>absent();
                createStackUsecase.createStack(id, summary);
                stackEditText.setText(null);
            }
        });
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
        Optional<String> parentId = stack.isPresent() ? Optional.of(stack.get().id()) : Optional.<String>absent();

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
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtras(new StackBundle().createBundleFrom(stack));
        startActivity(intent);
    }

    @Override
    public void onClickRemove(Stack stack) {
        removeStackUsecase.remove(stack);
    }

    private class StacksEventObserver implements Observer<Event<Stacks>> {

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onNext(Event<Stacks> event) {
            Optional<Stack> stack = getStackFrom(getIntent());
            if (event.getData().isPresent() && event.getData().get().size() > 0) {
                showData(stack, event.getData().get(), event.getType());
            } else {
                showEmptyScreen(stack, event.getType());
            }
        }

        private void showEmptyScreen(Optional<Stack> stack, Event.Type type) {
            StacksAdapter adapter = new StacksAdapter(stack, Stacks.create(Collections.<Stack>emptyList()), ViewActivity.this);
            recyclerView.swapAdapter(adapter, false);
        }

        private void showData(Optional<Stack> stack, Stacks stacks, Event.Type type) {
            StacksAdapter adapter = new StacksAdapter(stack, stacks, ViewActivity.this);
            recyclerView.swapAdapter(adapter, false);
        }

    }

}
