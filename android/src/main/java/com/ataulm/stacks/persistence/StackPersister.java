package com.ataulm.stacks.persistence;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.AsyncTask;

import com.ataulm.stacks.model.Stack;
import com.novoda.notils.logger.Novogger;

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

    public void persist(final Stack stack) {
        new AsyncTask<Stack, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Stack... params) {
                try {
                    if (update(stack)) {
                        return true;
                    }
                    contentResolver.insert(StacksProvider.URI_STACKS, contentValuesFrom(stack));
                } catch (Exception e) {
                    // TODO: Add BugSense/Crashlytics here
                    Novogger.e("Failed to persist stack", e);
                    return false;
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {
                    callbacks.onSuccessPersisting(stack);
                } else {
                    callbacks.onFailurePersisting(stack);
                }
            }

            private boolean update(Stack stack) {
                return contentResolver.update(StacksProvider.URI_STACKS, contentValuesFrom(stack), "_id=?", new String[]{stack.id}) == 1;
            }

        }.execute(stack);
    }

    private ContentValues contentValuesFrom(Stack stack) {
        ContentValues values = new ContentValues();

        values.put("_id", stack.id);
        values.put("parent", stack.parent);
        values.put("summary", stack.summary);
        values.put("description", stack.description);
        values.put("leaf_count", stack.leafCount);
        values.put("position", stack.position);
        values.put("created", stack.created.asMillis());
        values.put("modified", stack.modified.asMillis());
        values.put("deleted", stack.deleted.asMillis());

        return values;
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
