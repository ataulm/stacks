package com.ataulm.nists.nist;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import com.ataulm.nists.contentprovider.Stacks;

import java.util.UUID;

/**
 * Handles the insertion, updates and retrieval of Stacks to persistent storage.
 */
public final class NistPersistor {
    private static final String TAG = NistPersistor.class.getSimpleName();

    /**
     * Retrieve Nist with given ID from persistent storage.
     *
     * @param contentResolver
     * @param id
     * @return
     */
    public static final Nist retrieve(ContentResolver contentResolver, long id) {
        Log.i(TAG, "Retrieving Nist with id: " + id);
        Cursor result = contentResolver.query(
                Stacks.CONTENT_URI,
                new String[] {
                        Stacks.PARENT,
                        Stacks.CREATED_DATE,
                        Stacks.MODIFIED_DATE,
                        Stacks.DELETED,
                        Stacks.ACTION_ITEMS,
                        Stacks.STACK_NAME,
                        Stacks.LOCAL_SORT,
                        Stacks.NOTES,
                        Stacks.STARRED },
                Stacks._ID + "=" + id,
                null,
                null
        );

        if (result.getCount() == 0) return null;
        result.moveToFirst();

        Nist nist = new Nist.Builder()
                .id(id)
                .name(result.getString(result.getColumnIndex(Stacks.STACK_NAME)))
                .notes(result.getString(result.getColumnIndex(Stacks.NOTES)))
                .parent(result.getInt(result.getColumnIndex(Stacks.PARENT)))
                .deleted(result.getLong(result.getColumnIndex(Stacks.DELETED)))
                .created(result.getLong(result.getColumnIndex(Stacks.CREATED_DATE)))
                .modified(result.getLong(result.getColumnIndex(Stacks.MODIFIED_DATE)))
                .starred(result.getInt(result.getColumnIndex(Stacks.STARRED)) == 1)
                .position(result.getInt(result.getColumnIndex(Stacks.LOCAL_SORT)))
                .actionItems(result.getInt(result.getColumnIndex(Stacks.ACTION_ITEMS)))
                .build();

        return nist;
    }

    /**
     * Persists updates for an existing Nist.
     *
     * @param contentResolver
     * @param nist
     */
    public static final void persist(ContentResolver contentResolver, Nist nist) {
        Log.i(TAG, "Persisting Nist with id: " + nist.getId());

        final ContentValues values = new ContentValues();
        values.put(Stacks.STACK_NAME, nist.getStackName());
        values.put(Stacks.NOTES, nist.getNotes());
        values.put(Stacks.PARENT, nist.getParent());
        values.put(Stacks.DELETED, nist.getDeletedDate());
        values.put(Stacks.MODIFIED_DATE, System.currentTimeMillis());
        values.put(Stacks.STARRED, nist.isStarred());
        values.put(Stacks.LOCAL_SORT, nist.getPosition());
        values.put(Stacks.ACTION_ITEMS, nist.getActionItems());

        int updated = contentResolver.update(Stacks.CONTENT_URI, values,
                Stacks._ID + "=" + nist.getId(), null);

        if (updated > 0) {
            nist.notifyDatasetChanged();
        }
    }

    /**
     * Adds new Nist to persistent storage.
     *
     * @param contentResolver
     * @param stackName
     * @param parent
     * @return inserted
     */
    public static final boolean create(ContentResolver contentResolver,
                                       String stackName, long parent) {
        Log.i(TAG, "Adding new Nist: " + stackName);
        final ContentValues values = new ContentValues();
        values.put(Stacks.UUID, UUID.randomUUID().toString());
        values.put(Stacks.STACK_NAME, stackName);
        values.put(Stacks.PARENT, parent);

        return (contentResolver.insert(Stacks.CONTENT_URI, values) != null);
    }


}
