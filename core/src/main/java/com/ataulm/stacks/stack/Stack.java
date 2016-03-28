package com.ataulm.stacks.stack;

import com.ataulm.Optional;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Stack {

    public abstract String id();

    public abstract String summary();

    public abstract Optional<String> parentId();

    public static Stack create(String id, String summary, Optional<String> parentId) {
        return new AutoValue_Stack(id, summary, parentId);
    }

    Stack() {
        // instantiate AutoValue generated class
    }

    public boolean isParentOf(Stack stack) {
        if (stack.parentId().isPresent()) {
            return stack.parentId().get().equals(id());
        }
        return false;
    }

}
