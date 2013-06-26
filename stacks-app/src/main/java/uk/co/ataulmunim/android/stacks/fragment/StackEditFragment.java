package uk.co.ataulmunim.android.stacks.fragment;
import uk.co.ataulmunim.android.stacks.Stack;
import uk.co.ataulmunim.android.stacks.activity.StacksActivity;
import uk.co.ataulmunim.android.stacks.activity.StacksActivity.UserWarnedAboutBack;
import uk.co.ataulmunim.android.stacks.adapter.StacksCursorAdapter;
import uk.co.ataulmunim.android.stacks.contentprovider.Dates;
import uk.co.ataulmunim.android.stacks.contentprovider.Stacks;
import android.app.ListFragment;
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


public class StackEditFragment extends ListFragment
	implements OnStackUpdateListener, TextWatcher {
	
	public static final String TAG = "StackEditFragment";

	public static final String[] DATES_PROJECTION = {
		Dates._ID, Dates.STACK, Dates.DATE, Dates.ABOUT
	};

	private StacksActivity activity;
	private StacksCursorAdapter adapter;


    private EditText shortCodeInput;
    private EditText notesInput;

	/*
	public static StackEditFragment newInstance() {
		return new StackEditFragment();
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
        shortCodeInput = (EditText) getView().findViewById(R.id.input_shortcode);
        notesInput = (EditText) getView().findViewById(R.id.input_notes);
        
        shortCodeInput.addTextChangedListener(this);
        notesInput.addTextChangedListener(this);
        
        // FIXME: this destroys user progress on orientation change
        updateInputFields();

		// Create an empty adapter we will use to display the loaded data.
		adapter = new StacksCursorAdapter(
		            activity,
					R.layout.list_item_stacks,
					null,
					new String[] {Stacks.SHORTCODE},
					new int[] {
						R.id.listitem_name
                    }
		);		
		
		setListAdapter(adapter);
	}
	

	
	/**
	 * Input fields are updated to latest saved information.
	 * Called when the edit action is pressed, or onActivityCreated
	 */
	public void updateInputFields() {
	    Stack stack = activity.getStack();
        shortCodeInput.setText(stack.getShortCode());
        notesInput.setText(stack.getNotes());
        
        // Reset this after updates to counteract TextWatcher
        activity.setUserWarnedAboutBack(UserWarnedAboutBack.UNSET);
	}


	/**
	 * Called when the CursorLoader in StackViewFragment has been updated.
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
