package com.ataulm.stacks.stack;

import com.ataulm.Optional;

import java.util.Arrays;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class StacksTest {

    @Test
    public void given_stacksWithThreeLevels_when_LevelTwoIsRemoved_then_LevelThreeIsAlsoRemoved() {
        Stack a = Stack.create(Id.create("a"), "a", Optional.<Id>absent(), false);
        Stack b = Stack.create(Id.create("b"), "b", Optional.of(a.id()), false);
        Stack c = Stack.create(Id.create("c"), "c", Optional.of(b.id()), false);
        Stacks stacks = Stacks.create(Arrays.asList(a, b, c));

        Stacks updated = stacks.remove(b);

        assertThat(updated).containsOnly(a);
    }

    @Test
    public void given_stacksWithMultipleLevels_when_EligibleParentsForAGivenStackIsRequested_then_ListDoesNotContainThatStackOrDescendantsOfThatStack() {
        Stack a = Stack.create(Id.create("a"), "a", Optional.<Id>absent(), false);
        Stack a1 = Stack.create(Id.create("a.1"), "a.1", Optional.of(a.id()), false);
        Stack a2 = Stack.create(Id.create("a.2"), "a.2", Optional.of(a.id()), false);

        Stack b = Stack.create(Id.create("b"), "b", Optional.<Id>absent(), false);
        Stack b1 = Stack.create(Id.create("b.1"), "b.1", Optional.of(b.id()), false);
        Stack b1dot1 = Stack.create(Id.create("b.1.1"), "b.1.1", Optional.of(b1.id()), false);

        Stack c = Stack.create(Id.create("c"), "c", Optional.<Id>absent(), false);
        Stack c1 = Stack.create(Id.create("c.1"), "c.1", Optional.of(c.id()), false);
        Stack c2 = Stack.create(Id.create("c.2"), "c.2", Optional.of(c.id()), false);

        Stacks stacks = Stacks.create(Arrays.asList(a, a1, a2, b, b1, b1dot1, c, c1, c2));

        Stacks eligibleParents = stacks.getEligibleParentsFor(b1);

        assertThat(eligibleParents).containsOnly(a, a1, a2, b, c, c1, c2);
    }

}
