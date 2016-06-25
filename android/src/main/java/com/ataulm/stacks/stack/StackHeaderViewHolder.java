package com.ataulm.stacks.stack;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.stacks.R;

final class StackHeaderViewHolder extends RecyclerView.ViewHolder {

    private final StackHeaderView stackHeaderView;

    public static StackHeaderViewHolder inflate(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_stack_header, parent, false);
        return new StackHeaderViewHolder(view, ((StackHeaderView) view));
    }

    private StackHeaderViewHolder(View itemView, StackHeaderView stackHeaderView) {
        super(itemView);
        this.stackHeaderView = stackHeaderView;
    }

    public void bind(Stack stack) {
        stackHeaderView.bind(stack);
    }

}
