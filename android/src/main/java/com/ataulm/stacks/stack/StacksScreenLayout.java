package com.ataulm.stacks.stack;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.ataulm.Optional;
import com.ataulm.stacks.jabber.Jabber;
import com.ataulm.stacks.R;

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

    public void setupToolbar(String title, Optional<Id> parent) {
        toolbar.setTitle(title);
        updateToolbarMenuGivenParentId(parent);
    }

    public void showData(Stacks stacks, StackInputListener inputListener) {
        RecyclerView.Adapter adapter = StacksAdapter.create(stacks, inputListener);
        recyclerView.swapAdapter(adapter, false);

        emptyView.setVisibility(GONE);
    }

    public void showEmptyScreen(StackInputListener inputListener) {
        RecyclerView.Adapter adapter = StacksAdapter.create(Stacks.empty(), inputListener);
        recyclerView.swapAdapter(adapter, false);

        emptyView.setVisibility(VISIBLE);
    }

    private void updateToolbarMenuGivenParentId(Optional<Id> parentId) {
        if (parentId.isPresent()) {
            setupToolbarToNavigateUp();
        } else {
            setupToolbarToToggleMenu();
        }
    }

    private void setupToolbarToNavigateUp() {
        toolbar.setNavigationIcon(R.drawable.wire_up);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Jabber.toast("on click navigate up");
            }
        });
    }

    private void setupToolbarToToggleMenu() {
        toolbar.setNavigationIcon(R.drawable.wire_nav_drawer);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Jabber.toast("on click toggle menu");
            }
        });
    }

}
