package com.ataulm.stacks.stack;

import java.util.List;

import rx.Observable;

public interface StacksRepository {

    Observable<List<Stack>> getStacks();

    void save(List<Stack> stacks);

    void add(Stack stack);

    void remove(Stack stack);

}
