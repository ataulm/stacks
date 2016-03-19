package com.ataulm.stacks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ataulm.stacks.stack.FetchStacksUsecase;
import com.ataulm.stacks.stack.Stack;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class StackActivity extends AppCompatActivity {

    private final FetchStacksUsecase fetchStacksUsecase;
    private Subscription subscription;
    private RecyclerView recyclerView;

    public StackActivity() {
        this.fetchStacksUsecase = StacksApplication.createFetchStacksUsecase();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my);
        recyclerView = (RecyclerView) findViewById(R.id.stacks_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        subscription = fetchStacksUsecase.fetchStacks()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<List<Stack>>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onNext(List<Stack> stacks) {
                                RecyclerView.Adapter stacksAdapter = new StacksAdapter(stacks);
                                recyclerView.swapAdapter(stacksAdapter, false);
                            }
                        }
                );
    }

    private static class StacksAdapter extends RecyclerView.Adapter<StackViewHolder> {

        private final List<Stack> stacks;

        StacksAdapter(List<Stack> stacks) {
            this.stacks = stacks;
            setHasStableIds(true);
        }

        @Override
        public StackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return StackViewHolder.inflate(parent);
        }

        @Override
        public void onBindViewHolder(StackViewHolder holder, int position) {
            Stack stack = stacks.get(position);
            holder.bind(stack);
        }

        @Override
        public int getItemCount() {
            return stacks.size();
        }

        @Override
        public long getItemId(int position) {
            return stacks.get(position).id().hashCode();
        }

    }

    private static final class StackViewHolder extends RecyclerView.ViewHolder {

        private final StackItemView stackItemView;

        public static StackViewHolder inflate(ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.view_stack_item, parent, false);
            return new StackViewHolder(view, ((StackItemView) view));
        }

        private StackViewHolder(View itemView, StackItemView stackItemView) {
            super(itemView);
            this.stackItemView = stackItemView;
        }

        public void bind(Stack stack) {
            stackItemView.bind(stack);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        subscription.unsubscribe();
    }

}
