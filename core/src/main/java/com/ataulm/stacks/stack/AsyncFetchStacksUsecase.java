package com.ataulm.stacks.stack;

import com.ataulm.Event;
import com.ataulm.Optional;

import rx.Observable;
import rx.schedulers.Schedulers;

public class AsyncFetchStacksUsecase implements FetchStacksUsecase {

    private final SyncFetchStacksUsecase syncUsecase;

    public AsyncFetchStacksUsecase(SyncFetchStacksUsecase syncUsecase) {
        this.syncUsecase = syncUsecase;
    }

    @Override
    public Observable<Event<Stacks>> fetchStacks(Optional<Id> parentId) {
        return syncUsecase.fetchStacks(parentId).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Event<Stacks>> fetchStacksPendingRemoval() {
        return syncUsecase.fetchStacksPendingRemoval().subscribeOn(Schedulers.io());
    }

}
