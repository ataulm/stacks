package uk.co.ataulmunim.android.stacks.activity;

import uk.co.ataulmunim.android.stacks.stack.Stack;
import uk.co.ataulmunim.android.stacks.contentprovider.Stacks;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.app.LoaderManager;
import android.content.Loader;
import com.commonsware.cwac.loaderex.SharedPreferencesLoader;
import com.nicedistractions.shortstacks.R;


public class WelcomeActivity extends Activity implements
		LoaderManager.LoaderCallbacks<SharedPreferences> {
	
	public static final String LOG_TAG = WelcomeActivity.class.getSimpleName();
	public static final String PREFS_ROOT_CREATED = "rootCreated";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Checks SharedPrefs to see if default stack created yet
		getLoaderManager().initLoader(0, null, this);
	}
	
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	// TODO: create a welcome actionbar
        getMenuInflater().inflate(R.menu.menu_activity_stacks, menu);
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
	 * SuppressLint("CommitPrefEdits") is because it's done in
	 * {@link SharedPreferencesLoader#persist(SharedPreferences.Editor)}
	 */
	@SuppressLint("CommitPrefEdits")
	@Override
	public void onLoadFinished(Loader<SharedPreferences> loader,
			SharedPreferences prefs) {
		boolean rootCreated = prefs.getBoolean(PREFS_ROOT_CREATED, false);
		if (!rootCreated) {
			// add the root stack
            Stack.createDefaultStack(getContentResolver());

            SharedPreferences.Editor editor = prefs.edit();
			editor.putBoolean(PREFS_ROOT_CREATED, true);
			SharedPreferencesLoader.persist(editor);
		}
		
		// TODO: get the default open stack from SharedPrefs
		// Open the default stack
        final Uri stack = ContentUris.withAppendedId(Stacks.CONTENT_URI,
        		Stacks.ROOT_STACK_ID);
        final Intent viewStack = new Intent(Intent.ACTION_VIEW, stack);
        startActivity(viewStack);
	}

	@Override
	public void onLoaderReset(Loader<SharedPreferences> prefs) {
		// unused
	}

}
