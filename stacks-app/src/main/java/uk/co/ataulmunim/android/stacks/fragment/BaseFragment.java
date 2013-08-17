package uk.co.ataulmunim.android.stacks.fragment;

import android.app.Fragment;
import uk.co.ataulmunim.android.util.Logger;
import uk.co.ataulmunim.android.util.SimpleLogger;

public class BaseFragment extends Fragment implements Logger {
    private final SimpleLogger logger = new SimpleLogger();

    @Override
    public void log(String message) {
        logger.log(message);
    }

    @Override
    public void shouldLog(boolean switchLogging) {
        logger.shouldLog(switchLogging);
    }
}
