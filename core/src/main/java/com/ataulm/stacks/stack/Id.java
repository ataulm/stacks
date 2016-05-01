package com.ataulm.stacks.stack;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Id {

    public abstract String value();

    public static Id create(String value) {
        return new AutoValue_Id(value);
    }

    Id() {
        // instantiate AutoValue generated class
    }

}
