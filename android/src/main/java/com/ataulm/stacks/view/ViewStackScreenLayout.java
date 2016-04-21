package com.ataulm.stacks.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.ataulm.stacks.R;
import com.ataulm.stacks.StackInputListener;
import com.ataulm.stacks.StackItemListener;
import com.ataulm.stacks.StacksAdapter;
import com.ataulm.stacks.stack.Stacks;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ViewStackScreenLayout extends LinearLayout implements ViewStackScreen {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.view_recycler_view)
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
    public void setTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override
    public void showData(Stacks stacks, StackItemListener interactionListener, StackInputListener inputListener) {
        StacksAdapter adapter = StacksAdapter.create(stacks, interactionListener, inputListener);
        recyclerView.swapAdapter(adapter, false);
    }

    @Override
    public void showEmptyScreen(StackItemListener interactionListener, StackInputListener inputListener) {
        StacksAdapter adapter = StacksAdapter.create(Stacks.empty(), interactionListener, inputListener);
        recyclerView.swapAdapter(adapter, false);
    }

}
