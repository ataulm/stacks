package com.ataulm.stacks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.ataulm.Optional;
import com.ataulm.stacks.stack.PersistStacksUsecase;
import com.ataulm.stacks.stack.Stack;
import com.ataulm.stacks.stack.UpdateStackUsecase;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EditActivity extends AppCompatActivity {

    private final UpdateStackUsecase updateStackUsecase;
    private final PersistStacksUsecase persistStacksUsecase;

    @Bind(R.id.edit_text_summary)
    EditText summaryEditText;

    @Bind(R.id.edit_text_description)
    EditText descriptionEditText;

    @Bind(R.id.button_save)
    View saveButton;

    public EditActivity() {
        this.updateStackUsecase = StacksApplication.createUpdateStackUsecase();
        this.persistStacksUsecase = StacksApplication.createPersistStacksUsecase();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit);
        ButterKnife.bind(this);

        final Stack stack = woo();
        setTitle("Edit " + stack.summary());

        summaryEditText.setText(stack.summary());
        if (stack.description().isPresent()) {
            descriptionEditText.setText(stack.description().get());
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = descriptionEditText.getText().toString();
                if (description.isEmpty()) {
                    updateStackUsecase.updateSummary(stack, summaryEditText.getText().toString());
                } else {
                    updateStackUsecase.updateStack(stack, summaryEditText.getText().toString(), description);
                }
                finish();
            }
        });
    }

    private Stack woo() {
        Optional<Stack> stack = new StackBundle().createStackFrom(getIntent().getExtras());
        if (!stack.isPresent()) {
            throw new IllegalStateException("Can't start this activity without bundle from " + StackBundle.class.getName());
        }
        return stack.get();
    }

    @Override
    protected void onPause() {
        super.onPause();
        persistStacksUsecase.persistStacks();
    }

}
