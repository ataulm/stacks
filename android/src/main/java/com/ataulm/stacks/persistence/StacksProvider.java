package com.ataulm.stacks.persistence;

import android.net.Uri;

import com.ataulm.stacks.BuildConfig;

import novoda.lib.sqliteprovider.provider.SQLiteContentProviderImpl;

public class StacksProvider extends SQLiteContentProviderImpl {

    public static final Uri AUTHORITY = Uri.parse("content://" + BuildConfig.PROVIDER_AUTHORITY);
    public static final Uri URI_STACKS = AUTHORITY.buildUpon().appendPath("stacks").build();
    public static final String MIME_STACK = "vnd.android.cursor.item/vnd.com.ataulm.stack";

    @Override
    public String getType(Uri uri) {
        if (uri.toString().contains("stacks")) {
            return MIME_STACK;
        }
        throw new IllegalArgumentException("No type found for URI: " + uri);
    }

}
