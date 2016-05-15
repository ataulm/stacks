package com.ataulm.stacks.remove;

import com.ataulm.stacks.stack.Stacks;

public interface RemovedStacksScreen {

    void setTitle(String title);

    void showData(Stacks stacks, RemovedStackItemListener listener);

    void showEmptyScreen();

}
