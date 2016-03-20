package com.ataulm.stacks.stack;

import com.ataulm.Optional;
import com.ataulm.RxFunctions;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;

public class StacksRepository {

    private final JsonStacksRepository jsonStacksRepository;
    private final JsonStackConverter jsonStackConverter;

    private BehaviorSubject<Stacks> stacksSubject = BehaviorSubject.create();

    public StacksRepository(JsonStacksRepository jsonStacksRepository, JsonStackConverter jsonStackConverter) {
        this.jsonStacksRepository = jsonStacksRepository;
        this.jsonStackConverter = jsonStackConverter;
    }

    public Observable<Stacks> getStacks(Optional<Stack> parent) {
        refreshStacks();
        if (parent.isPresent()) {
            Optional<String> parentId = parent.get().parentId();
            return filterStacksWithParent(parentId);
        } else {
            return stacksSubject;
        }
    }

    private Observable<Stacks> filterStacksWithParent(Optional<String> parentId) {
        return stacksSubject.flatMap(RxFunctions.<Stack>emitEachElement())
                .filter(onlyStacksWithParent(parentId))
                .toList()
                .map(asStacks());
    }

    private static Func1<Stack, Boolean> onlyStacksWithParent(final Optional<String> parentId) {
        return new Func1<Stack, Boolean>() {
            @Override
            public Boolean call(Stack stack) {
                return stack.parentId().equals(parentId);
            }
        };
    }

    private static Func1<List<Stack>, Stacks> asStacks() {
        return new Func1<List<Stack>, Stacks>() {
            @Override
            public Stacks call(List<Stack> stacks) {
                return Stacks.create(stacks);
            }
        };
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

    public void persist() {
        ensureSubjectHasBeenInitialised();
        Stacks stacks = stacksSubject.getValue();

        List<JsonStack> jsonStacks = convertToJsonStacks(stacks);
        jsonStacksRepository.persist(jsonStacks);
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

    public void remove(Stack stack) {
        ensureSubjectHasBeenInitialised();
        Stacks stacks = stacksSubject.getValue();

        Stacks updated = stacks.remove(stack);

        stacksSubject.onNext(updated);
    }

    public void update(Stack stack, String summary) {
        ensureSubjectHasBeenInitialised();
        Stacks stacks = stacksSubject.getValue();

        Stack updatedStack = Stack.create(stack.id(), summary, stack.parentId(), stack.description());
        Stacks updated = stacks.update(updatedStack);

        stacksSubject.onNext(updated);
    }

    public void updateDescription(Stack stack, String description) {
        ensureSubjectHasBeenInitialised();
        Stacks stacks = stacksSubject.getValue();

        Stack updatedStack = Stack.create(stack.id(), stack.summary(), stack.parentId(), Optional.of(description));
        Stacks updated = stacks.update(updatedStack);

        stacksSubject.onNext(updated);
    }

    public void updateStack(Stack stack, String summary, String description) {
        ensureSubjectHasBeenInitialised();
        Stacks stacks = stacksSubject.getValue();

        Stack updatedStack = Stack.create(stack.id(), summary, stack.parentId(), Optional.of(description));
        Stacks updated = stacks.update(updatedStack);

        stacksSubject.onNext(updated);
    }

    public void updateParent(Stack stack, String parentId) {
        ensureSubjectHasBeenInitialised();
        Stacks stacks = stacksSubject.getValue();

        Stack updatedStack = Stack.create(stack.id(), stack.summary(), Optional.of(parentId), stack.description());
        Stacks updated = stacks.update(updatedStack);

        stacksSubject.onNext(updated);
    }

    private void ensureSubjectHasBeenInitialised() {
        if (!stacksSubject.hasValue()) {
            throw new IllegalStateException("Failed to add(Stack) - subject is not initialised");
        }
    }

}
