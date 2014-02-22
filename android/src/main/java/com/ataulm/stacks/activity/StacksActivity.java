package com.ataulm.stacks.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.*;
import android.view.inputmethod.InputMethodManager;

import com.ataulm.stacks.R;
import com.ataulm.stacks.adapter.StacksPagerAdapter;
import com.ataulm.stacks.fragment.StackEditFragment;
import com.ataulm.stacks.model.Stack;
import com.ataulm.stacks.model.StackPersistor;
import com.ataulm.stacks.view.FreezableViewPager;

import de.keyboardsurfer.android.widget.crouton.Crouton;


public class StacksActivity extends NistActivity {

    /* "Press back to lose unsaved changes" */
    public enum UserWarnedAboutBack {
        YES, NO, UNSET
    }

    private StacksPagerAdapter adapter;
    private FreezableViewPager pager;
    private Stack stack;
    private Menu menu;

    private UserWarnedAboutBack userWarned = UserWarnedAboutBack.UNSET;
    private int stackId = Stack.DEFAULT_STACK_ID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stacks);

        final Intent intent = getIntent();
        final Uri stackUri = intent.getData();

        // TODO: differentiate between INTENTs by performing different actions
        // final String action = intent.getAction();

        if (stackUri != null) {
            try {
                stackId = Integer.parseInt(stackUri.getLastPathSegment());
            } catch (NumberFormatException e) {
                toast("Invalid stack Uri passed (" + stackUri.getLastPathSegment() + ").");
            }
        }
        stack = StackPersistor.retrieve(getContentResolver(), stackId);

        adapter = new StacksPagerAdapter(getFragmentManager());
        pager = (FreezableViewPager) findViewById(R.id.pager);
        pager.setFrozen(true);
        pager.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        Crouton.clearCroutonsForActivity(this);
        super.onDestroy();
    }

    /**
     * Inflates the menu items for the action bar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.menu = menu;
        getActionBar().setDisplayOptions(
                ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE,
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
                        | ActionBar.DISPLAY_SHOW_TITLE
        );
        getMenuInflater().inflate(R.menu.menu_activity_stacks, menu);

        // Don't allow deletion of the default Stack
        if (stack.getId() == Stack.DEFAULT_STACK_ID) {
            menu.findItem(R.id.ab_menu_delete).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ab_menu_edit:
                onEditActionSelected();
                return true;

            case R.id.ab_menu_delete:
                onDeleteActionSelected();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onDeleteActionSelected() {
        stack.delete();
        StackPersistor.persist(getContentResolver(), stack);
        finish();
    }

    /**
     * Switch to StackEditFragment.
     *
     * Clears the ActionBar, and sets Done | Discard in its place.
     */
    private void onEditActionSelected() {
        pager.setCurrentItem(StacksPagerAdapter.EDIT_PAGE);
        menu.clear();
        setDoneDiscardBar();
        ((StackEditFragment) adapter.getItem(StacksPagerAdapter.EDIT_PAGE)).updateInputFields();
    }

    /**
     * Fires a warning if there are unsaved changes, but returns on second press.
     * <p/>
     * If the user is editing the stack name or notes properties of the stack and presses back, a
     * Crouton is displayed warning the user. The user can press back again to return to the
     * StackViewFragment. If the user makes subsequent changes, the warning flag is reset, and the
     * user will be warned again.
     * <p/>
     * If the user is in a StackEditFragment, the return destination is set as StackViewFragment,
     * otherwise it behaves as a regular back button (finishes the current Activity).
     */
    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == StacksPagerAdapter.EDIT_PAGE) {
            if (userWarned == UserWarnedAboutBack.YES) {
                Crouton.clearCroutonsForActivity(this);
                showInfo(getString(R.string.unsaved_changes));
                softDiscardChanges();
                userWarned = UserWarnedAboutBack.NO;
            } else if (userWarned == UserWarnedAboutBack.NO) {
                Crouton.clearCroutonsForActivity(this);
                showInfo(getString(R.string.warn_unsaved_changes));
                userWarned = UserWarnedAboutBack.YES;
            } else {
                softDiscardChanges();
            }
            return;
        }
        super.onBackPressed();
    }

    /**
     * Get a representation of the current stack in a {@link com.ataulm.stacks.model.Stack} object.
     * <p/>
     * The returned Stack isn't guaranteed to stay up-to-date, so don't keep a long-lived reference.
     *
     * @return stack the representation of the current stack as a Stack object
     */
    public Stack getStack() {
        return stack;
    }

    /**
     * Inflates the DISCARD/DONE action bar, setting listeners for the buttons.
     */
    private void setDoneDiscardBar() {
        ActionBar actionBar = getActionBar();

        final LayoutInflater inflater;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        } else {
            inflater = (LayoutInflater) actionBar.getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        final View doneDiscard = inflater.inflate(R.layout.actionbar_custom_view_done_discard, null);

        doneDiscard.findViewById(R.id.actionbar_done).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideKeyboard();
                        acceptEdits();
                    }
                }
        );

        doneDiscard.findViewById(R.id.actionbar_discard).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideKeyboard();
                        softDiscardChanges();
                    }
                }
        );

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);

        final int matchParent = ViewGroup.LayoutParams.MATCH_PARENT;
        actionBar.setCustomView(doneDiscard, new ActionBar.LayoutParams(
                matchParent, matchParent));
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    private void acceptEdits() {
        showConfirm("Changes saved");

        ((StackEditFragment) adapter.getItem(StacksPagerAdapter.EDIT_PAGE)).commitChanges(stack);

        pager.setCurrentItem(StacksPagerAdapter.STACKS_PAGE);
        invalidateOptionsMenu();
    }

    private void softDiscardChanges() {
        pager.setCurrentItem(StacksPagerAdapter.STACKS_PAGE);
        invalidateOptionsMenu();
    }

    public void setUserWarnedAboutBack(UserWarnedAboutBack flag) {
        userWarned = flag;
    }

    /**
     * Gets the local (SQL) ID of the current stack.
     *
     * @return
     */
    public long getStackId() {
        return stack.getId();
    }
}
