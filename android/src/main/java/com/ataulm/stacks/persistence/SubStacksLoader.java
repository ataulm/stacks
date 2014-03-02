package com.ataulm.stacks.persistence;

import android.content.Context;
import android.content.CursorLoader;

import com.ataulm.stacks.model.Time;

public class SubStacksLoader extends CursorLoader {

    public SubStacksLoader(Context context, String id) {
        super(context);
        setUri(StacksProvider.URI_STACKS);
        setSelection("parent=? AND deleted=?");
        setSelectionArgs(new String[]{id, String.valueOf(Time.UNSET.asMillis())});
        setSortOrder("position ASC");
    }

}
