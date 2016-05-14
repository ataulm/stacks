package com.ataulm.stacks.stack;

public interface RemoveStackUsecase {

    void markPendingRemove(Stack stack);

    void unmarkPendingRemove(Stack stack);

    void remove(Stack stack);

}
