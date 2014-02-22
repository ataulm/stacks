package com.ataulm.stacks.activity;

import android.app.Activity;
import android.widget.Toast;

import com.ataulm.stacks.widget.CroutonEx;

public class NistActivity extends Activity {

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

    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
