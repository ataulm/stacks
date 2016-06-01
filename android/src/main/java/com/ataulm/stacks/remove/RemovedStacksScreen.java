package com.ataulm.stacks.remove;

import com.ataulm.stacks.stack.Stacks;

public interface RemovedStacksScreen {

    void setupToolbar(String title, ToolbarActionListener toolbarActionListener);

    void showData(Stacks stacks, RemovedStackItemListener listener, ToolbarActionListener toolbarActionListener);

    void showEmptyScreen(ToolbarActionListener toolbarActionListener);

}
