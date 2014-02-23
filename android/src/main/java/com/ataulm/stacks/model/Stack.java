package com.ataulm.stacks.model;

import java.util.UUID;

public class Stack {

    public final String id;
    public final String parent;
    public final String summary;
    public final String description;
    public final int leafCount;
    public final int position;
    public final Time created;
    public final Time modified;
    public final Time deleted;

    private Stack(String id, String parent, String summary, String description, int leafCount, int position, Time created, Time modified, Time deleted) {
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

    public static Stack newInstance(String parent, String summary, int position) {
        if (parent == null || parent.trim().length() == 0) {
            throw new IllegalArgumentException("parent id must be specified.");
        }
        if (summary == null || summary.trim().length() == 0) {
            throw new IllegalArgumentException("summary must be specified.");
        }

        return new Stack(UUID.randomUUID().toString(), parent, summary, "", 0, position, Time.now(), Time.now(), Time.UNSET);
    }

    public Stack delete() {
        if (deleted.isSet()){
            return this;
        }
        return new Stack(id, parent, summary, description, leafCount, position, created, Time.now(), Time.now());
    }

    public Stack restore() {
        if (deleted.isSet()) {
            return new Stack(id, parent, summary, description, leafCount, position, created, Time.now(), Time.UNSET);
        }
        return this;
    }

    public static class Builder {

        private String id;
        private String parent;
        private String summary;
        private String description;
        private int leafCount;
        private int position;
        private Time created;
        private Time modified;
        private Time deleted;

        public Builder() {
            leafCount = Integer.MIN_VALUE;
            position = Integer.MIN_VALUE;
        }

        public static Builder from(Stack stack) {
            return new Builder().id(stack.id)
                    .parent(stack.parent)
                    .summary(stack.summary)
                    .description(stack.description)
                    .leafCount(stack.leafCount)
                    .created(stack.created)
                    .modified(stack.modified)
                    .deleted(stack.deleted);
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

        public Stack build() {
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

            return new Stack(id, parent, summary, description, leafCount, position, created, modified, deleted);
        }

    }

}
