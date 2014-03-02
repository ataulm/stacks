package com.ataulm.stacks.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.ataulm.stacks.R;
import com.ataulm.stacks.model.Stack;
import com.novoda.notils.caster.Views;

public class StackListHeaderView extends RelativeLayout {

    private TextView summary;
    private TextView description;
    private ViewSwitcher contentLengthIndicator;

    public StackListHeaderView(Context context) {
        this(context, null);
        findViews();
    }

    public StackListHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_stacks_header, this, true);
    }

    @Override
    protected void onFinishInflate() {
        findViews();
        setClickable(true);
        setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                contentLengthIndicator.getNextView();
            }

        });
    }

    private void findViews() {
        summary = Views.findById(this, R.id.textview_summary);
        description = Views.findById(this, R.id.textview_description);
        contentLengthIndicator = Views.findById(this, R.id.content_length_indicator);
    }

    public void updateWith(Stack stack) {
        summary.setText(stack.summary);

        if (stack.description.length() > 0) {
            description.setText(stack.description);
            description.setVisibility(VISIBLE);
            contentLengthIndicator.setVisibility(VISIBLE);
        } else {
            description.setVisibility(GONE);
            contentLengthIndicator.setVisibility(GONE);
        }
    }
}
