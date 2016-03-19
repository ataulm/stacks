package com.ataulm.stacks;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.stacks.stack.Stack;

final class StackViewHolder extends RecyclerView.ViewHolder {

    private final StackItemView stackItemView;

    public static StackViewHolder inflate(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_stack_item, parent, false);
        return new StackViewHolder(view, ((StackItemView) view));
    }

    private StackViewHolder(View itemView, StackItemView stackItemView) {
        super(itemView);
        this.stackItemView = stackItemView;
    }

    public void bind(Stack stack) {
        stackItemView.bind(stack);
    }

}
