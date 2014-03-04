package com.ataulm.stacks.persistence.task;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.AsyncTask;

import com.ataulm.stacks.marshallers.ContentValuesMarshaller;
import com.ataulm.stacks.model.Stack;
import com.ataulm.stacks.persistence.StackPersistCallback;
import com.ataulm.stacks.persistence.StacksProvider;

public class InsertTask extends AsyncTask<Void, Void, Boolean> {

    private final ContentResolver contentResolver;
    private final ContentValuesMarshaller marshaller;
    private final StackPersistCallback callback;
    private final Stack stack;

    public static InsertTask newInstance(ContentResolver contentResolver, Stack stack) {
        return InsertTask.newInstance(contentResolver, new NoActionCallback(), stack);
    }

    public static InsertTask newInstance(ContentResolver contentResolver, StackPersistCallback callback, Stack stack) {
        return new InsertTask(contentResolver, new ContentValuesMarshaller(new ContentValues()), callback, stack);
    }

    private InsertTask(ContentResolver contentResolver, ContentValuesMarshaller marshaller, StackPersistCallback callback, Stack stack) {
        this.contentResolver = contentResolver;
        this.marshaller = marshaller;
        this.callback = callback;
        this.stack = stack;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return contentResolver.insert(StacksProvider.URI_STACKS, marshaller.valuesForInsertFrom(stack)) != null;
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
