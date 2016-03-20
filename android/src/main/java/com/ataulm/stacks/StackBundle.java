package com.ataulm.stacks;

import android.os.Bundle;

import com.ataulm.Optional;
import com.ataulm.stacks.stack.Stack;

class StackBundle {

    private static final String ID = "ID";
    private static final String SUMMARY = "SUMMARY";
    private static final String PARENT_ID = "PARENT_ID";

    public Bundle createBundleFrom(Stack stack) {
        Bundle bundle = new Bundle();
        bundle.putString(ID, stack.id());
        bundle.putString(SUMMARY, stack.summary());
        bundle.putString(PARENT_ID, stack.parentId().isPresent() ? stack.parentId().get() : null);
        return bundle;
    }

    public Optional<Stack> createStackFrom(Bundle bundle) {
        String id = bundle.getString(ID);
        String summary = bundle.getString(SUMMARY);

        if (id == null || id.isEmpty() || summary == null || summary.isEmpty()) {
            return Optional.absent();
        }
        String parentId = bundle.getString(PARENT_ID);

        return Optional.of(Stack.create(id, summary, Optional.from(parentId)));
    }

}
