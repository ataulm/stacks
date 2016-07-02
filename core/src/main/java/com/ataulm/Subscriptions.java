package com.ataulm;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class Subscriptions {

    private CompositeSubscription compositeSubscription;

    public void add(Subscription subscription) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);
    }

    public void unsubscribe() {
        if (compositeSubscription == null) {
            return;
        }
        compositeSubscription.clear();
        compositeSubscription = null;
    }

}
