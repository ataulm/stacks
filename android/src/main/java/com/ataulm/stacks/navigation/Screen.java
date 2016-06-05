package com.ataulm.stacks.navigation;

public enum Screen {

    STACKS_LIST("stack"),
    REMOVED_STACKS("removed");

    private final String path;

    Screen(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
