package com.ataulm.stacks.stack;

import com.ataulm.Optional;

import java.util.UUID;

public class SyncCreateStackUsecase implements CreateStackUsecase {

    private final StacksRepository stacksRepository;

    public SyncCreateStackUsecase(StacksRepository stacksRepository) {
        this.stacksRepository = stacksRepository;
    }

    @Override
    public void createStack(Optional<String> parentId, String summary) {
        Stack stack = Stack.create(UUID.randomUUID().toString(), summary, parentId);
        stacksRepository.add(stack);
    }

}
