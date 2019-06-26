package io.openliberty.core.article;

import java.io.Serializable;
import java.util.UUID;

public class Tag implements Serializable {
    private String id;
    private String name;

    public Tag(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }
}
