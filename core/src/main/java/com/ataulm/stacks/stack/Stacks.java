package com.ataulm.stacks.stack;

import com.ataulm.Optional;
import com.google.auto.value.AutoValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@AutoValue
public abstract class Stacks implements Iterable<Stack> {

    public abstract Optional<Stack> info();

    abstract List<Stack> children();

    public static Stacks empty() {
        return create(Collections.<Stack>emptyList());
    }

    public static Stacks create(List<Stack> stacks) {
        return create(Optional.<Stack>absent(), stacks);
    }

    public static Stacks create(Optional<Stack> stack, List<Stack> stacks) {
        return new AutoValue_Stacks(stack, stacks);
    }

    Stacks() {
        // instantiate AutoValue generated class
    }

    public Stack get(int position) {
        return children().get(position);
    }

    public int size() {
        return children().size();
    }

    @Override
    public Iterator<Stack> iterator() {
        return children().iterator();
    }

    public Stacks add(Stack stack) {
        List<Stack> copy = new ArrayList<>(size() + 1);
        copy(children(), copy);
        copy.add(stack);
        return create(copy);
    }

    public Stacks remove(Stack stack) {
        if (size() == 1 && children().contains(stack)) {
            return create(Collections.<Stack>emptyList());
        } else {
            List<Stack> copy = new ArrayList<>(size());
            copy(children(), copy);
            copy.remove(stack);
            return create(copy);
        }
    }

    private static void copy(List<Stack> source, List<Stack> copy) {
        for (Stack stack : source) {
            copy.add(stack);
        }
    }

    public Stacks update(Stack updatedStack) {
        List<Stack> copy = new ArrayList<>(size());
        copyUpdatingStack(children(), copy, updatedStack);
        return create(copy);
    }

    private static void copyUpdatingStack(List<Stack> source, List<Stack> copy, Stack updatedStack) {
        for (Stack stack : source) {
            if (stack.id().equals(updatedStack.id())) {
                copy.add(updatedStack);
            } else {
                copy.add(stack);
            }
        }
    }

}
