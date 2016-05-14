package com.ataulm.stacks.stack;

public class SyncRemoveStackUsecase implements RemoveStackUsecase {

    private final StacksRepository stacksRepository;

    public SyncRemoveStackUsecase(StacksRepository stacksRepository) {
        this.stacksRepository = stacksRepository;
    }

    @Override
    public void markPendingRemove(Stack stack) {
        stacksRepository.markPendingRemove(stack);
    }

    @Override
    public void unmarkPendingRemove(Stack stack) {
        stacksRepository.unmarkPendingRemove(stack);
    }

    @Override
    public void remove(Stack stack) {
        stacksRepository.remove(stack);
    }

}
