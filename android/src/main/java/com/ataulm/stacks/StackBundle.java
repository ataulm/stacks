package com.ataulm.stacks;

import android.os.Bundle;

import com.ataulm.Optional;
import com.ataulm.stacks.stack.Id;
import com.ataulm.stacks.stack.Label;
import com.ataulm.stacks.stack.Labels;
import com.ataulm.stacks.stack.Stack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class StackBundle {

    private static final String ID = "ID";
    private static final String SUMMARY = "SUMMARY";
    private static final String PARENT_ID = "PARENT_ID";
    private static final String LABELS = "LABELS";
    private static final String CREATED = "CREATED";
    private static final String MODIFIED = "MODIFIED";
    private static final String COMPLETED = "COMPLETED";
    private static final String DELETED = "DELETED";

    public Bundle createBundleFrom(Stack stack) {
        Bundle bundle = new Bundle();
        bundle.putString(ID, stack.id().value());
        bundle.putString(SUMMARY, stack.summary());
        bundle.putString(PARENT_ID, stack.parentId().isPresent() ? stack.parentId().get().value() : null);
        bundle.putStringArrayList(LABELS, arrayListOfLabelsFrom(stack.labels()));
        bundle.putLong(CREATED, stack.dates().created());
        bundle.putLong(MODIFIED, stack.dates().modified());
        if (stack.dates().completed().isPresent()) {
            bundle.putLong(COMPLETED, stack.dates().completed().get());
        }
        if (stack.dates().deleted().isPresent()) {
            bundle.putLong(DELETED, stack.dates().deleted().get());
        }
        return bundle;
    }

    private static ArrayList<String> arrayListOfLabelsFrom(Labels labels) {
        ArrayList<String> list = new ArrayList<>(labels.size());
        for (Label label : labels) {
            list.add(label.value());
        }
        return list;
    }

    public Optional<Stack> createStackFrom(Bundle bundle) {
        String id = bundle.getString(ID);
        String summary = bundle.getString(SUMMARY);

        if (id == null || id.isEmpty() || summary == null || summary.isEmpty()) {
            return Optional.absent();
        }
        String rawParentId = bundle.getString(PARENT_ID);
        Optional<Id> parentId = rawParentId == null || rawParentId.isEmpty()
                ? Optional.<Id>absent()
                : Optional.of(Id.create(rawParentId));

        Stack.Dates dates = datesFrom(bundle);

        Labels labels = labelsFrom(bundle);

        if (labels == null) {
            return Optional.of(Stack.create(Id.create(id), summary, parentId, dates));
        } else {
            return Optional.of(Stack.create(Id.create(id), summary, parentId, dates, labels));
        }
    }

    private static Stack.Dates datesFrom(Bundle bundle) {
        long created = bundle.getLong(CREATED);
        long modified = bundle.getLong(MODIFIED);
        Optional<Long> completed = bundle.containsKey(COMPLETED) ? Optional.of(bundle.getLong(COMPLETED)) : Optional.<Long>absent();
        Optional<Long> deleted = bundle.containsKey(DELETED) ? Optional.of(bundle.getLong(DELETED)) : Optional.<Long>absent();
        return Stack.Dates.create(created, modified, completed, deleted);
    }

    @Nullable
    private static Labels labelsFrom(Bundle bundle) {
        List<String> labelValues = bundle.getStringArrayList(LABELS);
        if (labelValues == null) {
            return null;
        }

        Set<Label> labelSet = new HashSet<>(labelValues.size());
        for (String labelValue : labelValues) {
            labelSet.add(Label.create(labelValue));
        }
        return Labels.create(labelSet);
    }

}
