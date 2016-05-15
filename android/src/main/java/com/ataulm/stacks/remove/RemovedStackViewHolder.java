package com.ataulm.stacks.remove;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.stacks.R;
import com.ataulm.stacks.RemovedStackItemView;
import com.ataulm.stacks.stack.Stack;

final class RemovedStackViewHolder extends RecyclerView.ViewHolder {

    private final RemovedStackItemView removedStackItemView;

    public static RemovedStackViewHolder inflate(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_removed_stack, parent, false);
        return new RemovedStackViewHolder(view, ((RemovedStackItemView) view));
    }

    private RemovedStackViewHolder(View itemView, RemovedStackItemView removedStackItemView) {
        super(itemView);
        this.removedStackItemView = removedStackItemView;
    }

    public void bind(Stack stack, RemovedStackItemListener listener) {
        removedStackItemView.bind(stack, listener);
    }

}
