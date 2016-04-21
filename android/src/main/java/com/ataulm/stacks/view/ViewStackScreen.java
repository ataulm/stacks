package com.ataulm.stacks.view;

import com.ataulm.stacks.StackInputListener;
import com.ataulm.stacks.StackItemListener;
import com.ataulm.stacks.stack.Stacks;

public interface ViewStackScreen {

    void setTitle(String title);

    void showData(Stacks stacks, StackItemListener interactionListener, StackInputListener inputListener);

    void showEmptyScreen(StackItemListener interactionListener, StackInputListener inputListener);

}
