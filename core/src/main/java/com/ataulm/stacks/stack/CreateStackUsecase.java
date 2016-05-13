package com.ataulm.stacks.stack;

import com.ataulm.Optional;

public interface CreateStackUsecase {

    void createStack(Optional<Id> parentId, String summary);

    void createStackCompleted(Optional<Id> parentId, String summary);

}
