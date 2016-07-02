package com.ataulm.stacks.navigation;

import com.ataulm.Optional;
import com.ataulm.stacks.stack.Id;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class IdExtractor {

    private static final Pattern UUID_PATTERN = Pattern.compile("([aA-zZ0-9]{8}-[aA-zZ0-9]{4}-[aA-zZ0-9]{4}-[aA-zZ0-9]{4}-[aA-zZ0-9]{12})");

    Optional<Id> extractIdFrom(URI uri) {
        String path = uri.getPath();

        Matcher matcher = UUID_PATTERN.matcher(path);
        if (matcher.find()) {
            Id id = Id.create(matcher.group());
            return Optional.of(id);
        } else {
            return Optional.absent();
        }
    }

}
