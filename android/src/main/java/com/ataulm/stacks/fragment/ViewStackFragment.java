package com.ataulm.stacks.fragment;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.*;
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
import com.ataulm.stacks.view.StackListItemView;
import com.novoda.notils.caster.Views;
import com.novoda.notils.cursor.SimpleCursorList;

import java.util.List;

public class ViewStackFragment extends StacksBaseFragment implements StackInputCallbacks,
        LoaderManager.LoaderCallbacks<Cursor>, StackListItemView.Callback {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_stack, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new StacksListAdapter(this);
        listView = Views.findById(view, R.id.listview_children);
        setupListViewSandwich();
        listView.setAdapter(adapter);

        startLoaders(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.view_stack, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                navigateTo().editStack(getStack(), R.id.req_code_edit_stack);
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

    private void setupListViewSandwich() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        StackListHeaderView headerView = new StackListHeaderView(getActivity());
        headerView.setId(R.id.header_view);
        listView.addHeaderView(headerView);

        KeepLikeInputView footerView = ((KeepLikeInputView) inflater.inflate(R.layout.view_stacks_footer, null));
        footerView.setCallbacks(this);
        listView.addFooterView(footerView);
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
        StackListHeaderView header = (StackListHeaderView) listView.findViewById(R.id.header_view);
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
    public void onStackClick(Stack stack) {
        navigateTo().stack(stack);
    }

    @Override
    public void onMoveClick(Stack stack) {
        navigateTo().pickNewParentForStack(getStack(), stack);
    }

    @Override
    public void onDeleteClick(Stack stack) {
        Stack deletedStack = Stack.Builder.from(stack).deleted(Time.now()).build();
        UpdateTask.newInstance(getActivity().getContentResolver(), deletedStack).execute();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}
