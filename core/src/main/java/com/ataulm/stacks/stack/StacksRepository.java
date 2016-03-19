package com.ataulm.stacks.stack;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.BehaviorSubject;

public class StacksRepository {

    private final JsonStacksRepository jsonStacksRepository;
    private final JsonStackConverter jsonStackConverter;

    private BehaviorSubject<Stacks> stacksSubject = BehaviorSubject.create();

    public StacksRepository(JsonStacksRepository jsonStacksRepository, JsonStackConverter jsonStackConverter) {
        this.jsonStacksRepository = jsonStacksRepository;
        this.jsonStackConverter = jsonStackConverter;
    }

    public Observable<Stacks> getStacks() {
        refreshStacks();
        return stacksSubject;
    }

    private void refreshStacks() {
        Stacks stacks = readStacksFromJsonStacksRepository();
        stacksSubject.onNext(stacks);
    }

    private Stacks readStacksFromJsonStacksRepository() {
        List<JsonStack> jsonStacks = jsonStacksRepository.getJsonStacks();
        return convertToStacks(jsonStacks);
    }

    private Stacks convertToStacks(List<JsonStack> jsonStacks) {
        List<Stack> stacks = new ArrayList<>(jsonStacks.size());
        for (JsonStack jsonStack : jsonStacks) {
            Stack stack = jsonStackConverter.convert(jsonStack);
            if (stack != null) {
                stacks.add(stack);
            }
        }
        return Stacks.create(stacks);
    }

    public void persist(Stacks stacks) {
        List<JsonStack> jsonStacks = convertToJsonStacks(stacks);
        jsonStacksRepository.persist(jsonStacks);
        stacksSubject.onNext(stacks);
    }

    private List<JsonStack> convertToJsonStacks(Stacks stacks) {
        List<JsonStack> jsonStacks = new ArrayList<>(stacks.size());
        for (Stack stack : stacks) {
            JsonStack jsonStack = jsonStackConverter.convert(stack);
            if (jsonStack != null) {
                jsonStacks.add(jsonStack);
            }
        }
        return jsonStacks;
    }

    public void add(Stack stack) {
        ensureSubjectHasBeenInitialised();
        Stacks stacks = stacksSubject.getValue();
        Stacks updated = stacks.add(stack);
        stacksSubject.onNext(updated);
    }

    private void ensureSubjectHasBeenInitialised() {
        if (!stacksSubject.hasValue()) {
            throw new IllegalStateException("Failed to add(Stack) - subject is not initialised");
        }
    }

    public void remove(Stack stack) {
        ensureSubjectHasBeenInitialised();
        Stacks stacks = stacksSubject.getValue();
        Stacks updated = stacks.remove(stack);
        stacksSubject.onNext(updated);
    }

}
