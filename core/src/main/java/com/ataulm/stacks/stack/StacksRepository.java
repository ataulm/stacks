package com.ataulm.stacks.stack;

import com.ataulm.Optional;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.subjects.BehaviorSubject;

public class StacksRepository {

    private final JsonStacksRepository jsonStacksRepository;
    private final JsonStackConverter jsonStackConverter;

    private BehaviorSubject<Stacks> stacksSubject = BehaviorSubject.create();

    public StacksRepository(JsonStacksRepository jsonStacksRepository, JsonStackConverter jsonStackConverter) {
        this.jsonStacksRepository = jsonStacksRepository;
        this.jsonStackConverter = jsonStackConverter;
    }

    public Observable<Stacks> getStacks(Optional<Id> parentId) {
        refreshStacks();
        if (parentId.isPresent()) {
            Observable<List<Stack>> allStacks = stacksSubject.map(extractList());
            return Observable.zip(
                    allStacks.map(filterOnlyStackWithId(parentId.get())),
                    allStacks.map(filterOnlyStacksWithParent(parentId)),
                    asStacks()
            );
        } else {
            return stacksSubject
                    .map(extractList())
                    .map(filterOnlyRootStacks())
                    .map(listAsStacks());
        }
    }

    private static Func1<List<Stack>, List<Stack>> filterOnlyRootStacks() {
        return new Func1<List<Stack>, List<Stack>>() {
            @Override
            public List<Stack> call(List<Stack> stacks) {
                List<Stack> filtered = new ArrayList<>(stacks.size());
                for (Stack stack : stacks) {
                    if (!stack.parentId().isPresent()) {
                        filtered.add(stack);
                    }
                }
                return filtered;
            }
        };
    }

    private static Func1<List<Stack>, Stacks> listAsStacks() {
        return new Func1<List<Stack>, Stacks>() {
            @Override
            public Stacks call(List<Stack> stacks) {
                return Stacks.create(stacks);
            }
        };
    }

    private static Func1<List<Stack>, Optional<Stack>> filterOnlyStackWithId(final Id id) {
        return new Func1<List<Stack>, Optional<Stack>>() {
            @Override
            public Optional<Stack> call(List<Stack> stacks) {
                for (Stack stack : stacks) {
                    if (stack.id().equals(id)) {
                        return Optional.of(stack);
                    }
                }
                return Optional.absent();
            }
        };
    }

    private static Func1<Stacks, List<Stack>> extractList() {
        return new Func1<Stacks, List<Stack>>() {
            @Override
            public List<Stack> call(Stacks stacks) {
                return stacks.children();
            }
        };
    }

    private static Func1<List<Stack>, List<Stack>> filterOnlyStacksWithParent(final Optional<Id> parentId) {
        return new Func1<List<Stack>, List<Stack>>() {
            @Override
            public List<Stack> call(List<Stack> stacks) {
                List<Stack> filtered = new ArrayList<>(stacks.size());
                for (Stack stack : stacks) {
                    if (stack.parentId().equals(parentId)) {
                        filtered.add(stack);
                    }
                }
                return filtered;
            }
        };
    }

    private static Func2<Optional<Stack>, List<Stack>, Stacks> asStacks() {
        return new Func2<Optional<Stack>, List<Stack>, Stacks>() {
            @Override
            public Stacks call(Optional<Stack> stackOptional, List<Stack> stacks) {
                return Stacks.create(stackOptional, stacks);
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

        Stack updatedStack = Stack.create(stack.id(), summary, stack.parentId(), stack.completed());
        Stacks updated = stacks.update(updatedStack);

        stacksSubject.onNext(updated);
    }

    public void updateParent(Stack stack, Id parentId) {
        ensureSubjectHasBeenInitialised();
        Stacks stacks = stacksSubject.getValue();

        Stack updatedStack = Stack.create(stack.id(), stack.summary(), Optional.of(parentId), stack.completed());
        Stacks updated = stacks.update(updatedStack);

        stacksSubject.onNext(updated);
    }

    private void ensureSubjectHasBeenInitialised() {
        if (!stacksSubject.hasValue()) {
            throw new IllegalStateException("Failed to add(Stack) - subject is not initialised");
        }
    }

    public void addLabel(Stack stack, Label label) {
        ensureSubjectHasBeenInitialised();
        Stacks stacks = stacksSubject.getValue();

        Labels updatedLabels = stack.labels().add(label);
        Stack updatedStack = Stack.create(stack.id(), stack.summary(), stack.parentId(), updatedLabels, stack.completed());
        Stacks updated = stacks.update(updatedStack);

        stacksSubject.onNext(updated);
    }

    public void removeLabel(Stack stack, Label label) {
        ensureSubjectHasBeenInitialised();
        Stacks stacks = stacksSubject.getValue();

        Labels updatedLabels = stack.labels().remove(label);
        Stack updatedStack = Stack.create(stack.id(), stack.summary(), stack.parentId(), updatedLabels, stack.completed());
        Stacks updated = stacks.update(updatedStack);

        stacksSubject.onNext(updated);
    }

}
