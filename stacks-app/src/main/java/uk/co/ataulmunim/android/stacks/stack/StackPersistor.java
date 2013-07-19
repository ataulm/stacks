package uk.co.ataulmunim.android.stacks.stack;

import android.content.ContentResolver;
import android.database.Cursor;
import android.util.Log;
import uk.co.ataulmunim.android.stacks.contentprovider.Stacks;

/**
 * Handles the insertion, updates and retrieval of Stacks to persistent storage.
 */
public class StackPersistor {
    private static final String TAG = StackPersistor.class.getSimpleName();

    /**
     * Retrieve Stack with given ID from persistent storage.
     *
     * @param contentResolver
     * @param id
     * @return
     */
    public static Stack getStack(ContentResolver contentResolver, int id) {
        Log.i(TAG, "Attempting to return Stack with id = " + id);
        Cursor result = contentResolver.query(
                Stacks.CONTENT_URI,
                new String[] {
                        Stacks.PARENT,
                        Stacks.CREATED_DATE,
                        Stacks.MODIFIED_DATE,
                        Stacks.ACTION_ITEMS,
                        Stacks.STACK_NAME,
                        Stacks.NOTES,
                        Stacks.PARENT,
                        Stacks.STARRED },
                Stacks._ID + "=" + id,
                null,
                null
        );

        if (result.getCount() == 0) return null;
        result.moveToFirst();

        Stack stack = new Stack.Builder()
                .setId(id)
                .setStackName(result.getString(result.getColumnIndex(Stacks.STACK_NAME)))
                .setNotes(result.getString(result.getColumnIndex(Stacks.NOTES)))
                .setParent(result.getInt(result.getColumnIndex(Stacks.PARENT)))
                .setCreatedDate(result.getLong(result.getColumnIndex(Stacks.CREATED_DATE)))
                .setModifiedDate(result.getLong(result.getColumnIndex(Stacks.MODIFIED_DATE)))
                .setStarred(result.getInt(result.getColumnIndex(Stacks.STARRED)) == 1 ? true :false)
                .setPosition(result.getInt(result.getColumnIndex(Stacks.LOCAL_SORT)))
                .setActionItems(result.getInt(result.getColumnIndex(Stacks.ACTION_ITEMS)))
                .build();

        return stack;
    }


}
