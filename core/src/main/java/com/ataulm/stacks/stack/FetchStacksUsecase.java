package com.ataulm.stacks.stack;

import com.ataulm.Event;
import com.ataulm.Optional;

import rx.Observable;

public interface FetchStacksUsecase {

    Observable<Event<Stacks>> fetchStacks(Optional<Id> parentId);

    Observable<Event<Stacks>> fetchStacksPendingRemoval();

}
