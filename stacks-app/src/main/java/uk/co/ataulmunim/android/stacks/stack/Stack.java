package uk.co.ataulmunim.android.stacks.stack;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

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
    private static final String TAG = Stack.class.getSimpleName();


    public interface OnStackChangedListener {
        public void onStackChanged(Stack stack);
    }


    // _ID of the root-level stack
    public static final int DEFAULT_STACK_ID = 1;


    private final int id;
    private int parent;
    private String stackName;
    private String notes;
    private boolean isStarred;
    private int actionItems;
    private long createdDate;
    private long modifiedDate;
    private int position;

    private ArrayList<OnStackChangedListener> onChangedListeners;


    public static class Builder {
        private int id;
        private String stackName;
        private String notes;
        private int parent;
        private boolean isStarred;
        private int actionItems;
        private long createdDate;
        private long modifiedDate;
        private int position;

        public Builder() {
            id = -1;
            stackName = "";
            notes = "";
            parent = -1;
            isStarred = false;
            actionItems = 0;
            createdDate = 0;
            modifiedDate = 0;
            position = 0;
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setStackName(String stackName) {
            this.stackName = stackName;
            return this;
        }

        public Builder setNotes(String notes) {
            this.notes = notes;
            return this;
        }

        public Builder setParent(int parent) {
            this.parent = parent;
            return this;
        }

        public Builder setStarred(boolean isStarred) {
            this.isStarred = isStarred;
            return this;
        }

        public Builder setActionItems(int actionItems) {
            this.actionItems = actionItems;
            return this;
        }

        public Builder setCreatedDate(long createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder setModifiedDate(long modifiedDate) {
            this.modifiedDate = modifiedDate;
            return this;
        }

        public Builder setPosition(int position) {
            this.position = position;
            return this;
        }

        public final Stack build() {
            if (id < 0) {
                throw new IllegalStateException("id must be set.");
            }

            if (stackName == null ^ stackName.length() == 0) {
                throw new IllegalStateException("stackName must be set.");
            }

            Stack stack = new Stack(id, stackName);
            stack.notes = notes;
            stack.parent = parent;
            stack.createdDate = createdDate;
            stack.modifiedDate = modifiedDate;
            stack.actionItems = actionItems;
            stack.isStarred = isStarred;
            stack.position = position;

            return null;
        }
    }














    private Stack(int id, String stackName) {
        this.id = id;
        this.stackName = stackName;
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

    /**
     * Inserts a new Stack into the database.
     *
     * @param contentResolver
     * @param stackName
     * @param parent
     * @return stackUri the Uri of the added Stack, or null if the insert failed
     */
    public static Uri add(ContentResolver contentResolver, String stackName, int parent) {
        final ContentValues values = new ContentValues();
        values.put(Stacks.UUID, UUID.randomUUID().toString());
        values.put(Stacks.STACK_NAME, stackName);
        values.put(Stacks.PARENT, parent);

        final Uri stackUri = contentResolver.insert(Stacks.CONTENT_URI, values);

        return stackUri;
    }

    public Stack setNotes(ContentResolver contentResolver, String notes) {
        this.notes = notes;
        final ContentValues values = new ContentValues();
        values.put(Stacks.NOTES, notes);

        contentResolver.update(Stacks.CONTENT_URI, values,
                Stacks._ID + "=" + id, null);

        return StackPersistor.getStack(contentResolver, id);
    }

    public Stack setStackName(ContentResolver contentResolver, String stackName) {
        if (stackName.length() > 0) {
            this.stackName = stackName;
        } else {
            return null;
        }

        final ContentValues values = new ContentValues();
        values.put(Stacks.STACK_NAME, stackName);

        contentResolver.update(Stacks.CONTENT_URI, values,
                Stacks._ID + "=" + id, null);

        return StackPersistor.getStack(contentResolver, id);
    }

    /**
     * Adds the root level stack.
     *
     * This is created when the application is first opened, and its presence is checked whenever
     * StackView is opened without a specific
     *
     * @param contentResolver
     * @return
     */
    public static Uri createDefaultStack(ContentResolver contentResolver) {
        return add(contentResolver, "Stacks", DEFAULT_STACK_ID);
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
