package com.app.productservice.exceptions;
import java.util.UUID;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(UUID id) {
        super("Object " + id + " not found");
    }
}
