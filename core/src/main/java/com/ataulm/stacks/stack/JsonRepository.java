package com.ataulm.stacks.stack;

import com.ataulm.stacks.Optional;

public interface JsonRepository {

    Optional<String> getStacks();

    void persistStacks(String json);

}
