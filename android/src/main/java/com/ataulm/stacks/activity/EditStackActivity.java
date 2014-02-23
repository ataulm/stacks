package com.ataulm.stacks.activity;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;

import com.ataulm.stacks.R;
import com.ataulm.stacks.base.StacksDoneDiscardActivity;
import com.ataulm.stacks.model.Stack;
import com.ataulm.stacks.persistence.StackCursorMarshaller;
import com.ataulm.stacks.persistence.StackLoader;
import com.novoda.notils.caster.Views;
import com.novoda.notils.cursor.SimpleCursorList;

import java.util.List;

public class EditStackActivity extends StacksDoneDiscardActivity implements StacksDoneDiscardActivity.DoneDiscardListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private String stackId;
    private EditText summary;
    private EditText description;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stack);
        summary = Views.findById(this, R.id.edittext_summary);
        description = Views.findById(this, R.id.edittext_description);

        startLoaders(savedInstanceState);
    }

    @Override
    public void onDoneClick() {
        toast(R.string.beep);
    }

    @Override
    public void onDiscardClick() {
        toast(R.string.boop);
    }

    private void startLoaders(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getLoaderManager().initLoader(R.id.loader_stack, null, this);
        } else {
            getLoaderManager().restartLoader(R.id.loader_stack, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == R.id.loader_stack) {
            return new StackLoader(this, getStackId());
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

    private String getStackId() {
        if (stackId == null) {
            stackId = hasData() ? getIntent().getData().getLastPathSegment() : Stack.ZERO.id;
        }
        return stackId;
    }

    private boolean hasData() {
        return getIntent().getData() != null;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}
