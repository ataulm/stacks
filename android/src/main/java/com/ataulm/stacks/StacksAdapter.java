package com.ataulm.stacks;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ataulm.stacks.stack.Stack;
import com.ataulm.stacks.stack.Stacks;

class StacksAdapter extends RecyclerView.Adapter<StackViewHolder> {

    private final Stacks stacks;
    private final StackItemListener listener;

    StacksAdapter(Stacks stacks, StackItemListener listener) {
        this.stacks = stacks;
        this.listener = listener;
        setHasStableIds(true);
    }

    @Override
    public StackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return StackViewHolder.inflate(parent);
    }

    @Override
    public void onBindViewHolder(StackViewHolder holder, int position) {
        Stack stack = stacks.get(position);
        holder.bind(stack, listener);
    }

    @Override
    public int getItemCount() {
        return stacks.size();
    }

    @Override
    public long getItemId(int position) {
        return stacks.get(position).id().hashCode();
    }

}