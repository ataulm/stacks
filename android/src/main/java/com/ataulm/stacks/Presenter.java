package com.ataulm.stacks;

import android.net.Uri;

import com.ataulm.stacks.navigation.Screen;

public interface Presenter {

    void start(Uri uri);

    void stop();

    /**
     * @return true if the back event was consumed
     */
    boolean onBackPressed();

    /**
     * @return true if this presenter is currently being used to display the given screen type
     */
    boolean isDisplaying(Screen screen);
}
