package com.ataulm.stacks.persistence.task;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;

import com.ataulm.stacks.marshallers.ContentValuesFromStackMarshaller;
import com.ataulm.stacks.model.Stack;
import com.ataulm.stacks.persistence.StackPersistCallback;
import com.ataulm.stacks.persistence.StacksProvider;

public class UpdateTask extends AsyncTask<Void, Void, Boolean> {

    private final ContentResolver contentResolver;
    private final ContentValuesFromStackMarshaller marshaller;
    private final StackPersistCallback callback;
    private final Stack stack;

    public static UpdateTask newInstance(ContentResolver contentResolver, Stack stack) {
        return UpdateTask.newInstance(contentResolver, new NoActionCallback(), stack);
    }

    public static UpdateTask newInstance(ContentResolver contentResolver, StackPersistCallback callback, Stack stack) {
        return new UpdateTask(contentResolver, new ContentValuesFromStackMarshaller(), callback, stack);
    }

    private UpdateTask(ContentResolver contentResolver, ContentValuesFromStackMarshaller marshaller, StackPersistCallback callback, Stack stack) {
        this.contentResolver = contentResolver;
        this.marshaller = marshaller;
        this.callback = callback;
        this.stack = stack;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
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

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            callback.onSuccessPersisting(stack);
        } else {
            callback.onFailurePersisting(stack);
        }
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