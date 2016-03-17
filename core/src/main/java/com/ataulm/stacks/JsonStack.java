package com.ataulm.stacks;

import com.squareup.moshi.Json;

public class JsonStack {

    @Json(name = "id")
    public String id;

    @Json(name = "summary")
    public String summary;

    @Json(name = "parent")
    public String parentId;

    @Json(name = "desc")
    public String description;

}
