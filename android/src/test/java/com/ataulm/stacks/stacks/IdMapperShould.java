package com.ataulm.stacks.stacks;

import com.ataulm.Optional;
import com.ataulm.stacks.stack.Id;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class IdMapperShould {

    PreviouslyViewedStacks.IdMapper mapper = new PreviouslyViewedStacks.IdMapper();

    @Test
    public void returnEmptyList_givenNoRawIds() {
        String[] rawIds = {};

        List<Optional<Id>> optionals = mapper.mapRawIdsToIds(rawIds);

        assertThat(optionals).isEmpty();
    }

    @Test
    public void returnListOfOptionalIds_givenArrayOfRawIds() {
        String[] rawIds = {"1", "2", "3"};

        List<Optional<Id>> optionals = mapper.mapRawIdsToIds(rawIds);

        List<Optional<Id>> expected = Arrays.asList(
                Optional.of(Id.create("1")),
                Optional.of(Id.create("2")),
                Optional.of(Id.create("3"))
        );
        assertThat(optionals).isEqualTo(expected);
    }

    @Test
    public void returnListOfOptionalIds_withAbsent_givenArrayOfRawIds_withNull() {
        String[] rawIds = {"1", null, "3"};

        List<Optional<Id>> optionals = mapper.mapRawIdsToIds(rawIds);

        List<Optional<Id>> expected = Arrays.asList(
                Optional.of(Id.create("1")),
                Optional.<Id>absent(),
                Optional.of(Id.create("3"))
        );
        assertThat(optionals).isEqualTo(expected);
    }

    @Test
    public void returnEmptyArray_givenNoIds() {
        List<Optional<Id>> ids = Collections.emptyList();

        String[] rawIds = mapper.mapIdsToRawIds(ids);

        assertThat(rawIds).isEmpty();
    }

    @Test
    public void returnArrayOfRawIds_givenListOfOptionalIds() {
        List<Optional<Id>> ids = Arrays.asList(
                Optional.of(Id.create("1")),
                Optional.of(Id.create("2")),
                Optional.of(Id.create("3"))
        );

        String[] rawIds = mapper.mapIdsToRawIds(ids);

        String[] expected = {"1", "2", "3"};
        assertThat(rawIds).isEqualTo(expected);
    }

    @Test
    public void returnArrayOfRawIds_withNull_givenListOfOptionalIds_withAbsent() {
        List<Optional<Id>> ids = Arrays.asList(
                Optional.of(Id.create("1")),
                Optional.<Id>absent(),
                Optional.of(Id.create("3"))
        );

        String[] rawIds = mapper.mapIdsToRawIds(ids);

        String[] expected = {"1", null, "3"};
        assertThat(rawIds).isEqualTo(expected);
    }

}
