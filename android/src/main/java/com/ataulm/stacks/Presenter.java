package com.ataulm.stacks;

import android.net.Uri;

import com.ataulm.stacks.navigation.Screen;

public interface Presenter {

    void start(Uri uri);

    void stop();

    boolean onBackPressed();

    boolean isDisplaying(Screen screen);
}
