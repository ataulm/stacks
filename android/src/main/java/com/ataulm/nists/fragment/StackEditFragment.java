package com.ataulm.nists.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.ataulm.nists.R;
import com.ataulm.nists.activity.StacksActivity;
import com.ataulm.nists.activity.StacksActivity.UserWarnedAboutBack;
import com.ataulm.nists.nist.Nist;
import com.ataulm.nists.nist.NistPersistor;

public class StackEditFragment extends BaseFragment implements TextWatcher {
	private StacksActivity activity;

    private EditText stackNameInput;
    private EditText notesInput;


	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stack_edit, container, false);
    }

	/**
	 * Called after onCreateView(), after the parent activity is created
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		activity = (StacksActivity) getActivity();
		  
	    stackNameInput = (EditText) getView().findViewById(R.id.input_stackname);
        notesInput = (EditText) getView().findViewById(R.id.input_notes);
        
        stackNameInput.addTextChangedListener(this);
        notesInput.addTextChangedListener(this);

        updateInputFields();
	}

	/**
	 * Input fields are updated to latest saved information.
	 * Called when the edit action is pressed, or onActivityCreated
	 */
	public void updateInputFields() {
	    Nist nist = activity.getNist();
        stackNameInput.setText(nist.getStackName());
        notesInput.setText(nist.getNotes());
        
        // Reset this after updates to counteract TextWatcher
        activity.setUserWarnedAboutBack(UserWarnedAboutBack.UNSET);
	}

    /* no op */
	@Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	/* no op */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        log("after text changed");
        activity.setUserWarnedAboutBack(UserWarnedAboutBack.NO);               
    }

    public void commitChanges(Nist nist) {
        nist.setNotes(notesInput.getText().toString());
        nist.setStackName(stackNameInput.getText().toString());
        NistPersistor.persist(getActivity().getContentResolver(), nist);
    }
}
