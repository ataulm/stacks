package com.ataulm.stacks.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

public class AndroidStack implements Parcelable {

    public static final AndroidStack ZERO = new AndroidStack("id_zero", "as_batman", "zero", "this is root stack.", 0, 0, Time.UNSET, Time.UNSET, Time.UNSET);
    public static final Builder CREATOR = new Builder();

    public final String id;
    public final String parent;
    public final String summary;
    public final String description;
    public final int leafCount;
    public final int position;
    public final Time created;
    public final Time modified;
    public final Time deleted;

    public static AndroidStack newInstance(String parent, String summary, int position) {
        if (parent == null || parent.trim().length() == 0) {
            throw new IllegalArgumentException("parent id must be specified.");
        }
        if (summary == null || summary.trim().length() == 0) {
            throw new IllegalArgumentException("summary must be specified.");
        }

        return new AndroidStack(UUID.randomUUID().toString(), parent, summary, "", 0, position, Time.now(), Time.now(), Time.UNSET);
    }

    private AndroidStack(String id, String parent, String summary, String description, int leafCount, int position, Time created, Time modified, Time deleted) {
        this.id = id;
        this.parent = parent;
        this.summary = summary;
        this.description = description;
        this.leafCount = leafCount;
        this.position = position;
        this.created = created;
        this.modified = modified;
        this.deleted = deleted;
    }

    public AndroidStack delete() {
        if (deleted.isSet()){
            return this;
        }
        return new AndroidStack(id, parent, summary, description, leafCount, position, created, Time.now(), Time.now());
    }

    public AndroidStack restore() {
        if (deleted.isSet()) {
            return new AndroidStack(id, parent, summary, description, leafCount, position, created, Time.now(), Time.UNSET);
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof AndroidStack)) {
            return false;
        }

        return (((AndroidStack) o).id.equals(id));
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(parent);
        dest.writeString(summary);
        dest.writeString(description);
        dest.writeInt(leafCount);
        dest.writeInt(position);
        dest.writeLong(created.asMillis());
        dest.writeLong(modified.asMillis());
        dest.writeLong(deleted.asMillis());
    }

    public static class Builder implements Creator<AndroidStack> {

        private String id;
        private String parent;
        private String summary;
        private String description;
        private int leafCount;
        private int position;
        private Time created;
        private Time modified;
        private Time deleted;

        public static Builder from(AndroidStack stack) {
            return new Builder().id(stack.id)
                    .parent(stack.parent)
                    .summary(stack.summary)
                    .description(stack.description)
                    .leafCount(stack.leafCount)
                    .position(stack.position)
                    .created(stack.created)
                    .modified(stack.modified)
                    .deleted(stack.deleted);
        }

        public Builder() {
            leafCount = Integer.MIN_VALUE;
            position = Integer.MIN_VALUE;
        }

        @Override
        public AndroidStack createFromParcel(Parcel stack) {
            return new Builder().id(stack.readString())
                    .parent(stack.readString())
                    .summary(stack.readString())
                    .description(stack.readString())
                    .leafCount(stack.readInt())
                    .position(stack.readInt())
                    .created(new Time(stack.readLong()))
                    .modified(new Time(stack.readLong()))
                    .deleted(new Time(stack.readLong()))
                    .build();
        }

        @Override
        public AndroidStack[] newArray(int size) {
            return new AndroidStack[size];
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder parent(String parent) {
            this.parent = parent;
            return this;
        }

        public Builder summary(String summary) {
            this.summary = summary;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder leafCount(int leafCount) {
            this.leafCount = leafCount;
            return this;
        }

        public Builder position(int position) {
            this.position = position;
            return this;
        }

        public Builder created(Time created) {
            this.created = created;
            return this;
        }

        public Builder modified(Time modified) {
            this.modified = modified;
            return this;
        }

        public Builder deleted(Time deleted) {
            this.deleted = deleted;
            return this;
        }

        public AndroidStack build() {
            if (id == null || id.trim().length() == 0) {
                throw new IllegalStateException("id is not set");
            }

            if (parent == null || parent.trim().length() == 0) {
                throw new IllegalStateException("parent id is not set");
            }

            if (summary == null || summary.trim().length() == 0) {
                throw new IllegalStateException("summary is not set");
            }

            if (description == null) {
                description = "";
            }

            if (leafCount == Integer.MIN_VALUE) {
                throw new IllegalStateException("leafCount is not set");
            }

            if (position == Integer.MIN_VALUE) {
                throw new IllegalStateException("position is not set");
            }

            if (created == null || !created.isSet()) {
                throw new IllegalStateException("created time is not set");
            }

            if (modified == null || !modified.isSet()) {
                throw new IllegalStateException("last modified time is not set");
            }

            if (deleted == null) {
                throw new IllegalStateException("deleted time is null; specify Time.UNSET if not deleted");
            }

            return new AndroidStack(id, parent, summary, description, leafCount, position, created, modified, deleted);
        }
    }

}
