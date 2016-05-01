package com.ataulm.stacks.stack;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Label {

    public abstract String value();

    public static Label create(String value) {
        return new AutoValue_Label(value);
    }

    Label() {
        // instantiate AutoValue generated class
    }

}
