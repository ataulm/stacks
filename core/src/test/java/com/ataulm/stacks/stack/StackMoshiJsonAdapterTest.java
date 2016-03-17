package com.ataulm.stacks.stack;

import com.ataulm.stacks.Optional;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * These tests serve as doc for Moshi's JsonAdapter
 */
public class StackMoshiJsonAdapterTest {

    private JsonAdapter<Stack> stackJsonAdapter;

    @Before
    public void setUp() {
        stackJsonAdapter = new Moshi.Builder()
                .add(new StackConverter())
                .build()
                .adapter(Stack.class);
    }

    @Test
    public void given_nullStack_when_convertingToJson_then_returnStringNull() {
        Stack stack = null;

        String json = stackJsonAdapter.toJson(stack);

        assertThat(json).isEqualTo("null");
    }

    @Test
    public void given_completeStack_when_convertingToJson_then_allStackFieldsAreRepresentedInJson() {
        Stack stack = Stack.create("testId", "testSummary", Optional.of("testParentId"), Optional.of("testDescription"));

        String json = stackJsonAdapter.toJson(stack);

        assertThat(json).isEqualTo("{\"desc\":\"testDescription\",\"id\":\"testId\",\"parent\":\"testParentId\",\"summary\":\"testSummary\"}");
    }

    @Test
    public void given_completeJson_when_convertingToStack_then_allStackFieldsArePopulatedCorrectly() throws IOException {
        String json = "{\"desc\":\"testDescription\",\"id\":\"testId\",\"parent\":\"testParentId\",\"summary\":\"testSummary\"}";

        Stack stack = stackJsonAdapter.fromJson(json);

        Stack expected = Stack.create("testId", "testSummary", Optional.of("testParentId"), Optional.of("testDescription"));
        assertThat(stack).isEqualTo(expected);
    }

}
