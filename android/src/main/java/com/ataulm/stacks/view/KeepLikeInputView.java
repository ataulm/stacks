package com.ataulm.stacks.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.ataulm.stacks.R;
import com.novoda.notils.caster.Views;

public class KeepLikeInputView extends RelativeLayout {

    private EditText current;
    private EditText next;

    public KeepLikeInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        current = Views.findById(this, R.id.edittext_current);
        next = Views.findById(this, R.id.edittext_next);

        current.addTextChangedListener(new KeepLikeInputReactor(next));
    }

    private static class KeepLikeInputReactor implements TextWatcher {

        private final EditText next;

        private KeepLikeInputReactor(EditText next) {
            this.next = next;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                next.setVisibility(VISIBLE);
            } else {
                next.setVisibility(GONE);
            }
        }

    }
}
