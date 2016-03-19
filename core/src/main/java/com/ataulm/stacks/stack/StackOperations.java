package com.ataulm.stacks.stack;

import com.ataulm.stacks.Optional;
import com.squareup.moshi.JsonAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StackOperations {

    private final JsonRepository jsonRepository;
    private final JsonAdapter<List<JsonStack>> jsonAdapter;
    private final JsonStackConverter jsonStackConverter;

    public StackOperations(JsonRepository jsonRepository, JsonAdapter<List<JsonStack>> jsonAdapter, JsonStackConverter jsonStackConverter) {
        this.jsonRepository = jsonRepository;
        this.jsonAdapter = jsonAdapter;
        this.jsonStackConverter = jsonStackConverter;
    }

    public List<Stack> getStacks() {
        Optional<String> stacks = jsonRepository.getStacks();
        if (stacks.isPresent()) {
            List<JsonStack> jsonStacks = convertFromJson(stacks.get());
            return convertToStacks(jsonStacks);
        } else {
            return Collections.emptyList();
        }
    }

    private List<JsonStack> convertFromJson(String json) {
        try {
            return jsonAdapter.fromJson(json);
        } catch (IOException ignored) {
        }
        return Collections.emptyList();
    }

    private List<Stack> convertToStacks(List<JsonStack> jsonStacks) {
        List<Stack> stacks = new ArrayList<>(jsonStacks.size());
        for (JsonStack jsonStack : jsonStacks) {
            Stack stack = jsonStackConverter.convert(jsonStack);
            if (stack != null) {
                stacks.add(stack);
            }
        }
        return stacks;
    }

    public void persist(List<Stack> stacks) {
        List<JsonStack> jsonStacks = convertToJsonStacks(stacks);
        String json = jsonAdapter.toJson(jsonStacks);
        jsonRepository.persistStacks(json);
    }

    private List<JsonStack> convertToJsonStacks(List<Stack> stacks) {
        List<JsonStack> jsonStacks = new ArrayList<>(stacks.size());
        for (Stack stack : stacks) {
            JsonStack jsonStack = jsonStackConverter.convert(stack);
            if (jsonStack != null) {
                jsonStacks.add(jsonStack);
            }
        }
        return jsonStacks;
    }

}
