package com.ataulm.stacks;

import com.ataulm.Optional;
import com.ataulm.stacks.navigation.Screen;

import java.net.URI;

public interface Presenter {

    void start(Optional<URI> uri);

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
