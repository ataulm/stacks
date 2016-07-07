package com.ataulm.stacks.navigation;

import android.content.Context;
import android.support.annotation.Nullable;

import java.net.URI;
import java.util.regex.Pattern;

class MimeTypeResolver {

    static final String MIME_TYPE_SCREEN_TOP_LEVEL = "vnd.android.cursor.item/vnd.%1$s.toplevel";

    private static final String STACKS_PATH = "\\/stacks\\/([aA-zZ0-9]{8}-[aA-zZ0-9]{4}-[aA-zZ0-9]{4}-[aA-zZ0-9]{4}-[aA-zZ0-9]{12})";
    private static final Pattern STACKS_PATTERN = Pattern.compile(STACKS_PATH);

    private final String packageName;

    static MimeTypeResolver create(Context context) {
        return new MimeTypeResolver(context.getPackageName());
    }

    MimeTypeResolver(String packageName) {
        this.packageName = packageName;
    }

    @Nullable
    public String getType(URI uri) {
        if (isTopLevelScreen(uri) || isStacksScreen(uri)) {
            return String.format(MIME_TYPE_SCREEN_TOP_LEVEL, packageName);
        }
        return null;
    }

    private boolean isTopLevelScreen(URI uri) {
        for (Screen screen : Screen.values()) {
            if (uri.getPath().endsWith(screen.getPath())) {
                return true;
            }
        }
        return false;
    }

    private boolean isStacksScreen(URI uri) {
        return STACKS_PATTERN.matcher(uri.getPath()).find();
    }

}
