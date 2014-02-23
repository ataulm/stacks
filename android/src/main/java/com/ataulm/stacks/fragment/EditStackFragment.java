package com.ataulm.stacks.fragment;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ataulm.stacks.R;
import com.ataulm.stacks.base.StacksBaseFragment;
import com.ataulm.stacks.model.Stack;
import com.ataulm.stacks.persistence.StackCursorMarshaller;
import com.ataulm.stacks.persistence.StackLoader;
import com.novoda.notils.caster.Views;
import com.novoda.notils.cursor.SimpleCursorList;

import java.util.List;

public class EditStackFragment extends StacksBaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String ARGKEY_ID = "ID";

    private String stackId;
    private EditText summary;
    private EditText description;

    public static EditStackFragment newInstance(String id) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGKEY_ID, id);
        EditStackFragment fragment = new EditStackFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) {
            throw new IllegalStateException("EditStackFragment should only be started via Intent.ACTION_EDIT.");
        }
        stackId = getArguments().getString(ARGKEY_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_stack, container, false);
        summary = Views.findById(root, R.id.edittext_summary);
        description = Views.findById(root, R.id.edittext_description);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            getLoaderManager().initLoader(R.id.loader_stack, null, this);
        } else {
            getLoaderManager().restartLoader(R.id.loader_stack, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == R.id.loader_stack) {
            return new StackLoader(getActivity(), stackId);
        }
        throw new IllegalArgumentException("Unknown loader id: " + id);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == R.id.loader_stack) {
            List<Stack> stacks = new SimpleCursorList<Stack>(data, new StackCursorMarshaller());
            updateViews(stacks.get(0));
        }
    }

    private void updateViews(Stack stack) {
        summary.setText(stack.summary);
        description.setText(stack.description);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}
