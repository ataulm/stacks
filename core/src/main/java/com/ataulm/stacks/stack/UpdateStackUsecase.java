package com.ataulm.stacks.stack;

public interface UpdateStackUsecase {

    void updateSummary(Stack stack, String summary);

    void updateParent(Stack stack, Id parentId);

}
