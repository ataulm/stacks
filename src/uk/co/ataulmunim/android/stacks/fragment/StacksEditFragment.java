package uk.co.ataulmunim.android.stacks.fragment;
import uk.co.ataulmunim.android.stacks.activity.StacksActivity;
import uk.co.ataulmunim.android.stacks.adapter.StacksCursorAdapter;
import uk.co.ataulmunim.android.stacks.contentprovider.Dates;
import uk.co.ataulmunim.android.stacks.contentprovider.Stacks;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockListFragment;
import com.nicedistractions.shortstacks.R;


public class StacksEditFragment extends SherlockListFragment
	implements LoaderManager.LoaderCallbacks<Cursor>, OnStackUpdateListener {
	
	public static final String TAG = "StacksEditFragment";
	
	public static final String[] DATES_PROJECTION = {
		Dates._ID, Dates.STACK, Dates.DATE, Dates.ABOUT
	};
	
	
	private StacksCursorAdapter adapter;
	private int stackId = Stacks.ROOT_STACK_ID; // id of the current stack in the Stacks table
	
	/*
	public static StacksEditFragment newInstance() {
		return new StacksEditFragment();
    }
	*/
	
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
		  
		// Inflate the header and footer views
        LayoutInflater inflater = getActivity().getLayoutInflater();
        LinearLayout header = (LinearLayout) inflater.inflate(
        		R.layout.fragment_stack_edit_header,
        		null
        );
        Button footer = (Button) inflater.inflate(
        		R.layout.fragment_stack_edit_footer,
        		null
        );
        
        // Add them to the ListView - *here* these show even if list is empty
        getListView().addHeaderView(header, null, false);
        getListView().addFooterView(footer, null, false);
        
        
        
        
        
				
		final TextView shortcodeTextView = (TextView) getView().findViewById(
				R.id.input_shortcode);
		final TextView notesTextView = (TextView) getView().findViewById(
				R.id.input_notes);
		
		// TODO: It'll only be in a semi-modified state if the user rotates
		
		// Pre-fill the inputs with the current values, unless user input
		if (shortcodeTextView.getText().length() != 0) {
			String shortcode = ((StacksActivity) getActivity()).getShortcode();
			shortcodeTextView.setText(shortcode);
		}
		
		if (notesTextView.getText().length() != 0) {
			String notes = ((StacksActivity) getActivity()).getNotes();
			notesTextView.setText(notes);
		}
		
		
		//shortcode.setText(text);
		
		stackId = ((StacksActivity) getActivity()).getStackId();
		
		// Create an empty adapter we will use to display the loaded data.
		adapter = new StacksCursorAdapter(
					getActivity(),
					R.layout.list_item_stacks,
					null,
					new String[] {Stacks.SHORTCODE, Stacks.ACTION_ITEMS},
					new int[] {
						R.id.listitem_name,
						R.id.listitem_actionable_items
					}
		);		
		
		// TODO: add header view (label, short code, label, notes)
		// TODO: add footer view (add date button)
		// getListView().addFooterView(ADD DATE BUTTON);
		setListAdapter(adapter);
		
        // Prepare the loader.  Either re-connect with an existing one, or start a new one.
        //getActivity().getSupportLoaderManager().initLoader(STACKS_LOADER, null, this);
	}
		
	// Loaders ////////////////////////////////////////////////////////////////

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader cursorLoader = null;
		
		if (id == StacksActivity.DATES_LOADER) {
			Log.d(TAG, "Loading dates for stack " + stackId);
			
			final String where = Dates.STACK + "=" + stackId;
			
			cursorLoader = new CursorLoader(getActivity(),
					Dates.CONTENT_URI,
					DATES_PROJECTION,
					where,
					null,
					Dates.DATE
			);
		}
		
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (loader.getId() == StacksActivity.STACKS_LOADER) {
			Log.d(TAG, "Stacks loaded, swapping cursor, scrolling to end.");
			
			adapter.swapCursor(data);
		}
	}	
	
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (loader.getId() == StacksActivity.STACKS_LOADER) {
			Log.d(TAG, "Closing last Stacks cursor, so setting adapter cursor to null.");
			adapter.swapCursor(null);
		}
	}

	// Loaders end ////////////////////////////////////////////////////////////

	/**
	 * Called when the CursorLoader in StacksListFragment has been updated.
	 */
	@Override
	public void onStackUpdated() {
		String shortcode = ((StacksActivity) getActivity()).getShortcode();
		String notes = ((StacksActivity) getActivity()).getNotes();
		
		// TODO: update the EditTexts iff they're mid-completion		
	}
}
