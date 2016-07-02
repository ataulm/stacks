package com.ataulm.stacks.navigation;

import android.support.v4.widget.DrawerLayout;

public class DrawerController {

    private final DrawerLayout drawerLayout;
    private final int drawerGravity;

    DrawerController(DrawerLayout drawerLayout, int drawerGravity) {
        this.drawerLayout = drawerLayout;
        this.drawerGravity = drawerGravity;
    }

    public boolean isDrawerOpen() {
        return drawerLayout.isDrawerOpen(drawerGravity);
    }

    public void closeDrawer() {
        drawerLayout.closeDrawer(drawerGravity);
    }

    public void openDrawer() {
        drawerLayout.openDrawer(drawerGravity);
    }

}
