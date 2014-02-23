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
        // TODO: add all stack values to content values
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
