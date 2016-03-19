package com.ataulm.stacks.stack;

import java.util.List;

import rx.Observable;

public interface FetchStacksUsecase {

    Observable<List<Stack>> fetchStacks();

}
