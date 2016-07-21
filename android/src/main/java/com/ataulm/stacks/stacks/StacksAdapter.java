package com.ataulm.stacks.stacks;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ataulm.rv.StableIdDictionary;
import com.ataulm.stacks.stack.Stack;
import com.ataulm.stacks.stack.Stacks;
import com.ataulm.stacks.stacks.item.StackItemViewHolder;

public final class StacksAdapter extends RecyclerView.Adapter<StackItemViewHolder> {

    private final Stacks stacks;
    private final StableIdDictionary<Stack> ids;
    private final ItemClickActions itemClickActions;

    public static StacksAdapter create(Stacks stacks, StableIdDictionary<Stack> ids, ItemClickActions itemClickActions) {
        StacksAdapter stacksAdapter = new StacksAdapter(stacks, ids, itemClickActions);
        stacksAdapter.setHasStableIds(true);
        return stacksAdapter;
    }

    private StacksAdapter(Stacks stacks, StableIdDictionary<Stack> ids, ItemClickActions itemClickActions) {
        this.stacks = stacks;
        this.ids = ids;
        this.itemClickActions = itemClickActions;
    }

    @Override
    public StackItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return StackItemViewHolder.inflate(parent);
    }

    @Override
    public void onBindViewHolder(StackItemViewHolder holder, int position) {
        Stack stack = stacks.get(position);
        holder.bind(stack, itemClickActions);
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
