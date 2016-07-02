package com.ataulm.stacks.navigation;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.ataulm.Optional;
import com.ataulm.stacks.stack.Id;

import java.net.URI;
import java.util.List;

public class UriResolver {

    private final String authority;
    private final IdExtractor idExtractor;

    public static UriResolver create(Context context) {
        return new UriResolver(context.getPackageName(), new IdExtractor());
    }

    UriResolver(String authority, IdExtractor idExtractor) {
        this.authority = authority;
        this.idExtractor = idExtractor;
    }

    public boolean matches(@Nullable Uri uri, Screen screen) {
        if (uri == null || !uri.getAuthority().equals(authority)) {
            return false;
        }
        return pathMatches(uri, screen);
    }

    private boolean pathMatches(Uri uri, Screen screen) {
        List<String> pathSegments = uri.getPathSegments();
        return pathSegments.size() > 0 && pathSegments.get(0).equals(screen.getPath());
    }

    public Optional<Id> extractIdFrom(@Nullable Uri uri) {
        if (uri == null) {
            return Optional.absent();
        }

        URI javaUri = URI.create(uri.toString());
        return idExtractor.extractIdFrom(javaUri);
    }

}
