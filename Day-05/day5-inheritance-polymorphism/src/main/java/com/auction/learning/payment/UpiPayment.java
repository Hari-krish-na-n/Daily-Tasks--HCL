package com.auction.learning.payment;

/**
 * UpiPayment is a mock UPI-based payment (e.g., Google Pay, PhonePe style).
 * Also implements Refundable since UPI supports reversals.
 *
 * Demonstrates:
 * - Extending abstract class with super() chaining
 * - @Override on pay()
 * - Implementing the Refundable interface
 *
 * MOCK ONLY: No real UPI transaction or VPA validation happens here.
 *
 * Day 5 - Task D: Payment Learning Exercise
 */
public class UpiPayment extends Payment implements Refundable {

    private final String upiId;   // e.g. "user@upi" - mock only
    private double refundedSoFar;

    public UpiPayment(String payerName, double amount, String upiId) {
        super(payerName, amount); // chains up to Payment constructor
        this.upiId = upiId;
        this.refundedSoFar = 0.0;
    }

    /**
     * Overrides the abstract pay() from Payment.
     */
    @Override
    public String pay() {
        markPaid();
        String receipt = "UpiPayment: INR " + (long) amount
                + " sent via UPI ID " + upiId;
        System.out.println(receipt);
        return receipt;
    }

    @Override
    public void refund(double refundAmount) {
        if (refundAmount <= 0 || refundAmount > getRefundableAmount()) {
            System.out.println("[UPI] Refund rejected: invalid amount INR " + (long) refundAmount);
            return;
        }
        refundedSoFar += refundAmount;
        System.out.println("[UPI] Refunded INR " + (long) refundAmount
                + " to UPI ID " + upiId);
    }

    @Override
    public double getRefundableAmount() {
        return amount - refundedSoFar;
    }

    @Override
    public String toString() {
        return "UpiPayment[payer=" + getPayerName()
                + ", amount=" + amount + ", upiId=" + upiId + "]";
    }
}
