package uk.co.ataulmunim.android.stacks;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.nicedistractions.shortstacks.R;

import java.util.UUID;

import uk.co.ataulmunim.android.stacks.contentprovider.Stacks;

/**
 * A Stack represents a list or listitem (a node in the tree) in Stacks.
 *
 * Every Stack has a shortcode (title/label), and a parent (represented by its integer id in the
 * database).
 *
 * Stacks may also contain multiline notes.
 *
 * Created by ataulm on 24/06/13.
 */
public class Stack {
    private static final String TAG = "STACK";
    private final int id;
    private int parent;
    private String shortCode;
    private String notes;
    private boolean isStarred;
    private int actionItems;
    private final long createdDate;
    private long modifiedDate;
    private int position;


    private Stack(int id, long createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public int getId() {
        return id;
    }

    public int getPosition() {
        return position;
    }

    public int getParent() {
        return parent;
    }

    public String getShortCode() {
        return shortCode;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public long getModifiedDate() {
        return modifiedDate;
    }

    public int getActionItems() {
        return actionItems;
    }

    public String getNotes() {
        return notes;
    }

    public boolean isStarred() {
        return isStarred;
    }

    public static Stack getStack(Context context, int id) {
        Log.i(TAG, "Attempting to return Stack with id = " + id);
        Cursor result = context.getContentResolver().query(
                Stacks.CONTENT_URI,
                new String[]{ Stacks.PARENT, Stacks.CREATED_DATE, Stacks.MODIFIED_DATE,
                        Stacks.ACTION_ITEMS, Stacks.SHORTCODE, Stacks.NOTES, Stacks.PARENT,
                        Stacks.STARRED},
                Stacks._ID + "=" + id,
                null,
                null
        );

        if (result.getCount() == 0) return null;
        result.moveToFirst();

        Stack stack = new Stack(id, result.getLong(result.getColumnIndex(Stacks.CREATED_DATE)));
        stack.shortCode = result.getString(result.getColumnIndex(Stacks.SHORTCODE));
        stack.parent = result.getInt(result.getColumnIndex(Stacks.PARENT));
        stack.notes = result.getString(result.getColumnIndex(Stacks.NOTES));
        stack.isStarred = result.getInt(result.getColumnIndex(Stacks.STARRED)) == 1 ? true :false;
        stack.actionItems = result.getInt(result.getColumnIndex(Stacks.ACTION_ITEMS));
        stack.modifiedDate = result.getLong(result.getColumnIndex(Stacks.MODIFIED_DATE));

        return stack;
    }

    /**
     * Inserts a new Stack into the database.
     *
     * @param context the Activity
     * @param shortCode
     * @param parent
     * @return stackUri the Uri of the added Stack, or null if the insert failed
     */
    public static Uri add(Context context, String shortCode, int parent) {
        final ContentValues values = new ContentValues();
        values.put(Stacks.UUID, UUID.randomUUID().toString());
        values.put(Stacks.SHORTCODE, shortCode);
        values.put(Stacks.PARENT, parent);

        final Uri stackUri = context.getContentResolver().insert(Stacks.CONTENT_URI, values);

        if (stackUri == null) Log.e(TAG, context.getString(R.string.error_insert));
        else Log.d(TAG, "Added stack: " + stackUri.toString());

        return stackUri;
    }


}
