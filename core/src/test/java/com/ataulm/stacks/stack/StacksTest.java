package com.ataulm.stacks.stack;

import com.ataulm.Optional;

import java.util.Arrays;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class StacksTest {

    @Test
    public void given_stacksWithThreeLevels_when_LevelTwoIsRemoved_then_LevelThreeIsAlsoRemoved() {
        Stack a = Stack.create("a", "a", Optional.<String>absent());
        Stack b = Stack.create("b", "b", Optional.of(a.id()));
        Stack c = Stack.create("c", "c", Optional.of(b.id()));
        Stacks stacks = Stacks.create(Arrays.asList(a, b, c));

        Stacks updated = stacks.remove(b);

        assertThat(updated).containsOnly(a);
    }

}
