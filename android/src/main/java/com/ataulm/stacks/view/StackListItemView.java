package com.ataulm.stacks.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.ataulm.stacks.R;
import com.ataulm.stacks.model.Stack;
import com.novoda.notils.caster.Views;

public class StackListItemView extends LinearLayout implements PopupMenu.OnMenuItemClickListener {

    private TextView textViewSummary;
    private ImageView overflow;
    private Callback callback;
    private Stack stack;

    public StackListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        textViewSummary = Views.findById(this, R.id.textview_summary);
        overflow = Views.findById(this, R.id.overflow);
        overflow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }

        });

        setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                callback.onStackClick(stack);
            }

        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(getContext(), view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.list_item);
        popup.show();
    }

    public void setPopupMenuCallback(Callback callback) {
        this.callback = callback;
    }

    public void updateWith(Stack stack) {
        this.stack = stack;
        textViewSummary.setText(stack.summary);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.move:
                callback.onMoveClick(stack);
                return true;
            case R.id.delete:
                callback.onDeleteClick(stack);
                return true;
            default:
                throw new Error("Unknown menu item clicked");
        }
    }

    public interface Callback {

        void onStackClick(Stack stack);

        void onMoveClick(Stack stack);

        void onDeleteClick(Stack stack);

    }

}
