package uk.co.ataulmunim.android.stacks.fragment;
import uk.co.ataulmunim.android.stacks.activity.StacksActivity;
import uk.co.ataulmunim.android.stacks.activity.StacksActivity.UserWarnedAboutBack;
import uk.co.ataulmunim.android.stacks.adapter.StacksCursorAdapter;
import uk.co.ataulmunim.android.stacks.contentprovider.Dates;
import uk.co.ataulmunim.android.stacks.contentprovider.Stacks;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.nicedistractions.shortstacks.R;


public class StacksEditFragment extends ListFragment
	implements LoaderManager.LoaderCallbacks<Cursor>, OnStackUpdateListener, TextWatcher {
	
	public static final String TAG = "StacksEditFragment";
	
	public static final String[] DATES_PROJECTION = {
		Dates._ID, Dates.STACK, Dates.DATE, Dates.ABOUT
	};
	
	private StacksActivity activity;
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
		
		activity = (StacksActivity) getActivity();
		  
		// Inflate the header and footer views
        LayoutInflater inflater = activity.getLayoutInflater();
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
         
        // TODO: add listeners to all input fields
        shortcodeInput = (EditText) getView().findViewById(R.id.input_shortcode);
        notesInput = (EditText) getView().findViewById(R.id.input_notes);
        
        shortcodeInput.addTextChangedListener(this);
        notesInput.addTextChangedListener(this);
        
        // FIXME: this destroys user progress on orientation change
        updateInputFields();

        stackId = activity.getStackId();
		
		// Create an empty adapter we will use to display the loaded data.
		adapter = new StacksCursorAdapter(
		            activity,
					R.layout.list_item_stacks,
					null,
					new String[] {Stacks.SHORTCODE, Stacks.ACTION_ITEMS},
					new int[] {
						R.id.listitem_name,
						R.id.listitem_actionable_items
					}
		);		
		
		setListAdapter(adapter);
		
        // Prepare the loader.  Either re-connect with an existing one, or start a new one.
        //getActivity().getSupportLoaderManager().initLoader(STACKS_LOADER, null, this);
	}
	
	private EditText shortcodeInput;
	private EditText notesInput;
	
	/**
	 * Input fields are updated to latest saved information.
	 * Called when the edit action is pressed, or onActivityCreated
	 */
	public void updateInputFields() {
	    String shortcode = activity.getShortCode();
        shortcodeInput.setText(shortcode);
        
        String notes = activity.getNotes();
        notesInput.setText(notes);
        
        // Reset this after updates to counteract TextWatcher
        activity.setUserWarnedAboutBack(UserWarnedAboutBack.UNSET);
	}
		
	// Loaders ////////////////////////////////////////////////////////////////

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader cursorLoader = null;
		
		if (id == StacksActivity.DATES_LOADER) {
			Log.d(TAG, "Loading dates for stack " + stackId);
			
			final String where = Dates.STACK + "=" + stackId;
			
			cursorLoader = new CursorLoader(activity,
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
		updateInputFields();
	}
	
	/* no op */
	@Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	/* no op */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        Log.d(TAG, "after text changed");
        activity.setUserWarnedAboutBack(UserWarnedAboutBack.NO);               
    }
}
