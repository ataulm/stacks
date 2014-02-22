package com.ataulm.stacks.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public abstract class StacksBaseAdapter<T> extends BaseAdapter {

    private View emptyView;
    private List<T> elements;

    protected StacksBaseAdapter() {
        this.elements = Collections.emptyList();
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    @Override
    public abstract View getView(int position, View view, ViewGroup parent);

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public T getItem(int position) {
        return elements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position + 1;
    }

    public void swapList(List<T> elements) {
        closeListIfNecessary();
        this.elements = elements;
        showEmptyViewIfNecessary();
        notifyDataSetChanged();
    }

    private void showEmptyViewIfNecessary() {
        if (emptyView == null) {
            return;
        }

        if (elements.size() > 0) {
            emptyView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    public void reset() {
        swapList(Collections.<T>emptyList());
    }

    private void closeListIfNecessary() {
        if (elements instanceof Closeable) {
            try {
                ((Closeable) elements).close();
            } catch (IOException ignored) {
                ignored.printStackTrace();
            }
        }
    }
}
