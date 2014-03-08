package com.ataulm.stacks.persistence;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.stacks.R;
import com.ataulm.stacks.base.StacksBaseAdapter;
import com.ataulm.stacks.model.Stack;
import com.ataulm.stacks.view.BasicStackListItemView;

public class BasicStacksListAdapter extends StacksBaseAdapter<Stack> {

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = createItemView(LayoutInflater.from(parent.getContext()), parent);
        }
        updateItemView((BasicStackListItemView) view, position);
        return view;
    }

    protected View createItemView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.view_basic_stack_list_item, parent, false);
    }

    protected void updateItemView(BasicStackListItemView view, int position) {
        view.updateWith(getItem(position));
    }

}
