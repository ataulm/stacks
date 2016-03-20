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
    public Observable<Event<Stacks>> fetchStacks(Optional<String> parentId) {
        return stacksRepository.getStacks(parentId)
                .compose(EventRxFunctions.<Stacks>asEvents());
    }

}
