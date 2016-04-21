package com.ataulm.stacks;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StackInputView extends LinearLayout {

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
        inputEditText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                boolean enterPressed = keyCode == KeyEvent.KEYCODE_ENTER;
                boolean currentIsNotEmpty = inputEditText.getText().toString().trim().length() > 0;

                if (enterPressed && currentIsNotEmpty) {
                    addStack(listener);
                    return true;
                }

                return enterPressed;
            }
        });
    }

    private void addStack(StackInputListener listener) {
        listener.onClickAddStack(inputEditText.getText().toString().trim());
        inputEditText.setText(null);
    }

}
