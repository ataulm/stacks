package com.ataulm.stacks;

import android.util.Log;

import rx.Observer;

abstract class LoggingObserver<T> implements Observer<T> {

    @Override
    public void onCompleted() {
        Log.d(tag(), "onCompleted");
    }

    @Override
    public void onError(Throwable e) {
        Log.d(tag(), "onError", e);
    }

    @Override
    public void onNext(T t) {
        Log.d(tag(), "onCompleted");
    }

    private String tag() {
        return getClass().getSimpleName();
    }

}
