package com.ataulm.stacks.model;

public class Stack {

    public final String id;
    public final String parent;
    public final String summary;
    public final String description;
    public final Time created;
    public final Time modified;
    public final Time deleted;

    private Stack(String id, String parent, String summary, String description, Time created, Time modified, Time deleted) {
        this.id = id;
        this.parent = parent;
        this.summary = summary;
        this.description = description;
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

        return new Stack("", parent, summary, "", Time.now(), Time.now(), Time.UNSET);
    }

    public Stack delete() {
        if (deleted.isSet()){
            return this;
        }
        return new Stack(id, parent, summary, description, created, Time.now(), Time.now());
    }

    public Stack restore() {
        if (deleted.isSet()) {
            return new Stack(id, parent, summary, description, created, Time.now(), Time.UNSET);
        }
        return this;
    }

}
