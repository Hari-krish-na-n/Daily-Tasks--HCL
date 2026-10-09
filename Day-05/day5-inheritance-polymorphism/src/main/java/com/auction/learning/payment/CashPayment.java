package com.auction.learning.payment;

/**
 * CashPayment is a mock cash-based payment.
 * Cash does NOT implement Refundable because once cash is handed over
 * the platform cannot automatically reverse it.
 *
 * This shows that NOT every class needs to implement every interface.
 * The Refundable interface is optional - only card and UPI support it.
 *
 * Demonstrates:
 * - Extending abstract class without implementing the optional interface
 * - @Override on pay()
 * - super() constructor chaining
 *
 * Day 5 - Task D: Payment Learning Exercise
 */
public class CashPayment extends Payment {

    private final String collectedBy; // name of the person who collected cash

    public CashPayment(String payerName, double amount, String collectedBy) {
        super(payerName, amount); // constructor chaining up to Payment
        this.collectedBy = collectedBy;
    }

    /**
     * Overrides the abstract pay() from Payment.
     * Cash payment is simply recorded - no gateway call.
     */
    @Override
    public String pay() {
        markPaid();
        String receipt = "CashPayment: INR " + (long) amount
                + " collected in cash by " + collectedBy;
        System.out.println(receipt);
        return receipt;
    }

    @Override
    public String toString() {
        return "CashPayment[payer=" + getPayerName()
                + ", amount=" + amount + ", collectedBy=" + collectedBy + "]";
    }
}
