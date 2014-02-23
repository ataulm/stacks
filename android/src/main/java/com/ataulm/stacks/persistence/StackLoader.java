package com.ataulm.stacks.persistence;

import android.content.Context;
import android.content.CursorLoader;

public class StackLoader extends CursorLoader {

    public StackLoader(Context context, String id) {
        super(context);
        setUri(StacksProvider.URI_STACKS);
        setSelection("_id=?");
        setSelectionArgs(new String[]{id});
    }

}
