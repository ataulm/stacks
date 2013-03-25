package uk.co.ataulmunim.android.stacks.fragment;
import uk.co.ataulmunim.android.stacks.Crud;
import uk.co.ataulmunim.android.stacks.adapter.StacksCursorAdapter;
import uk.co.ataulmunim.android.stacks.contentprovider.Plans;
import uk.co.ataulmunim.android.stacks.contentprovider.Stacks;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;
import com.nicedistractions.shortstacks.R;


public class StacksEditFragment extends SherlockFragment
	implements LoaderManager.LoaderCallbacks<Cursor> {
	
	public static final String TAG = "StacksListFragment";
	
	public static final String[] STACKS_PROJECTION = {
		Stacks._ID,	Stacks.NAME, Stacks.ACTION_ITEMS
	};
	
	public static final int STACKS_LOADER = 0;
	public static final int DATES_LOADER = 1;
	
	/**
	 * Determines whether or not to close the soft input keyboard when adding Stacks to the list.
	 * A value of TRUE will leave it open, but this can be set in shared preferences.
	 * TODO: set in shared prefs
	 */
	private boolean quickAddMode;
	
	private StacksCursorAdapter adapter;
	private int stackId = Stacks.ROOT_STACK_ID; // id of the current stack in the Stacks table
	
	public static StacksEditFragment newInstance() {
		return new StacksEditFragment();
    }
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stack_edit, container, false);
    }
	
	
	
	/**
	 * Called after onCreateView(), after the parent activity is created
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.i(TAG, "onActivityCreated()");
		
		final Intent intent = getActivity().getIntent();
		final Uri stackUri = intent.getData();
		
		// TODO: differentiate between INTENTs by performing different actions
		final String action = intent.getAction();
		
		if (stackUri != null) {
			try {
				stackId = Integer.parseInt(stackUri.getLastPathSegment());	
			} catch (NumberFormatException e) {
				Log.w(TAG, "stackUri.getLastPathSegment() was not cool. (" +
						stackUri.getLastPathSegment() + ")." +
						"Stays unchanged as Stacks.ROOT_STACK_ID.");
			}	
		}
        
        // Prepare the loader.  Either re-connect with an existing one, or start a new one.
        //getActivity().getSupportLoaderManager().initLoader(STACKS_LOADER, null, this);
	}
	
	
	// Loaders ////////////////////////////////////////////////////////////////////////////////////

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader cursorLoader = null;
		
		if (id == STACKS_LOADER) {
			Log.d(TAG, "Loading stacks under stack " + stackId);
			final String where = Stacks.PARENT + "=" + stackId +
					" AND " + Stacks.DELETED + "<> 1" + " AND " + 
					Stacks._ID + "<>" + Stacks.ROOT_STACK_ID; // Don't show default stack as child
			
			cursorLoader = new CursorLoader(getActivity(),
					Stacks.CONTENT_URI,
					STACKS_PROJECTION,
					where,
					null,
					Stacks.LOCAL_SORT);
		}
		
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (loader.getId() == STACKS_LOADER) {
			Log.d(TAG, "Stacks loaded, swapping cursor, scrolling to end.");
			
			adapter.swapCursor(data);
		}
	}	
	
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (loader.getId() == STACKS_LOADER) {
			Log.d(TAG, "Closing last Stacks cursor, so setting adapter cursor to null.");
			adapter.swapCursor(null);
		}
	}
	
	// Loaders end ////////////////////////////////////////////////////////////////////////////////
}
