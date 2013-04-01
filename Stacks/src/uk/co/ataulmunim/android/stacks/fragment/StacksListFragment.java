package uk.co.ataulmunim.android.stacks.fragment;

import java.util.ArrayList;

import uk.co.ataulmunim.android.stacks.Crud;
import uk.co.ataulmunim.android.stacks.activity.StacksActivity;
import uk.co.ataulmunim.android.stacks.adapter.StacksCursorAdapter;
import uk.co.ataulmunim.android.stacks.adapter.StacksPagerAdapter;
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

import com.actionbarsherlock.app.SherlockListFragment;
import com.nicedistractions.shortstacks.R;


public class StacksListFragment extends SherlockListFragment
	implements LoaderManager.LoaderCallbacks<Cursor>, OnEditorActionListener, OnItemClickListener {
	
	public static final String LOG_TAG = "StacksListFragment";
	
	public static final String[] STACKS_PROJECTION = {
		Stacks._ID,	Stacks.SHORTCODE, Stacks.ACTION_ITEMS, Stacks.NOTES
	};
	
	public static final int STACKS_LOADER = 0;
	public static final int DATES_LOADER = 1;
	
	/**
	 * Determines whether or not to close the soft input keyboard when adding Stacks to the list.
	 * A value of TRUE will leave it open, but this can be set in shared preferences.
	 */
	private boolean quickAddMode;
	
	private StacksCursorAdapter adapter;
	private int stackId = Stacks.ROOT_STACK_ID; // id of the current stack in the Stacks table

	// List of objects wanting notification when STACKS_LOADER loader updates
	private ArrayList<OnStackUpdateListener> stackUpdateListeners;
	
	public static StacksListFragment newInstance() {
		return new StacksListFragment();
    }
		
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
		Log.i(LOG_TAG, "onActivityCreated()");
		
		final Intent intent = getActivity().getIntent();
		final Uri stackUri = intent.getData();
		
		// TODO: differentiate between INTENTs by performing different actions
		final String action = intent.getAction();
		
		if (stackUri != null) {
			try {
				stackId = Integer.parseInt(stackUri.getLastPathSegment());	
			} catch (NumberFormatException e) {
				Log.w(LOG_TAG, "stackUri.getLastPathSegment() was not cool. (" +
						stackUri.getLastPathSegment() + ")." +
						"Stays unchanged as Stacks.ROOT_STACK_ID.");
			}	
		}
		
		stackUpdateListeners = new ArrayList<OnStackUpdateListener>();
		
		// Create an empty adapter we will use to display the loaded data.
		adapter = new StacksCursorAdapter(
					getActivity(),
					R.layout.list_item_stacks,
					null,
					new String[] {Stacks.SHORTCODE, Stacks.ACTION_ITEMS},
					new int[] { R.id.listitem_name, R.id.listitem_actionable_items }
					);		
        setListAdapter(adapter);        
        getListView().setOnItemClickListener(this);
        
        // Prepare the loader.  Either re-connect with an existing one, or start a new one.
        getActivity().getSupportLoaderManager().initLoader(STACKS_LOADER, null, this);
        
        // TODO: Get/set quickAddMode via SharedPreferences
        quickAddMode = true;
        ((EditText) getView().findViewById(R.id.add_stack_field)).setOnEditorActionListener(this);
	}
	
	/**
	 * Opens the clicked Stack in a new StacksActivity.
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.d(LOG_TAG, "List item clicked, stackId: " + id);
		final Uri stack = ContentUris.withAppendedId(Stacks.CONTENT_URI, id);
        final Intent viewStack = new Intent(Intent.ACTION_VIEW, stack);
        startActivity(viewStack);
	}
	
	/**
	 * Adds a stack as a child to the current stack.
	 */
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		final String name = v.getText().toString().trim();
		if (name.length() == 0) return true;
		
		Log.d(LOG_TAG, "Adding " + name);
		Crud.addStack(getActivity(), name, null, stackId);
		v.setText("");
		
		if (!quickAddMode) {
			Log.d(LOG_TAG, "quickAddMode false, hiding keyboard.");
			InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
					Activity.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	    return true;
	}
	
	// Loaders ////////////////////////////////////////////////////////////////////////////////////

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader cursorLoader = null;
		
		if (id == STACKS_LOADER) {
			Log.d(LOG_TAG, "Loading stacks under stack " + stackId);
			final String where = Stacks.PARENT + "=" + stackId +
					" AND " + Stacks.DELETED + "<> 1" + " AND " + 
					Stacks._ID + "<>" + Stacks.ROOT_STACK_ID; // Never show default stack as child
			
			cursorLoader = new CursorLoader(
					getActivity(),
					Stacks.CONTENT_URI,
					STACKS_PROJECTION,
					where,
					null,
					Stacks.LOCAL_SORT
			);
		}
		
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (loader.getId() == STACKS_LOADER) {
			Log.d(LOG_TAG, "Stacks loaded, swapping cursor, scrolling to end.");
			
			adapter.swapCursor(data);
			getListView().smoothScrollToPosition(adapter.getCount());
			
			final String shortcode = data.getString(data.getColumnIndex(
					Stacks.SHORTCODE));
			final String notes = data.getString(data.getColumnIndex(
					Stacks.NOTES));
			
			((StacksActivity) getActivity()).setShortcode(shortcode);
			((StacksActivity) getActivity()).setNotes(notes);
			
			for (OnStackUpdateListener lis : stackUpdateListeners) {
				lis.onStackUpdated();
			}
		}
	}	
	
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (loader.getId() == STACKS_LOADER) {
			Log.d(LOG_TAG, "Closing last Stacks cursor, so setting adapter cursor to null.");
			adapter.swapCursor(null);
		}
	}
	
	// Loaders end ////////////////////////////////////////////////////////////////////////////////
	
	public void addOnStackUpdateListener(OnStackUpdateListener listener) {
		if (!stackUpdateListeners.contains(listener)) {
			stackUpdateListeners.add(listener);
		}
	}
	public void removeOnStackUpdateListener(OnStackUpdateListener listener) {
		if (stackUpdateListeners.contains(listener)) {
			stackUpdateListeners.remove(listener);
		}
	}
}
