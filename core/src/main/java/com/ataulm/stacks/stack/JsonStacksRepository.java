package com.ataulm.stacks.stack;

import com.ataulm.Optional;
import com.squareup.moshi.JsonAdapter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class JsonStacksRepository {

    private final JsonRepository jsonRepository;
    private final JsonAdapter<List<JsonStack>> jsonAdapter;

    public JsonStacksRepository(JsonRepository jsonRepository, JsonAdapter<List<JsonStack>> jsonAdapter) {
        this.jsonRepository = jsonRepository;
        this.jsonAdapter = jsonAdapter;
    }

    public List<JsonStack> getJsonStacks() {
        Optional<String> stacks = jsonRepository.getStacks();
        if (stacks.isPresent()) {
            return convertFromJson(stacks.get());
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

    public void persist(List<JsonStack> jsonStacks) {
        String json = jsonAdapter.toJson(jsonStacks);
        jsonRepository.persistStacks(json);
    }

}
