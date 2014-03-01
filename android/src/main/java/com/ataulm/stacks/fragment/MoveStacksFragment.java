package com.ataulm.stacks.fragment;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ataulm.stacks.R;
import com.ataulm.stacks.activity.MoveStacksActivity;
import com.ataulm.stacks.base.StacksBaseFragment;
import com.ataulm.stacks.marshallers.StackFromCursorMarshaller;
import com.ataulm.stacks.model.Stack;
import com.ataulm.stacks.persistence.StacksListAdapter;
import com.ataulm.stacks.persistence.SubStacksLoader;
import com.ataulm.stacks.view.StackListHeaderView;
import com.novoda.notils.caster.Classes;
import com.novoda.notils.caster.Views;
import com.novoda.notils.cursor.SimpleCursorList;

import java.util.List;

public class MoveStacksFragment extends StacksBaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private ListView listView;
    private StacksListAdapter adapter;
    private Callback callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.callback = Classes.from(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_stack, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new StacksListAdapter();
        listView = Views.findById(view, R.id.listview_children);
        View headerView = createHeaderView(listView);
        listView.addHeaderView(headerView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (itemIsListViewHeader(position)) {
                    return;
                }

                Stack stack = adapter.getItem(position - listView.getHeaderViewsCount());
                callback.onStackClicked(stack);
            }

            private boolean itemIsListViewHeader(int position) {
                return position < listView.getHeaderViewsCount();
            }

        });

        if (savedInstanceState == null) {
            getLoaderManager().initLoader(R.id.loader_sub_stacks, null, this);
        } else {
            getLoaderManager().restartLoader(R.id.loader_sub_stacks, null, this);
        }
    }

    private View createHeaderView(ListView parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_stacks_header, null);
        ((StackListHeaderView) view).updateWith(getParentStack());
        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == R.id.loader_sub_stacks) {
            // TODO: this loader should start NOT load the stacks are that being moved
            return new SubStacksLoader(getActivity(), getParentStack().id);
        }
        throw new IllegalArgumentException("Unknown loader id: " + id);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == R.id.loader_sub_stacks) {
            List<Stack> stacks = new SimpleCursorList<Stack>(data, new StackFromCursorMarshaller());
            adapter.swapList(stacks);
        }
    }

    private Stack getParentStack() {
        if (getActivity().getIntent().hasExtra(MoveStacksActivity.EXTRA_PARENT)) {
            return getActivity().getIntent().getParcelableExtra(MoveStacksActivity.EXTRA_PARENT);
        }
        throw new MoveStacksActivity.MoveStackException(MoveStacksActivity.EXTRA_PARENT);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    public interface Callback {

        void onStackClicked(Stack stack);

    }

}
