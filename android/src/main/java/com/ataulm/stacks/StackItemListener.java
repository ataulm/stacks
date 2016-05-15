package com.ataulm.stacks;

import com.ataulm.stacks.stack.Stack;

public interface StackItemListener {

    StackItemListener NO_OP = new StackItemListener() {
        @Override
        public void onClick(Stack stack) {

        }

        @Override
        public void onClickRemove(Stack stack) {

        }

        @Override
        public void onClickEdit(Stack stack) {

        }

        @Override
        public void onClickMarkComplete(Stack stack) {

        }

        @Override
        public void onClickMarkNotComplete(Stack stack) {

        }
    };

    void onClick(Stack stack);

    void onClickRemove(Stack stack);

    void onClickEdit(Stack stack);

    void onClickMarkComplete(Stack stack);

    void onClickMarkNotComplete(Stack stack);

}
