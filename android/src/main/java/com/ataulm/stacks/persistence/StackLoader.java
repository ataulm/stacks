package com.ataulm.stacks.persistence;

import android.content.Context;
import android.content.CursorLoader;

import com.ataulm.stacks.model.Time;

public class StackLoader extends CursorLoader {

    public StackLoader(Context context, String id) {
        super(context);
        setUri(StacksProvider.URI_STACKS);
        setSelection(Stacks.ID + "=? AND " + Stacks.DELETED + "=?");
        setSelectionArgs(new String[]{id, String.valueOf(Time.UNSET.asMillis())});
    }

}
