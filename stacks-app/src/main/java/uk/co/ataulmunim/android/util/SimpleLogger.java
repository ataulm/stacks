package uk.co.ataulmunim.android.util;

import android.util.Log;

/**
 * Reducing verbosity of logging methods throughout codebase, an instance of this type
 * can be part of the BaseActivity and BaseListFragment, each of which will implement the
 * complementary interface, {@link Logger}.
 *
 * Classes which use this should implement the {@link Logger} interface and delegate
 * all {@link Logger} methods to this.
 */
public class SimpleLogger implements Logger {
    private final String tag;
    private boolean logging = true;

    public SimpleLogger(String tag) {
        this.tag = tag;
    }

    /**
     * Logs message in the debug channel.
     *
     * @param message  the message to output
     */
    public void log(String message) {
        if (logging) {
            Log.d(tag, message);
        }
    }

    /**
     * Controls whether logging is enabled for this instance.
     *
     * @param switchLogging  value describing whether this instance should log or not
     */
    public void shouldLog(boolean switchLogging) {
        this.logging = switchLogging;
    }
}
