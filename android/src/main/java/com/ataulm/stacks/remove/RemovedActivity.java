package com.ataulm.stacks.remove;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;

import com.ataulm.Event;
import com.ataulm.stacks.LoggingObserver;
import com.ataulm.stacks.NavigationDrawerActivity;
import com.ataulm.stacks.R;
import com.ataulm.stacks.stack.FetchStacksUsecase;
import com.ataulm.stacks.stack.PersistStacksUsecase;
import com.ataulm.stacks.stack.RemoveStackUsecase;
import com.ataulm.stacks.stack.Stack;
import com.ataulm.stacks.stack.Stacks;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static com.ataulm.stacks.StacksApplication.*;

public class RemovedActivity extends NavigationDrawerActivity implements RemovedStackItemListener {

    public static void start(Context context) {
        Intent intent = new Intent(context, RemovedActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent, noActivityAnimation(context));
    }

    private static Bundle noActivityAnimation(Context context) {
        return ActivityOptionsCompat.makeCustomAnimation(context, 0, 0).toBundle();
    }

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

    @Override
    public void onClickRestore(Stack stack) {
        removeStackUsecase.unmarkPendingRemove(stack);
    }

    @Override
    public void onClickDelete(Stack stack) {
        removeStackUsecase.remove(stack);
    }

    private class StacksEventObserver extends LoggingObserver<Event<Stacks>> {

        @Override
        public void onNext(Event<Stacks> event) {
            if (event.getData().isPresent() && event.getData().get().size() > 0) {
                removedStacksScreen.showData(event.getData().get(), RemovedActivity.this);
            } else {
                removedStacksScreen.showEmptyScreen();
            }
        }

    }

}
