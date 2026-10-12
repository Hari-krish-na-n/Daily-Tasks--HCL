package com.auction.learning.payment;

/**
 * CardPayment is a mock card-based payment.
 * Implements Refundable because card transactions can be reversed.
 *
 * Demonstrates:
 * - Extending an abstract class AND implementing an interface (multiple types)
 * - Constructor chaining using super(payerName, amount)
 * - Overriding the abstract pay() method with @Override
 * - Implementing the Refundable contract
 *
 * MOCK ONLY: last4digits is for display purposes only. No real card data is stored.
 *
 * Day 5 - Task D: Payment Learning Exercise
 */
public class CardPayment extends Payment implements Refundable {

    // Only the last 4 digits are stored, never the full card number
    private final String last4Digits;
    private double refundedSoFar;

    public CardPayment(String payerName, double amount, String last4Digits) {
        super(payerName, amount); // constructor chaining - passes fields up to Payment
        this.last4Digits = last4Digits;
        this.refundedSoFar = 0.0;
    }

    /**
     * Overrides the abstract pay() from Payment.
     * The @Override annotation tells the compiler we intend to override, not create a new method.
     */
    @Override
    public String pay() {
        markPaid(); // calls the protected method inherited from Payment
        String receipt = "CardPayment: INR " + (long) amount
                + " charged to card ending " + last4Digits;
        System.out.println(receipt);
        return receipt;
    }

    /** Implements refund() from the Refundable interface. */
    @Override
    public void refund(double refundAmount) {
        if (refundAmount <= 0 || refundAmount > getRefundableAmount()) {
            System.out.println("[Card] Refund rejected: invalid amount INR " + (long) refundAmount);
            return;
        }
        refundedSoFar += refundAmount;
        System.out.println("[Card] Refunded INR " + (long) refundAmount
                + " to card ending " + last4Digits);
    }

    /** Implements getRefundableAmount() from the Refundable interface. */
    @Override
    public double getRefundableAmount() {
        return amount - refundedSoFar;
    }

    @Override
    public String toString() {
        return "CardPayment[payer=" + getPayerName()
                + ", amount=" + amount + ", card=****" + last4Digits + "]";
    }
}
