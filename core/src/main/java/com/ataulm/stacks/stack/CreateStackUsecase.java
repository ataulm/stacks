package com.ataulm.stacks.stack;

import com.ataulm.Optional;

public interface CreateStackUsecase {

    void createStack(Optional<String> parentId, String summary);

}
