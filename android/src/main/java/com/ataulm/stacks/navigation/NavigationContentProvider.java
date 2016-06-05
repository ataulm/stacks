package com.ataulm.stacks.navigation;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.util.List;

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
        return mimeTypeResolver.getType(uri);
    }

    private static class MimeTypeResolver {

        private static final String MIME_TYPE_SCREEN_TOP_LEVEL = "vnd.android.cursor.item/vnd.%1$s.screen.top_level.%2$s";

        private final String packageName;

        static MimeTypeResolver create(Context context) {
            return new MimeTypeResolver(context.getPackageName());
        }

        MimeTypeResolver(String packageName) {
            this.packageName = packageName;
        }

        @Nullable
        public String getType(Uri uri) {
            if (isTopLevelScreen(uri)) {
                return String.format(MIME_TYPE_SCREEN_TOP_LEVEL, packageName, uri.getLastPathSegment());
            } else {
                return null;
            }
        }

        private boolean isTopLevelScreen(Uri uri) {
            List<String> path = uri.getPathSegments();
            if (path.size() == 0) {
                return false;
            }
            return pathStartsWithScreen(path) && lastPathSegmentMatchesAtLeastOneScreen(path);
        }

        private boolean pathStartsWithScreen(List<String> path) {
            String pathPrefix = path.get(0);
            return "screen".equals(pathPrefix);
        }

        private boolean lastPathSegmentMatchesAtLeastOneScreen(List<String> path) {
            String lastPathSegment = path.get(path.size() - 1);
            for (Screen screen : Screen.values()) {
                if (screen.getPath().equals(lastPathSegment)) {
                    return true;
                }
            }
            return false;
        }

    }

}
