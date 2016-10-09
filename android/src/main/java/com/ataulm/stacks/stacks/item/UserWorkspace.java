package com.ataulm.stacks.stacks.item;

import android.content.Context;
import android.content.SharedPreferences;

import com.ataulm.stacks.BuildConfig;
import com.ataulm.stacks.stack.Id;

/**
 * keys in here can change no problem as it's meant to only be stable while app is running (only store temp things here)
 */
public class UserWorkspace {

    private static final String KEY_USER_WORKSPACE = ".USER_WORKSPACE";

    private static final String KEY_ID = "ID";
    private static final String KEY_SUMMARY = "SUMMARY";

    private final SharedPreferences sharedPreferences;

    public static UserWorkspace create(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID + KEY_USER_WORKSPACE, Context.MODE_PRIVATE);
        return new UserWorkspace(sharedPreferences);
    }

    private UserWorkspace(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    void updateStackBeingEdited(Id id, String summary) {
        sharedPreferences.edit()
                .putString(KEY_ID, id.value())
                .putString(KEY_SUMMARY, summary)
                .apply();
    }

    boolean stackIsBeingEdited() {
        return sharedPreferences.contains(KEY_ID) && sharedPreferences.contains(KEY_SUMMARY);
    }

    Id getStackBeingEdited() {
        String value = sharedPreferences.getString(KEY_ID, null);
        return Id.create(checkExists(value));
    }

    String getSummary() {
        String summary = sharedPreferences.getString(KEY_SUMMARY, null);
        return checkExists(summary);
    }

    private String checkExists(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalStateException("expected value to be present, did you check if a stack is being edited?");
        }
        return value;
    }

}
