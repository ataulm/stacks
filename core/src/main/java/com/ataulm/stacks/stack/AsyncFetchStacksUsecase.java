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
    public Observable<Event<Optional<Stack>>> fetchStack(Optional<Id> id) {
        return syncUsecase.fetchStack(id).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Event<Stacks>> fetchChildrenWithParent(Optional<Id> parentId) {
        return syncUsecase.fetchChildrenWithParent(parentId).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Event<Stacks>> fetchStacksPendingRemoval() {
        return syncUsecase.fetchStacksPendingRemoval().subscribeOn(Schedulers.io());
    }

}
