package com.ataulm.stacks.stacks;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.ataulm.Optional;
import com.ataulm.stacks.R;
import com.ataulm.stacks.stack.Id;
import com.ataulm.stacks.stack.Stack;
import com.ataulm.stacks.stack.Stacks;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StacksScreenLayout extends LinearLayout {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.view_screen_empty_view)
    View emptyView;

    @BindView(R.id.view_screen_recycler_view)
    RecyclerView recyclerView;

    public StacksScreenLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_view_stack_screen, this);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void update(Stacks stacks, ToolbarActions toolbarActions, StackInputListener inputListener) {
        updateToolbar(stacks, toolbarActions);

        RecyclerView.Adapter adapter = StacksAdapter.create(stacks);
        recyclerView.swapAdapter(adapter, false);

        if (stacks.size() == 0) {
            emptyView.setVisibility(VISIBLE);
            recyclerView.setVisibility(GONE);
        } else {
            recyclerView.setVisibility(VISIBLE);
            emptyView.setVisibility(GONE);
        }
    }

    private void updateToolbar(Stacks stacks, ToolbarActions actions) {
        if (stacks.info().isPresent()) {
            Stack stack = stacks.info().get();
            updateToolbarWith(stack, actions);
        } else {
            updateToolbarAsRootStack(actions);
        }
    }

    private void updateToolbarWith(final Stack stack, final ToolbarActions actions) {
        toolbar.setTitle(stack.summary());
        toolbar.setNavigationIcon(R.drawable.wire_up);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Optional<Id> id = stack.parentId();
                if (id.isPresent()) {
                    actions.onClickNavigateUpTo(id.get());
                } else {
                    throw new IllegalStateException("stack has no parent: " + stack);
                }
            }
        });
    }

    private void updateToolbarAsRootStack(final ToolbarActions actions) {
        toolbar.setTitle("Stacks");
        toolbar.setNavigationIcon(R.drawable.wire_nav_drawer);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                actions.onClickOpenNavigationDrawer();
            }
        });
    }

}
