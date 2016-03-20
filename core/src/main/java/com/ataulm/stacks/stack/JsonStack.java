package com.ataulm.stacks.stack;

import com.squareup.moshi.Json;

public class JsonStack {

    @Json(name = "id")
    public String id;

    @Json(name = "summary")
    public String summary;

    @Json(name = "parent")
    public String parentId;

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

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (summary != null ? summary.hashCode() : 0);
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        return result;
    }

}
