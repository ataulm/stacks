package com.ataulm.stacks.stack;

public interface UpdateStackUsecase {

    void updateSummary(Stack stack, String summary);

    void updateDescription(Stack stack, String description);

    void updateStack(Stack stack, String summary, String description);

    void updateParent(Stack stack, String parentId);

}
