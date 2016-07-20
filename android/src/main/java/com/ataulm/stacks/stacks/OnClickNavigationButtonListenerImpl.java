package com.ataulm.stacks.stacks;

import com.ataulm.Optional;
import com.ataulm.stacks.navigation.Navigator;
import com.ataulm.stacks.stack.Id;
import com.ataulm.stacks.stack.Stack;

public class OnClickNavigationButtonListenerImpl implements OnClickNavigationButtonListener {

    private final Navigator navigator;
    private final OnClickOpenNavigationDrawerListener listener;

    public OnClickNavigationButtonListenerImpl(Navigator navigator, OnClickOpenNavigationDrawerListener listener) {
        this.navigator = navigator;
        this.listener = listener;
    }

    @Override
    public void onClickNavigationButton(Optional<Stack> stack) {
        if (stack.isPresent()) {
            Optional<Id> parentId = stack.get().parentId();
            navigator.navigateUpToStack(parentId);
        } else {
            listener.onClickOpenNavigationDrawer();
        }
    }

}
