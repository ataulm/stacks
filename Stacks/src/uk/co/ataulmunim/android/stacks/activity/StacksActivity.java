package uk.co.ataulmunim.android.stacks.activity;

import uk.co.ataulmunim.android.stacks.adapter.StacksPagerAdapter;
import uk.co.ataulmunim.android.view.FreezableViewPager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.nicedistractions.shortstacks.R;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import com.actionbarsherlock.view.MenuItem;
import android.widget.Toast;

public class StacksActivity extends SherlockFragmentActivity {
	private static final String TAG = "StacksActivity";
	private static int instanceCounter = 0;
	
	private StacksPagerAdapter adapter;
	private FreezableViewPager pager; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stacks);
			
		Log.d(TAG, "Number of instances: " + ++instanceCounter);
		
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
}
