package com.ataulm.stacks.navigation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.widget.FrameLayout;

import com.ataulm.stacks.BaseActivity;
import com.ataulm.stacks.ContentViewSetter;
import com.ataulm.stacks.Presenter;
import com.ataulm.stacks.R;
import com.ataulm.stacks.removed_stacks.RemovedStacksPresenter;
import com.ataulm.stacks.stack.CreateStackUsecase;
import com.ataulm.stacks.stack.FetchStacksUsecase;
import com.ataulm.stacks.stack.PersistStacksUsecase;
import com.ataulm.stacks.stacks.StacksPresenter;
import com.ataulm.stacks.stacks.StacksToolbarActions;

import java.util.Arrays;
import java.util.Collection;

import butterknife.ButterKnife;

import static com.ataulm.stacks.jabber.Jabber.*;

public class TopLevelActivity extends BaseActivity {

    private final UriCreator uriCreator = uriCreator();
    private final UriResolver uriResolver = uriResolver();
    private final FetchStacksUsecase fetchStacksUsecase = fetchStacksUsecase();
    private final CreateStackUsecase createStackUsecase = createStacksUsecase();
    private final PersistStacksUsecase persistStacksUsecase = persistStacksUsecase();

    private TopLevelPresenter presenter;
    private Navigator navigator;
    private DrawerController drawerController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_level);

        navigator = new Navigator(this, uriCreator);
        setupNavigationDrawer();

        presenter = new TopLevelPresenter(uriResolver, createScreenPresenters());
    }

    private Collection<Presenter> createScreenPresenters() {
        FrameLayout contentFrame = ButterKnife.findById(this, R.id.drawer_layout_content);
        ContentViewSetter contentViewSetter = new DrawerLayoutContentViewSetter(getLayoutInflater(), contentFrame);
        StacksToolbarActions toolbarActions = StacksToolbarActions.create(navigator, drawerController);
        return Arrays.asList(
                new StacksPresenter(contentViewSetter, uriResolver, fetchStacksUsecase, createStackUsecase, persistStacksUsecase, toolbarActions, navigator),
                new RemovedStacksPresenter(contentViewSetter)
        );
    }

    private void setupNavigationDrawer() {
        DrawerLayout drawerLayout = ButterKnife.findById(this, R.id.drawer_layout);
        drawerController = new DrawerController(drawerLayout, GravityCompat.START);

        NavigationDrawerView view = ButterKnife.findById(this, R.id.drawer);
        view.set(
                new NavigationDrawerView.Listener() {
                    @Override
                    public void onClickViewStacks() {
                        navigator.navigateTo(Screen.STACKS);
                        drawerController.closeDrawer();
                    }

                    @Override
                    public void onClickRemovedStacks() {
                        navigator.navigateTo(Screen.REMOVED_STACKS);
                        drawerController.closeDrawer();
                    }
                }
        );
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        Uri uri = intent.getData();
        presenter.start(uri);
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

    @Override
    public void onBackPressed() {
        if (drawerController.isDrawerOpen()) {
            drawerController.closeDrawer();
        } else {
            handleBackNavigation();
        }
    }

    private void handleBackNavigation() {
        if (presenter.onBackPressed()) {
            return;
        }

        if (presenter.isDisplaying(Screen.STACKS)) {
            finish();
        } else {
            navigator.navigateTo(Screen.STACKS);
        }
    }

}
