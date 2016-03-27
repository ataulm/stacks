package com.ataulm.stacks;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ataulm.Optional;
import com.ataulm.stacks.stack.Stack;
import com.ataulm.stacks.stack.Stacks;

class StacksAdapter extends RecyclerView.Adapter {

    private final Stacks stacks;
    private final StackItemListener listener;
    private final Optional<Stack> header;

    StacksAdapter(Stacks stacks, StackItemListener listener) {
        this.stacks = stacks;
        this.listener = listener;
        this.header = stacks.info();
        setHasStableIds(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ViewType.HEADER.ordinal()) {
            return StackHeaderViewHolder.inflate(parent);
        } else {
            return StackItemViewHolder.inflate(parent);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ViewType.HEADER.ordinal()) {
            ((StackHeaderViewHolder) holder).bind(header.get());
        } else {
            Stack stack;
            if (header.isPresent()) {
                stack = stacks.get(position - 1);
            } else {
                stack = stacks.get(position);
            }
            ((StackItemViewHolder) holder).bind(stack, listener);
        }
    }

    @Override
    public int getItemCount() {
        if (header.isPresent()) {
            return stacks.size() + 1;
        } else {
            return stacks.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (header.isPresent() && position == 0) {
            return ViewType.HEADER.ordinal();
        } else {
            return ViewType.ROW.ordinal();
        }
    }

    @Override
    public long getItemId(int position) {
        if (getItemViewType(position) == ViewType.HEADER.ordinal()) {
            return 0;
        } else {
            if (header.isPresent()) {
                return stacks.get(position - 1).id().hashCode();
            } else {
                return stacks.get(position).id().hashCode();
            }
        }
    }

    private enum ViewType {

        HEADER,
        ROW

    }

}
