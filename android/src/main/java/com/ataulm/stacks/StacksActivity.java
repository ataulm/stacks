package com.ataulm.stacks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ataulm.Event;
import com.ataulm.stacks.stack.FetchStacksUsecase;
import com.ataulm.stacks.stack.Stacks;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class StacksActivity extends AppCompatActivity {

    private final FetchStacksUsecase fetchStacksUsecase;
    private Subscription subscription;
    private RecyclerView recyclerView;

    public StacksActivity() {
        this.fetchStacksUsecase = StacksApplication.createFetchStacksUsecase();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my);
        recyclerView = (RecyclerView) findViewById(R.id.stacks_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        View addButton = findViewById(R.id.stacks_debug_add_stack_button);
        assert addButton != null;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add debug stack
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        subscription = fetchStacksUsecase.fetchStacks()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new StacksEventObserver());
    }

    @Override
    protected void onStop() {
        super.onStop();
        subscription.unsubscribe();
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
            if (event.getData().isPresent()) {
                showData(event.getData().get(), event.getType());
            } else {
                showEmptyScreen(event.getType());
            }
        }

        private void showEmptyScreen(Event.Type type) {

        }

        private void showData(Stacks stacks, Event.Type type) {
            recyclerView.swapAdapter(new StacksAdapter(stacks), false);
        }

    }

}
