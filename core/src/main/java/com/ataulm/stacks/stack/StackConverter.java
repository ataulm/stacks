package com.ataulm.stacks.stack;

import com.ataulm.stacks.Optional;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import javax.annotation.Nullable;

public class StackConverter {

    @FromJson
    @Nullable
    public Stack convert(@Nullable JsonStack jsonStack) {
        if (jsonStack == null || emptyString(jsonStack.id) || emptyString(jsonStack.summary)) {
            return null;
        }

        Optional<String> parentId;
        if (emptyString(jsonStack.parentId)) {
            parentId = Optional.absent();
        } else {
            parentId = Optional.of(jsonStack.parentId);
        }

        Optional<String> description;
        if (emptyString(jsonStack.description)) {
            description = Optional.absent();
        } else {
            description = Optional.of(jsonStack.description);
        }

        return Stack.create(jsonStack.id, jsonStack.summary, parentId, description);
    }

    private static boolean emptyString(String value) {
        return value == null || value.isEmpty();
    }

    @ToJson
    @Nullable
    public JsonStack convert(@Nullable Stack stack) {
        if (stack == null) {
            return null;
        }

        JsonStack json = new JsonStack();
        json.id = stack.id();
        json.summary = stack.summary();
        json.parentId = stack.parentId().isPresent() ? stack.parentId().get() : null;
        json.description = stack.description().isPresent() ? stack.description().get() : null;
        return json;
    }

}
