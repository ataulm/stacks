package com.ataulm.stacks.activity;

import android.os.Bundle;
import android.view.View;

import com.ataulm.stacks.R;
import com.ataulm.stacks.base.StacksBaseActivity;
import com.ataulm.stacks.fragment.MoveStacksFragment;
import com.ataulm.stacks.model.Stack;
import com.ataulm.stacks.persistence.task.GetAncestorsTask;
import com.ataulm.stacks.persistence.task.UpdateTask;

import java.util.List;

public class MoveStacksActivity extends StacksBaseActivity implements MoveStacksFragment.Callback, GetAncestorsTask.Callback {

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
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textview_move:
                Stack stack = getIntent().getParcelableExtra(EXTRA_PARENT);
                GetAncestorsTask.newInstance(getContentResolver(), this, stack).execute();
                break;
            case R.id.textview_cancel:
                finish();
                break;
            default:
                throw new IllegalArgumentException("Expected View from R.layout.activity_move_stack as parameter");
        }
    }

    @Override
    public void onAncestorsRetrieved(List<String> ancestorIds) {
        for (Stack stack : stacks) {
            if (ancestorIds.contains(stack.id)) {
                toast(R.string.cannot_move_stacks_here);
                return;
            }
        }
        moveStacks();
        finish();
    }

    private void moveStacks() {
        Stack parent = getIntent().getParcelableExtra(EXTRA_PARENT);
        for (Stack stack : stacks) {
            Stack movedStack = Stack.Builder.from(stack).parent(parent.id).build();
            UpdateTask.newInstance(getContentResolver(), movedStack).execute();
        }
    }

    public static class MoveStackException extends RuntimeException {

        public MoveStackException(String extraId) {
            super("MoveStacksActivity started with missing Intent extras: " + extraId);
        }

    }

}
