package com.ataulm.stacks.persistence;

import android.content.Context;
import android.content.CursorLoader;

public class StacksLoader extends CursorLoader {

    public StacksLoader(Context context) {
        super(context);
        setUri(StacksProvider.URI_STACKS);
        setSortOrder("position ASC");
    }

}
