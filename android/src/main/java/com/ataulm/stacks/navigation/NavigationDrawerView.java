package com.ataulm.stacks.navigation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.ataulm.stacks.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NavigationDrawerView extends LinearLayout {

    @BindView(R.id.navigation_drawer_item_home)
    View homeButton;

    @BindView(R.id.navigation_drawer_item_removed)
    View removedButton;

    private Listener listener;

    public NavigationDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOrientation(VERTICAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_navigation_drawer, this);
        ButterKnife.bind(this);

        homeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickViewStacks();
            }
        });

        removedButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickRemovedStacks();
            }
        });
    }

    public void set(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {

        void onClickViewStacks();

        void onClickRemovedStacks();

    }

}
