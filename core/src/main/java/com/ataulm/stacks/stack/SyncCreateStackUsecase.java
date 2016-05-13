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
        Stack stack = Stack.create(id, summary, parentId, completed);
        stacksRepository.add(stack);
    }

}
