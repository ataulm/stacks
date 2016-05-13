package com.ataulm.stacks.stack;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class AsyncUpdateStacksUsecase implements UpdateStackUsecase {

    private final SyncUpdateStackUsecase syncUsecase;

    public AsyncUpdateStacksUsecase(SyncUpdateStackUsecase syncUsecase) {
        this.syncUsecase = syncUsecase;
    }

    @Override
    public void updateSummary(final Stack stack, final String summary) {
        doItNowOffTheMainThread(new Action0() {
            @Override
            public void call() {
                syncUsecase.updateSummary(stack, summary);
            }
        });
    }

    @Override
    public void updateParent(final Stack stack, final Id parentId) {
        doItNowOffTheMainThread(new Action0() {
            @Override
            public void call() {
                syncUsecase.updateParent(stack, parentId);
            }
        });
    }

    @Override
    public void addLabel(final Stack stack, final Label label) {
        doItNowOffTheMainThread(new Action0() {
            @Override
            public void call() {
                syncUsecase.addLabel(stack, label);
            }
        });
    }

    @Override
    public void removeLabel(final Stack stack, final Label label) {
        doItNowOffTheMainThread(new Action0() {
            @Override
            public void call() {
                syncUsecase.removeLabel(stack, label);
            }
        });
    }

    @Override
    public void markCompleted(final Stack stack) {
        doItNowOffTheMainThread(new Action0() {
            @Override
            public void call() {
                syncUsecase.markCompleted(stack);
            }
        });
    }

    @Override
    public void markNotCompleted(final Stack stack) {
        doItNowOffTheMainThread(new Action0() {
            @Override
            public void call() {
                syncUsecase.markNotCompleted(stack);
            }
        });
    }

    private static void doItNowOffTheMainThread(final Action0 action) {
        Observable.create(
                new Observable.OnSubscribe<Void>() {
                    @Override
                    public void call(Subscriber<? super Void> subscriber) {
                        action.call();
                        subscriber.onCompleted();
                    }
                }
        )
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

}
