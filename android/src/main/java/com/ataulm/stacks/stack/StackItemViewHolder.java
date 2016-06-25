package com.ataulm.stacks.stack;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.stacks.R;

final class StackItemViewHolder extends RecyclerView.ViewHolder {

    private final StackItemView stackItemView;

    public static StackItemViewHolder inflate(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_stack_item, parent, false);
        return new StackItemViewHolder(view, ((StackItemView) view));
    }

    private StackItemViewHolder(View itemView, StackItemView stackItemView) {
        super(itemView);
        this.stackItemView = stackItemView;
    }

    public void bind(Stack stack) {
        stackItemView.bind(stack);
    }

}
