package com.ataulm.stacks.persistence;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;

import com.ataulm.stacks.marshallers.ContentValuesFromStackMarshaller;
import com.ataulm.stacks.model.Stack;

public class StackPersistTask extends AsyncTask<Void, Void, Boolean> {

    private final ContentResolver contentResolver;
    private final ContentValuesFromStackMarshaller marshaller;
    private final StackPersistCallback callback;
    private final Stack stack;

    public static StackPersistTask newInstance(ContentResolver contentResolver, Stack stack) {
        return StackPersistTask.newInstance(contentResolver, new NoActionCallback(), stack);
    }

    public static StackPersistTask newInstance(ContentResolver contentResolver, StackPersistCallback callback, Stack stack) {
        return new StackPersistTask(contentResolver, new ContentValuesFromStackMarshaller(), callback, stack);
    }

    private StackPersistTask(ContentResolver contentResolver, ContentValuesFromStackMarshaller marshaller, StackPersistCallback callback, Stack stack) {
        this.contentResolver = contentResolver;
        this.marshaller = marshaller;
        this.callback = callback;
        this.stack = stack;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        if (update(stack)) {
            return true;
        }
        contentResolver.insert(StacksProvider.URI_STACKS, marshaller.newContentValuesForInsert(stack));
        return true;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            callback.onSuccessPersisting(stack);
        } else {
            callback.onFailurePersisting(stack);
        }
    }

    private boolean update(Stack stack) {
        ContentValues values = marshaller.newContentValuesForUpdate(stack);
        String where = "_id=?";
        String[] selectionArgs = {stack.id};

        Cursor cursor = contentResolver.query(StacksProvider.URI_STACKS, null, where, selectionArgs, null);
        if (cursor == null || cursor.getCount() == 0) {
            return false;
        }

        contentResolver.update(StacksProvider.URI_STACKS, values, where, selectionArgs);
        return true;
    }

    private static class NoActionCallback implements StackPersistCallback {

        @Override
        public void onSuccessPersisting(Stack stack) {
        }

        @Override
        public void onFailurePersisting(Stack stack) {
        }

    }

}
