package com.ataulm.stacks.navigation;

import android.content.Context;
import android.support.annotation.Nullable;

import com.ataulm.Optional;
import com.ataulm.stacks.stack.Id;

import java.net.URI;

public class UriResolver {

    private static final int TOP_LEVEL_PATH_PART = 1;

    private final String authority;
    private final IdExtractor idExtractor;

    public static UriResolver create(Context context) {
        return new UriResolver(context.getPackageName(), new IdExtractor());
    }

    UriResolver(String authority, IdExtractor idExtractor) {
        this.authority = authority;
        this.idExtractor = idExtractor;
    }

    public boolean matches(@Nullable URI uri, Screen screen) {
        if (uri == null || !uri.getAuthority().equals(authority)) {
            return false;
        }
        return pathMatches(uri, screen);
    }

    private boolean pathMatches(URI uri, Screen screen) {
        String[] pathSegments = uri.getPath().split("/");
        return pathSegments.length > 0 && pathSegments[TOP_LEVEL_PATH_PART].equals(screen.getPath());
    }

    public Optional<Id> extractIdFrom(@Nullable URI uri) {
        if (uri == null) {
            return Optional.absent();
        }
        return idExtractor.extractIdFrom(uri);
    }

}
