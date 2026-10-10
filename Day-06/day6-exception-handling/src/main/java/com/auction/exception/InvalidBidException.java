package com.auction.exception;

/**
 * Custom unchecked exception thrown when an invalid bid amount is submitted
 * (e.g. zero, negative, or not meeting basic validation rules).
 *
 * Demonstrates:
 * - Extending RuntimeException for business validation of bids.
 * - Exception chaining constructors (message, cause).
 */
public class InvalidBidException extends RuntimeException {

    public InvalidBidException(String message) {
        super(message);
    }

    public InvalidBidException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidBidException(Throwable cause) {
        super(cause);
    }
}
