package uk.co.ataulmunim.android.stacks.stack;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import uk.co.ataulmunim.android.stacks.contentprovider.Stacks;

import java.util.UUID;

/**
 * Handles the insertion, updates and retrieval of Stacks to persistent storage.
 */
public final class StackPersistor {
    private static final String TAG = StackPersistor.class.getSimpleName();

    /**
     * Retrieve Stack with given ID from persistent storage.
     *
     * @param contentResolver
     * @param id
     * @return
     */
    public static final Stack retrieve(ContentResolver contentResolver, int id) {
        Log.i(TAG, "Retrieving Stack with id: " + id);
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

        Stack stack = new Stack.Builder()
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

        return stack;
    }

    /**
     * Persists updates for an existing Stack.
     *
     * @param contentResolver
     * @param stack
     */
    public static final void persist(ContentResolver contentResolver, Stack stack) {
        Log.i(TAG, "Persisting Stack with id: " + stack.getId());

        final ContentValues values = new ContentValues();
        values.put(Stacks.STACK_NAME, stack.getStackName());
        values.put(Stacks.NOTES, stack.getNotes());
        values.put(Stacks.PARENT, stack.getParent());
        values.put(Stacks.DELETED, stack.getDeletedDate());
        values.put(Stacks.MODIFIED_DATE, System.currentTimeMillis());
        values.put(Stacks.STARRED, stack.isStarred());
        values.put(Stacks.LOCAL_SORT, stack.getPosition());
        values.put(Stacks.ACTION_ITEMS, stack.getActionItems());

        int updated = contentResolver.update(Stacks.CONTENT_URI, values,
                Stacks._ID + "=" + stack.getId(), null);

        if (updated > 0) {
            stack.notifyDatasetChanged();
        }
    }

    /**
     * Adds new Stack to persistent storage.
     *
     * @param contentResolver
     * @param stackName
     * @param parent
     * @return inserted
     */
    public static final boolean create(ContentResolver contentResolver,
                                       String stackName, int parent) {
        Log.i(TAG, "Adding new Stack: " + stackName);
        final ContentValues values = new ContentValues();
        values.put(Stacks.UUID, UUID.randomUUID().toString());
        values.put(Stacks.STACK_NAME, stackName);
        values.put(Stacks.PARENT, parent);

        return (contentResolver.insert(Stacks.CONTENT_URI, values) != null);
    }


}
