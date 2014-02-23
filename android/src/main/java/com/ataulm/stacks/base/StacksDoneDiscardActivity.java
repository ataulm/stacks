package com.ataulm.stacks.base;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.stacks.R;
import com.novoda.notils.caster.Classes;

public class StacksDoneDiscardActivity extends StacksBaseActivity {

    private DoneDiscardListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listener = Classes.from(this);

        setDoneDiscardActionBar();
    }

    private void setDoneDiscardActionBar() {
        final View customActionBarView = getLayoutInflater().inflate(R.layout.actionbar_custom_view_done, null);

        customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onDoneClick();
            }

        });

        final ActionBar actionBar = getActionBar();
        int mask = ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE;
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, mask);
        actionBar.setCustomView(customActionBarView, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.discard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actionbar_discard) {
            listener.onDiscardClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public interface DoneDiscardListener {

        void onDoneClick();

        void onDiscardClick();

    }

}
