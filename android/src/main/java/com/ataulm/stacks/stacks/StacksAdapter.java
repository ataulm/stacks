package com.ataulm.stacks.stacks;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ataulm.stacks.stack.Stack;
import com.ataulm.stacks.stack.Stacks;

public final class StacksAdapter extends RecyclerView.Adapter<StackItemViewHolder> {

    private final Stacks stacks;

    public static StacksAdapter create(Stacks stacks) {
        StacksAdapter stacksAdapter = new StacksAdapter(stacks);
        stacksAdapter.setHasStableIds(true);
        return stacksAdapter;
    }

    private StacksAdapter(Stacks stacks) {
        this.stacks = stacks;
    }

    @Override
    public StackItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return StackItemViewHolder.inflate(parent);
    }

    @Override
    public void onBindViewHolder(StackItemViewHolder holder, int position) {
        Stack stack = stacks.get(position);
        holder.bind(stack);
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
