package com.ataulm.stacks.persistence.task;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;

import com.ataulm.stacks.model.Stack;
import com.ataulm.stacks.persistence.Stacks;
import com.ataulm.stacks.persistence.StacksProvider;

import java.util.ArrayList;
import java.util.List;

public class GetAncestorsTask extends AsyncTask<Void, Void, List<String>> {

    private final ContentResolver contentResolver;
    private final Callback callback;
    private final Stack stack;

    public static GetAncestorsTask newInstance(ContentResolver contentResolver, Callback callback, Stack stack) {
        return new GetAncestorsTask(contentResolver, callback, stack);
    }

    private GetAncestorsTask(ContentResolver contentResolver, Callback callback, Stack stack) {
        this.contentResolver = contentResolver;
        this.callback = callback;
        this.stack = stack;
    }

    @Override
    protected List<String> doInBackground(Void... params) {
        List<String> ancestorIds = new ArrayList<String>();
        String id = stack.id;
        do {
            ancestorIds.add(id);
            id = getParent(id);
        } while (!Stack.ZERO.parent.equals(id));

        return ancestorIds;
    }

    private String getParent(String stackId) {
        String[] projection = {Stacks.ID, Stacks.PARENT};
        Cursor query = contentResolver.query(StacksProvider.URI_STACKS, projection, Stacks.ID + "=?", new String[]{stackId}, null);
        query.moveToFirst();
        return query.getString(query.getColumnIndex(Stacks.PARENT));
    }

    @Override
    protected void onPostExecute(List<String> ancestorIds) {
        callback.onAncestorsRetrieved(ancestorIds);
    }

    public interface Callback {

        void onAncestorsRetrieved(List<String> ancestorIds);

    }

}
