package com.ataulm.stacks.stacks;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.ataulm.rv.StableIdDictionary;
import com.ataulm.stacks.R;
import com.ataulm.stacks.stack.Stack;
import com.ataulm.stacks.stack.Stacks;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StacksView extends FrameLayout {

    @BindView(R.id.stacks_empty_view)
    View emptyView;

    @BindView(R.id.stacks_recycler_view)
    RecyclerView recyclerView;

    private final StableIdDictionary<Stack> ids;

    public StacksView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ids = new StableIdDictionary<>();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_stacks, this);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void update(Stacks stacks, UserItemActions userItemActions) {
        RecyclerView.Adapter adapter = StacksAdapter.create(stacks, ids, userItemActions);
        recyclerView.swapAdapter(adapter, false);

        if (stacks.size() == 0) {
            emptyView.setVisibility(VISIBLE);
        } else {
            emptyView.setVisibility(GONE);
        }
    }

}
