package uk.co.ataulmunim.android.stacks.activity;

import uk.co.ataulmunim.android.stacks.Crud;
import uk.co.ataulmunim.android.stacks.adapter.StacksPagerAdapter;
import uk.co.ataulmunim.android.stacks.contentprovider.Stacks;
import uk.co.ataulmunim.android.stacks.fragment.StacksEditFragment;
import uk.co.ataulmunim.android.stacks.fragment.StacksListFragment;
import uk.co.ataulmunim.android.view.FreezableViewPager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.nicedistractions.shortstacks.R;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import com.actionbarsherlock.view.MenuItem;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import android.widget.Toast;

public class StacksActivity extends SherlockFragmentActivity {
	public static final String TAG = "StacksActivity";
	
	public static final int STACKS_LOADER = 0;
	public static final int DATES_LOADER = 1;
	
	private StacksPagerAdapter adapter;
	private FreezableViewPager pager;
	
	private StacksListFragment listFragment;
    private StacksEditFragment editFragment;
    
    /**
     * Flag indicating if the user has been warned that there are unsaved
     * changes (user pressed System Back).
     */
    private boolean userWarnedAboutBack;
	
	private String shortcode;
	private String notes;
	
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
		
		adapter = new StacksPagerAdapter(getSupportFragmentManager());
		pager = (FreezableViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        
        editFragment = (StacksEditFragment) adapter.getItem(
        		StacksPagerAdapter.EDIT_PAGE);
        listFragment = (StacksListFragment) adapter.getItem(
        		StacksPagerAdapter.STACKS_PAGE);
	}
	
    @Override
	public void onDestroy() {
		Crouton.clearCroutonsForActivity(this);
		super.onDestroy();
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
        getSupportMenuInflater().inflate(R.menu.menu_activity_stacks, menu);
        
        return true;
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ab_menu_edit:
                pager.setCurrentItem(StacksPagerAdapter.EDIT_PAGE);
                pager.setFrozen(true);
                // TODO: launch DISCARD | DONE                
                
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
    @Override
    public void onBackPressed() {
    	if (pager.isFrozen()) {
    		Log.i(TAG, "Back pressed in frozen pager");
    		    		
    		if (!userWarnedAboutBack) {
    			Style shortWarnStyle = new Style.Builder(Style.WARNING)
    					.setDuration(Style.DURATION_SHORT).build();
    			
    			// TODO: crouton warning, "unsaved changes will be lost"
    			Crouton.makeText(this,
    					R.string.warn_unsaved_changes,
    					shortWarnStyle).show();
    			
        		userWarnedAboutBack = true;	
    		} else {
    			Crouton.clearCroutonsForActivity(this);
    			Style shortInfoStyle = new Style.Builder(Style.INFO)
    					.setDuration(Style.DURATION_SHORT).build();	
    			Crouton.makeText(this, R.string.unsaved_changes, shortInfoStyle)
    					.show();
    			discardChanges();
    			userWarnedAboutBack = false;
    			// LAYOUT: back pressed in EditFragment to discard changes
    			// returns to previous Fragment
    			pager.setCurrentItem(StacksPagerAdapter.STACKS_PAGE);
    		}
    		
    	} else super.onBackPressed();
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
