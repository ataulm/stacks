package uk.co.ataulmunim.android.stacks.activity;

import android.app.Activity;
import uk.co.ataulmunim.android.util.Logger;
import uk.co.ataulmunim.android.util.SimpleLogger;
import uk.co.ataulmunim.android.widget.CroutonEx;

public class BaseActivity extends Activity implements Logger {
    private final SimpleLogger logger = new SimpleLogger(getClass().getSimpleName());

    @Override
    public void log(String message) {
        logger.log(message);
    }

    @Override
    public void shouldLog(boolean switchLogging) {
        logger.shouldLog(switchLogging);
    }

    public void showConfirm(String message) {
        CroutonEx.makeText(this, message, CroutonEx.CONFIRM).show();
    }

    public void showWarning(String message) {
        CroutonEx.makeText(this, message, CroutonEx.WARN).show();
    }

    public void showInfo(String message) {
        CroutonEx.makeText(this, message, CroutonEx.INFO).show();
    }

    public void showAlert(String message) {
        CroutonEx.makeText(this, message, CroutonEx.ALERT).show();
    }
}
