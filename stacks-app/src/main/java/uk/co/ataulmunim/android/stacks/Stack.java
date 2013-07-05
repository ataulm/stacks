package uk.co.ataulmunim.android.stacks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.nicedistractions.shortstacks.R;

import java.util.ArrayList;
import java.util.UUID;

import uk.co.ataulmunim.android.stacks.contentprovider.Stacks;

/**
 * A Stack represents a list or listitem (a node in the tree) in Stacks.
 *
 * Every Stack has a name (title/label), and a parent (represented by its integer id in the
 * database).
 *
 * Stacks may also contain multiline notes.
 *
 * Created by ataulm on 24/06/13.
 */
public class Stack {
    public interface OnStackChangedListener {
        public void onStackChanged(Stack stack);
    }


    // _ID of the root-level stack
    public static final int DEFAULT_STACK_ID = 1;

    private static final String TAG = Stack.class.getSimpleName();
    private final int id;
    private int parent;
    private String stackName;
    private String notes;
    private boolean isStarred;
    private int actionItems;
    private final long createdDate;
    private long modifiedDate;
    private int position;

    private ArrayList<OnStackChangedListener> onChangedListeners;

    private Stack(int id, long createdDate) {
        this.id = id;
        this.createdDate = createdDate;
        onChangedListeners = new ArrayList<OnStackChangedListener>();
    }

    public int getId() {
        return id;
    }

    public String getStackName() {
        return stackName;
    }

    public String getNotes() {
        if (notes != null && notes.length() > 0) return notes;
        return "";
    }

    public static Stack getStack(Context context, int id) {
        Log.i(TAG, "Attempting to return Stack with id = " + id);
        Cursor result = context.getContentResolver().query(
                Stacks.CONTENT_URI,
                new String[]{ Stacks.PARENT, Stacks.CREATED_DATE, Stacks.MODIFIED_DATE,
                        Stacks.ACTION_ITEMS, Stacks.STACK_NAME, Stacks.NOTES, Stacks.PARENT,
                        Stacks.STARRED},
                Stacks._ID + "=" + id,
                null,
                null
        );

        if (result.getCount() == 0) return null;
        result.moveToFirst();

        Stack stack = new Stack(id, result.getLong(result.getColumnIndex(Stacks.CREATED_DATE)));
        stack.stackName = result.getString(result.getColumnIndex(Stacks.STACK_NAME));
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
     * @param stackName
     * @param parent
     * @return stackUri the Uri of the added Stack, or null if the insert failed
     */
    public static Uri add(Context context, String stackName, int parent) {
        final ContentValues values = new ContentValues();
        values.put(Stacks.UUID, UUID.randomUUID().toString());
        values.put(Stacks.STACK_NAME, stackName);
        values.put(Stacks.PARENT, parent);

        final Uri stackUri = context.getContentResolver().insert(Stacks.CONTENT_URI, values);

        if (stackUri == null) Log.e(TAG, context.getString(R.string.error_insert));
        else Log.d(TAG, "Added stack: " + stackUri.toString());

        return stackUri;
    }

    public Stack setNotes(Context context, String notes) {
        this.notes = notes;
        final ContentValues values = new ContentValues();
        values.put(Stacks.NOTES, notes);

        context.getContentResolver().update(Stacks.CONTENT_URI, values,
                Stacks._ID + "=" + id, null);

        return getStack(context, id);
    }

    public Stack setStackName(Context context, String stackName) {
        if (stackName.length() > 0) {
            this.stackName = stackName;
        } else {
            return null;
        }

        final ContentValues values = new ContentValues();
        values.put(Stacks.STACK_NAME, stackName);

        context.getContentResolver().update(Stacks.CONTENT_URI, values,
                Stacks._ID + "=" + id, null);

        return getStack(context, id);
    }

    /**
     * Adds the root level stack.
     *
     * This is created when the application is first opened, and its presence is checked whenever
     * StackView is opened without a specific
     *
     * @param context
     * @return
     */
    public static Uri createDefaultStack(Context context) {
        return add(context, "Stacks", DEFAULT_STACK_ID);
    }

    /**
     * Call this to trigger a notification to all listeners on this object to update.
     */
    public void notifyDatasetChanged() {
        for (OnStackChangedListener listener:onChangedListeners) {
            listener.onStackChanged(this);
        }
    }

    public void addOnStackChangedListener(OnStackChangedListener listener) {
        if (!onChangedListeners.contains(listener)) onChangedListeners.add(listener);
    }
}
