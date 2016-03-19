package com.ataulm.stacks.stack;

import com.ataulm.Event;

import rx.Observable;
import rx.schedulers.Schedulers;

public class AsyncFetchStacksUsecase implements FetchStacksUsecase {

    private final SyncFetchStacksUsecase syncUsecase;

    public AsyncFetchStacksUsecase(SyncFetchStacksUsecase syncUsecase) {
        this.syncUsecase = syncUsecase;
    }

    @Override
    public Observable<Event<Stacks>> fetchStacks() {
        return syncUsecase.fetchStacks().subscribeOn(Schedulers.io());
    }

}
