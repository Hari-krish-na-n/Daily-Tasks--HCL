package com.auction.learning.payment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Payment learning exercise.
 *
 * Covers:
 * - Abstract class instantiation restriction
 * - Overridden pay() behavior per subclass
 * - Overloaded pay(String note) in the base class
 * - Refundable interface on card and UPI, but NOT on cash
 * - super() chaining (verified through inherited getters)
 *
 * Day 5 - Task D tests
 */
class PaymentTest {

    @Test
    void cardPayment_pay_returns_receipt_containing_last4digits() {
        CardPayment card = new CardPayment("Krishna", 52000.0, "4242");
        String receipt = card.pay();
        assertTrue(receipt.contains("4242"), "Receipt should mention last 4 digits");
        assertTrue(receipt.contains("52000"), "Receipt should mention amount");
    }

    @Test
    void upiPayment_pay_returns_receipt_containing_upi_id() {
        UpiPayment upi = new UpiPayment("Meera", 90000.0, "meera@upi");
        String receipt = upi.pay();
        assertTrue(receipt.contains("meera@upi"), "Receipt should mention UPI ID");
    }

    @Test
    void cashPayment_pay_returns_receipt_containing_collector_name() {
        CashPayment cash = new CashPayment("Ravi", 15000.0, "Counter Staff");
        String receipt = cash.pay();
        assertTrue(receipt.contains("Counter Staff"));
    }

    @Test
    void overloaded_pay_with_note_appends_to_base_receipt() {
        CardPayment card = new CardPayment("Krishna", 52000.0, "4242");
        String receipt = card.pay("Winning bid");
        assertTrue(receipt.contains("Winning bid"), "Overloaded pay() should include the note");
        assertTrue(receipt.contains("4242"), "Overloaded pay() should still include card info");
    }

    @Test
    void card_and_upi_are_refundable_but_cash_is_not() {
        Payment card = new CardPayment("Krishna", 52000.0, "4242");
        Payment upi  = new UpiPayment("Meera", 90000.0, "meera@upi");
        Payment cash = new CashPayment("Ravi", 15000.0, "Counter Staff");

        assertInstanceOf(Refundable.class, card, "CardPayment should be Refundable");
        assertInstanceOf(Refundable.class, upi,  "UpiPayment should be Refundable");
        assertFalse(cash instanceof Refundable,   "CashPayment should NOT be Refundable");
    }

    @Test
    void cardPayment_refund_reduces_refundable_amount() {
        CardPayment card = new CardPayment("Krishna", 52000.0, "4242");
        card.pay(); // must pay before refunding

        double beforeRefund = card.getRefundableAmount();
        card.refund(10000.0);
        double afterRefund = card.getRefundableAmount();

        assertEquals(beforeRefund - 10000.0, afterRefund, 0.01);
    }

    @Test
    void constructor_chaining_populates_payer_name_from_super() {
        // Verifies that super(payerName, amount) in CardPayment sets fields in Payment
        CardPayment card = new CardPayment("Krishna", 52000.0, "4242");
        assertEquals("Krishna", card.getPayerName());
        assertEquals(52000.0, card.getAmount(), 0.01);
    }

    @Test
    void runtime_polymorphism_calls_correct_pay_per_subclass() {
        Payment[] payments = {
            new CardPayment("A", 1000.0, "1111"),
            new UpiPayment("B", 2000.0, "b@upi"),
            new CashPayment("C", 3000.0, "Staff")
        };

        // Each pay() is dispatched to a different class at runtime
        String cardReceipt = payments[0].pay();
        String upiReceipt  = payments[1].pay();
        String cashReceipt = payments[2].pay();

        assertTrue(cardReceipt.contains("1111"));
        assertTrue(upiReceipt.contains("b@upi"));
        assertTrue(cashReceipt.contains("Staff"));
    }

    @Test
    void payment_amount_must_be_positive() {
        assertThrows(IllegalArgumentException.class,
                () -> new CardPayment("X", -100.0, "9999"));
        assertThrows(IllegalArgumentException.class,
                () -> new CashPayment("Y", 0.0, "Staff"));
    }

    @Test
    void isPaid_false_before_pay_and_true_after() {
        CardPayment card = new CardPayment("Krishna", 52000.0, "4242");
        assertFalse(card.isPaid(), "Should not be paid before pay() is called");
        card.pay();
        assertTrue(card.isPaid(), "Should be paid after pay() is called");
    }
}
