package com.ataulm.stacks.stack;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class SyncFetchStacksUsecase implements FetchStacksUsecase {

    private final StackOperations stackOperations;

    public SyncFetchStacksUsecase(StackOperations stackOperations) {
        this.stackOperations = stackOperations;
    }

    @Override
    public Observable<List<Stack>> fetchStacks() {
        return Observable.create(new Observable.OnSubscribe<List<Stack>>() {
            @Override
            public void call(Subscriber<? super List<Stack>> subscriber) {
                List<Stack> stacks = stackOperations.getStacks();
                subscriber.onNext(stacks);
                subscriber.onCompleted();
            }
        });
    }

}
