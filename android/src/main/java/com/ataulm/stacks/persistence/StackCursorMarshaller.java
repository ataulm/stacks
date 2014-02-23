package com.ataulm.stacks.persistence;

import android.database.Cursor;

import com.ataulm.stacks.model.Stack;
import com.ataulm.stacks.model.Time;
import com.novoda.notils.cursor.CursorMarshaller;

public class StackCursorMarshaller implements CursorMarshaller<Stack> {

    @Override
    public Stack marshall(Cursor cursor) {
        Stack.Builder builder = new Stack.Builder();
        builder.id(cursor.getString(cursor.getColumnIndex("_id")));
        builder.parent(cursor.getString(cursor.getColumnIndex("parent")));
        builder.summary(cursor.getString(cursor.getColumnIndex("summary")));
        builder.description(cursor.getString(cursor.getColumnIndex("description")));
        builder.leafCount(cursor.getInt(cursor.getColumnIndex("leaf_count")));

        Time created = new Time(cursor.getLong(cursor.getColumnIndex("created")));
        builder.created(created);

        Time modified = new Time(cursor.getLong(cursor.getColumnIndex("modified")));
        builder.modified(modified);

        Time deleted = new Time(cursor.getLong(cursor.getColumnIndex("deleted")));
        builder.deleted(deleted);

        return builder.build();
    }

}
