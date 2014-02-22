package com.ataulm.stacks.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.ataulm.stacks.R;
import com.ataulm.stacks.activity.StacksActivity;
import com.ataulm.stacks.activity.StacksActivity.UserWarnedAboutBack;
import com.ataulm.stacks.model.Stack;
import com.ataulm.stacks.model.StackPersistor;

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
	    Stack stack = activity.getStack();
        stackNameInput.setText(stack.getStackName());
        notesInput.setText(stack.getNotes());
        
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

    public void commitChanges(Stack stack) {
        stack.setNotes(notesInput.getText().toString());
        stack.setStackName(stackNameInput.getText().toString());
        StackPersistor.persist(getActivity().getContentResolver(), stack);
    }
}
