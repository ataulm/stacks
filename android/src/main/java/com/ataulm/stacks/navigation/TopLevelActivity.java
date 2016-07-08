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
import com.ataulm.stacks.stack.RemoveStackUsecase;
import com.ataulm.stacks.stack.UpdateStackUsecase;
import com.ataulm.stacks.stacks.PreviouslyViewedStacks;
import com.ataulm.stacks.stacks.StacksPresenter;
import com.ataulm.stacks.stacks.StacksToolbarActions;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import butterknife.ButterKnife;

import static com.ataulm.stacks.jabber.Jabber.*;

public class TopLevelActivity extends BaseActivity {

    private final UriCreator uriCreator = uriCreator();
    private final UriResolver uriResolver = uriResolver();
    private final FetchStacksUsecase fetchStacksUsecase = fetchStacksUsecase();
    private final CreateStackUsecase createStackUsecase = createStacksUsecase();
    private final UpdateStackUsecase updateStackUsecase = updateStacksUsecase();
    private final RemoveStackUsecase removeStackUsecase = removeStackUsecase();
    private final PersistStacksUsecase persistStacksUsecase = persistStacksUsecase();
    private final NewIntentBackStack newIntentBackStack = new NewIntentBackStack();

    private TopLevelPresenter presenter;
    private Navigator navigator;
    private DrawerController drawerController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_level);

        navigator = new Navigator(this, uriCreator);
        setupNavigationDrawer();

        presenter = new TopLevelPresenter(uriResolver, createScreenPresenters(savedInstanceState));
    }

    private Collection<Presenter> createScreenPresenters(Bundle savedInstanceState) {
        FrameLayout contentFrame = ButterKnife.findById(this, R.id.drawer_layout_content);
        ContentViewSetter contentViewSetter = new DrawerLayoutContentViewSetter(getLayoutInflater(), contentFrame);
        StacksToolbarActions toolbarActions = StacksToolbarActions.create(navigator, drawerController);
        StacksPresenter stacksPresenter = createStacksPresenter(contentViewSetter, toolbarActions, savedInstanceState);
        return Arrays.asList(
                stacksPresenter,
                new RemovedStacksPresenter(contentViewSetter)
        );
    }

    private StacksPresenter createStacksPresenter(ContentViewSetter contentViewSetter, StacksToolbarActions toolbarActions, Bundle savedInstanceState) {
        return StacksPresenter.create(
                contentViewSetter,
                uriResolver,
                fetchStacksUsecase,
                createStackUsecase,
                updateStackUsecase,
                removeStackUsecase,
                persistStacksUsecase,
                toolbarActions,
                navigator,
                PreviouslyViewedStacks.create(savedInstanceState)
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
        newIntentBackStack.onNewIntent(intent);

        setIntent(intent);

        URI uri = uriFrom(intent.getData());
        presenter.start(uri);
    }

    @Override
    protected void onResume() {
        super.onResume();
        URI uri = getUri();
        presenter.start(uri);
    }

    @Nullable
    private URI getUri() {
        Uri data = getIntent().getData();
        return uriFrom(data);
    }

    @Nullable
    private static URI uriFrom(Uri uri) {
        if (uri == null) {
            return null;
        }
        return URI.create(uri.toString());
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
            onBackPressedOnStacksScreen();
        } else {
            navigator.navigateTo(Screen.STACKS);
        }
    }

    private void onBackPressedOnStacksScreen() {
        if (newIntentBackStack.hasBackStack()) {
            finish();
        } else {
            Intent intent = newIntentBackStack.pop();
            startActivity(intent);
            finish();
        }
    }




    private static class NewIntentBackStack {

        private final List<Intent> intents = new ArrayList<>();

        public void onNewIntent(Intent intent) {
            intents.add(intent);
        }

        public boolean hasBackStack() {
            return intents.size() > 1;
        }

        public Intent pop() {
            if (intents.isEmpty()) {
                throw new ArrayIndexOutOfBoundsException();
            }
            int indexLatestItem = intents.size() - 1;
            return intents.remove(indexLatestItem);
        }

    }

}
