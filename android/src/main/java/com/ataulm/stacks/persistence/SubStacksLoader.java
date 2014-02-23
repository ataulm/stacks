package com.ataulm.stacks.persistence;

import android.content.Context;
import android.content.CursorLoader;

public class SubStacksLoader extends CursorLoader {

    public SubStacksLoader(Context context, String id) {
        super(context);
        setUri(StacksProvider.URI_STACKS);
        setSelection("parent=?");
        setSelectionArgs(new String[]{id});
        setSortOrder("position ASC");
    }

}
