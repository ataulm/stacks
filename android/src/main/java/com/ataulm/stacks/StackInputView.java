package com.ataulm.stacks;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StackInputView extends LinearLayout {

    private static final int REQUEST_FOCUS_AFTER_DELAY_MILLIS = 200;

    @Bind(R.id.input_edit_text)
    EditText inputEditText;

    @Bind(R.id.add_button)
    Button addButton;

    public StackInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOrientation(HORIZONTAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_stack_input_view, this);
        ButterKnife.bind(this);

        inputEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                boolean ifTextIsNotEmpty = s.length() > 0;
                addButton.setEnabled(ifTextIsNotEmpty);
            }
        });
    }

    public void bind(final StackInputListener listener) {
        setEnterKeyListenerToMoveToNextAndPreventMultilineInput(listener);
        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addStack(listener);
            }
        });
    }

    private void setEnterKeyListenerToMoveToNextAndPreventMultilineInput(final StackInputListener listener) {
        inputEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (enterOrDone(actionId)) {
                    addStack(listener);
                    return true;
                }
                return false;
            }

            private boolean enterOrDone(int actionId) {
                return actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL;
            }
        });

        inputEditText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                boolean enterPressed = keyCode == KeyEvent.KEYCODE_ENTER;

                if (enterPressed) {
                    addStack(listener);
                    return true;
                }

                return false;
            }
        });
    }

    private void addStack(StackInputListener listener) {
        String summary = inputEditText.getText().toString().trim();
        if (summary.isEmpty()) {
            return;
        }

        listener.onClickAddStack(summary);
        inputEditText.setText(null);
        requestFocusAfterDelay();
    }

    private void requestFocusAfterDelay() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                inputEditText.requestFocus();
            }
        }, REQUEST_FOCUS_AFTER_DELAY_MILLIS);
    }

}
