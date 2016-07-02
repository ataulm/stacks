package com.ataulm.stacks.navigation;

import android.net.Uri;
import android.support.annotation.Nullable;

import java.net.URI;

public class NavigationContentProvider extends SimpleContentProvider {

    private MimeTypeResolver mimeTypeResolver;

    @Override
    public boolean onCreate() {
        mimeTypeResolver = MimeTypeResolver.create(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        URI javaUri = URI.create(uri.toString());
        return mimeTypeResolver.getType(javaUri);
    }

}
