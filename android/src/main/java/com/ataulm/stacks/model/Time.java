package com.ataulm.stacks.model;

import org.joda.time.DateTime;
import org.joda.time.Instant;

public class Time {

    public static final Time UNSET;
    static {
        final long YEAR_NINETEEN_HUNDRED = -2208988800000l;
        UNSET = new Time(YEAR_NINETEEN_HUNDRED);
    }

    private final DateTime dateTime;

    public Time(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Time(long millisSinceEpoch) {
        this.dateTime = new DateTime(millisSinceEpoch);
    }

    public static Time now() {
        return new Time(new DateTime(Instant.now()));
    }

    public boolean isSet() {
        return !UNSET.equals(this);
    }

}
