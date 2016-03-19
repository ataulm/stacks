package com.ataulm.stacks.marshallers;

import android.database.Cursor;

import com.ataulm.stacks.model.AndroidStack;
import com.ataulm.stacks.model.Time;
import com.ataulm.stacks.persistence.Stacks;

public class StackFromCursorMarshaller {

    public AndroidStack marshall(Cursor cursor) {
        AndroidStack.Builder builder = new AndroidStack.Builder();
        builder.id(cursor.getString(cursor.getColumnIndex(Stacks.ID)));
        builder.parent(cursor.getString(cursor.getColumnIndex(Stacks.PARENT)));
        builder.summary(cursor.getString(cursor.getColumnIndex(Stacks.SUMMARY)));
        builder.description(cursor.getString(cursor.getColumnIndex(Stacks.DESCRIPTION)));
        builder.leafCount(cursor.getInt(cursor.getColumnIndex(Stacks.LEAF_COUNT)));
        builder.position(cursor.getInt(cursor.getColumnIndex(Stacks.POSITION)));

        Time created = new Time(cursor.getLong(cursor.getColumnIndex(Stacks.CREATED)));
        builder.created(created);

        Time modified = new Time(cursor.getLong(cursor.getColumnIndex(Stacks.MODIFIED)));
        builder.modified(modified);

        Time deleted = new Time(cursor.getLong(cursor.getColumnIndex(Stacks.DELETED)));
        builder.deleted(deleted);

        return builder.build();
    }

}
