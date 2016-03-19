package com.ataulm.stacks.stack;

import com.ataulm.Event;

import rx.Observable;

public interface FetchStacksUsecase {

    Observable<Event<Stacks>> fetchStacks();

}
