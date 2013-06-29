package uk.co.ataulmunim.android.stacks.fragment;

import java.util.ArrayList;

import android.widget.*;
import uk.co.ataulmunim.android.stacks.Stack;
import uk.co.ataulmunim.android.stacks.activity.StacksActivity;
import uk.co.ataulmunim.android.stacks.adapter.StacksCursorAdapter;
import uk.co.ataulmunim.android.stacks.contentprovider.Stacks;
import android.app.Activity;
import android.app.ListFragment;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.LoaderManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

import com.nicedistractions.shortstacks.R;


public class StackViewFragment extends ListFragment
	implements LoaderManager.LoaderCallbacks<Cursor>, OnEditorActionListener, OnItemClickListener {
	
	public static final String TAG = "StackViewFragment";
	public static final String HEADER_TAG = "header";
    public static final String FOOTER_TAG= "footer";
	
	public static final String[] STACKS_PROJECTION = {
		Stacks._ID,	Stacks.SHORTCODE, Stacks.ACTION_ITEMS, Stacks.NOTES
	};

    private TextView shortCode;
    private TextView notes;
    private ImageView showMoreNotes;
    private ImageView showLessNotes;

	/**
	 * Determines whether or not to close the soft input keyboard when adding Stacks to the list.
	 * A value of TRUE will leave it open, but this can be set in shared preferences.
	 */
	private boolean quickAddMode;

	/**
	 * Determines whether or not to scroll to the end of the list after the
	 * data set has been updated. Initially false so it doesn't scroll when the
	 * activity is first opened, but true after that.
	 */
	private boolean scrollToEnd;

	private StacksCursorAdapter adapter;
	private int stackId = Stacks.ROOT_STACK_ID; // id of the current stack in the Stacks table

	// List of objects wanting notification when STACKS_LOADER loader updates
	private ArrayList<OnStackUpdateListener> stackUpdateListeners;

	// Indicates whether the Stack Info view (header) is expanded
	private boolean isNotesViewUnlimited;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stack_view, container, false);
    }


	/**
	 * Called after onCreateView(), after the parent activity is created
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// Inflate the stackInfoView and footer views
        LayoutInflater inflater = getActivity().getLayoutInflater();

        RelativeLayout stackInfoView = (RelativeLayout) inflater.inflate(R.layout.fragment_stack_view_header, null);
        stackInfoView.setTag(HEADER_TAG);

        findViewsInHeader(stackInfoView);
        updateHeaderView();

        EditText newStackInput = (EditText) inflater.inflate(R.layout.fragment_stack_view_footer, null);

        getListView().addHeaderView(stackInfoView, null, true);
        getListView().addFooterView(newStackInput, null, false);

        getListView().setSelector(R.drawable.list_selector); // TODO: we don't want header view to show selected


        stackId = ((StacksActivity) getActivity()).getStackId();
		
		stackUpdateListeners = new ArrayList<OnStackUpdateListener>();
		
		// Create an empty adapter we will use to display the loaded data.
		adapter = new StacksCursorAdapter(
					getActivity(),
					R.layout.list_item_stacks,
					null,
					new String[] {Stacks.SHORTCODE},
					new int[] { R.id.listitem_shortcode}
	    );
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
        getListView().setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        
        // Prepare the loader.  Either re-connect with an existing one, or start a new one.
        getActivity().getLoaderManager().initLoader(StacksActivity.STACKS_LOADER, null, this);

        quickAddMode = true;
        newStackInput.setOnEditorActionListener(this);
	}

    private void findViewsInHeader(View stackInfoView) {
        shortCode = (TextView) stackInfoView.findViewById(R.id.short_code);
        notes = (TextView) stackInfoView.findViewById(R.id.notes);
        showMoreNotes = (ImageView) stackInfoView.findViewById(R.id.show_more_indicator);
        showLessNotes = (ImageView) stackInfoView.findViewById(R.id.show_less_indicator);
    }




    private void updateHeaderView() {
        Stack stack = ((StacksActivity) getActivity()).getStack();
        String notesText = stack.getNotes();

        shortCode.setText(stack.getShortCode());

        if (notesText.length() <= 0) return;

        notes.setText(stack.getNotes());

        if (notes.getVisibility() != View.VISIBLE) notes.setVisibility(View.VISIBLE);
    }


    private void toggleStackInfoExpanded() {
        Log.i(TAG, "toggleStackInfoExpanded() called");
        Log.i(TAG, "Pre: isNotesViewUnlimited == " + isNotesViewUnlimited);
        int limitedLines = getResources().getInteger(R.integer.notes_line_height);

        if (!isNotesViewUnlimited) {
            notes.setMaxLines(Integer.MAX_VALUE);
            showMoreNotes.setVisibility(View.INVISIBLE);
            showLessNotes.setVisibility(View.VISIBLE);
            isNotesViewUnlimited = true;
        } else {
            notes.setMaxLines(limitedLines);
            showMoreNotes.setVisibility(View.VISIBLE);
            showLessNotes.setVisibility(View.INVISIBLE);
            isNotesViewUnlimited = false;
        }
        Log.i(TAG, "Post: isNotesViewUnlimited == " + isNotesViewUnlimited);
           /*
        LIMITED_LINES = 2;
        maxLines is either LIMITED_LINES or INFINITY

        IF no notes, or notes.lines <= LIMITED_LINES, THEN don't show any indicator
        IF notes.lines > LIMITED_LINES &&
            IF notes.maxLines == LIMITED_LINES, THEN display "show_more_indicator"
            IF notes.maxLines > LIMITED_LINES, THEN display "show_less_indicator"
         */
    }



    /**
	 * Opens the clicked Stack in a new StacksActivity, or toggles the expanded
	 * property of the Stack's header view.
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.d(TAG, "List item clicked, stackId: " + id);
		
		if (view.getTag().equals(HEADER_TAG)) {
            toggleStackInfoExpanded();
		} else {
			final Uri stack = ContentUris.withAppendedId(Stacks.CONTENT_URI, id);
	        final Intent viewStack = new Intent(Intent.ACTION_VIEW, stack);
	        startActivity(viewStack);
		}
	}


    /**
	 * Adds a stack as a child to the current stack.
	 */
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		final String name = v.getText().toString().trim();
		if (name.length() == 0) return true;
		
		Log.d(TAG, "Adding " + name);
        Stack.add(getActivity(), name, stackId);
        v.setText("");
		
		if (!quickAddMode) {
			Log.d(TAG, "quickAddMode false, hiding keyboard.");
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
		
		if (id == StacksActivity.STACKS_LOADER) {
			Log.d(TAG, "Loading stacks under stack " + stackId);
			
			final String where = Stacks.PARENT + "=" + stackId +
					" AND " + Stacks.DELETED + "<> 1" +
					" AND " + Stacks._ID + "<>" + Stacks.ROOT_STACK_ID;
			
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
		if (loader.getId() == StacksActivity.STACKS_LOADER) {
			Log.d(TAG, "Stacks loaded, swapping cursor, scrolling to end.");
			
			adapter.swapCursor(data);
			if (scrollToEnd) {
				getListView().smoothScrollToPosition(adapter.getCount());
			} else {
				scrollToEnd = true;
			}
		}
	}	
	
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (loader.getId() == StacksActivity.STACKS_LOADER) {
			Log.d(TAG, "Closing last Stacks cursor, so setting adapter cursor to null.");
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
