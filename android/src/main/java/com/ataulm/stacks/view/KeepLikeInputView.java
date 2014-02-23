package com.ataulm.stacks.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.ataulm.stacks.R;
import com.novoda.notils.caster.Views;

public class KeepLikeInputView extends RelativeLayout {

    private EditText current;
    private EditText next;
    private StackInputCallbacks callbacks;
    private InputReactor reactor;

    public KeepLikeInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.callbacks = new NoActionCallbacks();
    }

    public void setCallbacks(StackInputCallbacks callbacks) {
        this.callbacks = callbacks;
        reactor.setCallbacks(callbacks);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        current = Views.findById(this, R.id.edittext_current);
        next = Views.findById(this, R.id.edittext_next);

        reactor = new InputReactor(current, next, callbacks);
        current.addTextChangedListener(reactor);
        current.setOnKeyListener(reactor);
        next.setOnFocusChangeListener(reactor);
    }

    private static class InputReactor implements TextWatcher, OnFocusChangeListener, OnKeyListener {

        private final EditText current;
        private final EditText next;

        private StackInputCallbacks callbacks;

        InputReactor(EditText current, EditText next, StackInputCallbacks callbacks) {
            this.callbacks = callbacks;
            this.current = current;
            this.next = next;
        }

        void setCallbacks(StackInputCallbacks callbacks) {
            this.callbacks = callbacks;
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                next.setVisibility(VISIBLE);
            } else {
                next.setVisibility(GONE);
            }
        }

        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (view.getId() == next.getId() && hasFocus) {
                callbacks.addStack(current.getText().toString().trim());
                current.setText(null);
                current.requestFocus();
            }
        }

        @Override
        public void beforeTextChanged(CharSequence sq, int s, int c, int a) {
        }

        @Override
        public void onTextChanged(CharSequence sq, int s, int b, int c) {
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (current.getText().toString().trim().length() == 0) {
                return true;
            }
            return false;
        }

    }

    private static class NoActionCallbacks implements StackInputCallbacks {

        @Override
        public void addStack(String summary) {
        }

    }

}
