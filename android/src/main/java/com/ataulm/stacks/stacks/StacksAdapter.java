package com.ataulm.stacks.stacks;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ataulm.Optional;
import com.ataulm.stacks.stack.Stack;
import com.ataulm.stacks.stack.Stacks;

public class StacksAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_INPUT_COUNT = 1;

    private final Optional<Stack> header;
    private final Stacks stacks;
    private final StackInputListener stackInputListener;

    public static StacksAdapter create(Stacks stacks, StackInputListener stackInputListener) {
        StacksAdapter stacksAdapter = new StacksAdapter(stacks.info(), stacks, stackInputListener);
        stacksAdapter.setHasStableIds(true);
        return stacksAdapter;
    }

    private StacksAdapter(Optional<Stack> header, Stacks stacks, StackInputListener stackInputListener) {
        this.header = header;
        this.stacks = stacks;
        this.stackInputListener = stackInputListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ViewType.HEADER.ordinal()) {
            return StackHeaderViewHolder.inflate(parent);
        } else if (viewType == ViewType.INPUT.ordinal()) {
            return StackInputViewHolder.inflate(parent);
        } else {
            return StackItemViewHolder.inflate(parent);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ViewType.HEADER.ordinal()) {
            ((StackHeaderViewHolder) holder).bind(getHeader());
        } else if (getItemViewType(position) == ViewType.INPUT.ordinal()) {
            ((StackInputViewHolder) holder).bind(stackInputListener);
        } else {
            Stack stack = stacks.get(position - headerCount());
            ((StackItemViewHolder) holder).bind(stack);
        }
    }

    @Override
    public int getItemCount() {
        return headerCount() + stacks.size() + VIEW_TYPE_INPUT_COUNT;
    }

    private int headerCount() {
        return header.isPresent() ? 1 : 0;
    }

    private Stack getHeader() {
        if (!header.isPresent()) {
            throw new RuntimeException("Header? There is no header.");
        }
        return header.get();
    }

    @Override
    public int getItemViewType(int position) {
        if (headerCount() != 0 && position == 0) {
            return ViewType.HEADER.ordinal();
        } else if (position == getItemCount() - VIEW_TYPE_INPUT_COUNT) {
            return ViewType.INPUT.ordinal();
        } else {
            return ViewType.ROW.ordinal();
        }
    }

    @Override
    public long getItemId(int position) {
        if (getItemViewType(position) == ViewType.HEADER.ordinal()) {
            return 0;
        } else if (getItemViewType(position) == ViewType.INPUT.ordinal()) {
            return 1;
        } else {
            return stacks.get(position - headerCount()).id().hashCode();
        }
    }

    private enum ViewType {

        HEADER,
        ROW,
        INPUT

    }

}
