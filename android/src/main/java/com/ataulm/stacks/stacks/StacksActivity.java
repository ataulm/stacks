package com.ataulm.stacks.stacks;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ataulm.stacks.ActivityContentViewSetter;
import com.ataulm.stacks.BaseActivity;
import com.ataulm.stacks.R;
import com.ataulm.stacks.jabber.Jabber;
import com.ataulm.stacks.navigation.Navigator;

import java.net.URI;

import static com.ataulm.stacks.jabber.Jabber.*;
import static com.ataulm.stacks.jabber.Jabber.uriCreator;

public class StacksActivity extends BaseActivity {

    private StacksPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stacks);

        Navigator navigator = new Navigator(this, uriCreator());
        presenter = StacksPresenter.create(
                new ActivityContentViewSetter(this),
                uriResolver(),
                usecases(),
                navigator,
                new OnClickNavigationButtonListenerImpl(navigator, new IllegalStateOnClickOpenNavigationDrawerListener())
        );
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

    private static class IllegalStateOnClickOpenNavigationDrawerListener implements OnClickOpenNavigationDrawerListener {

        @Override
        public void onClickOpenNavigationDrawer() {
            throw new IllegalStateException("StacksActivity doesn't support navigation drawer");
        }

    }

}
