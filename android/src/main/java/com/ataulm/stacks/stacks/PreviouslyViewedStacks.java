package com.ataulm.stacks.stacks;

import android.os.Bundle;

import com.ataulm.Optional;
import com.ataulm.stacks.BuildConfig;
import com.ataulm.stacks.stack.Id;

import java.util.ArrayList;
import java.util.List;

public final class PreviouslyViewedStacks {

    private static final String KEY_RAW_IDS = BuildConfig.APPLICATION_ID + ".KEY_RAW_IDS";

    private final List<Optional<Id>> stacks;
    private final IdMapper idMapper;

    public static PreviouslyViewedStacks create(Bundle savedInstanceState) {
        String[] rawIds = extractRawIdsFrom(savedInstanceState);

        IdMapper idMapper = new IdMapper();
        List<Optional<Id>> ids = idMapper.mapRawIdsToIds(rawIds);
        return new PreviouslyViewedStacks(ids, idMapper);
    }

    private static String[] extractRawIdsFrom(Bundle savedInstanceState) {
        if (savedInstanceState == null || !savedInstanceState.containsKey(KEY_RAW_IDS)) {
            return new String[0];
        }
        return savedInstanceState.getStringArray(KEY_RAW_IDS);
    }

    PreviouslyViewedStacks(List<Optional<Id>> stacks, IdMapper idMapper) {
        this.stacks = stacks;
        this.idMapper = idMapper;
    }

    public void add(Optional<Id> stack) {
        stacks.add(stack);
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        String[] rawIds = idMapper.mapIdsToRawIds(stacks);
        bundle.putStringArray(KEY_RAW_IDS, rawIds);
        return bundle;
    }

    static class IdMapper {

        List<Optional<Id>> mapRawIdsToIds(String[] rawIds) {
            List<Optional<Id>> ids = new ArrayList<>(rawIds.length);
            for (String rawId : rawIds) {
                Optional<Id> id = rawId == null ? Optional.<Id>absent() : Optional.of(Id.create(rawId));
                ids.add(id);
            }
            return ids;
        }

        String[] mapIdsToRawIds(List<Optional<Id>> ids) {
            String[] rawIds = new String[ids.size()];
            for (int i = 0; i < ids.size(); i++) {
                rawIds[i] = ids.get(i).isPresent() ? ids.get(i).get().value() : null;
            }
            return rawIds;
        }

    }

}
