package com.ataulm.stacks.marshallers;

import android.content.ContentValues;

import com.ataulm.stacks.model.Stack;
import com.ataulm.stacks.persistence.Stacks;

public class ContentValuesFromStackMarshaller {

    public ContentValues newContentValuesForInsert(Stack stack) {
        ContentValues values = newContentValuesForUpdate(stack);

        values.put(Stacks.ID, stack.id);

        return values;
    }

    public ContentValues newContentValuesForUpdate(Stack stack) {
        ContentValues values = new ContentValues();

        values.put(Stacks.PARENT, stack.parent);
        values.put(Stacks.SUMMARY, stack.summary);
        values.put(Stacks.DESCRIPTION, stack.description);
        values.put(Stacks.LEAF_COUNT, stack.leafCount);
        values.put(Stacks.POSITION, stack.position);
        values.put(Stacks.CREATED, stack.created.asMillis());
        values.put(Stacks.MODIFIED, stack.modified.asMillis());
        values.put(Stacks.DELETED, stack.deleted.asMillis());

        return values;
    }

}
