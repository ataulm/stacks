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
        Id id = Id.create(UUID.randomUUID().toString());
        Stack stack = Stack.create(id, summary, parentId);
        stacksRepository.add(stack);
    }

}
