package com.ataulm.stacks.base;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class StacksBaseActivity extends Activity {

    private Navigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navigator = new Navigator(this);
    }

    protected Navigator navigateTo() {
        return navigator;
    }

    protected void toast(int messageResId) {
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

}
