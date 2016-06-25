package com.ataulm.stacks;

import android.app.Application;

import com.ataulm.stacks.jabber.Jabber;

public class StacksApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Jabber.init(this);
    }

}
