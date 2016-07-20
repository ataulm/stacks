package com.ataulm.stacks.stacks;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ataulm.stacks.R;
import com.ataulm.stacks.SimpleTextWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StackInputView extends LinearLayout {

    @BindView(R.id.input_edit_current)
    EditText inputEditText;

    @BindView(R.id.add_button)
    View addButton;

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
                boolean textIsPresent = s.length() > 0;
                addButton.setEnabled(textIsPresent);
            }
        });
    }

    public void bind(final StackInputListener listener) {
        setEnterKeyListenerToAddStackAndPreventMultilineInput(listener);

        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addStack(listener);
            }
        });
    }

    private void setEnterKeyListenerToAddStackAndPreventMultilineInput(final StackInputListener listener) {
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
        if (!summary.isEmpty()) {
            listener.onClickAddStack(summary);
        }
        resetInput();
    }

    private void resetInput() {
        inputEditText.setText(null);
        inputEditText.requestFocus();
    }

}
