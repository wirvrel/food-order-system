package com.zizen.foodorder.persistence.exception;

import java.util.Set;

public class EntityArgumentException extends IllegalArgumentException {

    private final Set<String> errors;

    public EntityArgumentException(Set<String> errors) {
        this.errors = errors;
    }

    public Set<String> getErrors() {
        return errors;
    }
}
