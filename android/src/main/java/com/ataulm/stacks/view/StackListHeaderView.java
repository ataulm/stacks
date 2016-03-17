package com.ataulm.stacks.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.ataulm.stacks.R;
import com.ataulm.stacks.model.AndroidStack;
import com.novoda.notils.caster.Views;

public class StackListHeaderView extends RelativeLayout {

    private TextView summary;
    private TextView description;
    private ViewSwitcher contentLengthIndicator;

    public StackListHeaderView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_stacks_header, this, true);
        summary = Views.findById(this, R.id.textview_summary);
        description = Views.findById(this, R.id.textview_description);
        contentLengthIndicator = Views.findById(this, R.id.content_length_indicator);

        setBackgroundColor(getResources().getColor(R.color.list_header_container));
        int padding = getResources().getDimensionPixelSize(R.dimen.stack_list_header_view_padding);
        setPadding(padding, padding, padding, padding);
    }

    public void updateWith(AndroidStack stack) {
        summary.setText(stack.summary);

        if (stack.description.length() > 0) {
            description.setText(stack.description);
            contentLengthIndicator.setVisibility(VISIBLE);

            setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    contentLengthIndicator.showNext();
                    description.setVisibility(description.getVisibility() == VISIBLE ? GONE : VISIBLE);
                }

            });
        } else {
            description.setVisibility(GONE);
            contentLengthIndicator.setVisibility(GONE);
            setOnClickListener(null);
        }
    }

}
