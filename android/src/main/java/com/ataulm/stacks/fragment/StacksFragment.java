package com.ataulm.stacks.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.stacks.R;
import com.ataulm.stacks.base.BaseFragment;

public class StacksFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stacks, container, false);
    }

}
