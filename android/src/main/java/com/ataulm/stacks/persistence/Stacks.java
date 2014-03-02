package com.ataulm.stacks.persistence;

public enum Stacks {

    INSTANCE;

    public static final String TABLE_NAME = "stacks";
    public static final String ID = "_id";
    public static final String PARENT = "parent";
    public static final String SUMMARY = "summary";
    public static final String DESCRIPTION = "description";
    public static final String LEAF_COUNT = "leaf_count";
    public static final String POSITION = "position";
    public static final String CREATED = "created";
    public static final String MODIFIED = "modified";
    public static final String DELETED = "deleted";

    @Override
    public String toString() {
        return TABLE_NAME;
    }

}
