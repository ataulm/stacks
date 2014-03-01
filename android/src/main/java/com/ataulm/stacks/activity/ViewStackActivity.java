package com.ataulm.stacks.activity;

import android.os.Bundle;

import com.ataulm.stacks.R;
import com.ataulm.stacks.base.StacksBaseActivity;
import com.ataulm.stacks.fragment.ViewStackFragment;

public class ViewStackActivity extends StacksBaseActivity {

    public static final String EXTRA_STACK = "com.ataulm.stacks.extra.EXTRA_PARENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stacks);

        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new ViewStackFragment()).commit();
    }

}
