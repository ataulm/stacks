package com.ataulm.stacks.stack;

import com.google.auto.value.AutoValue;

import java.util.Iterator;
import java.util.List;

@AutoValue
public abstract class Stacks implements Iterable<Stack> {

    abstract List<Stack> stacks();

    public static Stacks create(List<Stack> stacks) {
        return new AutoValue_Stacks(stacks);
    }

    Stacks() {
        // instantiate AutoValue generated class
    }

    public Stack get(int position) {
        return stacks().get(position);
    }

    public int size() {
        return stacks().size();
    }

    @Override
    public Iterator<Stack> iterator() {
        return stacks().iterator();
    }

}
