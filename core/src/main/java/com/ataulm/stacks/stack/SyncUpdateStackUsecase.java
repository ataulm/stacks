package com.ataulm.stacks.stack;

public class SyncUpdateStackUsecase implements UpdateStackUsecase {

    private final StacksRepository stacksRepository;

    public SyncUpdateStackUsecase(StacksRepository stacksRepository) {
        this.stacksRepository = stacksRepository;
    }

    @Override
    public void updateSummary(Stack stack, String summary) {
        stacksRepository.update(stack, summary);
    }

    @Override
    public void updateParent(Stack stack, Id parentId) {
        stacksRepository.updateParent(stack, parentId);
    }

}
