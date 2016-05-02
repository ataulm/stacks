package com.ataulm.stacks.stack;

import com.ataulm.Optional;
import com.google.auto.value.AutoValue;

import java.util.Collections;
import java.util.Set;

@AutoValue
public abstract class Stack {

    public abstract Id id();

    public abstract String summary();

    public abstract Optional<Id> parentId();

    public abstract Labels labels();

    public static Stack create(Id id, String summary, Optional<Id> parentId) {
        return new AutoValue_Stack(id, summary, parentId, Labels.create(Collections.<Label>emptySet()));
    }

    public static Stack create(Id id, String summary, Optional<Id> parentId, Labels labels) {
        return new AutoValue_Stack(id, summary, parentId, labels);
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
