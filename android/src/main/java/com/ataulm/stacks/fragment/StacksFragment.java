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
import com.ataulm.stacks.persistence.StackCursorMarshaller;
import com.ataulm.stacks.persistence.StackPersister;
import com.ataulm.stacks.persistence.StacksListAdapter;
import com.ataulm.stacks.persistence.StacksLoader;
import com.ataulm.stacks.view.KeepLikeInputView;
import com.ataulm.stacks.view.StackInputCallbacks;
import com.novoda.notils.caster.Views;
import com.novoda.notils.cursor.SimpleCursorList;

import java.util.List;

public class StacksFragment extends StacksBaseFragment implements StackInputCallbacks, LoaderManager.LoaderCallbacks<Cursor> {

    private String parentId;
    private ListView listView;
    private StacksListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: get parentId from extras
        parentId = "root";
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
            getLoaderManager().initLoader(0, null, this);
        } else {
            getLoaderManager().restartLoader(0, null, this);
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
    public void addStack(String summary) {
        StackPersister persister = new StackPersister(getActivity().getContentResolver());
        persister.persist(Stack.newInstance(parentId, summary, adapter.getCount()));
        toast(R.string.added_new_stack);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new StacksLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<Stack> stacks = new SimpleCursorList<Stack>(data, new StackCursorMarshaller());
        adapter.swapList(stacks);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}
