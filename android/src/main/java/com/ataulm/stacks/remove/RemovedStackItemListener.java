package com.ataulm.stacks.remove;

import com.ataulm.stacks.stack.Stack;

public interface RemovedStackItemListener {

    RemovedStackItemListener NO_OP = new RemovedStackItemListener() {
        @Override
        public void onClickRestore(Stack stack) {
        }

        @Override
        public void onClickDelete(Stack stack) {
        }
    };

    void onClickRestore(Stack stack);

    void onClickDelete(Stack stack);

}
