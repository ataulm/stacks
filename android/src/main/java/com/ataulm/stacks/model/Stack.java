package com.ataulm.stacks.model;

public class Stack {

    public final String id;
    public final String parent;
    public final String summary;
    public final String description;
    public final int leafCount;
    public final Time created;
    public final Time modified;
    public final Time deleted;

    private Stack(String id, String parent, String summary, String description, int leafCount, Time created, Time modified, Time deleted) {
        this.id = id;
        this.parent = parent;
        this.summary = summary;
        this.description = description;
        this.leafCount = leafCount;
        this.created = created;
        this.modified = modified;
        this.deleted = deleted;
    }

    public static Stack newInstance(String parent, String summary) {
        if (parent == null || parent.trim().length() == 0) {
            throw new IllegalArgumentException("parent id must be specified.");
        }
        if (summary == null || summary.trim().length() == 0) {
            throw new IllegalArgumentException("summary must be specified.");
        }

        return new Stack("", parent, summary, "", 0, Time.now(), Time.now(), Time.UNSET);
    }

    public Stack delete() {
        if (deleted.isSet()){
            return this;
        }
        return new Stack(id, parent, summary, description, leafCount, created, Time.now(), Time.now());
    }

    public Stack restore() {
        if (deleted.isSet()) {
            return new Stack(id, parent, summary, description, leafCount, created, Time.now(), Time.UNSET);
        }
        return this;
    }

    public static class Builder {

        private String id;
        private String parent;
        private String summary;
        private String description;
        private int leafCount;
        private Time created;
        private Time modified;
        private Time deleted;

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

            if (created == null || !created.isSet()) {
                throw new IllegalStateException("created time is not set");
            }

            if (modified == null || !modified.isSet()) {
                throw new IllegalStateException("last modified time is not set");
            }

            if (deleted == null) {
                throw new IllegalStateException("deleted time is null; specify Time.UNSET if not deleted");
            }

            return new Stack(id, parent, summary, description, leafCount, created, modified, deleted);
        }

    }

}
