package com.ataulm.stacks.navigation;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

public class ContentViewSetter {

    private final LayoutInflater layoutInflater;
    private final FrameLayout contentFrame;

    @LayoutRes
    private int layout;

    ContentViewSetter(LayoutInflater layoutInflater, FrameLayout contentFrame) {
        this.layoutInflater = layoutInflater;
        this.contentFrame = contentFrame;
    }

    public <T> T display(@LayoutRes int layout) {
        if (alreadyDisplaying(layout)) {
            return getContentView();
        } else {
            return setContentView(layout);
        }
    }

    private boolean alreadyDisplaying(@LayoutRes int layout) {
        return this.layout == layout && hasContent();
    }

    private boolean hasContent() {
        return contentFrame.getChildCount() == 1;
    }

    @SuppressWarnings("unchecked")
    private <T> T getContentView() {
        if (!hasContent()) {
            throw new ContentViewNotSetException();
        }
        return (T) contentFrame.getChildAt(0);
    }

    @SuppressWarnings("unchecked")
    private <T> T setContentView(@LayoutRes int layout) {
        contentFrame.removeAllViews();
        View view = layoutInflater.inflate(layout, contentFrame, false);
        contentFrame.addView(view);

        this.layout = layout;
        return (T) view;
    }

    private static class ContentViewNotSetException extends RuntimeException {
    }

}
