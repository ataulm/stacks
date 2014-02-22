package com.ataulm.stacks.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.ataulm.stacks.R;
import com.ataulm.stacks.base.StacksBaseFragment;
import com.ataulm.stacks.model.Stack;
import com.ataulm.stacks.persistence.StacksListAdapter;
import com.ataulm.stacks.view.KeepLikeInputView;
import com.ataulm.stacks.view.StacksInputCallbacks;
import com.novoda.notils.caster.Views;

import java.util.ArrayList;
import java.util.List;

public class StacksFragment extends StacksBaseFragment implements StacksInputCallbacks {

    private ListView listView;
    private StacksListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stacks, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = Views.findById(view, R.id.listview_children);

        setupListViewSandwich();

        listView.setAdapter(getAdapter());
    }

    private void setupListViewSandwich() {
        View headerView = createHeaderView(listView);
        listView.addHeaderView(headerView);

        View footerView = createFooterView(listView);
        listView.addFooterView(footerView);
    }

    private View createHeaderView(ListView parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return inflater.inflate(R.layout.view_stacks_header, null);
    }

    private View createFooterView(ListView parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        KeepLikeInputView footer = ((KeepLikeInputView) inflater.inflate(R.layout.view_stacks_footer, null));
        footer.setCallbacks(this);

        return footer;
    }

    public ListAdapter getAdapter() {
        if (adapter == null) {
            adapter = new StacksListAdapter();

            List<Stack> temp = new ArrayList<Stack>();
            temp.add(Stack.newInstance("root", "one"));
            temp.add(Stack.newInstance("root", "two"));
            adapter.swapList(temp);
        }
        return adapter;
    }

    @Override
    public void addStack(String summary) {
        List<Stack> temp = new ArrayList<Stack>(adapter.getCount());
        for (int i = 0; i < adapter.getCount(); i++) {
            temp.add(adapter.getItem(i));
        }
        temp.add(Stack.newInstance("root", summary));
        adapter.swapList(temp);
    }
}
