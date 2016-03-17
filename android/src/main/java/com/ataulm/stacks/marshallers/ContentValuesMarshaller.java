package com.ataulm.stacks.marshallers;

import android.content.ContentValues;

import com.ataulm.stacks.model.AndroidStack;
import com.ataulm.stacks.persistence.Stacks;

public class ContentValuesMarshaller {

    private final ContentValues values;

    public ContentValuesMarshaller(ContentValues values) {
        this.values = values;
    }

    public ContentValues valuesForInsertFrom(AndroidStack stack) {
        ContentValues values = valuesForUpdateFrom(stack);

        values.put(Stacks.ID, stack.id);

        return values;
    }

    public ContentValues valuesForUpdateFrom(AndroidStack stack) {
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
