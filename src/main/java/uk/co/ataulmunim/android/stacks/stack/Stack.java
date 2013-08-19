package uk.co.ataulmunim.android.stacks.stack;

import android.content.ContentResolver;
import android.content.ContentValues;
import uk.co.ataulmunim.android.stacks.contentprovider.Stacks;

import java.util.ArrayList;
import java.util.List;

/**
 * A Stack represents a list or listitem (a node in the tree) in Stacks.
 *
 * Every Stack has a name (title/label), and a parent (represented by its integer id in the database).
 *
 * Stacks may also contain multiline notes.
 */
public final class Stack {
    public static final int DEFAULT_STACK_ID = 1;
    private static final String TAG = Stack.class.getSimpleName();

    private final List<OnStackChangedListener> onChangedListeners;
    private final int id;
    private final long createdDate;
    private long modifiedDate;
    private long deletedDate;
    private int parent;
    private int actionItems;
    private int position;
    private String stackName;
    private String notes;
    private boolean isStarred;

    private Stack(int id, String stackName, String notes, int parent, long createdDate, long modifiedDate, long deletedDate, int actionItems, boolean isStarred, int position) {
        this.id = id;
        this.stackName = stackName;
        this.parent = parent;
        this.notes = notes;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.deletedDate = deletedDate;
        this.actionItems = actionItems;
        this.isStarred = isStarred;
        this.position = position;

        onChangedListeners = new ArrayList<OnStackChangedListener>();
    }

    public interface OnStackChangedListener {
        public void onStackChanged(Stack stack);
    }

    public int getId() {
        return id;
    }

    public String getStackName() {
        return stackName;
    }

    public void setStackName(String stackName) {
        if (stackName != null && stackName.trim().length() > 0) {
            this.stackName = stackName.trim();
        }
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        if (notes != null && notes.trim().length() > 0) {
            this.notes = notes.trim();
        }
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        if (parent >= 0) {
            this.parent = parent;
        }
    }

    public boolean isStarred() {
        return isStarred;
    }

    public void setStarred(boolean isStarred) {
        this.isStarred = isStarred;
    }

    public int getActionItems() {
        return actionItems;
    }

    // TODO: formulate how ActionItems will be calculated (and modified)

    public long getCreatedDate() {
        return createdDate;
    }

    public long getModifiedDate() {
        return modifiedDate;
    }

    public long getDeletedDate() {
        return deletedDate;
    }

    public void delete() {
        this.deletedDate = System.currentTimeMillis();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        if (position >= 0) {
            this.position = position;
        }
    }

    public static class Builder {
        private int id;
        private String stackName;
        private String notes;
        private int parent;
        private boolean isStarred;
        private int actionItems;
        private long createdDate;
        private long modifiedDate;
        private long deletedDate;
        private int position;

        public Builder() {
            id = -1;
            stackName = "";
            notes = "";
            parent = -1;
            isStarred = false;
            actionItems = 0;
            createdDate = -1;
            modifiedDate = -1;
            deletedDate = -1;
            position = 0;
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder name(String stackName) {
            this.stackName = stackName;
            return this;
        }

        public Builder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public Builder parent(int parent) {
            this.parent = parent;
            return this;
        }

        public Builder starred(boolean isStarred) {
            this.isStarred = isStarred;
            return this;
        }

        public Builder actionItems(int actionItems) {
            this.actionItems = actionItems;
            return this;
        }

        public Builder created(long createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder modified(long modifiedDate) {
            this.modifiedDate = modifiedDate;
            return this;
        }

        public Builder deleted(long deletedDate) {
            this.deletedDate = deletedDate;
            return this;
        }

        public Builder position(int position) {
            this.position = position;
            return this;
        }

        public final Stack build() {
            if (id < 0) {
                throw new IllegalStateException("id must be set.");
            }

            if (parent < 0) {
                throw new IllegalStateException("id must be set.");
            }

            if (stackName == null || stackName.length() == 0) {
                throw new IllegalStateException("Stack name must be set.");
            }

            if (notes == null) {
                throw new IllegalStateException("Notes can be empty (\"\" - default) but not null.");
            }

            if (createdDate < 0) {
                throw new IllegalStateException("Created date must be set.");
            }

            if (modifiedDate < 0) {
                throw new IllegalStateException("Modified date must be set.");
            }

            if (deletedDate < 0) {
                throw new IllegalStateException("Deleted date must be set.");
            }

            Stack stack = new Stack(id, stackName, notes, parent, createdDate,
                    modifiedDate, deletedDate, actionItems, isStarred, position);

            return stack;
        }
    }

    /**
     * Adds the root level stack.
     *
     * This is created when the application is first opened, and its presence is checked whenever
     * StackView is opened without a URI specified.
     *
     * @param contentResolver
     * @return
     */
    public static boolean createDefaultStack(ContentResolver contentResolver) {
        if (StackPersistor.retrieve(contentResolver, DEFAULT_STACK_ID) == null) {
            return StackPersistor.create(contentResolver, "Stacks", DEFAULT_STACK_ID);
        }

        return false;
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
        if (!onChangedListeners.contains(listener)) {
            onChangedListeners.add(listener);
        }
    }
}
