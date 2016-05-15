package com.ataulm.stacks.remove;

import android.os.Bundle;

import com.ataulm.Event;
import com.ataulm.stacks.LoggingObserver;
import com.ataulm.stacks.NavigationDrawerActivity;
import com.ataulm.stacks.R;
import com.ataulm.stacks.stack.FetchStacksUsecase;
import com.ataulm.stacks.stack.PersistStacksUsecase;
import com.ataulm.stacks.stack.RemoveStackUsecase;
import com.ataulm.stacks.stack.Stacks;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static com.ataulm.stacks.StacksApplication.*;

public class RemovedActivity extends NavigationDrawerActivity {

    private final FetchStacksUsecase fetchStacksUsecase = createFetchStacksUsecase();
    private final RemoveStackUsecase removeStackUsecase = createRemoveStackUsecase();
    private final PersistStacksUsecase persistStacksUsecase = createPersistStacksUsecase();

    @Bind(R.id.content)
    RemovedStacksScreen removedStacksScreen;

    private Subscription subscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_removed);
        ButterKnife.bind(this);

        removedStacksScreen.setTitle("Removed Stacks");
    }

    @Override
    protected void onResume() {
        super.onResume();
        subscription = fetchStacksUsecase.fetchStacksPendingRemoval()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new StacksEventObserver());
    }

    @Override
    protected void onPause() {
        super.onPause();
        persistStacksUsecase.persistStacks();
        subscription.unsubscribe();
    }

    private class StacksEventObserver extends LoggingObserver<Event<Stacks>> {

        @Override
        public void onNext(Event<Stacks> event) {
            if (event.getData().isPresent() && event.getData().get().size() > 0) {
                removedStacksScreen.showData(event.getData().get());
            } else {
                removedStacksScreen.showEmptyScreen();
            }
        }

    }

}
