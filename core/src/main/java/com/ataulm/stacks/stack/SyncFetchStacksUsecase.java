package com.ataulm.stacks.stack;

import com.ataulm.Event;
import com.ataulm.EventRxFunctions;
import com.ataulm.Optional;

import rx.Observable;

public class SyncFetchStacksUsecase implements FetchStacksUsecase {

    private final StacksRepository stacksRepository;

    public SyncFetchStacksUsecase(StacksRepository stacksRepository) {
        this.stacksRepository = stacksRepository;
    }

    @Override
    public Observable<Event<Stacks>> fetchStacks(Optional<Stack> parent) {
        return stacksRepository.getStacks(parent)
                .compose(EventRxFunctions.<Stacks>asEvents());
    }

}
