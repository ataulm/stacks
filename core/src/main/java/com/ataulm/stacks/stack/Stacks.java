package com.ataulm.stacks.stack;

import com.ataulm.Optional;
import com.google.auto.value.AutoValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        Collections.sort(stacks, new StackComparator());
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
        if (!children().contains(stack)) {
            return this;
        }

        if (size() == 1 && children().contains(stack)) {
            return create(Collections.<Stack>emptyList());
        } else {
            List<Stack> copy = new ArrayList<>(size());
            copy(children(), copy);
            removeStackAndDescendents(copy, stack);
            List<Stack> nonNullStacks = nonNulls(copy);

            return create(nonNullStacks);
        }
    }

    public Stacks update(Stack updatedStack) {
        List<Stack> copy = new ArrayList<>(size());
        copyUpdatingStack(children(), copy, updatedStack);
        return create(copy);
    }

    private static void copy(List<Stack> source, List<Stack> copy) {
        for (Stack stack : source) {
            copy.add(stack);
        }
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

    private static void removeStackAndDescendents(List<Stack> stacks, Stack stack) {
        stacks.set(stacks.indexOf(stack), null);
        for (int i = 0; i < stacks.size(); i++) {
            Stack s = stacks.get(i);
            if (s != null && stack.isParentOf(s)) {
                removeStackAndDescendents(stacks, s);
            }
        }
    }

    private static List<Stack> nonNulls(List<Stack> copy) {
        List<Stack> stacks = new ArrayList<>(copy.size());
        for (Stack stack : copy) {
            if (stack != null) {
                stacks.add(stack);
            }
        }
        return stacks;
    }

    public Stacks getEligibleParentsFor(Stack stack) {
        List<Stack> ineligibleParents = getIneligibleParents(children(), stack);
        List<Stack> eligibleParents = new ArrayList<>();

        for (Stack s : children()) {
            if (!ineligibleParents.contains(s)) {
                eligibleParents.add(s);
            }
        }

        return Stacks.create(eligibleParents);
    }

    private static List<Stack> getIneligibleParents(List<Stack> stacks, Stack stack) {
        List<Stack> ineligibleParents = new ArrayList<>();
        ineligibleParents.add(stack);
        for (Stack s : stacks) {
            if (stack.isParentOf(s)) {
                ineligibleParents.addAll(getIneligibleParents(stacks, s));
            }
        }
        return ineligibleParents;
    }

    private static class StackComparator implements Comparator<Stack> {

        @Override
        public int compare(Stack o1, Stack o2) {
            if (o1.completed() == o2.completed()) {
                return 0;
            }
            if (o1.completed()) {
                return 1;
            } else {
                return -1;
            }
        }

    }

}
