package com.ataulm.stacks.removed_stacks;

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

import butterknife.BindView;
import butterknife.ButterKnife;

public class RemovedStacksScreenLayout extends LinearLayout {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.removed_stacks_screen_empty_view)
    View emptyView;

    @BindView(R.id.removed_stacks_screen_recycler_view)
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

    public void setupToolbar(String title, ToolbarActionListener toolbarActionListener) {
        toolbar.setTitle(title);
        updateToolbar(toolbarActionListener);
    }

    public void showData(Stacks stacks, ToolbarActionListener toolbarActionListener) {
        updateToolbar(toolbarActionListener);

        RecyclerView.Adapter adapter = RemovedStacksAdapter.create(stacks);
        recyclerView.swapAdapter(adapter, false);

        emptyView.setVisibility(GONE);
    }

    public void showEmptyScreen(ToolbarActionListener toolbarActionListener) {
        updateToolbar(toolbarActionListener);

        RecyclerView.Adapter adapter = RemovedStacksAdapter.create(Stacks.empty());
        recyclerView.swapAdapter(adapter, false);

        emptyView.setVisibility(VISIBLE);
    }

    private void updateToolbar(final ToolbarActionListener toolbarActionListener) {
        toolbar.setNavigationIcon(R.drawable.wire_nav_drawer);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarActionListener.onClickToggleNavigationMenu();
            }
        });
    }

    private static final class RemovedStacksAdapter extends RecyclerView.Adapter<RemovedStackViewHolder> {
        private final Stacks stacks;

        public static RemovedStacksAdapter create(Stacks stacks) {
            RemovedStacksAdapter stacksAdapter = new RemovedStacksAdapter(stacks);
            stacksAdapter.setHasStableIds(true);
            return stacksAdapter;
        }

        private RemovedStacksAdapter(Stacks stacks) {
            this.stacks = stacks;
        }

        @Override
        public RemovedStackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return RemovedStackViewHolder.inflate(parent);
        }

        @Override
        public void onBindViewHolder(RemovedStackViewHolder holder, int position) {
            Stack stack = stacks.get(position);
            holder.bind(stack);
        }

        @Override
        public int getItemCount() {
            return stacks.size();
        }

    }

}
