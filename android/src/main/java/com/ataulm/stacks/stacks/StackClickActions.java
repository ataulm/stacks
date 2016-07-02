package com.ataulm.stacks.stacks;

import com.ataulm.stacks.jabber.Jabber;
import com.ataulm.stacks.navigation.Navigator;
import com.ataulm.stacks.stack.Stack;

class StackClickActions implements ClickActions {

    private final Navigator navigator;

    StackClickActions(Navigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public void onClick(Stack stack) {
        navigator.navigateToStack(stack.id());
    }

    @Override
    public void onClickMarkComplete(Stack stack) {
        Jabber.toast("on click: mark complete");
    }

    @Override
    public void onClickMarkNotComplete(Stack stack) {
        Jabber.toast("on click: mark incomplete");
    }

    @Override
    public void onClickRemove(Stack stack) {
        Jabber.toast("on click: delete");
    }

}
