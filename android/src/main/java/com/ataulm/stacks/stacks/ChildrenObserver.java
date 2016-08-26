package com.ataulm.stacks.stacks;

import com.ataulm.Event;
import com.ataulm.stacks.LoggingObserver;
import com.ataulm.stacks.jabber.Jabber;
import com.ataulm.stacks.stack.Stacks;

class ChildrenObserver extends LoggingObserver<Event<Stacks>> {

    private final StacksScreenLayout contentView;
    private final UserItemActions userItemActions;

    ChildrenObserver(StacksScreenLayout contentView, UserItemActions userItemActions) {
        this.contentView = contentView;
        this.userItemActions = userItemActions;
    }

    @Override
    public void onNext(Event<Stacks> event) {
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

    private void update(Event<Stacks> event) {
        if (event.getData().isPresent()) {
            Stacks stacks = event.getData().get();
            contentView.update(stacks, userItemActions);
        }
    }

}
