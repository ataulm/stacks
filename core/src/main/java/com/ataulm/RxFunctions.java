package com.ataulm;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public final class RxFunctions {

    private RxFunctions() {
        // utility class
    }

    public static Func1<String, Boolean> onlyNonEmptyStrings() {
        return new Func1<String, Boolean>() {

            @Override
            public Boolean call(String string) {
                return string != null && !string.trim().isEmpty();
            }

        };
    }

    public static <T> Func1<T, Boolean> onlyNonNull() {
        return new Func1<T, Boolean>() {
            @Override
            public Boolean call(T t) {
                return t != null;
            }
        };
    }

    public static <T> Func1<Iterable<T>, Observable<T>> emitEachElement() {
        return new Func1<Iterable<T>, Observable<T>>() {

            @Override
            public Observable<T> call(Iterable<T> iterable) {
                return Observable.from(iterable);
            }

        };
    }

    public static <T> Observable.Operator<T, T> swallowOnCompleteEvents() {
        return new Observable.Operator<T, T>() {

            @Override
            public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
                return new Subscriber<T>() {

                    @Override
                    public void onCompleted() {
                        // Swallow
                    }

                    @Override
                    public void onError(Throwable e) {
                        subscriber.onError(e);
                    }

                    @Override
                    public void onNext(T edition) {
                        subscriber.onNext(edition);
                    }

                };
            }

        };
    }

}
