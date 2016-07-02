package com.ataulm.stacks.stacks;

import com.ataulm.Optional;
import com.ataulm.stacks.navigation.DrawerController;
import com.ataulm.stacks.navigation.Navigator;
import com.ataulm.stacks.stack.Stack;

public final class StacksToolbarActions implements ToolbarActions {

    private final Navigator navigator;
    private final Optional<DrawerController> drawerController;

    public static StacksToolbarActions create(Navigator navigator) {
        return new StacksToolbarActions(navigator, Optional.<DrawerController>absent());
    }

    public static StacksToolbarActions create(Navigator navigator, DrawerController drawerController) {
        return new StacksToolbarActions(navigator, Optional.of(drawerController));
    }

    StacksToolbarActions(Navigator navigator, Optional<DrawerController> drawerController) {
        this.navigator = navigator;
        this.drawerController = drawerController;
    }

    @Override
    public void onClickNavigateUpToParentOf(Stack stack) {
        navigator.navigateUpToStack(stack.parentId());
    }

    @Override
    public void onClickOpenNavigationDrawer() {
        if (drawerController.isPresent()) {
            drawerController.get().openDrawer();
        } else {
            throw new UnsupportedOperationException("no drawer controller - this cannot be called");
        }
    }

}
