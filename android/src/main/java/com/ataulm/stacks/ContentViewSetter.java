package com.ataulm.stacks;

import android.support.annotation.LayoutRes;

public interface ContentViewSetter {

    <T> T display(@LayoutRes int layout);

}
