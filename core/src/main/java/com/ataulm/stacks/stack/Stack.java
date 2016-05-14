package com.ataulm.stacks.stack;

import com.ataulm.Optional;
import com.google.auto.value.AutoValue;

import java.util.Collections;

@AutoValue
public abstract class Stack {

    public abstract Id id();

    public abstract String summary();

    public abstract Optional<Id> parentId();

    public abstract Dates dates();

    public abstract Labels labels();

    public static Stack create(Id id, String summary, Optional<Id> parentId, Dates dates) {
        return create(id, summary, parentId, dates, Labels.create(Collections.<Label>emptySet()));
    }

    public static Stack create(Id id, String summary, Optional<Id> parentId, Dates dates, Labels labels) {
        return new AutoValue_Stack(id, summary, parentId, dates, labels);
    }

    Stack() {
        // instantiate AutoValue generated class
    }

    public boolean isParentOf(Stack stack) {
        return stack.parentId().isPresent() && stack.parentId().get().equals(id());
    }

    public boolean completed() {
        return dates().completed().isPresent();
    }

    public boolean deleted() {
        return dates().deleted().isPresent();
    }

    @AutoValue
    public abstract static class Dates {

        public static Dates create(long created) {
            return create(created, created, Optional.<Long>absent(), Optional.<Long>absent());
        }

        public static Dates create(long created, long modified, Optional<Long> completed, Optional<Long> deleted) {
            return new AutoValue_Stack_Dates(created, modified, completed, deleted);
        }

        Dates() {
            // instantiate AutoValue generated class
        }

        public abstract long created();

        /**
         * Modified summary/labels/completed/parent
         */
        public abstract long modified();

        public abstract Optional<Long> completed();

        public abstract Optional<Long> deleted();
    }

}
