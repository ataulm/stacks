package com.ataulm.stacks.stacks;

import com.ataulm.stacks.stack.Stack;

public interface ItemClickActions {

    void onClick(Stack stack);

    void onClickMarkComplete(Stack stack);

    void onClickMarkNotComplete(Stack stack);

    void onClickRemove(Stack stack);

    void onClickEdit(Stack stack, String summary);

}
