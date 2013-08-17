package uk.co.ataulmunim.android.stacks.activity;

import android.app.Activity;
import uk.co.ataulmunim.android.util.Logger;
import uk.co.ataulmunim.android.util.SimpleLogger;

public class BaseActivity extends Activity implements Logger {
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
