package com.auction.learning.payment;

/**
 * Refundable is an interface that marks a payment type as refundable.
 *
 * Not all payment types support refunds (e.g. cash may not).
 * Using an interface here means we can check "is this payment refundable?"
 * at runtime without knowing the concrete type.
 *
 * Demonstrates:
 * - Interface definition (separate from abstract class)
 * - Interface-based design: code to the contract, not the implementation
 *
 * Day 5 - Task D: Payment Learning Exercise
 */
public interface Refundable {

    /**
     * Processes a refund for the given amount.
     * @param amount the amount to refund (must be > 0)
     */
    void refund(double amount);

    /**
     * Returns the maximum amount that can be refunded.
     */
    double getRefundableAmount();
}
