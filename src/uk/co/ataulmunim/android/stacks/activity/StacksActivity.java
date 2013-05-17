package uk.co.ataulmunim.android.stacks.activity;

import uk.co.ataulmunim.android.stacks.Crud;
import uk.co.ataulmunim.android.stacks.adapter.StacksPagerAdapter;
import uk.co.ataulmunim.android.stacks.contentprovider.Stacks;
import uk.co.ataulmunim.android.stacks.fragment.StacksEditFragment;
import uk.co.ataulmunim.android.stacks.fragment.StacksListFragment;
import uk.co.ataulmunim.android.view.FreezableViewPager;

import com.nicedistractions.shortstacks.R;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import android.widget.Toast;



public class StacksActivity extends Activity {
	public static final String TAG = "StacksActivity";
	
	public enum UserWarnedAboutBack { YES, NO, UNSET };
	/* "Press back to lose unsaved changes" */
	private UserWarnedAboutBack userWarned = UserWarnedAboutBack.UNSET;
	
	public static final int STACKS_LOADER = 0;
	
	public static final int DATES_LOADER = 1;
	
	private StacksPagerAdapter adapter;
	private FreezableViewPager pager;
	
	private String shortcode;
	private String notes;
	
	// Set with the default value, updated in the intent
	private int stackId = Stacks.ROOT_STACK_ID;  
	
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
				Log.w(TAG, 
						"Invalid stack Uri passed ("
						+ stackUri.getLastPathSegment()
						+ "). Stays unchanged as Stacks.ROOT_STACK_ID.");
			}	
		}
		
		// Sets `shortcode` and `notes` for this stack 
		shortcode = Crud.getStackShortcode(getContentResolver(), stackId);
		notes = Crud.getStackNotes(getContentResolver(), stackId);
		
		// shortcode must have a valid value, notes can be empty but not null
		if (shortcode == null) Log.e(TAG, "Shortcode value not resolved.");
		if (notes == null) notes = "";
		
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
	    this.menu = menu;
	    super.onCreateOptionsMenu(menu);
        
	    getMenuInflater().inflate(R.menu.menu_activity_stacks, menu);
                 
        return true;
    }
	private Menu menu;
	
	// TODO: there's a proper method like onPrepareOptionsMenu, search for invalidateOptionsMenu() or something
	private void reinflateOptionsMenu() {
	    getActionBar().setDisplayOptions(
                ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE,
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
                        | ActionBar.DISPLAY_SHOW_TITLE
        );
	    
	    getMenuInflater().inflate(R.menu.menu_activity_stacks, menu);
	}
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ab_menu_edit:
                onEditActionSelected(item);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void onEditActionSelected(MenuItem item) {
        pager.setCurrentItem(StacksPagerAdapter.EDIT_PAGE);
        menu.clear();
        setDoneDiscardBar();
        ((StacksEditFragment) adapter.getItem(StacksPagerAdapter.EDIT_PAGE))
                .updateInputFields();
    }
    
    /**
     * Inflates the DISCARD/DONE action bar, setting listeners for the buttons.
     */
    private void setDoneDiscardBar() {
        ActionBar actionBar = getActionBar();
        
        LayoutInflater inflater;
        
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            inflater = (LayoutInflater) getSystemService(
                    LAYOUT_INFLATER_SERVICE);
        } else {
            inflater = (LayoutInflater) actionBar.getThemedContext()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);    
        }
        
        final View doneDiscard = inflater.inflate(
                R.layout.actionbar_custom_view_done_discard, null);
        
        doneDiscard.findViewById(R.id.actionbar_done).setOnClickListener(
                new View.OnClickListener() {
                    
                    @Override
                    public void onClick(View v) {
                        // TODO: save changes, update EditFragment to reflect new content
                        Crouton.makeText(StacksActivity.this, "Changes saved",
                                Style.CONFIRM, shortConfig).show();
                        pager.setCurrentItem(StacksPagerAdapter.STACKS_PAGE);
                        reinflateOptionsMenu();
                    }
                });
        
        doneDiscard.findViewById(R.id.actionbar_discard).setOnClickListener(
                new View.OnClickListener() {
                    
                    @Override
                    public void onClick(View v) {
                        softDiscardChanges();
                    }
                });
        
        actionBar.setDisplayOptions(
                ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
                        | ActionBar.DISPLAY_SHOW_TITLE
        );
        
        final int matchParent = ViewGroup.LayoutParams.MATCH_PARENT;
        actionBar.setCustomView(doneDiscard, new ActionBar.LayoutParams(
                matchParent, matchParent));
    }
    
    private static final Configuration shortConfig;
    private static final Style what;
    static {
    	shortConfig = new Configuration.Builder()
				.setDuration(Configuration.DURATION_SHORT).build();
    	what = new Style.Builder(Style.INFO)
    		.setHeightDimensionResId(R.dimen.abs__action_bar_default_height)
    		.build();
    	// todo: set a proper height, with dedicated dimension
    }
    
    private void softDiscardChanges() {
        pager.setCurrentItem(StacksPagerAdapter.STACKS_PAGE);
        
        reinflateOptionsMenu();
    }
    
    /*
     * TODO:
     * Remove swipe to edit screen.
     * To move to the edit screen, the user can press the edit action.
     * 
     * In the edit screen, it displays a DISCARD/DONE bar at all times.
     * The fields will be pre-populated with the name and notes.
     * 
     * Without edited fields:
     * If (DISCARD/system-back/DONE): return to the stack without action.
     * 
     * With edited fields:
     * If (DISCARD/system-back): return to stack, show "Undo discard changes"
     * popup, which returns to edit fragment, with all the changes intact.
     * 
     * If (DONE): return to stack, showing "Undo changes" which will revert all
     * the changes.
     * 
     * @see android.support.v4.app.FragmentActivity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
    	if (pager.getCurrentItem() == StacksPagerAdapter.EDIT_PAGE
    	        && userWarned == UserWarnedAboutBack.YES) {
    	    
    	    Crouton.clearCroutonsForActivity(this);
    	    Crouton.makeText(this, R.string.unsaved_changes, what,
                    shortConfig).show();
    	    softDiscardChanges();  
    	    userWarned = UserWarnedAboutBack.NO;
    	    
    	} else if (pager.getCurrentItem() == StacksPagerAdapter.EDIT_PAGE
    	        && userWarned == UserWarnedAboutBack.NO) {
    	    
    	    Crouton.clearCroutonsForActivity(this);
    	    Crouton.makeText(this, R.string.warn_unsaved_changes, Style.WARN,
    	            shortConfig).show();
    	    userWarned = UserWarnedAboutBack.YES;
    	    
    	} else if (pager.getCurrentItem() == StacksPagerAdapter.EDIT_PAGE
                && userWarned == UserWarnedAboutBack.UNSET) {
            
            softDiscardChanges();
            
        } else super.onBackPressed();
    }
    
    
    public void setUserWarnedAboutBack(UserWarnedAboutBack flag) {
        userWarned = flag;
    }
    
    /**
	 * Gets the local (SQL) ID of the current stack.
	 * @return
	 */
	public int getStackId() {
		return stackId;
	}
	
	/**
	 * Used to initialise the shortcode input in {@link StacksEditFragment}
	 * without having to query the content provider for it.
	 * @return
	 */
	public String getShortcode() {
		return shortcode;
	}
	
	/**
	 * Set (and updated) in {@link StacksListFragment#onLoadFinished(android.support.v4.content.Loader, android.database.Cursor)}.
	 * @param shortcode
	 */
	public void setShortcode(String shortcode) {
		this.shortcode = shortcode;
	}
	
	/**
	 * Used to initialise the notes input in {@link StacksEditFragment} without
	 * having to query the content provider for it.
	 * @return
	 */
	public String getNotes() {
		return notes;
	}
	
	/**
	 * Set (and updated) in {@link StacksListFragment#onLoadFinished(android.support.v4.content.Loader, android.database.Cursor)}.
	 * @param shortcode
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	private void discardChanges() {
		Log.i(TAG, "discarding changes in EditFragment");
		pager.setFrozen(false);
		// TODO: close DISCARD | DONE, revert EditTexts
	}
}
