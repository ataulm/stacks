package com.ataulm.stacks.stack;

import com.ataulm.Optional;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Stack {

    public abstract String id();

    public abstract String summary();

    public abstract Optional<String> parentId();

    public abstract Optional<String> description();

    public static Stack create(String id, String summary, Optional<String> parentId, Optional<String> description) {
        return new AutoValue_Stack(id, summary, parentId, description);
    }

    Stack() {
        // instantiate AutoValue generated class
    }

}
