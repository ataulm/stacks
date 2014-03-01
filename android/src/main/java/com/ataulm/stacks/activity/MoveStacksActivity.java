package com.ataulm.stacks.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ataulm.stacks.R;
import com.ataulm.stacks.base.StacksBaseActivity;
import com.ataulm.stacks.fragment.MoveStacksFragment;
import com.ataulm.stacks.model.Stack;

import java.util.List;

public class MoveStacksActivity extends StacksBaseActivity implements MoveStacksFragment.Callback {

    public static final String EXTRA_PARENT = "com.ataulm.stacks.extra.EXTRA_PARENT";
    public static final String EXTRA_STACKS_TO_MOVE = "com.ataulm.stacks.extra.EXTRA_STACKS_TO_MOVE";

    private List<Stack> stacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findExtrasOrThrowException();
        setContentView(R.layout.activity_move_stack);

        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new MoveStacksFragment()).commit();
    }

    private void findExtrasOrThrowException() {
        if (!getIntent().hasExtra(EXTRA_PARENT)) {
            throw new MoveStackException(EXTRA_PARENT);
        }

        if (!getIntent().hasExtra(EXTRA_STACKS_TO_MOVE)) {
            throw new MoveStackException(EXTRA_STACKS_TO_MOVE);
        }
        stacks = getIntent().getParcelableArrayListExtra(EXTRA_STACKS_TO_MOVE);
    }

    @Override
    public void onStackClicked(Stack stack) {
        getIntent().putExtra(EXTRA_PARENT, stack);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new MoveStacksFragment())
                .addToBackStack(null)
                .commit();
        Toast.makeText(this, "nav to stack:" + stack.summary, Toast.LENGTH_SHORT).show();
    }

    public void onDecisionClick(View view) {
        switch (view.getId()) {
            case R.id.textview_move:
                Stack stack = getIntent().getParcelableExtra(EXTRA_PARENT);
                Toast.makeText(this, "moving to: " + stack.summary, Toast.LENGTH_SHORT).show();
                break;
            case R.id.textview_cancel:
                finish();
                break;
            default:
                throw new IllegalArgumentException("Method called without expected activity_move_stack view as parameter");
        }
    }

    public static class MoveStackException extends RuntimeException {

        public MoveStackException(String extraId) {
            super("MoveStacksActivity started with missing Intent extras: " + extraId);
        }

    }

}
