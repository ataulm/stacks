package com.ataulm.stacks;

import android.os.Bundle;

import com.ataulm.Optional;
import com.ataulm.stacks.stack.Stack;

class StackBundle {

    private static final String ID = "ID";
    private static final String SUMMARY = "SUMMARY";
    private static final String PARENT_ID = "PARENT_ID";
    private static final String DESCRIPTION = "DESCRIPTION";

    public Bundle createBundleFrom(Stack stack) {
        Bundle bundle = new Bundle();
        bundle.putString(ID, stack.id());
        bundle.putString(SUMMARY, stack.summary());
        bundle.putString(PARENT_ID, stack.parentId().isPresent() ? stack.parentId().get() : null);
        bundle.putString(DESCRIPTION, stack.description().isPresent() ? stack.description().get() : null);
        return bundle;
    }

    public Stack createStackFrom(Bundle bundle) {
        String id = bundle.getString(ID);
        String summary = bundle.getString(SUMMARY);

        if (id == null || id.isEmpty() || summary == null || summary.isEmpty()) {
            throw new IllegalArgumentException("Missing values, bundle was not created with " + StackBundle.class.getName());
        }
        String parentId = bundle.getString(PARENT_ID);
        String description = bundle.getString(DESCRIPTION);

        return Stack.create(id, summary, Optional.from(parentId), Optional.from(description));
    }

}
