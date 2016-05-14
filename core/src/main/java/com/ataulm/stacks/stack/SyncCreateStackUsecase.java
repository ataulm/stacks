package com.ataulm.stacks.stack;

import com.ataulm.Optional;

import java.util.UUID;

public class SyncCreateStackUsecase implements CreateStackUsecase {

    private final StacksRepository stacksRepository;

    public SyncCreateStackUsecase(StacksRepository stacksRepository) {
        this.stacksRepository = stacksRepository;
    }

    @Override
    public void createStack(Optional<Id> parentId, String summary) {
        createStack(parentId, summary, false);
    }

    @Override
    public void createStackCompleted(Optional<Id> parentId, String summary) {
        createStack(parentId, summary, true);
    }

    private void createStack(Optional<Id> parentId, String summary, boolean completed) {
        Id id = Id.create(UUID.randomUUID().toString());

        Stack.Dates dates;
        long currentTime = System.currentTimeMillis();
        if (completed) {
            dates = Stack.Dates.create(currentTime, currentTime, Optional.of(currentTime), Optional.<Long>absent());
        } else {
            dates = Stack.Dates.create(currentTime);
        }

        Stack stack = Stack.create(id, summary, parentId, dates);
        stacksRepository.add(stack);
    }

}
