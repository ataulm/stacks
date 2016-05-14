package com.ataulm.stacks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.ataulm.stacks.remove.RemovedActivity;
import com.ataulm.stacks.view.ViewActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NavigationDrawerView extends LinearLayout {

    @Bind(R.id.navigation_drawer_item_home)
    View homeButton;

    @Bind(R.id.navigation_drawer_item_removed)
    View removedButton;

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
                goTo(ViewActivity.class);
            }
        });

        removedButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                goTo(RemovedActivity.class);
            }
        });
    }

    private void goTo(Class<? extends Activity> cls) {
        // TODO this should be a callback to the activity
        getContext().startActivity(new Intent(getContext(), cls));
    }

}
