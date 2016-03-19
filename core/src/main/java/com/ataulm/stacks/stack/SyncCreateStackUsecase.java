package com.ataulm.stacks.stack;

import com.ataulm.Optional;

import java.util.UUID;

public class SyncCreateStackUsecase implements CreateStackUsecase {

    private final StacksRepository stacksRepository;

    public SyncCreateStackUsecase(StacksRepository stacksRepository) {
        this.stacksRepository = stacksRepository;
    }

    @Override
    public void createStack(String summary) {
        Stack stack = Stack.create(UUID.randomUUID().toString(), summary, Optional.<String>absent(), Optional.<String>absent());
        stacksRepository.add(stack);
    }

}
