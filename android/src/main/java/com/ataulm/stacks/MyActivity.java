package com.ataulm.stacks;

import android.support.v7.app.AppCompatActivity;

import com.ataulm.stacks.stack.FetchStacksUsecase;
import com.ataulm.stacks.stack.Stack;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class MyActivity extends AppCompatActivity {

    private final FetchStacksUsecase fetchStacksUsecase;
    private Subscription subscription;

    public MyActivity() {
        this.fetchStacksUsecase = StacksApplication.createFetchStacksUsecase();
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

                            }
                        }
                );
    }

    @Override
    protected void onStop() {
        super.onStop();
        subscription.unsubscribe();
    }

}
