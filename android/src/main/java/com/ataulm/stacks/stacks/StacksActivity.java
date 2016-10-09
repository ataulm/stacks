package com.ataulm.stacks.stacks;

import android.net.Uri;
import android.os.Bundle;

import com.ataulm.Optional;
import com.ataulm.stacks.ActivityContentViewSetter;
import com.ataulm.stacks.BaseActivity;
import com.ataulm.stacks.R;
import com.ataulm.stacks.navigation.Navigator;

import java.net.URI;

import static com.ataulm.stacks.jabber.Jabber.*;

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

    private static class IllegalStateOnClickOpenNavigationDrawerListener implements OnClickOpenNavigationDrawerListener {

        @Override
        public void onClickOpenNavigationDrawer() {
            throw new IllegalStateException("StacksActivity doesn't support navigation drawer");
        }

    }

}
