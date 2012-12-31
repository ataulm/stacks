package uk.co.ataulmunim.android.stacks.fragment;
import uk.co.ataulmunim.android.stacks.Crud;
import uk.co.ataulmunim.android.stacks.contentprovider.Stacks;

import com.nicedistractions.shortstacks.R;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;

import com.actionbarsherlock.app.SherlockListFragment;


public class StacksListFragment extends SherlockListFragment
	implements LoaderManager.LoaderCallbacks<Cursor>, OnEditorActionListener {
	
	public static final String LOG_TAG = "StacksListFragment";
	public static final int STACKS_LOADER = 0;
	public static final int DATES_LOADER = 1;
	public static final int PLANS_LOADER = 2;
	
	private SimpleCursorAdapter adapter;
	private int stackId; // id of the current stack in the Stacks table
		
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stack_view, container, false);
    }
	
	public static final String[] STACKS_PROJECTION = {
		Stacks.NAME,
		Stacks.ACTION_ITEMS
	};
	
	/**
	 * Called after onCreateView(), after the parent activity is created
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// Restore stuff if needed
		// if (savedInstanceState != null) editmode = savedInstanceState.getBoolean("editing");
		
		// Create an empty adapter we will use to display the loaded data.
		adapter = new SimpleCursorAdapter(
					getActivity(),
					R.layout.list_item_stacks,
					null,
					STACKS_PROJECTION,
					new int[] { R.id.listitem_name, R.id.listitem_actionable_items },
					0);

        setListAdapter(adapter);
        
        // Start out with a progress indicator.
        //setListShown(false);
        
        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        //getLoaderManager().initLoader(STACKS_LOADER, null, this);
        
        ((EditText) getView().findViewById(R.id.add_stack_field)).setOnEditorActionListener(this);
	}
	
	/**
	 * Allows user to quick-add items to the Stack by using the "Done" button on their keyboard.
	 * The keyboard will not close unless the "back" button or the "close-keyboard" button is
	 * pressed.
	 */
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		Log.d(LOG_TAG, "Done pressed.");
		// TODO: Insert a new Stack based on the trimmed input from the field and the current stackId
		v.setText("");
	    return true;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		final String select = Stacks.PARENT + "=" + stackId + " AND " + Stacks.DELETED + "= 0";
		
		CursorLoader cursor = new CursorLoader(getActivity(),
				Stacks.CONTENT_URI,
				STACKS_PROJECTION,
				select,
				null,
				Stacks.LOCAL_SORT);		
		
		return cursor;
	}
	
	

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {

	}
	

	
}
