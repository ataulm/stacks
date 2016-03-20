package com.ataulm.stacks.stack;

import com.google.auto.value.AutoValue;

import java.util.ArrayList;
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

    public Stacks add(Stack stack) {
        List<Stack> copy = new ArrayList<>(size() + 1);
        copy(stacks(), copy);
        copy.add(stack);
        return create(copy);
    }

    public Stacks remove(Stack stack) {
        List<Stack> copy = new ArrayList<>(size() - 1);
        copy(stacks(), copy);
        copy.remove(stack);
        return create(copy);
    }

    private static void copy(List<Stack> source, List<Stack> copy) {
        for (Stack stack : source) {
            copy.add(stack);
        }
    }

    public Stacks update(Stack updatedStack) {
        List<Stack> copy = new ArrayList<>(size());
        copyUpdatingStack(stacks(), copy, updatedStack);
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
