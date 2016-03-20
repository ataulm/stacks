package com.ataulm.stacks.stack;

public class SyncRemoveStackUsecase implements RemoveStackUsecase {

    private final StacksRepository stacksRepository;

    public SyncRemoveStackUsecase(StacksRepository stacksRepository) {
        this.stacksRepository = stacksRepository;
    }

    @Override
    public void remove(Stack stack) {
        stacksRepository.remove(stack);
    }

}
