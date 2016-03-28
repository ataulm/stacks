package com.ataulm.stacks;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

class StackInputView extends LinearLayout {

    @Bind(R.id.current_edit_text)
    EditText currentEditText;

    @Bind(R.id.next_edit_text)
    EditText nextEditText;

    public StackInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOrientation(VERTICAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_stack_input_view, this);
        ButterKnife.bind(this);

        addTextChangedListenerToShowOrHideNextEditText();
        setEnterKeyListenerToMoveToNextAndPreventMultilineInput();
    }

    private void addTextChangedListenerToShowOrHideNextEditText() {
        currentEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    nextEditText.setVisibility(VISIBLE);
                } else {
                    nextEditText.setVisibility(GONE);
                }
            }
        });
    }

    private void setEnterKeyListenerToMoveToNextAndPreventMultilineInput() {
        currentEditText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                boolean enterPressed = keyCode == KeyEvent.KEYCODE_ENTER;
                boolean currentIsNotEmpty = currentEditText.getText().toString().trim().length() > 0;

                if (enterPressed && currentIsNotEmpty) {
                    nextEditText.requestFocus();
                    return true;
                }

                return enterPressed;
            }
        });
    }

    public void bind(final StackInputListener listener) {
        setFocusChangeListenerToAddStackThenReset(listener);
    }

    private void setFocusChangeListenerToAddStackThenReset(final StackInputListener listener) {
        nextEditText.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v.equals(nextEditText) && hasFocus) {
                    addStackThenReset();
                }
            }

            private void addStackThenReset() {
                listener.onClickAddStack(currentEditText.getText().toString().trim());
                currentEditText.setText(null);
                currentEditText.requestFocus();
            }

        });
    }

}
