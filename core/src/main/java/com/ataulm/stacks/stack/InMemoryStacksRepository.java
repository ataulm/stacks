package com.ataulm.stacks.stack;

import java.util.List;

import rx.Observable;
import rx.subjects.BehaviorSubject;

public class InMemoryStacksRepository implements StacksRepository {

    BehaviorSubject<List<Stack>> foo;

    @Override
    public Observable<List<Stack>> getStacks() {
        return null;
    }

    @Override
    public void save(List<Stack> stacks) {

    }

    @Override
    public void add(Stack stack) {

    }

    @Override
    public void remove(Stack stack) {

    }

}
