package uk.co.ataulmunim.android.stacks.fragment;
import uk.co.ataulmunim.android.stacks.StacksCursorAdapter;
import uk.co.ataulmunim.android.stacks.contentprovider.Stacks;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.actionbarsherlock.app.SherlockListFragment;
import com.nicedistractions.shortstacks.R;


public class StacksListFragment extends SherlockListFragment
	implements LoaderManager.LoaderCallbacks<Cursor>, OnEditorActionListener {
	
	public static final String LOG_TAG = "StacksListFragment";
	
	public static final String[] STACKS_PROJECTION = {
		Stacks.NAME,
		Stacks.ACTION_ITEMS,
		Stacks._ID
	};
	
	public static final int STACKS_LOADER = 0;
	public static final int DATES_LOADER = 1;
	public static final int PLANS_LOADER = 2;
	
	/**
	 * Determines whether or not to close the soft input keyboard when adding Stacks to the list.
	 * A value of TRUE will leave it open, but this can be set in shared preferences.
	 */
	private boolean quickAddMode;
	
	private SimpleCursorAdapter adapter;
	private int stackId; // id of the current stack in the Stacks table
		
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stack_view, container, false);
    }
	
	
	
	/**
	 * Called after onCreateView(), after the parent activity is created
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// Restore stuff if needed
		// if (savedInstanceState != null) editmode = savedInstanceState.getBoolean("editing");
		
		// TODO: get id from intent
		stackId = Stacks.ROOT_STACK_ID;
		
		// Create an empty adapter we will use to display the loaded data.
		adapter = new StacksCursorAdapter(
					getActivity(),
					R.layout.list_item_stacks,
					null,
					STACKS_PROJECTION,
					new int[] { R.id.listitem_name, R.id.listitem_actionable_items }
					);

        setListAdapter(adapter);
        
        // Start out with a progress indicator.
        //setListShown(false);
        
        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getActivity().getSupportLoaderManager().initLoader(STACKS_LOADER, null, this);
        
        // TODO: Get/set quickAddMode via SharedPreferences
        quickAddMode = true;
        ((EditText) getView().findViewById(R.id.add_stack_field)).setOnEditorActionListener(this);
	}
	
	/**
	 * Adds a stack as a child to the current stack.
	 */
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		Log.d(LOG_TAG, "Done pressed.");
		// TODO: Insert a new Stack based on the trimmed input from the field and the current stackId
		v.setText("");
		
		if (!quickAddMode) {
			Log.d(LOG_TAG, "quickAddMode false, hiding keyboard.");
			InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
					Activity.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	    return true;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		if (id == STACKS_LOADER) {
			Log.d(LOG_TAG, "Loading Stacks.");
			final String select = Stacks.PARENT + "=" + stackId + " AND " + Stacks.DELETED + "<> 1";
			
			CursorLoader cursorLoader = new CursorLoader(getActivity(),
					Stacks.CONTENT_URI,
					STACKS_PROJECTION,
					select,
					null,
					Stacks.LOCAL_SORT);		
			
			return cursorLoader;	
		}
		
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (loader.getId() == STACKS_LOADER) {
			Log.d(LOG_TAG, "Stacks loaded, swapping cursor.");
			adapter.swapCursor(data);	
		}		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (loader.getId() == STACKS_LOADER) {
			Log.d(LOG_TAG, "Closing last Stacks cursor, so setting adapter cursor to null.");
			adapter.swapCursor(null);
		}
	}
	

	
}
