package uk.co.ataulmunim.android.stacks.activity;

import uk.co.ataulmunim.android.stacks.Crud;
import uk.co.ataulmunim.android.stacks.contentprovider.Stacks;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.commonsware.cwac.loaderex.acl.SharedPreferencesLoader;
import com.nicedistractions.shortstacks.R;

public class WelcomeActivity extends SherlockFragmentActivity implements
		LoaderManager.LoaderCallbacks<SharedPreferences> {
	
	public static final String LOG_TAG = "WelcomeActivity";
	public static final String PREFS_ROOT_CREATED = "rootCreated";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Checks SharedPrefs to see if default stack created yet
		getSupportLoaderManager().initLoader(0, null, this);
		
		Intent stacks = new Intent(this, StacksActivity.class);
		startActivity(stacks);
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

	@Override
	public Loader<SharedPreferences> onCreateLoader(int id, Bundle args) {
		return(new SharedPreferencesLoader(this));
	}

	/**
	 * Creates a default root stack if one hasn't been made.
	 * SuppressLint("CommitPrefEdits") is because it's done in {@link SharedPreferencesLoader#persist(SharedPreferences.Editor)}
	 */
	@SuppressLint("CommitPrefEdits")
	@Override
	public void onLoadFinished(Loader<SharedPreferences> loader, SharedPreferences prefs) {
		boolean rootCreated = prefs.getBoolean(PREFS_ROOT_CREATED, false);
		if (!rootCreated) {
			// add the root stack
			Crud.addDefaultStack(this);
			
			SharedPreferences.Editor editor = prefs.edit();
			editor.putBoolean(PREFS_ROOT_CREATED, true);
			SharedPreferencesLoader.persist(editor);
		}
	}

	@Override
	public void onLoaderReset(Loader<SharedPreferences> prefs) {
		// unused
	}

}
