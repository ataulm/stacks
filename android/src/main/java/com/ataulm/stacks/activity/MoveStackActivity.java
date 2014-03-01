package com.ataulm.stacks.activity;

import android.os.Bundle;

import com.ataulm.stacks.R;
import com.ataulm.stacks.base.StacksBaseActivity;
import com.ataulm.stacks.fragment.ViewStackFragment;

import java.util.List;

public class MoveStackActivity extends StacksBaseActivity {

    public static final String EXTRA_MOVE_CURRENT_PARENT = "com.ataulm.stacks.extra.EXTRA_MOVE_CURRENT_PARENT";
    public static final String EXTRA_MOVE_STACKS_TO_MOVE = "com.ataulm.stacks.extra.EXTRA_MOVE_STACKS_TO_MOVE";

    private List<String> stacks;
    private String parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findExtrasOrThrowException();
        setContentView(R.layout.activity_move_stack);

        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new ViewStackFragment()).commit();
    }

    private void findExtrasOrThrowException() {
        if (!getIntent().hasExtra(EXTRA_MOVE_CURRENT_PARENT)) {
            throw new MoveStackException(EXTRA_MOVE_CURRENT_PARENT);
        }

        if (!getIntent().hasExtra(EXTRA_MOVE_STACKS_TO_MOVE)) {
            throw new MoveStackException(EXTRA_MOVE_STACKS_TO_MOVE);
        }

        parent = getIntent().getStringExtra(EXTRA_MOVE_CURRENT_PARENT);
        stacks = getIntent().getStringArrayListExtra(EXTRA_MOVE_STACKS_TO_MOVE);
    }

    private static class MoveStackException extends RuntimeException {

        MoveStackException(String extraId) {
            super("MoveStackActivity started with missing Intent extras: " + extraId);
        }

    }

}
