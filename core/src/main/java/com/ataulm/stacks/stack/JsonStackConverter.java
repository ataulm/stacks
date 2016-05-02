package com.ataulm.stacks.stack;

import com.ataulm.Optional;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class JsonStackConverter {

    // TODO: should provide Id and Label jsonConverters, not do it manually

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

        if (jsonStack.labels == null || jsonStack.labels.isEmpty()) {
            return Stack.create(id, jsonStack.summary, parentId);
        } else {
            return Stack.create(id, jsonStack.summary, parentId, makeLabels(jsonStack.labels));
        }
    }

    private static Set<Label> makeLabels(Set<String> stringLabels) {
        Set<Label> labels = new HashSet<>(stringLabels.size());
        for (String label : stringLabels) {
            labels.add(Label.create(label));
        }
        return labels;
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
        json.labels = unmakeLabels(stack.labels());
        return json;
    }

    private static Set<String> unmakeLabels(Set<Label> labels) {
        Set<String> stringLabels = new HashSet<>(labels.size());
        for (Label label : labels) {
            stringLabels.add(label.value());
        }
        return stringLabels;
    }

}
