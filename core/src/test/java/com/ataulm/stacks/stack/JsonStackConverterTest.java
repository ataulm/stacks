package com.ataulm.stacks.stack;

import com.ataulm.Optional;

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
        JsonStack json = createJsonStack(null, TEST_SUMMARY, TEST_PARENT_ID);

        Stack stack = jsonStackConverter.convert(json);

        assertThat(stack).isNull();
    }

    @Test
    public void given_jsonStackWithEmptyId_when_convertingToStack_then_returnNull() {
        JsonStack json = createJsonStack("", TEST_SUMMARY, TEST_PARENT_ID);

        Stack stack = jsonStackConverter.convert(json);

        assertThat(stack).isNull();
    }

    @Test
    public void given_jsonStackWithMissingSummary_when_convertingToStack_then_returnNull() {
        JsonStack json = createJsonStack(TEST_ID, null, TEST_PARENT_ID);

        Stack stack = jsonStackConverter.convert(json);

        assertThat(stack).isNull();
    }

    @Test
    public void given_jsonStackWithEmptySummary_when_convertingToStack_then_returnNull() {
        JsonStack json = createJsonStack(TEST_ID, "", TEST_PARENT_ID);

        Stack stack = jsonStackConverter.convert(json);

        assertThat(stack).isNull();
    }

    @Test
    public void given_jsonStackWithMissingParentId_when_convertingToStack_then_stackHasAbsentParentId() {
        JsonStack json = createJsonStack(TEST_ID, TEST_SUMMARY, null);

        Stack stack = jsonStackConverter.convert(json);

        assertThat(stack.parentId().isPresent()).isFalse();
    }

    @Test
    public void given_jsonStackWithEmptyParentId_when_convertingToStack_then_stackHasAbsentParentId() {
        JsonStack json = createJsonStack(TEST_ID, TEST_SUMMARY, "");

        Stack stack = jsonStackConverter.convert(json);

        assertThat(stack.parentId().isPresent()).isFalse();
    }

    @Test
    public void given_completeJsonStack_when_convertingToStack_then_returnCompleteStack() {
        JsonStack json = createJsonStack(TEST_ID, TEST_SUMMARY, TEST_PARENT_ID);

        Stack stack = jsonStackConverter.convert(json);

        Stack expected = Stack.create(Id.create(TEST_ID), TEST_SUMMARY, Optional.of(Id.create(TEST_PARENT_ID)));
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
        Stack stack = Stack.create(Id.create(TEST_ID), TEST_SUMMARY, Optional.of(Id.create(TEST_PARENT_ID)));

        JsonStack json = jsonStackConverter.convert(stack);

        JsonStack expected = createJsonStack(TEST_ID, TEST_SUMMARY, TEST_PARENT_ID);
        assertThat(json).isEqualTo(expected);
    }

    @Test
    public void given_stackWithAbsentParentId_when_convertingToJsonStack_then_returnJsonStackWithMissingParentId() {
        Stack stack = Stack.create(Id.create(TEST_ID), TEST_SUMMARY, Optional.<Id>absent());

        JsonStack json = jsonStackConverter.convert(stack);

        JsonStack expected = createJsonStack(TEST_ID, TEST_SUMMARY, null);
        assertThat(json).isEqualTo(expected);
    }

    private static JsonStack createJsonStack(String testId, String testSummary, String testParentId) {
        JsonStack json = new JsonStack();
        json.id = testId;
        json.summary = testSummary;
        json.parentId = testParentId;
        return json;
    }

}
