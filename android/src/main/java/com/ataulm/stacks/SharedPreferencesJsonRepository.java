package com.ataulm.stacks;

import android.content.Context;
import android.content.SharedPreferences;

import com.ataulm.Optional;
import com.ataulm.stacks.stack.JsonRepository;

public final class SharedPreferencesJsonRepository implements JsonRepository {

    private static final String KEY_STACKS = "STACKS";

    private final SharedPreferences sharedPreferences;

    public static SharedPreferencesJsonRepository create(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID + ".LOCALREPO", Context.MODE_PRIVATE);
        return new SharedPreferencesJsonRepository(sharedPreferences);
    }

    private SharedPreferencesJsonRepository(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public Optional<String> getStacks() {
        if (sharedPreferences.contains(KEY_STACKS)) {
            return Optional.of(sharedPreferences.getString(KEY_STACKS, null));
        } else {
            return Optional.absent();
        }
    }

    @Override
    public void persistStacks(String json) {
        sharedPreferences.edit().putString(KEY_STACKS, json).apply();
    }

}
