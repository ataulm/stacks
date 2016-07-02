package com.ataulm.stacks.stacks;

import com.ataulm.stacks.navigation.Navigator;
import com.ataulm.stacks.stack.RemoveStackUsecase;
import com.ataulm.stacks.stack.Stack;
import com.ataulm.stacks.stack.UpdateStackUsecase;

public class StackClickActions implements ClickActions {

    private final Navigator navigator;
    private final UpdateStackUsecase updateStackUsecase;
    private final RemoveStackUsecase removeStackUsecase;

    public StackClickActions(Navigator navigator, UpdateStackUsecase updateStackUsecase, RemoveStackUsecase removeStackUsecase) {
        this.navigator = navigator;
        this.updateStackUsecase = updateStackUsecase;
        this.removeStackUsecase = removeStackUsecase;
    }

    @Override
    public void onClick(Stack stack) {
        navigator.navigateToStack(stack.id());
    }

    @Override
    public void onClickMarkComplete(Stack stack) {
        updateStackUsecase.markCompleted(stack);
    }

    @Override
    public void onClickMarkNotComplete(Stack stack) {
        updateStackUsecase.markNotCompleted(stack);
    }

    @Override
    public void onClickRemove(Stack stack) {
        removeStackUsecase.markPendingRemove(stack);
    }

}
