package com.ataulm.stacks.fragment;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ataulm.stacks.R;
import com.ataulm.stacks.base.StacksBaseFragment;
import com.ataulm.stacks.model.Stack;
import com.ataulm.stacks.persistence.*;
import com.ataulm.stacks.view.KeepLikeInputView;
import com.ataulm.stacks.view.StackInputCallbacks;
import com.ataulm.stacks.view.StackListHeaderView;
import com.novoda.notils.caster.Views;
import com.novoda.notils.cursor.SimpleCursorList;

import java.util.List;

public class StacksFragment extends StacksBaseFragment implements StackInputCallbacks, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String ARGKEY_ID = "ID";

    private String stackId;
    private ListView listView;
    private StacksListAdapter adapter;

    public static StacksFragment newInstance(String id) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGKEY_ID, id);
        StacksFragment fragment = new StacksFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            stackId = getArguments().getString(ARGKEY_ID);
        } else {
            stackId = Stack.ZERO.id;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stacks, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new StacksListAdapter();
        listView = Views.findById(view, R.id.listview_children);
        setupListViewSandwich();
        listView.setAdapter(adapter);

        if (savedInstanceState == null) {
            getLoaderManager().initLoader(R.id.loader_stack, null, this);
            getLoaderManager().initLoader(R.id.loader_sub_stacks, null, this);
        } else {
            getLoaderManager().restartLoader(R.id.loader_stack, null, this);
            getLoaderManager().restartLoader(R.id.loader_sub_stacks, null, this);
        }
    }

    private void setupListViewSandwich() {
        View headerView = createHeaderView(listView);
        listView.addHeaderView(headerView);

        View footerView = createFooterView(listView);
        listView.addFooterView(footerView);
    }

    private View createHeaderView(ListView parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.view_stacks_header, null);
    }

    private View createFooterView(ListView parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        KeepLikeInputView footer = ((KeepLikeInputView) inflater.inflate(R.layout.view_stacks_footer, null));
        footer.setCallbacks(this);

        return footer;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == R.id.loader_stack) {
            return new StackLoader(getActivity(), stackId);
        } else if (id == R.id.loader_sub_stacks) {
            return new SubStacksLoader(getActivity(), stackId);
        }
        throw new IllegalArgumentException("Unknown loader id: " + id);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == R.id.loader_stack) {
            List<Stack> stacks = new SimpleCursorList<Stack>(data, new StackCursorMarshaller());
            updateHeader(stacks.get(0));
        } else if (loader.getId() == R.id.loader_sub_stacks) {
            List<Stack> stacks = new SimpleCursorList<Stack>(data, new StackCursorMarshaller());
            adapter.swapList(stacks);
        }
    }

    private void updateHeader(Stack stack) {
        StackListHeaderView header = (StackListHeaderView) listView.findViewById(R.id.header);
        header.updateWith(stack);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void addStack(String summary) {
        StackPersister persister = new StackPersister(getActivity().getContentResolver());
        persister.persist(Stack.newInstance(stackId, summary, adapter.getCount()));
        toast(R.string.added_new_stack);
    }

}
