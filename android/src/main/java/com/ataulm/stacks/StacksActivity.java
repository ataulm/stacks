package com.ataulm.stacks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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

public class StacksActivity extends AppCompatActivity implements StackItemListener {

    private static final String INTENT_EXTRA_SUMMARY = "summary";
    private static final String INTENT_EXTRA_PARENT_ID = "parent_id";

    private final FetchStacksUsecase fetchStacksUsecase;
    private final CreateStackUsecase createStackUsecase;
    private final RemoveStackUsecase removeStackUsecase;
    private final PersistStacksUsecase persistStacksUsecase;

    @Bind(R.id.stacks_recycler_view)
    RecyclerView recyclerView;

    private Subscription subscription;
    private int count;

    public StacksActivity() {
        this.fetchStacksUsecase = StacksApplication.createFetchStacksUsecase();
        this.createStackUsecase = StacksApplication.createCreateStackUsecase();
        this.removeStackUsecase = StacksApplication.createRemoveStackUsecase();
        this.persistStacksUsecase = StacksApplication.createPersistStacksUsecase();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stacks);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final Optional<String> summary = getSummaryFrom(getIntent());
        if (summary.isPresent()) {
            setTitle(summary.get());
        }

        ButterKnife.findById(this, R.id.stacks_debug_add_stack_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createStackUsecase.createStack(getParentIdFrom(getIntent()), "test " + ++count);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Optional<String> parentId = getParentIdFrom(getIntent());
        subscription = fetchStacksUsecase.fetchStacks(parentId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new StacksEventObserver());
    }

    private static Optional<String> getSummaryFrom(Intent intent) {
        String id = intent.getStringExtra(INTENT_EXTRA_SUMMARY);
        return Optional.from(id);
    }

    private static Optional<String> getParentIdFrom(Intent intent) {
        String id = intent.getStringExtra(INTENT_EXTRA_PARENT_ID);
        return Optional.from(id);
    }

    @Override
    protected void onPause() {
        super.onPause();
        persistStacksUsecase.persistStacks();
        subscription.unsubscribe();
    }

    @Override
    public void onClick(Stack stack) {
        Intent intent = new Intent(this, StacksActivity.class);
        intent.putExtra(INTENT_EXTRA_PARENT_ID, stack.id());
        intent.putExtra(INTENT_EXTRA_SUMMARY, stack.summary());
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
            if (event.getData().isPresent() && event.getData().get().size() > 0) {
                showData(event.getData().get(), event.getType());
            } else {
                showEmptyScreen(event.getType());
            }
        }

        private void showEmptyScreen(Event.Type type) {
            recyclerView.swapAdapter(new StacksAdapter(Stacks.create(Collections.<Stack>emptyList()), StacksActivity.this), false);
        }

        private void showData(Stacks stacks, Event.Type type) {
            recyclerView.swapAdapter(new StacksAdapter(stacks, StacksActivity.this), false);
        }

    }

}
