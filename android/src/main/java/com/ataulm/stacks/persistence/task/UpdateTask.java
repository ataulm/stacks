package com.ataulm.stacks.persistence.task;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;

import com.ataulm.stacks.marshallers.ContentValuesMarshaller;
import com.ataulm.stacks.model.AndroidStack;
import com.ataulm.stacks.persistence.StackPersistCallback;
import com.ataulm.stacks.persistence.Stacks;
import com.ataulm.stacks.persistence.StacksProvider;

public class UpdateTask extends AsyncTask<Void, Void, Boolean> {

    private final ContentResolver contentResolver;
    private final ContentValuesMarshaller marshaller;
    private final StackPersistCallback callback;
    private final AndroidStack stack;

    public static UpdateTask newInstance(ContentResolver contentResolver, AndroidStack stack) {
        return UpdateTask.newInstance(contentResolver, new NoActionCallback(), stack);
    }

    public static UpdateTask newInstance(ContentResolver contentResolver, StackPersistCallback callback, AndroidStack stack) {
        return new UpdateTask(contentResolver, new ContentValuesMarshaller(new ContentValues()), callback, stack);
    }

    private UpdateTask(ContentResolver contentResolver, ContentValuesMarshaller marshaller, StackPersistCallback callback, AndroidStack stack) {
        this.contentResolver = contentResolver;
        this.marshaller = marshaller;
        this.callback = callback;
        this.stack = stack;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        ContentValues values = marshaller.valuesForUpdateFrom(stack);
        String where = Stacks.ID + "=?";
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
        public void onSuccessPersisting(AndroidStack stack) {
        }

        @Override
        public void onFailurePersisting(AndroidStack stack) {
        }

    }

}
