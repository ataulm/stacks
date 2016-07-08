package com.ataulm.stacks.stacks;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

class UriBackStack {

    private final List<URI> uris = new ArrayList<>();

    public void addToBackStack(URI uri) {
        uris.add(uri);
    }

    public void onBackPressed() {
        URI latestItem = getPreviousUri();
        uris.remove(latestItem);
    }

    public URI getPreviousUri() {
        if (uris.isEmpty()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int indexLatestItem = uris.size() - 1;
        return uris.get(indexLatestItem);
    }

}
