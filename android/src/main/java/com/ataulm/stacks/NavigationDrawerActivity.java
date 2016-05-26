package com.ataulm.stacks;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.stacks.stack.Id;
import com.ataulm.stacks.view.ViewActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

import static butterknife.ButterKnife.findById;

public abstract class NavigationDrawerActivity extends BaseActivity implements ToolbarActionListener {

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Bind(R.id.drawer)
    View drawerView;

    @Override
    public void setContentView(@LayoutRes int layout) {
        super.setContentView(R.layout.activity_navigation_drawer);
        ViewGroup content = findById(this, R.id.drawer_layout_content);
        getLayoutInflater().inflate(layout, content);
        ButterKnife.bind(this);
    }

    @Override
    public void onClickNavigateUpToStackWith(Id id) {
        Intent intent = new Intent(this, ViewActivity.class);

        StacksApplication.displayToast("navigate up to stack with id");
        // TODO: this should be like a clear stack style, perhaps can build a fake backstack with all the stacks to root so pressing back after this is like pressing up
    }

    @Override
    public void onClickToggleNavigationMenu() {
        if (isDrawerOpen()) {
            closeDrawer();
        } else {
            openDrawer();
        }
    }

    @Override
    public void onBackPressed() {
        if (isDrawerOpen()) {
            closeDrawer();
        } else if (notOnHomeActivity()) {
            ViewActivity.start(this);
        } else {
            super.onBackPressed();
        }
    }

    private boolean notOnHomeActivity() {
        return !(this instanceof ViewActivity);
    }

    private boolean isDrawerOpen() {
        return drawerLayout.isDrawerOpen(drawerView);
    }

    private void closeDrawer() {
        drawerLayout.closeDrawer(drawerView);
    }

    private void openDrawer() {
        drawerLayout.openDrawer(drawerView);
    }

}
