package com.ataulm.stacks.navigation;

import com.ataulm.Optional;
import com.ataulm.stacks.stack.Id;

import java.net.URI;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class IdExtractorShould {

    private static final String STACK_ID = "73735d55-9fa8-4439-b1ab-293c1a66b5c5";
    private static final URI STACKS_URI = URI.create("content://com.ataulm.stacks/stacks/" + STACK_ID);
    private static final URI INVALID_STACKS_URI = URI.create("content://com.ataulm.stacks/stacks/invalid");

    IdExtractor idExtractor = new IdExtractor();

    @Test
    public void returnId_givenStacksUri() {
        Optional<Id> id = idExtractor.extractIdFrom(STACKS_URI);

        assertThat(id.get().value()).isEqualTo(STACK_ID);
    }

    @Test
    public void returnAbsent_givenStacksUri() {
        Optional<Id> id = idExtractor.extractIdFrom(INVALID_STACKS_URI);

        assertThat(id.isPresent()).isFalse();
    }

}
