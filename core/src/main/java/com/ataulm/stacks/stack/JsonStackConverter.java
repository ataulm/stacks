package com.ataulm.stacks.stack;

import com.ataulm.Optional;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import javax.annotation.Nullable;

public class JsonStackConverter {

    @FromJson
    @Nullable
    public Stack convert(@Nullable JsonStack jsonStack) {
        if (jsonStack == null || emptyString(jsonStack.id) || emptyString(jsonStack.summary)) {
            return null;
        }

        Id id = Id.create(jsonStack.id);
        Optional<Id> parentId;
        if (emptyString(jsonStack.parentId)) {
            parentId = Optional.absent();
        } else {
            parentId = Optional.of(Id.create(jsonStack.parentId));
        }

        return Stack.create(id, jsonStack.summary, parentId);
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
        json.id = stack.id().value();
        json.summary = stack.summary();
        json.parentId = stack.parentId().isPresent() ? stack.parentId().get().value() : null;
        return json;
    }

}
