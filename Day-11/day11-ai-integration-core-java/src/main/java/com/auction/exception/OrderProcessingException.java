package com.auction.exception;

/**
 * Higher-level checked exception representing a failure in processing an order or auction action.
 *
 * Demonstrates:
 * - Exception chaining: wrapping lower-level checked or unchecked exceptions
 *   (e.g., InsufficientStockException, InvalidQuantityException, or parse errors)
 *   while preserving the original cause via Throwable cause and getCause().
 */
public class OrderProcessingException extends Exception {

    public OrderProcessingException(String message) {
        super(message);
    }

    public OrderProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderProcessingException(Throwable cause) {
        super(cause);
    }
}
