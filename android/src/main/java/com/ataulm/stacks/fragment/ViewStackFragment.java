package com.ataulm.stacks.fragment;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ataulm.stacks.R;
import com.ataulm.stacks.activity.ViewStackActivity;
import com.ataulm.stacks.base.StacksBaseFragment;
import com.ataulm.stacks.marshallers.StackFromCursorMarshaller;
import com.ataulm.stacks.model.Stack;
import com.ataulm.stacks.model.Time;
import com.ataulm.stacks.persistence.StackLoader;
import com.ataulm.stacks.persistence.StacksListAdapter;
import com.ataulm.stacks.persistence.SubStacksLoader;
import com.ataulm.stacks.persistence.task.InsertTask;
import com.ataulm.stacks.persistence.task.UpdateTask;
import com.ataulm.stacks.view.KeepLikeInputView;
import com.ataulm.stacks.view.StackInputCallbacks;
import com.ataulm.stacks.view.StackListHeaderView;
import com.novoda.notils.caster.Views;
import com.novoda.notils.cursor.SimpleCursorList;

import java.util.List;

public class ViewStackFragment extends StacksBaseFragment implements StackInputCallbacks, LoaderManager.LoaderCallbacks<Cursor> {

    private ListView listView;
    private StacksListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Stack.ZERO.equals(getStack())) {
            setHasOptionsMenu(true);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.view_stack, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                navigateTo().editStack(getStack());
                return true;

            case R.id.delete:
                Stack deletedStack = Stack.Builder.from(getStack()).deleted(Time.now()).build();
                UpdateTask.newInstance(getActivity().getContentResolver(), deletedStack).execute();
                getActivity().finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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
        setupListViewSandwich();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (itemIsListViewHeader(position)) {
                    return;
                }

                Stack stack = adapter.getItem(position - listView.getHeaderViewsCount());
                navigateTo().stack(stack);
            }

            private boolean itemIsListViewHeader(int position) {
                return position < listView.getHeaderViewsCount();
            }

        });

        // TODO: this should be in a per item overflow, not long click derp face
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (itemIsListViewHeader(position)) {
                    return true;
                }

                Stack stack = adapter.getItem(position - listView.getHeaderViewsCount());
                navigateTo().pickNewParentForStack(getStack(), stack);
                return true;
            }

            private boolean itemIsListViewHeader(int position) {
                return position < listView.getHeaderViewsCount();
            }
        });

        startLoaders(savedInstanceState);
    }

    private void startLoaders(Bundle savedInstanceState) {
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
            return new StackLoader(getActivity(), getStack().id);
        } else if (id == R.id.loader_sub_stacks) {
            return new SubStacksLoader(getActivity(), getStack().id);
        }
        throw new IllegalArgumentException("Unknown loader id: " + id);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == R.id.loader_stack) {
            List<Stack> stacks = new SimpleCursorList<Stack>(data, new StackFromCursorMarshaller());
            if (stacks.size() > 0) {
                updateHeader(stacks.get(0));
            }
        } else if (loader.getId() == R.id.loader_sub_stacks) {
            List<Stack> stacks = new SimpleCursorList<Stack>(data, new StackFromCursorMarshaller());
            adapter.swapList(stacks);
        }
    }

    private void updateHeader(Stack stack) {
        StackListHeaderView header = (StackListHeaderView) listView.findViewById(R.id.header);
        header.updateWith(stack);
    }

    private Stack getStack() {
        if (getActivity().getIntent().hasExtra(ViewStackActivity.EXTRA_STACK)) {
            return getActivity().getIntent().getParcelableExtra(ViewStackActivity.EXTRA_STACK);
        }
        return Stack.ZERO;
    }

    @Override
    public void addStack(String summary) {
        Stack stack = Stack.newInstance(getStack().id, summary, adapter.getCount());
        InsertTask.newInstance(getActivity().getContentResolver(), stack).execute();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}
