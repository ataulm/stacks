package com.ataulm.stacks.navigation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.widget.FrameLayout;

import com.ataulm.Optional;
import com.ataulm.stacks.BaseActivity;
import com.ataulm.stacks.ContentViewSetter;
import com.ataulm.stacks.Presenter;
import com.ataulm.stacks.R;
import com.ataulm.stacks.jabber.Usecases;
import com.ataulm.stacks.removed_stacks.RemovedStacksPresenter;
import com.ataulm.stacks.stacks.OnClickNavigationButtonListenerImpl;
import com.ataulm.stacks.stacks.OnClickOpenNavigationDrawerListener;
import com.ataulm.stacks.stacks.StacksPresenter;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;

import butterknife.ButterKnife;

import static com.ataulm.stacks.jabber.Jabber.*;

public class TopLevelActivity extends BaseActivity {

    private final UriCreator uriCreator = uriCreator();
    private final UriResolver uriResolver = uriResolver();
    private final Usecases usecases = usecases();

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
        OnClickNavigationButtonListenerImpl onClickNavigationButtonListener = new OnClickNavigationButtonListenerImpl(navigator, createOnClickOpenNavDrawerListener());

        StacksPresenter stacksPresenter = createStacksPresenter(contentViewSetter, onClickNavigationButtonListener);
        RemovedStacksPresenter removedStacksPresenter = new RemovedStacksPresenter(contentViewSetter);

        return Arrays.asList(
                stacksPresenter,
                removedStacksPresenter
        );
    }

    private StacksPresenter createStacksPresenter(ContentViewSetter contentViewSetter, OnClickNavigationButtonListenerImpl onClickNavigationButtonListener) {
        return StacksPresenter.create(
                contentViewSetter,
                uriResolver,
                usecases,
                navigator,
                onClickNavigationButtonListener
        );
    }

    private OnClickOpenNavigationDrawerListener createOnClickOpenNavDrawerListener() {
        return new OnClickOpenNavigationDrawerListener() {
            @Override
            public void onClickOpenNavigationDrawer() {
                drawerController.openDrawer();
            }
        };
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

        Optional<URI> uri = uriFrom(intent.getData());
        presenter.start(uri);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Optional<URI> uri = getUri();
        presenter.start(uri);
    }

    private Optional<URI> getUri() {
        Uri data = getIntent().getData();
        return uriFrom(data);
    }

    private static Optional<URI> uriFrom(Uri uri) {
        URI converted = uri == null ? null : URI.create(uri.toString());
        return Optional.fromNullable(converted);
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
            return;
        }

        if (handleBackNavigation()) {
            return;
        }

        super.onBackPressed();
    }

    private boolean handleBackNavigation() {
        if (presenter.onBackPressed()) {
            return true;
        }

        if (presenter.isDisplaying(Screen.STACKS)) {
            return false;
        }

        navigator.navigateTo(Screen.STACKS);
        return true;
    }

}
