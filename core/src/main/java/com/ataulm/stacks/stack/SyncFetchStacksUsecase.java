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
    public Observable<Event<Optional<Stack>>> fetchStack(Optional<Id> id) {
        return stacksRepository.getStack(id)
                .compose(EventRxFunctions.<Optional<Stack>>asEvents());
    }

    @Override
    public Observable<Event<Stacks>> fetchChildrenWithParent(Optional<Id> parentId) {
        return stacksRepository.getStacks(parentId)
                .compose(EventRxFunctions.<Stacks>asEvents());
    }

    @Override
    public Observable<Event<Stacks>> fetchStacksPendingRemoval() {
        return stacksRepository.getStacksPendingRemoval()
                .compose(EventRxFunctions.<Stacks>asEvents());
    }

}
