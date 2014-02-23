package com.ataulm.stacks.persistence;

import com.ataulm.stacks.model.Stack;

public interface StackPersisterCallbacks {

    /**
     * Called when the stack is successfully persisted.
     *
     * @param stack the stack as returned from the database
     */
    void onSuccessPersisting(Stack stack);

    /**
     * Called when the stack could not be persisted.
     *
     * @param stack the stack which was intended for persistence
     */
    void onFailurePersisting(Stack stack);

}
