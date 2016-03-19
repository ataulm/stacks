package com.ataulm.stacks.stack;

import com.ataulm.Event;
import com.ataulm.EventRxFunctions;

import rx.Observable;

public class SyncFetchStacksUsecase implements FetchStacksUsecase {

    private final StacksRepository stacksRepository;

    public SyncFetchStacksUsecase(StacksRepository stacksRepository) {
        this.stacksRepository = stacksRepository;
    }

    @Override
    public Observable<Event<Stacks>> fetchStacks() {
        return stacksRepository.getStacks()
                .compose(EventRxFunctions.<Stacks>asEvents());
    }

}
