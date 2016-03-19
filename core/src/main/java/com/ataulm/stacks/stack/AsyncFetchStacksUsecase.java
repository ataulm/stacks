package com.ataulm.stacks.stack;

import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

public class AsyncFetchStacksUsecase implements FetchStacksUsecase {

    private final SyncFetchStacksUsecase syncUsecase;

    public AsyncFetchStacksUsecase(SyncFetchStacksUsecase syncUsecase) {
        this.syncUsecase = syncUsecase;
    }

    @Override
    public Observable<List<Stack>> fetchStacks() {
        return syncUsecase.fetchStacks().subscribeOn(Schedulers.io());
    }

}
