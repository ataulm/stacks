package com.ataulm.stacks;

import com.ataulm.stacks.stack.Stack;

public interface StackItemListener {

    void onClick(Stack stack);

    void onClickRemove(Stack stack);

    void onClickEdit(Stack stack);

    void onClickMarkComplete(Stack stack);

    void onClickMarkNotComplete(Stack stack);

}
