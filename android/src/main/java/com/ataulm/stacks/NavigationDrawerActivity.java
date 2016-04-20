package com.ataulm.stacks;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

import static butterknife.ButterKnife.findById;

public abstract class NavigationDrawerActivity extends BaseActivity {

    @Override
    public void setContentView(@LayoutRes int layout) {
        super.setContentView(R.layout.activity_navigation_drawer);

        ViewGroup content = findById(this, R.id.drawer_layout_content);
        getLayoutInflater().inflate(layout, content);

        View drawerView = findById(this, R.id.drawer);
    }

}
