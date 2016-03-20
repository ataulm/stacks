package com.ataulm.stacks.stack;

public class SyncPersistStacksUsecase implements PersistStacksUsecase {

    private final StacksRepository stacksRepository;

    public SyncPersistStacksUsecase(StacksRepository stacksRepository) {
        this.stacksRepository = stacksRepository;
    }

    @Override
    public void persistStacks() {
        stacksRepository.persist();
    }

}
