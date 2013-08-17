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
    private boolean logging = true;
    private final String TAG = getClass().getSimpleName();

    public void log(String message) {
        if (logging) {
            Log.i(TAG, message);
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
