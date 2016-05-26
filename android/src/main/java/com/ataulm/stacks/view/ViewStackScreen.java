package com.ataulm.stacks.view;

import com.ataulm.Optional;
import com.ataulm.stacks.StackInputListener;
import com.ataulm.stacks.StackItemListener;
import com.ataulm.stacks.ToolbarActionListener;
import com.ataulm.stacks.stack.Id;
import com.ataulm.stacks.stack.Stacks;

public interface ViewStackScreen {

    void setupToolbar(String title, Optional<Id> parentId, ToolbarActionListener toolbarActionListener);

    void showData(Stacks stacks, Optional<Id> parentId, StackItemListener interactionListener, StackInputListener inputListener, ToolbarActionListener toolbarActionListener);

    void showEmptyScreen(Optional<Id> parentId, StackInputListener inputListener, ToolbarActionListener toolbarActionListener);

}
