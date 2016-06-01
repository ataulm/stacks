package com.ataulm.stacks.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.ataulm.Optional;
import com.ataulm.stacks.R;
import com.ataulm.stacks.StackInputListener;
import com.ataulm.stacks.StackItemListener;
import com.ataulm.stacks.StacksAdapter;
import com.ataulm.stacks.stack.Id;
import com.ataulm.stacks.stack.Stacks;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ViewStackScreenLayout extends LinearLayout implements ViewStackScreen {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.view_screen_empty_view)
    View emptyView;

    @Bind(R.id.view_screen_recycler_view)
    RecyclerView recyclerView;

    public ViewStackScreenLayout(Context context, AttributeSet attrs) {
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

    @Override
    public void setupToolbar(String title, Optional<Id> parent, ToolbarActionListener toolbarActionListener) {
        toolbar.setTitle(title);
        updateToolbarMenuGivenParentId(parent, toolbarActionListener);
    }

    @Override
    public void showData(Stacks stacks, Optional<Id> parentId, StackItemListener interactionListener, StackInputListener inputListener, ToolbarActionListener toolbarActionListener) {
        updateToolbarMenuGivenParentId(parentId, toolbarActionListener);

        RecyclerView.Adapter adapter = StacksAdapter.create(stacks, interactionListener, inputListener);
        recyclerView.swapAdapter(adapter, false);

        emptyView.setVisibility(GONE);
    }

    @Override
    public void showEmptyScreen(Optional<Id> parentId, StackInputListener inputListener, ToolbarActionListener toolbarActionListener) {
        updateToolbarMenuGivenParentId(parentId, toolbarActionListener);

        RecyclerView.Adapter adapter = StacksAdapter.create(Stacks.empty(), StackItemListener.NO_OP, inputListener);
        recyclerView.swapAdapter(adapter, false);

        emptyView.setVisibility(VISIBLE);
    }

    private void updateToolbarMenuGivenParentId(final Optional<Id> parentId, final ToolbarActionListener toolbarActionListener) {
        if (parentId.isPresent()) {
            toolbar.setNavigationIcon(R.drawable.wire_up);
            toolbar.setNavigationOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    toolbarActionListener.onClickNavigateUpToStackWith(parentId.get());
                }
            });
        } else {
            toolbar.setNavigationIcon(R.drawable.wire_nav_drawer);
            toolbar.setNavigationOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    toolbarActionListener.onClickToggleNavigationMenu();
                }
            });
        }
    }

}
