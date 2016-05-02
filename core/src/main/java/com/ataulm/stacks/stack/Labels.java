package com.ataulm.stacks.stack;

import com.google.auto.value.AutoValue;

import java.util.Iterator;
import java.util.Set;

@AutoValue
public abstract class Labels implements Iterable<Label> {

    abstract Set<Label> value();

    public static Labels create(Set<Label> labels) {
        return new AutoValue_Labels(labels);
    }

    Labels() {
        // instantiate AutoValue generated class
    }

    @Override
    public Iterator<Label> iterator() {
        return value().iterator();
    }

    public int size() {
        return value().size();
    }

}
