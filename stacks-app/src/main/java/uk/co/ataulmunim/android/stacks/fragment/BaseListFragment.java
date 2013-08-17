package uk.co.ataulmunim.android.stacks.fragment;

import android.app.ListFragment;
import uk.co.ataulmunim.android.util.Logger;
import uk.co.ataulmunim.android.util.SimpleLogger;

public class BaseListFragment extends ListFragment implements Logger {
    private final SimpleLogger logger = new SimpleLogger(getClass().getSimpleName());

    @Override
    public void log(String message) {
        logger.log(message);
    }

    @Override
    public void shouldLog(boolean switchLogging) {
        logger.shouldLog(switchLogging);
    }
}
