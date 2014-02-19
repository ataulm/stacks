package com.ataulm.nists.activity;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import com.commonsware.cwac.loaderex.SharedPreferencesLoader;
import com.ataulm.nists.contentprovider.Stacks;
import com.ataulm.nists.nist.Nist;


public class WelcomeActivity extends BaseActivity implements
		LoaderManager.LoaderCallbacks<SharedPreferences> {

	public static final String PREFS_ROOT_CREATED = "rootCreated";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Checks SharedPrefs to see if default stack created yet
		getLoaderManager().initLoader(0, null, this);
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
	public void onLoadFinished(Loader<SharedPreferences> loader, SharedPreferences prefs) {
		boolean rootCreated = prefs.getBoolean(PREFS_ROOT_CREATED, false);
		if (!rootCreated) {
            boolean created = Nist.createDefaultStack(getContentResolver());

            SharedPreferences.Editor editor = prefs.edit();
			editor.putBoolean(PREFS_ROOT_CREATED, created);
			SharedPreferencesLoader.persist(editor);
		}

		// Open the default stack
        final Uri stack = ContentUris.withAppendedId(Stacks.CONTENT_URI,
        		Stacks.ROOT_STACK_ID);
        final Intent viewStack = new Intent(Intent.ACTION_VIEW, stack);
        startActivity(viewStack);
	}

	@Override
	public void onLoaderReset(Loader<SharedPreferences> prefs) {} // unused
}
