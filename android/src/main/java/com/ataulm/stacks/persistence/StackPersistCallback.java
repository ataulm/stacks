package com.ataulm.stacks.persistence;

import com.ataulm.stacks.model.AndroidStack;

public interface StackPersistCallback {

    /**
     * Called when the stack is successfully persisted.
     *
     * @param stack the stack to be persisted
     */
    void onSuccessPersisting(AndroidStack stack);

    /**
     * Called when the stack could not be persisted.
     *
     * @param stack the stack to be persisted
     */
    void onFailurePersisting(AndroidStack stack);

}
