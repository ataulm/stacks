package com.ataulm.stacks.marshallers;

import android.content.ContentValues;

import com.ataulm.stacks.model.Stack;

public class ContentValuesFromStackMarshaller {

    public ContentValues newContentValuesForInsert(Stack stack) {
        ContentValues values = newContentValuesForUpdate(stack);

        values.put("_id", stack.id);

        return values;
    }

    public ContentValues newContentValuesForUpdate(Stack stack) {
        ContentValues values = new ContentValues();

        values.put("parent", stack.parent);
        values.put("summary", stack.summary);
        values.put("description", stack.description);
        values.put("leaf_count", stack.leafCount);
        values.put("position", stack.position);
        values.put("created", stack.created.asMillis());
        values.put("modified", stack.modified.asMillis());
        values.put("deleted", stack.deleted.asMillis());

        return values;
    }

}
