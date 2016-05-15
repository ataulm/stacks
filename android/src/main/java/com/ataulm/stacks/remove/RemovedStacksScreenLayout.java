package com.ataulm.stacks.remove;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ataulm.stacks.R;
import com.ataulm.stacks.stack.Stack;
import com.ataulm.stacks.stack.Stacks;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RemovedStacksScreenLayout extends LinearLayout implements RemovedStacksScreen {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.removed_stacks_screen_empty_view)
    View emptyView;

    @Bind(R.id.removed_stacks_screen_recycler_view)
    RecyclerView recyclerView;

    public RemovedStacksScreenLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_removed_stacks_screen, this);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void setTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override
    public void showData(Stacks stacks, RemovedStackItemListener listener) {
        RecyclerView.Adapter adapter = RemovedStacksAdapter.create(stacks, listener);
        recyclerView.swapAdapter(adapter, false);

        emptyView.setVisibility(GONE);
    }

    @Override
    public void showEmptyScreen() {
        RecyclerView.Adapter adapter = RemovedStacksAdapter.create(Stacks.empty(), RemovedStackItemListener.NO_OP);
        recyclerView.swapAdapter(adapter, false);

        emptyView.setVisibility(VISIBLE);
    }

    private static final class RemovedStacksAdapter extends RecyclerView.Adapter<RemovedStackViewHolder> {

        private final Stacks stacks;
        private final RemovedStackItemListener listener;

        public static RemovedStacksAdapter create(Stacks stacks, RemovedStackItemListener listener) {
            RemovedStacksAdapter stacksAdapter = new RemovedStacksAdapter(stacks, listener);
            stacksAdapter.setHasStableIds(true);
            return stacksAdapter;
        }

        private RemovedStacksAdapter(Stacks stacks, RemovedStackItemListener listener) {
            this.stacks = stacks;
            this.listener = listener;
        }

        @Override
        public RemovedStackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return RemovedStackViewHolder.inflate(parent);
        }

        @Override
        public void onBindViewHolder(RemovedStackViewHolder holder, int position) {
            Stack stack = stacks.get(position);
            holder.bind(stack, listener);
        }

        @Override
        public int getItemCount() {
            return stacks.size();
        }

    }

}
