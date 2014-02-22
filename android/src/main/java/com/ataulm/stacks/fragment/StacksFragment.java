package com.ataulm.stacks.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.ataulm.stacks.R;
import com.ataulm.stacks.base.BaseFragment;
import com.novoda.notils.caster.Views;

import java.util.ArrayList;
import java.util.List;

public class StacksFragment extends BaseFragment {

    private ListView listView;
    private ListAdapter adapter;

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
        return inflater.inflate(R.layout.view_stacks_footer, null);
    }

    public ListAdapter getAdapter() {
        if (adapter == null) {
            List<String> temp = new ArrayList<String>();
            temp.add("one");
            temp.add("two");
            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, temp);
        }
        return adapter;
    }
}
