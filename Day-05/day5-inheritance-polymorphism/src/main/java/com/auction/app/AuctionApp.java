package com.auction.app;

import com.auction.learning.payment.CardPayment;
import com.auction.learning.payment.CashPayment;
import com.auction.learning.payment.Payment;
import com.auction.learning.payment.Refundable;
import com.auction.learning.payment.UpiPayment;
import com.auction.model.AuctionItem;
import com.auction.model.AuctionRole;
import com.auction.model.Bidder;
import com.auction.model.Seller;
import com.auction.model.User;
import com.auction.service.AuctionService;
import com.auction.strategy.PremiumBidValidationStrategy;
import com.auction.strategy.StandardBidValidationStrategy;

/**
 * Main application demonstrating all Day 5 OOP concepts:
 *
 * Task A - Inheritance and BaseEntity
 * Task B - Role Hierarchy (AuctionRole enum)
 * Task C - Strategy Pattern (BidValidationStrategy)
 * Task D - Payment Learning Exercise
 *
 * Run: mvn compile exec:java -Dexec.mainClass="com.auction.app.AuctionApp"
 */
public class AuctionApp {

    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("  ONLINE AUCTION SYSTEM - DAY 5");
        System.out.println("  Inheritance, Polymorphism & Strategy");
        System.out.println("=========================================");

        demoInheritanceAndBaseEntity();
        demoRoleHierarchy();
        demoStrategyPattern();
        demoPaymentHierarchy();
    }

    // -----------------------------------------------------------------------
    // Task A: Inheritance and BaseEntity
    // -----------------------------------------------------------------------
    private static void demoInheritanceAndBaseEntity() {
        System.out.println("\n--- Task A: Inheritance & BaseEntity ---");

        // Seller and Bidder extend User, User extends BaseEntity
        Seller seller = new Seller(101, "Ravi", "ravi@auction.com", "Ravi Electronics", 4.9);
        Bidder bidder = new Bidder(201, "Krishna", "krishna@auction.com", 75000.0);
        AuctionItem item = new AuctionItem(1001, "Vintage Camera", 15000.0);

        // All three have getId() and getCreatedAt() inherited from BaseEntity
        System.out.println("Seller ID (from BaseEntity): " + seller.getId());
        System.out.println("Bidder ID (from BaseEntity): " + bidder.getId());
        System.out.println("Item ID   (from BaseEntity): " + item.getId());
        System.out.println("Item created at            : " + item.getCreatedAt());

        // getSummary() is abstract in BaseEntity - each subclass overrides it differently
        // The variable type is the parent, but the method called is from the subclass
        // This is RUNTIME POLYMORPHISM
        User[] users = { seller, bidder };
        System.out.println("\nRuntime polymorphism via getSummary():");
        for (User u : users) {
            // Compiler sees User, but JVM calls Seller.getSummary() or Bidder.getSummary()
            System.out.println("  " + u.getSummary());
        }
    }

    // -----------------------------------------------------------------------
    // Task B: Role Hierarchy
    // -----------------------------------------------------------------------
    private static void demoRoleHierarchy() {
        System.out.println("\n--- Task B: Role Hierarchy (AuctionRole enum) ---");

        // Enum constants each override canBid, canListItems, getDescription
        for (AuctionRole role : AuctionRole.values()) {
            System.out.println(role.getAuthority()
                    + " | canBid=" + role.canBid()
                    + " | canList=" + role.canListItems()
                    + " | " + role.getDescription());
        }

        // Access control check
        Bidder bidder = new Bidder(201, "Krishna", "krishna@auction.com");
        System.out.println("\n" + bidder.getName() + " authority: " + bidder.getAuthority());
        System.out.println("Can bid? " + bidder.getRole().canBid());
    }

    // -----------------------------------------------------------------------
    // Task C: Strategy Pattern - BidValidationStrategy
    // -----------------------------------------------------------------------
    private static void demoStrategyPattern() {
        System.out.println("\n--- Task C: Strategy Pattern ---");

        Seller seller = new Seller(101, "Ravi", "ravi@auction.com");
        Bidder newBidder = new Bidder(201, "Krishna", "krishna@auction.com", 75000.0);
        Bidder verifiedBidder = new Bidder(202, "Meera", "meera@auction.com", 200000.0);
        // Simulate Meera having bid before
        verifiedBidder.incrementBidCount();

        AuctionItem regularItem = new AuctionItem(1001, "Laptop", 50000.0);
        AuctionItem premiumItem = new AuctionItem(2001, "Vintage Watch", 80000.0);

        // --- Standard Strategy ---
        System.out.println("\nUsing StandardBidValidationStrategy:");
        AuctionService standardService = new AuctionService(new StandardBidValidationStrategy());
        standardService.addItem(regularItem);

        standardService.placeBid(1001, newBidder, 49000.0);  // below minimum - rejected
        standardService.placeBid(1001, newBidder, 52000.0);  // valid - accepted

        // --- Premium Strategy (swapped at runtime - same method call, different behavior) ---
        System.out.println("\nUsing PremiumBidValidationStrategy (floor=80000):");
        AuctionService premiumService = new AuctionService(new PremiumBidValidationStrategy(80000.0));
        premiumService.addItem(premiumItem);

        premiumService.placeBid(2001, newBidder, 90000.0);   // rejected: no prior bids
        premiumService.placeBid(2001, verifiedBidder, 85000.0); // rejected: below 5% increment
        premiumService.placeBid(2001, verifiedBidder, 90000.0); // accepted

        System.out.println("\nActive strategy (standard): " + standardService.getActiveStrategyName());
        System.out.println("Active strategy (premium) : " + premiumService.getActiveStrategyName());
    }

    // -----------------------------------------------------------------------
    // Task D: Payment Learning Exercise
    // -----------------------------------------------------------------------
    private static void demoPaymentHierarchy() {
        System.out.println("\n--- Task D: Payment Hierarchy (Learning Exercise) ---");

        // Runtime polymorphism: Payment reference holds CardPayment, UpiPayment, CashPayment
        Payment[] payments = {
            new CardPayment("Krishna", 52000.0, "4242"),
            new UpiPayment("Meera", 90000.0, "meera@upi"),
            new CashPayment("Ravi", 15000.0, "Counter Staff")
        };

        System.out.println("\nProcessing all payments via Payment reference (runtime polymorphism):");
        for (Payment p : payments) {
            p.pay(); // JVM calls the correct overridden pay() at runtime
        }

        System.out.println("\nMethod Overloading - pay(String note):");
        Payment card = new CardPayment("Krishna", 52000.0, "4242");
        System.out.println(card.pay("Winning bid for Laptop"));

        System.out.println("\nRefund capability check (Refundable interface):");
        for (Payment p : payments) {
            if (p instanceof Refundable refundable) {
                System.out.println(p.getPayerName() + " is refundable. Max refund: INR "
                        + (long) refundable.getRefundableAmount());
                // Note: we called pay() in the loop above so we need fresh objects for refund
            } else {
                System.out.println(p.getPayerName() + " - cash payment is not refundable.");
            }
        }

        System.out.println("\nPrint receipt (inherited concrete method from Payment):");
        Payment upi = new UpiPayment("Meera", 90000.0, "meera@upi");
        upi.pay();
        upi.printReceipt();
    }
}
