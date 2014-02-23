package com.ataulm.stacks.persistence;

import android.content.Context;
import android.content.CursorLoader;
import android.net.Uri;

public class StacksLoader extends CursorLoader {

    public StacksLoader(Context context) {
        super(context);
        Uri uri = StacksProvider.AUTHORITY.buildUpon().appendPath("stacks").build();
        setUri(uri);
    }

}
