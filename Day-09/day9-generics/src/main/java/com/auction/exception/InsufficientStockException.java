package com.auction.exception;

/**
 * Custom checked exception thrown when an inventory or item quantity
 * cannot fulfill a requested purchase, reservation, or dispatch order.
 *
 * Demonstrates:
 * - Extending Exception to create a checked exception.
 * - Enforcing handling via try-catch or method signature throws.
 * - Exception chaining constructors (message, cause, message + cause).
 */
public class InsufficientStockException extends Exception {

    public InsufficientStockException(String message) {
        super(message);
    }

    public InsufficientStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsufficientStockException(Throwable cause) {
        super(cause);
    }
}
