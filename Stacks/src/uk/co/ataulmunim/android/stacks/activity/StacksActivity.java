package uk.co.ataulmunim.android.stacks.activity;

import uk.co.ataulmunim.android.stacks.Crud;
import uk.co.ataulmunim.android.stacks.adapter.StacksPagerAdapter;
import uk.co.ataulmunim.android.stacks.contentprovider.Stacks;
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
import android.widget.Toast;

public class StacksActivity extends SherlockFragmentActivity {
	public static final int STACKS_LOADER = 0;
	public static final int DATES_LOADER = 1;
	
	private static final String TAG = "StacksActivity";
	
	private StacksPagerAdapter adapter;
	private FreezableViewPager pager;
	
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
		
		adapter = new StacksPagerAdapter(getSupportFragmentManager());
		pager = (FreezableViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
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
            case R.id.menu_item_edit:
                Toast.makeText(this, "Entering edit mode", Toast.LENGTH_SHORT).show();
                pager.setCurrentItem(StacksPagerAdapter.EDIT_PAGE);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onBackPressed() {
    	if (pager.getCurrentItem() == StacksPagerAdapter.EDIT_PAGE) {
    		// TODO: there should be some user notification at this point
    		// so the user doesn't feel like it's unresponsive, perhaps
    		// a crouton.
    		Log.i(TAG, "Back pressed");
    		// toggle the frozen state - this is just for testing time, we
    		// don't want to get stuck on the screen.
    		// TODO: when the pager enters EDIT MODE, it should freeze.
    		pager.setFrozen(!pager.isFrozen());
    		Toast.makeText(this, "Pager frozen: " + pager.isFrozen(), Toast.LENGTH_SHORT).show();
    		
    	} else super.onBackPressed();
    }
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	/**
	 * Gets the local (SQL) ID of the current stack.
	 * @return
	 */
	public int getStackId() {
		return stackId;
	}
	
	/**
	 * Used to initialise the shortcode input in StacksEditFragment without
	 * having to query the content provider for it.
	 * @return
	 */
	public String getShortcode() {
		return shortcode;
	}
	
	/**
	 * Set (and updated) in StacksListFragment#onLoadFinished().
	 * @param shortcode
	 */
	public void setShortcode(String shortcode) {
		this.shortcode = shortcode;
	}
	
	/**
	 * Used to initialise the notes input in StacksEditFragment without having
	 * to query the content provider for it.
	 * @return
	 */
	public String getNotes() {
		return notes;
	}
	
	/**
	 * Set (and updated) in StacksListFragment#onLoadFinished().
	 * @param shortcode
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}
}
