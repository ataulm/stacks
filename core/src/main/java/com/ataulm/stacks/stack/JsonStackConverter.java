package com.ataulm.stacks.stack;

import com.ataulm.Optional;
import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class JsonStackConverter {

    // TODO: should provide Id, Dates and Label jsonConverters, not do it manually

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
            return Stack.create(id, jsonStack.summary, parentId, makeDates(jsonStack, jsonStack.dates));
        } else {
            return Stack.create(id, jsonStack.summary, parentId, makeDates(jsonStack, jsonStack.dates), makeLabels(jsonStack.labels));
        }
    }

    private static Stack.Dates makeDates(JsonStack stack, @Nullable JsonStack.Dates jsonDates) {
        if (jsonDates == null) {
            return Stack.Dates.create(System.currentTimeMillis());
        }
        try {
            long created = Long.parseLong(jsonDates.created);
            long modified = Long.parseLong(jsonDates.modified);
            Optional<Long> completed = jsonDates.completed == null ? Optional.<Long>absent() : Optional.of(Long.parseLong(jsonDates.completed));
            Optional<Long> deleted = jsonDates.deleted == null ? Optional.<Long>absent() : Optional.of(Long.parseLong(jsonDates.deleted));
            return Stack.Dates.create(
                    created,
                    modified,
                    completed,
                    deleted
            );
        } catch (NumberFormatException e) {
            System.out.println(stack);
            throw e;
        }
    }

    private static Labels makeLabels(Set<String> stringLabels) {
        Set<Label> labels = new HashSet<>(stringLabels.size());
        for (String label : stringLabels) {
            labels.add(Label.create(label));
        }
        return Labels.create(labels);
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
        json.dates = unmakeDates(stack.dates());
        json.labels = unmakeLabels(stack.labels());
        return json;
    }

    private static JsonStack.Dates unmakeDates(Stack.Dates dates) {
        JsonStack.Dates jsonDates = new JsonStack.Dates();
        jsonDates.created = String.valueOf(dates.created());
        jsonDates.modified = String.valueOf(dates.modified());
        jsonDates.completed = dates.completed().isPresent() ? String.valueOf(dates.completed().get()) : null;
        jsonDates.deleted = dates.deleted().isPresent() ? String.valueOf(dates.deleted().get()) : null;
        return jsonDates;
    }

    private static Set<String> unmakeLabels(Labels labels) {
        Set<String> stringLabels = new HashSet<>(labels.size());
        for (Label label : labels) {
            stringLabels.add(label.value());
        }
        return stringLabels;
    }

}
