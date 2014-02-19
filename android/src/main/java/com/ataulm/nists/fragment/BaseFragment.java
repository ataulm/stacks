package com.ataulm.nists.fragment;

import android.app.Fragment;
import android.content.ContentResolver;
import com.ataulm.nists.util.Logger;
import com.ataulm.nists.util.SimpleLogger;
import com.ataulm.nists.widget.CroutonEx;

public class BaseFragment extends Fragment implements Logger {
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
