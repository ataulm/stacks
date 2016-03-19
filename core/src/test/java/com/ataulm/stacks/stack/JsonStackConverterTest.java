package com.ataulm.stacks.stack;

import com.ataulm.stacks.Optional;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class JsonStackConverterTest {

    private static final String TEST_ID = "TEST_ID";
    private static final String TEST_SUMMARY = "TEST_SUMMARY";
    private static final String TEST_PARENT_ID = "TEST_PARENT_ID";
    private static final String TEST_DESCRIPTION = "TEST_DESCRIPTION";

    private JsonStackConverter jsonStackConverter;

    @Before
    public void setUp() {
        jsonStackConverter = new JsonStackConverter();
    }

    @Test
    public void given_nullJsonStack_when_convertingToStack_then_returnNull() {
        JsonStack json = null;

        Stack stack = jsonStackConverter.convert(json);

        assertThat(stack).isNull();
    }

    @Test
    public void given_jsonStackWithMissingId_when_convertingToStack_then_returnNull() {
        JsonStack json = createJsonStack(null, TEST_SUMMARY, TEST_PARENT_ID, TEST_DESCRIPTION);

        Stack stack = jsonStackConverter.convert(json);

        assertThat(stack).isNull();
    }

    @Test
    public void given_jsonStackWithEmptyId_when_convertingToStack_then_returnNull() {
        JsonStack json = createJsonStack("", TEST_SUMMARY, TEST_PARENT_ID, TEST_DESCRIPTION);

        Stack stack = jsonStackConverter.convert(json);

        assertThat(stack).isNull();
    }

    @Test
    public void given_jsonStackWithMissingSummary_when_convertingToStack_then_returnNull() {
        JsonStack json = createJsonStack(TEST_ID, null, TEST_PARENT_ID, TEST_DESCRIPTION);

        Stack stack = jsonStackConverter.convert(json);

        assertThat(stack).isNull();
    }

    @Test
    public void given_jsonStackWithEmptySummary_when_convertingToStack_then_returnNull() {
        JsonStack json = createJsonStack(TEST_ID, "", TEST_PARENT_ID, TEST_DESCRIPTION);

        Stack stack = jsonStackConverter.convert(json);

        assertThat(stack).isNull();
    }

    @Test
    public void given_jsonStackWithMissingParentId_when_convertingToStack_then_stackHasAbsentParentId() {
        JsonStack json = createJsonStack(TEST_ID, TEST_SUMMARY, null, TEST_DESCRIPTION);

        Stack stack = jsonStackConverter.convert(json);

        assertThat(stack.parentId().isPresent()).isFalse();
    }

    @Test
    public void given_jsonStackWithEmptyParentId_when_convertingToStack_then_stackHasAbsentParentId() {
        JsonStack json = createJsonStack(TEST_ID, TEST_SUMMARY, "", TEST_DESCRIPTION);

        Stack stack = jsonStackConverter.convert(json);

        assertThat(stack.parentId().isPresent()).isFalse();
    }

    @Test
    public void given_jsonStackWithMissingDescription_when_convertingToStack_then_stackHasAbsentDescription() {
        JsonStack json = createJsonStack(TEST_ID, TEST_SUMMARY, TEST_PARENT_ID, null);

        Stack stack = jsonStackConverter.convert(json);

        assertThat(stack.description().isPresent()).isFalse();
    }

    @Test
    public void given_jsonStackWithEmptyDescription_when_convertingToStack_then_stackHasAbsentDescription() {
        JsonStack json = createJsonStack(TEST_ID, TEST_SUMMARY, TEST_PARENT_ID, "");

        Stack stack = jsonStackConverter.convert(json);

        assertThat(stack.description().isPresent()).isFalse();
    }

    @Test
    public void given_completeJsonStack_when_convertingToStack_then_returnCompleteStack() {
        JsonStack json = createJsonStack(TEST_ID, TEST_SUMMARY, TEST_PARENT_ID, TEST_DESCRIPTION);

        Stack stack = jsonStackConverter.convert(json);

        Stack expected = Stack.create(TEST_ID, TEST_SUMMARY, Optional.of(TEST_PARENT_ID), Optional.of(TEST_DESCRIPTION));
        assertThat(stack).isEqualTo(expected);
    }

    @Test
    public void given_nullStack_when_convertingToJsonStack_then_returnNull() {
        Stack stack = null;

        JsonStack json = jsonStackConverter.convert(stack);

        assertThat(json).isNull();
    }

    @Test
    public void given_completeStack_when_convertingToJsonStack_then_returnCompleteJsonStack() {
        Stack stack = Stack.create(TEST_ID, TEST_SUMMARY, Optional.of(TEST_PARENT_ID), Optional.of(TEST_DESCRIPTION));

        JsonStack json = jsonStackConverter.convert(stack);

        JsonStack expected = createJsonStack(TEST_ID, TEST_SUMMARY, TEST_PARENT_ID, TEST_DESCRIPTION);
        assertThat(json).isEqualTo(expected);
    }

    @Test
    public void given_stackWithAbsentParentId_when_convertingToJsonStack_then_returnJsonStackWithMissingParentId() {
        Stack stack = Stack.create(TEST_ID, TEST_SUMMARY, Optional.<String>absent(), Optional.of(TEST_DESCRIPTION));

        JsonStack json = jsonStackConverter.convert(stack);

        JsonStack expected = createJsonStack(TEST_ID, TEST_SUMMARY, null, TEST_DESCRIPTION);
        assertThat(json).isEqualTo(expected);
    }

    @Test
    public void given_stackWithAbsentDescription_when_convertingToJsonStack_then_returnJsonStackWithMissingDescription() {
        Stack stack = Stack.create(TEST_ID, TEST_SUMMARY, Optional.of(TEST_PARENT_ID), Optional.<String>absent());

        JsonStack json = jsonStackConverter.convert(stack);

        JsonStack expected = createJsonStack(TEST_ID, TEST_SUMMARY, TEST_PARENT_ID, null);
        assertThat(json).isEqualTo(expected);
    }

    private static JsonStack createJsonStack(String testId, String testSummary, String testParentId, String testDescription) {
        JsonStack json = new JsonStack();
        json.id = testId;
        json.summary = testSummary;
        json.parentId = testParentId;
        json.description = testDescription;
        return json;
    }

}
