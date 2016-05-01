package com.ataulm.stacks.stack;

import com.ataulm.Optional;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Stack {

    public abstract Id id();

    public abstract String summary();

    public abstract Optional<Id> parentId();

    public static Stack create(Id id, String summary, Optional<Id> parentId) {
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
