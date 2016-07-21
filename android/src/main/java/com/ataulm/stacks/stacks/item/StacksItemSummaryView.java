package com.ataulm.stacks.stacks.item;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ataulm.stacks.R;
import com.ataulm.stacks.stack.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StacksItemSummaryView extends LinearLayout {

    @BindView(R.id.stacks_item_summary_text)
    TextView summaryTextView;

    @BindView(R.id.stacks_item_summary_button_show_menu)
    View showMenuButton;

    public StacksItemSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_stacks_item_summary, this);
        ButterKnife.bind(this);
    }

    public void bind(Stack stack, Listener listener) {
        summaryTextView.setText(stack.summary());

        final PopupMenu popupMenu = setupPopupMenu(listener);
        showMenuButton.setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        popupMenu.show();
                    }

                }
        );
    }

    private PopupMenu setupPopupMenu(Listener listener) {
        PopupMenu popupMenu = new PopupMenu(getContext(), showMenuButton);
        popupMenu.inflate(R.menu.menu_stack_item);
        popupMenu.setOnMenuItemClickListener(new MenuClickListener(listener));
        return popupMenu;
    }

    private static class MenuClickListener implements PopupMenu.OnMenuItemClickListener {

        private final Listener listener;

        MenuClickListener(Listener listener) {
            this.listener = listener;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.remove:
                    onClickRemove();
                    return false;
                case R.id.edit:
                    onClickEdit();
                    return true;
                default:
                    throw new IllegalArgumentException("unhandled item: " + item.getTitle());
            }
        }

        private void onClickEdit() {
            listener.onClickEdit();
        }

        private void onClickRemove() {
            listener.onClickRemove();
        }

    }

    interface Listener {

        void onClickEdit();

        void onClickRemove();

    }

}
