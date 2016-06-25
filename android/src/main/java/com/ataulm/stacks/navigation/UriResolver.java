package com.ataulm.stacks.navigation;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.ataulm.Optional;
import com.ataulm.stacks.stack.Id;

import java.util.List;

public class UriResolver {

    private final String authority;

    public static UriResolver create(Context context) {
        return new UriResolver(context.getPackageName());
    }

    UriResolver(String authority) {
        this.authority = authority;
    }

    public boolean matches(@Nullable Uri uri, Screen screen) {
        if (invalid(uri)) {
            return false;
        }
        return pathMatches(uri, screen);
    }

    private boolean invalid(@Nullable Uri uri) {
        return uri == null || !uri.getAuthority().equals(authority);
    }

    private boolean pathMatches(Uri uri, Screen screen) {
        List<String> pathSegments = uri.getPathSegments();
        return pathSegments.size() > 0 && pathSegments.get(0).equals(screen.getPath());
    }

    public Optional<Id> extractIdFrom(Uri uri) {
        if (matches(uri, Screen.STACKS)) {
            return extractIdFrom(uri, Screen.STACKS);
        } else {
            return Optional.absent();
        }
    }

    private Optional<Id> extractIdFrom(Uri uri, Screen screen) {
        List<String> pathSegments = uri.getPathSegments();
        return extractIdFrom(pathSegments, screen);
    }

    private Optional<Id> extractIdFrom(List<String> pathSegments, Screen screen) {
        for (int index = 0; index < pathSegments.size(); index++) {
            String pathSegment = pathSegments.get(index);
            int indexNextPathSegment = index + 1;
            if (screen.getPath().equals(pathSegment) && pathSegments.size() < indexNextPathSegment) {
                return Optional.of(Id.create(pathSegments.get(indexNextPathSegment)));
            }
        }
        return Optional.absent();
    }

}
