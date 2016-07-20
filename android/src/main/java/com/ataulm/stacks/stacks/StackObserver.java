package com.ataulm.stacks.stacks;

import com.ataulm.Event;
import com.ataulm.Optional;
import com.ataulm.stacks.LoggingObserver;
import com.ataulm.stacks.jabber.Jabber;
import com.ataulm.stacks.stack.Stack;

class StackObserver extends LoggingObserver<Event<Optional<Stack>>> {

    private final StacksScreenLayout contentView;
    private final OnClickNavigationButtonListener onClickNavigationButtonListener;

    StackObserver(StacksScreenLayout contentView, OnClickNavigationButtonListener onClickNavigationButtonListener) {
        this.contentView = contentView;
        this.onClickNavigationButtonListener = onClickNavigationButtonListener;
    }

    @Override
    public void onNext(Event<Optional<Stack>> event) {
        super.onNext(event);
        switch (event.getType()) {
            case LOADING:
                break;
            case ERROR:
                Jabber.toast("uh oh, error");
                break;
            case IDLE:
                update(event);
                break;
            default:
                throw new IllegalArgumentException("unknown type: " + event.getType());
        }
    }

    private void update(Event<Optional<Stack>> event) {
        if (event.getData().isPresent()) {
            Optional<Stack> stack = event.getData().get();
            updateToolbar(stack);
        }
    }

    private void updateToolbar(Optional<Stack> stack) {
        contentView.updateToolbar(stack, onClickNavigationButtonListener);
    }

}
