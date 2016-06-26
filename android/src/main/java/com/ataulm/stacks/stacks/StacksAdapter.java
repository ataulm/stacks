package com.ataulm.stacks.stacks;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ataulm.rv.StableIdDictionary;
import com.ataulm.stacks.stack.Stack;
import com.ataulm.stacks.stack.Stacks;

public final class StacksAdapter extends RecyclerView.Adapter<StackItemViewHolder> {

    private final Stacks stacks;
    private final StableIdDictionary<Stack> ids;

    public static StacksAdapter create(Stacks stacks, StableIdDictionary<Stack> ids) {
        StacksAdapter stacksAdapter = new StacksAdapter(stacks, ids);
        stacksAdapter.setHasStableIds(true);
        return stacksAdapter;
    }

    private StacksAdapter(Stacks stacks, StableIdDictionary<Stack> ids) {
        this.stacks = stacks;
        this.ids = ids;
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
        Stack stack = stacks.get(position);
        return ids.getId(stack);
    }

}