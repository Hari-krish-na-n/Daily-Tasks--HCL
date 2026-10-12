package com.auction.exception;

/**
 * Custom unchecked exception thrown when an invalid quantity (zero or negative)
 * is passed into an inventory or ordering operation.
 *
 * Demonstrates:
 * - Extending RuntimeException to create an unchecked exception.
 * - Exception chaining constructors (message, cause, message + cause).
 */
public class InvalidQuantityException extends RuntimeException {

    public InvalidQuantityException(String message) {
        super(message);
    }

    public InvalidQuantityException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidQuantityException(Throwable cause) {
        super(cause);
    }
}
