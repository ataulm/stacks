package com.ataulm.stacks.base;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

public class StacksBaseActivity extends Activity {

    private ViewServerManager viewServerManager;
    private Navigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Crashlytics.start(this.getApplicationContext());

        viewServerManager = new ViewServerManager(this);
        viewServerManager.onCreate();
        navigator = new Navigator(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewServerManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewServerManager.onDestroy();
    }

    protected Navigator navigateTo() {
        return navigator;
    }

    protected void toast(int messageResId) {
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

}
