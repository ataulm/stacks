package com.ataulm.stacks.stack;

import com.ataulm.Optional;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class JsonStackConverterTest {

    private static final String TEST_ID = "TEST_ID";
    private static final String TEST_SUMMARY = "TEST_SUMMARY";
    private static final String TEST_PARENT_ID = "TEST_PARENT_ID";
    private static final Set<String> TEST_LABELS;
    private static final boolean TEST_COMPLETED = false;

    static {
        TEST_LABELS = new HashSet<>(3);
        TEST_LABELS.add("test1");
        TEST_LABELS.add("test2");
        TEST_LABELS.add("test3");
    }

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
        JsonStack json = createJsonStack(null, TEST_SUMMARY, TEST_PARENT_ID, Collections.<String>emptySet());

        Stack stack = jsonStackConverter.convert(json);

        assertThat(stack).isNull();
    }

    @Test
    public void given_jsonStackWithEmptyId_when_convertingToStack_then_returnNull() {
        JsonStack json = createJsonStack("", TEST_SUMMARY, TEST_PARENT_ID, Collections.<String>emptySet());

        Stack stack = jsonStackConverter.convert(json);

        assertThat(stack).isNull();
    }

    @Test
    public void given_jsonStackWithMissingSummary_when_convertingToStack_then_returnNull() {
        JsonStack json = createJsonStack(TEST_ID, null, TEST_PARENT_ID, Collections.<String>emptySet());

        Stack stack = jsonStackConverter.convert(json);

        assertThat(stack).isNull();
    }

    @Test
    public void given_jsonStackWithEmptySummary_when_convertingToStack_then_returnNull() {
        JsonStack json = createJsonStack(TEST_ID, "", TEST_PARENT_ID, Collections.<String>emptySet());

        Stack stack = jsonStackConverter.convert(json);

        assertThat(stack).isNull();
    }

    @Test
    public void given_jsonStackWithMissingParentId_when_convertingToStack_then_stackHasAbsentParentId() {
        JsonStack json = createJsonStack(TEST_ID, TEST_SUMMARY, null, Collections.<String>emptySet());

        Stack stack = jsonStackConverter.convert(json);

        assertThat(stack.parentId().isPresent()).isFalse();
    }

    @Test
    public void given_jsonStackWithEmptyParentId_when_convertingToStack_then_stackHasAbsentParentId() {
        JsonStack json = createJsonStack(TEST_ID, TEST_SUMMARY, "", Collections.<String>emptySet());

        Stack stack = jsonStackConverter.convert(json);

        assertThat(stack.parentId().isPresent()).isFalse();
    }

    @Test
    public void given_jsonStackWithMissingLabels_when_convertingToStack_then_stackHasEmptyLabels() {
        JsonStack json = createJsonStack(TEST_ID, TEST_SUMMARY, null, null);

        Stack stack = jsonStackConverter.convert(json);

        assertThat(stack.labels()).isEmpty();
    }

    @Test
    public void given_jsonStackWithEmptyLabels_when_convertingToStack_then_stackHasEmptyLabels() {
        JsonStack json = createJsonStack(TEST_ID, TEST_SUMMARY, null, Collections.<String>emptySet());

        Stack stack = jsonStackConverter.convert(json);

        assertThat(stack.labels()).isEmpty();
    }

    @Test
    public void given_completeJsonStack_when_convertingToStack_then_returnCompleteStack() {
        JsonStack json = createJsonStack(TEST_ID, TEST_SUMMARY, TEST_PARENT_ID, TEST_LABELS);

        Stack stack = jsonStackConverter.convert(json);

        Set<Label> labels = new HashSet<>(TEST_LABELS.size());
        for (String testLabel : TEST_LABELS) {
            labels.add(Label.create(testLabel));
        }
        Labels expectedLabels = Labels.create(labels);
        Stack expected = Stack.create(Id.create(TEST_ID), TEST_SUMMARY, Optional.of(Id.create(TEST_PARENT_ID)), expectedLabels, TEST_COMPLETED);
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
        Stack stack = Stack.create(Id.create(TEST_ID), TEST_SUMMARY, Optional.of(Id.create(TEST_PARENT_ID)), TEST_COMPLETED);

        JsonStack json = jsonStackConverter.convert(stack);

        JsonStack expected = createJsonStack(TEST_ID, TEST_SUMMARY, TEST_PARENT_ID, Collections.<String>emptySet());
        assertThat(json).isEqualTo(expected);
    }

    @Test
    public void given_stackWithAbsentParentId_when_convertingToJsonStack_then_returnJsonStackWithMissingParentId() {
        Stack stack = Stack.create(Id.create(TEST_ID), TEST_SUMMARY, Optional.<Id>absent(), TEST_COMPLETED);

        JsonStack json = jsonStackConverter.convert(stack);

        JsonStack expected = createJsonStack(TEST_ID, TEST_SUMMARY, null, Collections.<String>emptySet());
        assertThat(json).isEqualTo(expected);
    }

    private static JsonStack createJsonStack(String testId, String testSummary, String testParentId, Set<String> testLabels) {
        JsonStack json = new JsonStack();
        json.id = testId;
        json.summary = testSummary;
        json.parentId = testParentId;
        json.labels = testLabels;
        return json;
    }

}
