package com.ataulm.stacks.persistence;

import android.net.Uri;

import com.ataulm.stacks.BuildConfig;

import novoda.lib.sqliteprovider.provider.SQLiteContentProviderImpl;

public class StacksProvider extends SQLiteContentProviderImpl {

    public static final Uri AUTHORITY = Uri.parse("content://" + BuildConfig.PROVIDER_AUTHORITY);
    public static final String MIME_STACK = "vnd.android.cursor.item/vnd.com.ataulm.stack";

}
