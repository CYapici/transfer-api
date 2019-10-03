package com.cy.transferapi.exception;

public class EntityCreationException extends RuntimeException {

    private static final long serialVersionUID = 5529313319738191205L;

    public EntityCreationException(String message) {
        super(message);
    }

    public EntityCreationException(Throwable cause) {
        super(cause);
    }

}
