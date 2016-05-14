package com.ataulm.stacks.stack;

import com.ataulm.Optional;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * These tests serve as doc for Moshi's JsonAdapter
 */
public class StackMoshiJsonAdapterTest {

    private JsonAdapter<Stack> stackJsonAdapter;
    private JsonAdapter<List<JsonStack>> jsonStackListAdapter;

    @Before
    public void setUp() {
        stackJsonAdapter = new Moshi.Builder()
                .add(new JsonStackConverter())
                .build()
                .adapter(Stack.class);

        Type listStack = Types.newParameterizedType(List.class, JsonStack.class);
        jsonStackListAdapter = new Moshi.Builder().build().adapter(listStack);
    }

    @Test
    public void given_nullStack_when_convertingToJson_then_returnStringNull() {
        Stack stack = null;

        String json = stackJsonAdapter.toJson(stack);

        assertThat(json).isEqualTo("null");
    }

    @Test
    public void given_completeStack_when_convertingToJson_then_allStackFieldsAreRepresentedInJson() {
        Stack.Dates dates = createDates(0, 1, 2, 3);
        Stack stack = Stack.create(Id.create("testId"), "testSummary", Optional.of(Id.create("testParentId")), dates);

        String json = stackJsonAdapter.toJson(stack);

        String expectedJson = getStackAsJsonObject("testId", "testSummary", "testParentId", 0, 1, 2, 3);
        assertThat(json).isEqualTo(expectedJson);
    }



    @Test
    public void given_completeJson_when_convertingToStack_then_allStackFieldsArePopulatedCorrectly() throws IOException {
        String json = getStackAsJsonObject("testId", "testSummary", "testParentId", 0, 1, 2, 3);

        Stack stack = stackJsonAdapter.fromJson(json);

        Stack.Dates dates = createDates(0, 1, 2, 3);
        Stack expected = Stack.create(Id.create("testId"), "testSummary", Optional.of(Id.create("testParentId")), dates);
        assertThat(stack).isEqualTo(expected);
    }

    @Test
    public void given_jsonArrayOfStacks_when_convertingToListOfJsonStacks_then_returnListOfJsonStacks() throws IOException {
        String jsonList = "[" +
                getStackAsJsonObject("1", "sum1", "p1") + "," +
                getStackAsJsonObject("2", "sum2", "p2") + "," +
                getStackAsJsonObject("3", "sum3", "p3") +
                "]";

        List<JsonStack> stacks = jsonStackListAdapter.fromJson(jsonList);

        List<JsonStack> expected = Arrays.asList(
                createJsonStack("1", "sum1", "p1"),
                createJsonStack("2", "sum2", "p2"),
                createJsonStack("3", "sum3", "p3")
        );
        assertThat(stacks).isEqualTo(expected);
    }

    private static String getStackAsJsonObject(String id, String summary, String parent) {
        return "{" +
                "\"id\":\"" + id + "\"" + "," +
                "\"summary\":\"" + summary + "\"" + "," +
                "\"parent\":\"" + parent + "\"" +
                "}";
    }

    private static String getStackAsJsonObject(String id, String summary, String parent, long created, long modified, long completed, long deleted) {
        return "{" +
                "\"dates\":{" +
                "\"completed\":\"" + completed + "\"" + "," +
                "\"created\":\"" + created + "\"" + "," +
                "\"deleted\":\"" + deleted + "\"" + "," +
                "\"modified\":\"" + modified + "\"" +
                "}," +
                "\"id\":\"" + id + "\"" + "," +
                "\"labels\":[]," +
                "\"parent\":\"" + parent + "\"" + "," +
                "\"summary\":\"" + summary + "\"" +
                "}";
    }

    private static Stack.Dates createDates(long created, long modified, long completed, long deleted) {
        return Stack.Dates.create(created, modified, Optional.of(completed), Optional.of(deleted));
    }

    private static JsonStack createJsonStack(String id, String summary, String parent) {
        JsonStack json = new JsonStack();
        json.id = id;
        json.summary = summary;
        json.parentId = parent;
        return json;
    }

}
