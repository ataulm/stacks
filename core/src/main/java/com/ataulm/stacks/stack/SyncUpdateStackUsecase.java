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
    public void updateDescription(Stack stack, String description) {
        stacksRepository.updateDescription(stack, description);
    }

    @Override
    public void updateStack(Stack stack, String summary, String description) {
        stacksRepository.updateStack(stack, summary, description);
    }

    @Override
    public void updateParent(Stack stack, String parentId) {
        stacksRepository.updateParent(stack, parentId);
    }

}
