package com.ataulm.stacks;

import android.os.Bundle;

import com.ataulm.Optional;
import com.ataulm.stacks.stack.Id;
import com.ataulm.stacks.stack.Stack;

class StackBundle {

    private static final String ID = "ID";
    private static final String SUMMARY = "SUMMARY";
    private static final String PARENT_ID = "PARENT_ID";

    public Bundle createBundleFrom(Stack stack) {
        Bundle bundle = new Bundle();
        bundle.putString(ID, stack.id().value());
        bundle.putString(SUMMARY, stack.summary());
        bundle.putString(PARENT_ID, stack.parentId().isPresent() ? stack.parentId().get().value() : null);
        return bundle;
    }

    public Optional<Stack> createStackFrom(Bundle bundle) {
        String id = bundle.getString(ID);
        String summary = bundle.getString(SUMMARY);

        if (id == null || id.isEmpty() || summary == null || summary.isEmpty()) {
            return Optional.absent();
        }
        Optional<Id> parentId = bundle.containsKey(PARENT_ID)
                ? Optional.of(Id.create(bundle.getString(PARENT_ID)))
                : Optional.<Id>absent();

        return Optional.of(Stack.create(Id.create(id), summary, parentId));
    }

}
