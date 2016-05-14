package com.ataulm.stacks.stack;

import com.ataulm.Optional;

import java.util.Arrays;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class StacksTest {

    @Test
    public void given_stacksWithThreeLevels_when_LevelTwoIsRemoved_then_LevelThreeIsAlsoRemoved() {
        Stack a = createRootStack("a");
        Stack b = createChildStack("b", a.id());
        Stack c = createChildStack("c", b.id());
        Stacks stacks = Stacks.create(Arrays.asList(a, b, c));

        Stacks updated = stacks.remove(b);

        assertThat(updated).containsOnly(a);
    }

    @Test
    public void given_stacksWithMultipleLevels_when_EligibleParentsForAGivenStackIsRequested_then_ListDoesNotContainThatStackOrDescendantsOfThatStack() {
        Stack a = createRootStack("a");
        Stack a1 = createChildStack("a.1", a.id());
        Stack a2 = createChildStack("a.2", a.id());

        Stack b = createRootStack("b");
        Stack b1 = createChildStack("b.1", b.id());
        Stack b1dot1 = createChildStack("b.1.1", b1.id());

        Stack c = createRootStack("c");
        Stack c1 = createChildStack("c.1", c.id());
        Stack c2 = createChildStack("c.2", c.id());

        Stacks stacks = Stacks.create(Arrays.asList(a, a1, a2, b, b1, b1dot1, c, c1, c2));

        Stacks eligibleParents = stacks.getEligibleParentsFor(b1);

        assertThat(eligibleParents).containsOnly(a, a1, a2, b, c, c1, c2);
    }

    private static Stack createRootStack(String id) {
        return createChildStack(id, null);
    }

    private static Stack createChildStack(String id, Id parentId) {
        return createStack(id, Optional.from(parentId));
    }

    private static Stack createStack(String id, Optional<Id> parentId) {
        String summary = id;
        return Stack.create(Id.create(id), summary, parentId, Stack.Dates.create(0));
    }

}
