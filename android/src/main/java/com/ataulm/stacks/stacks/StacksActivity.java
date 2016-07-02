package com.ataulm.stacks.stacks;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ataulm.stacks.ActivityContentViewSetter;
import com.ataulm.stacks.BaseActivity;
import com.ataulm.stacks.ContentViewSetter;
import com.ataulm.stacks.R;
import com.ataulm.stacks.navigation.Navigator;
import com.ataulm.stacks.navigation.UriCreator;
import com.ataulm.stacks.navigation.UriResolver;
import com.ataulm.stacks.stack.CreateStackUsecase;
import com.ataulm.stacks.stack.FetchStacksUsecase;
import com.ataulm.stacks.stack.PersistStacksUsecase;
import com.ataulm.stacks.stack.RemoveStackUsecase;
import com.ataulm.stacks.stack.UpdateStackUsecase;

import static com.ataulm.stacks.jabber.Jabber.*;

public class StacksActivity extends BaseActivity {

    private final UriCreator uriCreator = uriCreator();
    private final UriResolver uriResolver = uriResolver();
    private final FetchStacksUsecase fetchStacksUsecase = fetchStacksUsecase();
    private final CreateStackUsecase createStackUsecase = createStacksUsecase();
    private final UpdateStackUsecase updateStackUsecase = updateStacksUsecase();
    private final RemoveStackUsecase removeStackUsecase = removeStackUsecase();
    private final PersistStacksUsecase persistStacksUsecase = persistStacksUsecase();

    private StacksPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        ContentViewSetter contentViewSetter = new ActivityContentViewSetter(this);
        Navigator navigator = new Navigator(this, uriCreator);
        StacksToolbarActions toolbarActions = StacksToolbarActions.create(navigator);
        presenter = StacksPresenter.create(
                contentViewSetter,
                uriResolver,
                fetchStacksUsecase,
                createStackUsecase,
                updateStackUsecase,
                removeStackUsecase,
                persistStacksUsecase,
                toolbarActions,
                navigator
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        Uri uri = getIntent().getData();
        presenter.start(uri);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.stop();
    }

}
