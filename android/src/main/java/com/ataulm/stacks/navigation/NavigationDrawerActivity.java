package com.ataulm.stacks.navigation;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.ViewGroup;

import com.ataulm.stacks.BaseActivity;
import com.ataulm.stacks.R;
import com.ataulm.stacks.StacksApplication;
import com.ataulm.stacks.view.ToolbarActionListener;
import com.ataulm.stacks.stack.Id;
import com.ataulm.stacks.view.ViewActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

import static butterknife.ButterKnife.findById;

public abstract class NavigationDrawerActivity extends BaseActivity implements ToolbarActionListener {

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Bind(R.id.drawer)
    NavigationDrawerView drawerView;

    private TopLevelActivityNavigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.navigator = new TopLevelActivityNavigator(this);
    }

    @Override
    public void setContentView(@LayoutRes int layout) {
        super.setContentView(R.layout.activity_navigation_drawer);
        ViewGroup content = findById(this, R.id.drawer_layout_content);
        getLayoutInflater().inflate(layout, content);
        ButterKnife.bind(this);

        drawerView.set(new NavigationDrawerView.Listener() {
            @Override
            public void onClickViewStacks() {
                navigator.navigateToViewStacks();
            }

            @Override
            public void onClickRemovedStacks() {
                navigator.navigateToRemovedStacks();
            }
        });
    }

    @Override
    public void onClickNavigateUpToStackWith(Id id) {
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
            navigator.navigateToViewStacks();
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
