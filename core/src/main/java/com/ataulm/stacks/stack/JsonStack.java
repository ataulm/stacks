package com.ataulm.stacks.stack;

import com.squareup.moshi.Json;

import java.util.Set;

public class JsonStack {

    @Json(name = "id")
    public String id;

    @Json(name = "summary")
    public String summary;

    @Json(name = "parent")
    public String parentId;

    @Json(name = "dates")
    public Dates dates;

    @Json(name = "labels")
    public Set<String> labels;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JsonStack jsonStack = (JsonStack) o;

        if (id != null ? !id.equals(jsonStack.id) : jsonStack.id != null) {
            return false;
        }
        if (summary != null ? !summary.equals(jsonStack.summary) : jsonStack.summary != null) {
            return false;
        }
        if (parentId != null ? !parentId.equals(jsonStack.parentId) : jsonStack.parentId != null) {
            return false;
        }
        if (dates != null ? !dates.equals(jsonStack.dates) : jsonStack.dates != null) {
            return false;
        }
        return labels != null ? labels.equals(jsonStack.labels) : jsonStack.labels == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (summary != null ? summary.hashCode() : 0);
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        result = 31 * result + (dates != null ? dates.hashCode() : 0);
        result = 31 * result + (labels != null ? labels.hashCode() : 0);
        return result;
    }

    public static class Dates {

        @Json(name = "created")
        public String created;

        @Json(name = "modified")
        public String modified;

        @Json(name = "completed")
        public String completed;

        @Json(name = "deleted")
        public String deleted;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Dates dates = (Dates) o;

            if (created != null ? !created.equals(dates.created) : dates.created != null) {
                return false;
            }
            if (modified != null ? !modified.equals(dates.modified) : dates.modified != null) {
                return false;
            }
            if (completed != null ? !completed.equals(dates.completed) : dates.completed != null) {
                return false;
            }
            return deleted != null ? deleted.equals(dates.deleted) : dates.deleted == null;
        }

        @Override
        public int hashCode() {
            int result = created != null ? created.hashCode() : 0;
            result = 31 * result + (modified != null ? modified.hashCode() : 0);
            result = 31 * result + (completed != null ? completed.hashCode() : 0);
            result = 31 * result + (deleted != null ? deleted.hashCode() : 0);
            return result;
        }

    }

}
