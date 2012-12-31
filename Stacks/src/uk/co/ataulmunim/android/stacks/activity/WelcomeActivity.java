package uk.co.ataulmunim.android.stacks.activity;

import uk.co.ataulmunim.android.stacks.Crud;
import uk.co.ataulmunim.android.stacks.contentprovider.Stacks;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.nicedistractions.shortstacks.R;

public class WelcomeActivity extends SherlockFragmentActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ////////////////////////////////////////////////////////////////////////////////////////
		init();
		
		Intent stacks = new Intent(this, StacksActivity.class);
		startActivity(stacks);
		// ////////////////////////////////////////////////////////////////////////////////////////
	}
	
	/**
	 * TODO: assess and remove
	 * Clears the DB, then adds some items
	 */
	private void init() {
		// delete all stacks
		Crud.removeAll(this);
		// add the root stack
		Crud.addDefaultStack(this);
		// add a few more stacks
		
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	// TODO: create a welcome actionbar
        getSupportMenuInflater().inflate(R.menu.menu_activity_stacks, menu);
        return true;
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
