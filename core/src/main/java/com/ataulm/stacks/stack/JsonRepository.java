package com.ataulm.stacks.stack;

import com.ataulm.Optional;

public interface JsonRepository {

    Optional<String> getStacks();

    void persistStacks(String json);

}
