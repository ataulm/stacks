package com.ataulm;

import rx.Observable;
import rx.functions.Func1;

public final class EventRxFunctions {

    private EventRxFunctions() {
        // utility class
    }

    /**
     * Starts with an empty Event of type LOADING, with each
     * onNext<T> being delivered as an Event of type IDLE; i.e.
     * no partial loading. Errors will be delivered as Event of
     * type ERROR.
     */
    public static <T> Observable.Transformer<T, Event<T>> asEvents() {
        return new Observable.Transformer<T, Event<T>>() {

            @Override
            public Observable<Event<T>> call(Observable<T> data) {
                return data
                        .map(EventRxFunctions.<T>asIdleEventWithData())
                        .startWith(EventRxFunctions.<T>loadingEvent())
                        .onErrorReturn(EventRxFunctions.<T>errorEvent());
            }

        };
    }

    private static <T> Event<T> loadingEvent() {
        return Event.loading();
    }

    private static <T> Func1<Throwable, Event<T>> errorEvent() {
        return new Func1<Throwable, Event<T>>() {

            @Override
            public Event<T> call(Throwable throwable) {
                return Event.error(throwable);
            }

        };
    }

    private static <T> Func1<T, Event<T>> asIdleEventWithData() {
        return new Func1<T, Event<T>>() {

            @Override
            public Event<T> call(T t) {
                return Event.idle(t);

            }

        };
    }

}
