package com.ataulm.stacks.stacks.item;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ataulm.stacks.R;
import com.ataulm.stacks.jabber.Jabber;
import com.ataulm.stacks.stack.Stack;
import com.ataulm.stacks.stacks.UserItemActions;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StackItemView extends LinearLayout {

    @BindView(R.id.stack_item_check_completed)
    CheckBox completedCheckBox;

    @BindView(R.id.stacks_item_summary_text)
    TextView summaryTextView;

    @BindView(R.id.stacks_item_summary_button_show_menu)
    View showMenuButton;

    public StackItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_stack_item, this);
        ButterKnife.bind(this);
    }

    public void bind(final Stack stack, final UserItemActions userItemActions) {
        summaryTextView.setText(stack.summary());

        final PopupMenu popupMenu = setupPopupMenu(new StacksItemSummaryView.Listener() {
            @Override
            public void onClickEdit() {
                Jabber.toast("edit pressed");
            }

            @Override
            public void onClickRemove() {
                userItemActions.onClickRemove(stack);
            }
        });
        showMenuButton.setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        popupMenu.show();
                    }

                }
        );

        bindCompletedCheckBox(stack, userItemActions);
        applyTreatmentForCompletedState(stack);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                userItemActions.onClick(stack);
            }
        });
    }

    private PopupMenu setupPopupMenu(StacksItemSummaryView.Listener listener) {
        PopupMenu popupMenu = new PopupMenu(getContext(), showMenuButton);
        popupMenu.inflate(R.menu.menu_stack_item);
        popupMenu.setOnMenuItemClickListener(new MenuClickListener(listener));
        return popupMenu;
    }

    private static class MenuClickListener implements PopupMenu.OnMenuItemClickListener {

        private final StacksItemSummaryView.Listener listener;

        MenuClickListener(StacksItemSummaryView.Listener listener) {
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

    private void bindCompletedCheckBox(final Stack stack, final UserItemActions userItemActions) {
        completedCheckBox.setOnCheckedChangeListener(null);
        completedCheckBox.setChecked(stack.completed());
        completedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !stack.completed()) {
                    userItemActions.onClickMarkComplete(stack);
                } else if (!isChecked && stack.completed()) {
                    userItemActions.onClickMarkNotComplete(stack);
                }
            }
        });
    }

    private void applyTreatmentForCompletedState(Stack stack) {
        if (stack.completed()) {
            setAlpha(0.54f);
        } else {
            setAlpha(1f);
        }
    }

}
