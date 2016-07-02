package com.ataulm.stacks.stacks;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ataulm.Optional;
import com.ataulm.rv.StableIdDictionary;
import com.ataulm.stacks.R;
import com.ataulm.stacks.stack.Stack;
import com.ataulm.stacks.stack.Stacks;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StacksScreenLayout extends LinearLayout {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.view_screen_stacks_view)
    StacksView stacksView;

    @BindView(R.id.view_screen_stack_input_view)
    StackInputView stackInputView;

    public StacksScreenLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_view_stack_screen, this);
        ButterKnife.bind(this);
    }

    public void set(StackInputListener inputListener) {
        stackInputView.bind(inputListener);
    }

    public void update(Stacks stacks, ClickActions clickActions) {
        stacksView.update(stacks, clickActions);
    }

    public void updateToolbar(Optional<Stack> stack, ToolbarActions actions) {
        if (stack.isPresent()) {
            updateToolbar(stack.get(), actions);
        } else {
            updateToolbar(actions);
        }
    }

    private void updateToolbar(final Stack stack, final ToolbarActions actions) {
        toolbar.setTitle(stack.summary());
        toolbar.setNavigationIcon(R.drawable.wire_up);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                actions.onClickNavigateUpToParentOf(stack);
            }
        });
    }

    private void updateToolbar(final ToolbarActions actions) {
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
