package com.ataulm.stacks.stack;

import com.ataulm.Event;
import com.ataulm.EventRxFunctions;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class SyncFetchStacksUsecase implements FetchStacksUsecase {

    private final StackOperations stackOperations;

    public SyncFetchStacksUsecase(StackOperations stackOperations) {
        this.stackOperations = stackOperations;
    }

    @Override
    public Observable<Event<Stacks>> fetchStacks() {
        return Observable.create(new Observable.OnSubscribe<Stacks>() {
            @Override
            public void call(Subscriber<? super Stacks> subscriber) {
                List<Stack> stacksList = stackOperations.getStacks();
                Stacks stacks = Stacks.create(stacksList);
                subscriber.onNext(stacks);
                subscriber.onCompleted();
            }
        }).compose(EventRxFunctions.<Stacks>asEvents());
    }

}
