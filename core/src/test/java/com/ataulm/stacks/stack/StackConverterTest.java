package com.ataulm.stacks.stack;

import com.ataulm.stacks.Optional;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class StackConverterTest {

    private static final String TEST_ID = "TEST_ID";
    private static final String TEST_SUMMARY = "TEST_SUMMARY";
    private static final String TEST_PARENT_ID = "TEST_PARENT_ID";
    private static final String TEST_DESCRIPTION = "TEST_DESCRIPTION";

    private StackConverter stackConverter;

    @Before
    public void setUp() {
        stackConverter = new StackConverter();
    }

    @Test
    public void given_nullJsonStack_then_returnNull() {
        JsonStack json = null;

        Stack stack = stackConverter.convert(json);

        assertThat(stack).isNull();
    }

    @Test
    public void given_jsonStackWithMissingId_then_returnNull() {
        JsonStack json = createJsonStack(null, TEST_SUMMARY, TEST_PARENT_ID, TEST_DESCRIPTION);

        Stack stack = stackConverter.convert(json);

        assertThat(stack).isNull();
    }

    @Test
    public void given_jsonStackWithEmptyId_then_returnNull() {
        JsonStack json = createJsonStack("", TEST_SUMMARY, TEST_PARENT_ID, TEST_DESCRIPTION);

        Stack stack = stackConverter.convert(json);

        assertThat(stack).isNull();
    }

    @Test
    public void given_jsonStackWithMissingSummary_then_returnNull() {
        JsonStack json = createJsonStack(TEST_ID, null, TEST_PARENT_ID, TEST_DESCRIPTION);

        Stack stack = stackConverter.convert(json);

        assertThat(stack).isNull();
    }

    @Test
    public void given_jsonStackWithEmptySummary_then_returnNull() {
        JsonStack json = createJsonStack(TEST_ID, "", TEST_PARENT_ID, TEST_DESCRIPTION);

        Stack stack = stackConverter.convert(json);

        assertThat(stack).isNull();
    }

    @Test
    public void given_jsonStackWithMissingParentId_then_stackHasAbsentParentId() {
        JsonStack json = createJsonStack(TEST_ID, TEST_SUMMARY, null, TEST_DESCRIPTION);

        Stack stack = stackConverter.convert(json);

        assertThat(stack.parentId().isPresent()).isFalse();
    }

    @Test
    public void given_jsonStackWithEmptyParentId_then_stackHasAbsentParentId() {
        JsonStack json = createJsonStack(TEST_ID, TEST_SUMMARY, "", TEST_DESCRIPTION);

        Stack stack = stackConverter.convert(json);

        assertThat(stack.parentId().isPresent()).isFalse();
    }

    @Test
    public void given_jsonStackWithMissingDescription_then_stackHasAbsentDescription() {
        JsonStack json = createJsonStack(TEST_ID, TEST_SUMMARY, TEST_PARENT_ID, null);

        Stack stack = stackConverter.convert(json);

        assertThat(stack.description().isPresent()).isFalse();
    }

    @Test
    public void given_jsonStackWithEmptyDescription_then_stackHasAbsentDescription() {
        JsonStack json = createJsonStack(TEST_ID, TEST_SUMMARY, TEST_PARENT_ID, "");

        Stack stack = stackConverter.convert(json);

        assertThat(stack.description().isPresent()).isFalse();
    }

    @Test
    public void given_completeJsonStack_then_returnCompleteStack() {
        JsonStack json = createJsonStack(TEST_ID, TEST_SUMMARY, TEST_PARENT_ID, TEST_DESCRIPTION);

        Stack stack = stackConverter.convert(json);

        Stack expected = Stack.create(TEST_ID, TEST_SUMMARY, Optional.of(TEST_PARENT_ID), Optional.of(TEST_DESCRIPTION));
        assertThat(stack).isEqualTo(expected);
    }

    // TODO: test convert toJson method

    private static JsonStack createJsonStack(String testId, String testSummary, String testParentId, String testDescription) {
        JsonStack json = new JsonStack();
        json.id = testId;
        json.summary = testSummary;
        json.parentId = testParentId;
        json.description = testDescription;
        return json;
    }

}
