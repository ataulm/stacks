package com.ataulm.stacks.navigation;

import java.net.URI;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class MimeTypeResolverShould {

    private static final String PACKAGE_NAME = "com.ataulm.stacks";
    private static final String MIME_TYPE_SCREEN_TOP_LEVEL = String.format(MimeTypeResolver.MIME_TYPE_SCREEN_TOP_LEVEL, PACKAGE_NAME);

    private MimeTypeResolver mimeTypeResolver = new MimeTypeResolver(PACKAGE_NAME);

    @Test
    public void resolveToTopLevelMimeType_given_uriWithStacksPath() {
        URI uri = URI.create("content://com.ataulm.stacks/stacks");

        String mimeType = mimeTypeResolver.getType(uri);

        assertThat(mimeType).isEqualTo(MIME_TYPE_SCREEN_TOP_LEVEL);
    }

    @Test
    public void resolveToTopLevelMimeType_given_uriWithRemovedStacksPath() {
        URI uri = URI.create("content://com.ataulm.stacks/removed");

        String mimeType = mimeTypeResolver.getType(uri);

        assertThat(mimeType).isEqualTo(MIME_TYPE_SCREEN_TOP_LEVEL);
    }

    @Test
    public void resolveToStacksMimeType_given_uriWithStacksWithValidId() {
        URI uri = URI.create("content://com.ataulm.stacks/stacks/73735d55-9fa8-4439-b1ab-293c1a66b5c5");

        String mimeType = mimeTypeResolver.getType(uri);

        assertThat(mimeType).isEqualTo(MIME_TYPE_SCREEN_TOP_LEVEL);
    }

    @Test
    public void resolveNoMimeType_given_uriWithStacksWithInvalidId() {
        URI uri = URI.create("content://com.ataulm.stacks/stacks/invalid");

        String mimeType = mimeTypeResolver.getType(uri);

        assertThat(mimeType).isNull();
    }

    @Test
    public void resolveNoMimeType_given_uriWithUnrecognizedPath() {
        URI uri = URI.create("content://com.ataulm.stacks/unknown");

        String mimeType = mimeTypeResolver.getType(uri);

        assertThat(mimeType).isNull();
    }

}
