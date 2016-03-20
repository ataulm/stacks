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
    public Observable<Event<Stacks>> fetchStacks(Optional<Stack> parent) {
        return syncUsecase.fetchStacks(parent).subscribeOn(Schedulers.io());
    }

}
