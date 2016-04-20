package com.ataulm.stacks;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

public abstract class NavigationDrawerActivity extends BaseActivity {

    @Override
    public void setContentView(@LayoutRes int layout) {
        super.setContentView(R.layout.activity_navigation_drawer);
        ViewGroup content = (ViewGroup) findViewById(R.id.content);
        getLayoutInflater().inflate(layout, content);

        View drawerView = findViewById(R.id.drawer);
    }

}
