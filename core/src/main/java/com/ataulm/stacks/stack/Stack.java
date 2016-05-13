package com.ataulm.stacks.stack;

import com.ataulm.Optional;
import com.google.auto.value.AutoValue;

import java.util.Collections;

@AutoValue
public abstract class Stack {

    public abstract Id id();

    public abstract String summary();

    public abstract Optional<Id> parentId();

    public abstract Labels labels();

    public abstract boolean completed();

    public static Stack create(Id id, String summary, Optional<Id> parentId, boolean completed) {
        return new AutoValue_Stack(id, summary, parentId, Labels.create(Collections.<Label>emptySet()), completed);
    }

    public static Stack create(Id id, String summary, Optional<Id> parentId, Labels labels, boolean completed) {
        return new AutoValue_Stack(id, summary, parentId, labels, completed);
    }

    Stack() {
        // instantiate AutoValue generated class
    }

    public boolean isParentOf(Stack stack) {
        return stack.parentId().isPresent() && stack.parentId().get().equals(id());
    }

}
