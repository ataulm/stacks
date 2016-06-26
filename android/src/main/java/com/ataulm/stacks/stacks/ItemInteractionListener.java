package com.ataulm.stacks.stacks;

import com.ataulm.stacks.stack.Stack;

public interface ItemInteractionListener {

    void onClick(Stack stack);

    void onClickRemove(Stack stack);

    void onClickMarkComplete(Stack stack);

    void onClickMarkNotComplete(Stack stack);

}
