package com.ataulm.stacks.stack;

public interface ItemInteractionListener {

    void onClick(Stack stack);

    void onClickRemove(Stack stack);

    void onClickMarkComplete(Stack stack);

    void onClickMarkNotComplete(Stack stack);

}
