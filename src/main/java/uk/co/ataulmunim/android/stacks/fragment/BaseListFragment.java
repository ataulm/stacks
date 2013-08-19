package uk.co.ataulmunim.android.stacks.fragment;

import android.app.ListFragment;
import android.content.ContentResolver;
import uk.co.ataulmunim.android.util.Logger;
import uk.co.ataulmunim.android.util.SimpleLogger;
import uk.co.ataulmunim.android.widget.CroutonEx;

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

    public void showConfirm(String message) {
        CroutonEx.makeText(getActivity(), message, CroutonEx.CONFIRM).show();
    }

    public void showWarning(String message) {
        CroutonEx.makeText(getActivity(), message, CroutonEx.WARN).show();
    }

    public void showInfo(String message) {
        CroutonEx.makeText(getActivity(), message, CroutonEx.INFO).show();
    }

    public void showAlert(String message) {
        CroutonEx.makeText(getActivity(), message, CroutonEx.ALERT).show();
    }

    protected ContentResolver getContentResolver() {
        return getActivity().getContentResolver();
    }
}
