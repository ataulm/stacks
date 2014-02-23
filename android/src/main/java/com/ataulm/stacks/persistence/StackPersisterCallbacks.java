package com.ataulm.stacks.persistence;

import com.ataulm.stacks.model.Stack;

public interface StackPersisterCallbacks {

    /**
     * Called when the stack is successfully persisted.
     *
     * @param stack the stack to be persisted
     */
    void onSuccessPersisting(Stack stack);

    /**
     * Called when the stack could not be persisted.
     *
     * @param stack the stack to be persisted
     */
    void onFailurePersisting(Stack stack);

}
