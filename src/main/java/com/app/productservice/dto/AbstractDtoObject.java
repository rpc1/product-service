package com.app.productservice.dto;

import javax.validation.constraints.Size;
import java.util.UUID;

public class AbstractDtoObject {

    private UUID id;

    @Size(max=1025)
    private String title;

    private boolean deleted;

    public AbstractDtoObject(UUID id, String title) {
        this.id = id;
        this.title = title;
    }

    public  AbstractDtoObject() {

    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
