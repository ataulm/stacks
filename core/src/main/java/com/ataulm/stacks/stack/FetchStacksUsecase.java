package com.ataulm.stacks.stack;

import com.ataulm.Event;
import com.ataulm.Optional;

import rx.Observable;

public interface FetchStacksUsecase {

    Observable<Event<Optional<Stack>>> fetchStack(Optional<Id> id);

    Observable<Event<Stacks>> fetchChildrenWithParent(Optional<Id> parentId);

    Observable<Event<Stacks>> fetchStacksPendingRemoval();

}
