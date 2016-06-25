package com.ataulm.stacks.navigation;

import android.content.Context;
import android.net.Uri;

import com.ataulm.stacks.stack.Id;

public final class UriCreator {

    private static final String CONTENT_SCHEME = "content";
    private final String authority;

    public static UriCreator create(Context context) {
        return new UriCreator(context.getPackageName());
    }

    private UriCreator(String authority) {
        this.authority = authority;
    }

    public Uri createUriToView(Screen screen) {
        return new Uri.Builder()
                .scheme(CONTENT_SCHEME)
                .authority(authority)
                .appendPath(screen.getPath()).build();
    }

    public Uri createUriToView(Screen screen, Id id) {
        return createUriToView(screen)
                .buildUpon()
                .appendPath(id.value())
                .build();
    }

}
