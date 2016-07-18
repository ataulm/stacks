package com.ataulm.stacks.jabber;

import com.ataulm.stacks.stack.AsyncFetchStacksUsecase;
import com.ataulm.stacks.stack.AsyncUpdateStacksUsecase;
import com.ataulm.stacks.stack.CreateStackUsecase;
import com.ataulm.stacks.stack.FetchStacksUsecase;
import com.ataulm.stacks.stack.PersistStacksUsecase;
import com.ataulm.stacks.stack.RemoveStackUsecase;
import com.ataulm.stacks.stack.StacksRepository;
import com.ataulm.stacks.stack.SyncCreateStackUsecase;
import com.ataulm.stacks.stack.SyncFetchStacksUsecase;
import com.ataulm.stacks.stack.SyncPersistStacksUsecase;
import com.ataulm.stacks.stack.SyncRemoveStackUsecase;
import com.ataulm.stacks.stack.SyncUpdateStackUsecase;
import com.ataulm.stacks.stack.UpdateStackUsecase;

public final class Usecases {

    private final FetchStacksUsecase fetchStacksUsecase;
    private final CreateStackUsecase createStackUsecase;
    private final RemoveStackUsecase removeStackUsecase;
    private final PersistStacksUsecase persistStacksUsecase;
    private final UpdateStackUsecase updateStacksUsecase;

    static Usecases create(StacksRepository stacksRepository) {
        return new Usecases(
                new AsyncFetchStacksUsecase(new SyncFetchStacksUsecase(stacksRepository)),
                new SyncCreateStackUsecase(stacksRepository),
                new SyncRemoveStackUsecase(stacksRepository),
                new SyncPersistStacksUsecase(stacksRepository),
                new AsyncUpdateStacksUsecase(new SyncUpdateStackUsecase(stacksRepository))
        );
    }

    private Usecases(
            FetchStacksUsecase fetchStacksUsecase,
            CreateStackUsecase createStackUsecase,
            RemoveStackUsecase removeStackUsecase,
            PersistStacksUsecase persistStacksUsecase,
            UpdateStackUsecase updateStacksUsecase
    ) {
        this.fetchStacksUsecase = fetchStacksUsecase;
        this.createStackUsecase = createStackUsecase;
        this.removeStackUsecase = removeStackUsecase;
        this.persistStacksUsecase = persistStacksUsecase;
        this.updateStacksUsecase = updateStacksUsecase;
    }

    public FetchStacksUsecase fetchStacks() {
        return fetchStacksUsecase;
    }

    public CreateStackUsecase createStacks() {
        return createStackUsecase;
    }

    public RemoveStackUsecase removeStacks() {
        return removeStackUsecase;
    }

    public PersistStacksUsecase persistStacks() {
        return persistStacksUsecase;
    }

    public UpdateStackUsecase updateStacks() {
        return updateStacksUsecase;
    }

}
