package com.ataulm.stacks.persistence;

import android.content.ContentResolver;
import android.content.ContentValues;

import com.ataulm.stacks.model.Stack;

public class StackPersister {

    private final ContentResolver contentResolver;
    private final StackPersisterCallbacks callbacks;

    public StackPersister(ContentResolver contentResolver) {
        this(contentResolver, new NoActionCallbacks());
    }

    public StackPersister(ContentResolver contentResolver, StackPersisterCallbacks callbacks) {
        this.contentResolver = contentResolver;
        this.callbacks = callbacks;
    }

    public void persist(Stack stack) {
        // TODO: handle insert OR update existing, consider import from backup
        // TODO: do asynchronously and callback on success/failure
        ContentValues values = new ContentValues();

        values.put("_id", stack.id);
        values.put("parent", stack.parent);
        values.put("summary", stack.summary);
        values.put("description", stack.description);
        values.put("leaf_count", stack.leafCount);
        values.put("created", stack.created.asMillis());
        values.put("modified", stack.modified.asMillis());
        values.put("deleted", stack.deleted.asMillis());

        contentResolver.insert(StacksProvider.URI_STACKS, values);
    }

    private static class NoActionCallbacks implements StackPersisterCallbacks {

        @Override
        public void onSuccessPersisting(Stack stack) {
        }

        @Override
        public void onFailurePersisting(Stack stack) {
        }

    }

}
