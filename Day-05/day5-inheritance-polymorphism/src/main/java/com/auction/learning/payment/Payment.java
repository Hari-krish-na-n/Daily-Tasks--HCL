package com.auction.learning.payment;

/**
 * Payment is an abstract class representing a mock payment in the auction system.
 *
 * WHY abstract class instead of interface?
 * - Payment has shared state (payerName, amount, paid) that all subclasses need.
 * - Abstract classes can have constructors, instance fields, and concrete methods.
 * - Interfaces cannot have constructors or mutable instance state.
 * - Use an abstract class when subclasses genuinely share implementation, not just a contract.
 *
 * Demonstrates:
 * - Abstract class with constructors (subclasses chain using super())
 * - Abstract method pay() - every subclass must override it
 * - Concrete method printReceipt() - shared by all subclasses
 * - Method overloading: pay() and pay(String note) exist side by side
 *
 * Day 5 - Task D: Payment Learning Exercise
 *
 * MOCK ONLY: No real card details, bank calls, or credentials are used.
 */
public abstract class Payment {

    private final String payerName;
    protected double amount;
    private boolean paid;

    // Constructor: every subclass must call super(payerName, amount)
    protected Payment(String payerName, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero.");
        }
        this.payerName = payerName;
        this.amount = amount;
        this.paid = false;
    }

    /**
     * Abstract method: each subclass defines HOW payment is processed.
     * The return value is a simple mock receipt string.
     *
     * Demonstrates method OVERRIDING - subclasses must use @Override.
     */
    public abstract String pay();

    /**
     * Overloaded pay(): same name, different parameter.
     * This adds an optional note to the payment receipt.
     *
     * Demonstrates method OVERLOADING - same method name, different signature.
     */
    public String pay(String note) {
        String receipt = pay(); // calls the overridden version in the subclass
        return receipt + " | Note: " + note;
    }

    /**
     * Concrete shared method: all payment types print the same receipt format.
     * Subclasses do NOT override this - they inherit it as-is.
     */
    public void printReceipt() {
        System.out.println("-------- Payment Receipt --------");
        System.out.println("Payer  : " + payerName);
        System.out.println("Amount : INR " + (long) amount);
        System.out.println("Status : " + (paid ? "PAID" : "PENDING"));
        System.out.println("---------------------------------");
    }

    public String getPayerName() {
        return payerName;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isPaid() {
        return paid;
    }

    // Protected: only subclasses can mark the payment as paid
    protected void markPaid() {
        this.paid = true;
    }
}
