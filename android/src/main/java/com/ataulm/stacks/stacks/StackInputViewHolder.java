package com.ataulm.stacks.stacks;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.stacks.R;

final class StackInputViewHolder extends RecyclerView.ViewHolder {

    private final StackInputView stackInputView;

    public static StackInputViewHolder inflate(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.view_stack_input, parent, false);
        return new StackInputViewHolder(view, ((StackInputView) view));
    }

    private StackInputViewHolder(View itemView, StackInputView stackInputView) {
        super(itemView);
        this.stackInputView = stackInputView;
    }

    public void bind(StackInputListener listener) {
        stackInputView.bind(listener);
    }

}
