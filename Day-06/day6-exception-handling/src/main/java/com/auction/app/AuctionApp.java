package com.auction.app;

import com.auction.exception.InsufficientStockException;
import com.auction.exception.InvalidBidException;
import com.auction.exception.InvalidQuantityException;
import com.auction.exception.OrderProcessingException;
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
import com.auction.service.AuditService;
import com.auction.service.AuctionService;
import com.auction.service.OrderProcessor;
import com.auction.strategy.PremiumBidValidationStrategy;
import com.auction.strategy.StandardBidValidationStrategy;

import java.util.Scanner;

/**
 * Main application for Day 6: Exception Handling + AI-Assisted Debugging
 * in the Online Auction System.
 *
 * Demonstrates:
 * 1. Checked exception handling (InsufficientStockException, OrderProcessingException)
 * 2. Unchecked exception handling (InvalidQuantityException, InvalidBidException, NumberFormatException)
 * 3. try, catch, and finally semantics
 * 4. Multi-catch block (catching multiple compatible exceptions)
 * 5. Exception chaining with getCause() inspection
 * 6. Interactive console menu with graceful error recovery (never crashing on bad input or business rule violations)
 * 7. In-memory audit logging through finally blocks on both success and failure paths
 * 8. Reusable domain entities from Days 1-5 (User, Seller, Bidder, AuctionItem, Strategies, Payments)
 */
public class AuctionApp {

    private static final AuditService auditService = new AuditService();
    private static final OrderProcessor orderProcessor = new OrderProcessor(auditService);
    private static final AuctionService standardAuctionService =
            new AuctionService(new StandardBidValidationStrategy(), auditService);
    private static final AuctionService premiumAuctionService =
            new AuctionService(new PremiumBidValidationStrategy(80000.0), auditService);

    private static final Bidder krishna = new Bidder(201, "Krishna", "krishna@auction.com", 75000.0);
    private static final Bidder meera = new Bidder(202, "Meera", "meera@auction.com", 200000.0);
    private static final Seller ravi = new Seller(101, "Ravi", "ravi@auction.com", "Ravi Electronics", 4.9);

    private static final AuctionItem laptop = new AuctionItem(1001, "ThinkPad Laptop", 50000.0);
    private static final AuctionItem watch = new AuctionItem(2001, "Vintage Swiss Watch", 80000.0);
    private static final AuctionItem camera = new AuctionItem(3001, "DSLR Camera Kit", 25000.0);

    static {
        // Seed inventory stock for auction fulfillment items
        orderProcessor.setStock(laptop.getId(), 5);
        orderProcessor.setStock(watch.getId(), 2);
        orderProcessor.setStock(camera.getId(), 10);

        // Seed auction services
        standardAuctionService.addItem(laptop);
        standardAuctionService.addItem(camera);
        premiumAuctionService.addItem(watch);

        // Let Meera have prior bid activity so she qualifies for premium auctions
        meera.incrementBidCount();
    }

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("     ONLINE AUCTION SYSTEM - DAY 6");
        System.out.println("  Exception Handling & AI-Assisted Debugging");
        System.out.println("=================================================");

        // Run non-interactive automated showcase first so any test/direct invocation sees full concepts
        if (args.length > 0 && "--demo".equalsIgnoreCase(args[0])) {
            runAutomatedDemonstration();
            return;
        }

        // Start interactive menu loop
        runInteractiveMenu();
    }

    /**
     * Interactive console menu demonstrating graceful error recovery.
     * The application never crashes due to invalid input or business rule exceptions.
     */
    public static void runInteractiveMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            displayMenu();
            System.out.print("Select an option (0-8): ");
            String input = scanner.nextLine();

            int choice;
            try {
                // Concept: Handling NumberFormatException safely
                choice = Integer.parseInt(input.trim());
            } catch (NumberFormatException nfe) {
                System.out.println("\n[ERROR] Invalid menu selection! '" + input + "' is not a valid number.");
                System.out.println("Please enter a numeric option between 0 and 8.\n");
                continue;
            }

            switch (choice) {
                case 1 -> menuPlaceBid(scanner);
                case 2 -> menuOrderInventoryStock(scanner);
                case 3 -> menuChainedOrderSafe(scanner);
                case 4 -> menuViewInventoryAndAuctions();
                case 5 -> menuViewAuditLog();
                case 6 -> runAutomatedDemonstration();
                case 7 -> demoExceptionChainingDetailed();
                case 8 -> demoRoleAndPaymentHierarchy();
                case 0 -> {
                    System.out.println("\nExiting Online Auction System. Happy Bidding!");
                    running = false;
                }
                default -> System.out.println("\n[WARNING] Choice " + choice + " is out of range. Please choose between 0 and 8.");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n----------------- MAIN MENU -----------------");
        System.out.println(" 1. Place a Bid (InvalidBidException demonstration)");
        System.out.println(" 2. Order/Fulfill Stock (InsufficientStockException demonstration)");
        System.out.println(" 3. Safe Order via Exception Chaining (OrderProcessingException)");
        System.out.println(" 4. View Current Auctions and Inventory");
        System.out.println(" 5. View Audit Trail Log (Recorded via finally)");
        System.out.println(" 6. Run Full Automated Exception Showcase");
        System.out.println(" 7. Deep-Dive Exception Chaining & Multi-Catch Demo");
        System.out.println(" 8. View Days 1-5 Reused Role & Payment Hierarchies");
        System.out.println(" 0. Exit");
        System.out.println("---------------------------------------------");
    }

    /**
     * Menu Action 1: Place a bid.
     * Recovers safely from NumberFormatException, InvalidBidException, and strategy rejections.
     */
    private static void menuPlaceBid(Scanner scanner) {
        System.out.println("\n--- Option 1: Place Bid ---");
        System.out.println("Available items:");
        System.out.println(" 1001. ThinkPad Laptop (Current: INR " + (long) laptop.getCurrentBid() + ")");
        System.out.println(" 3001. DSLR Camera Kit (Current: INR " + (long) camera.getCurrentBid() + ")");
        System.out.println(" 2001. Vintage Swiss Watch [Premium] (Current: INR " + (long) watch.getCurrentBid() + ")");

        try {
            System.out.print("Enter Item ID (1001, 3001, 2001): ");
            int itemId = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Bid Amount in INR: ");
            double amount = Double.parseDouble(scanner.nextLine().trim());

            System.out.println("Select Bidder: 1. Krishna (Budget: INR " + (long) krishna.getMaxBudget()
                    + ") | 2. Meera (Budget: INR " + (long) meera.getMaxBudget() + ")");
            int bidderChoice = Integer.parseInt(scanner.nextLine().trim());
            Bidder chosenBidder = (bidderChoice == 2) ? meera : krishna;

            AuctionService targetService = (itemId == 2001) ? premiumAuctionService : standardAuctionService;
            boolean accepted = targetService.placeBid(itemId, chosenBidder, amount);

            if (accepted) {
                System.out.println(">>> SUCCESS: Bid accepted! New current bid: INR " + amount);
            } else {
                System.out.println(">>> FAILED: Bid was rejected based on auction strategy rules.");
            }

        } catch (NumberFormatException nfe) {
            System.out.println("[RECOVERED] Input parsing error: Please enter valid numbers for ID and amount.");
        } catch (InvalidBidException ibe) {
            // Unchecked custom exception caught and recovered
            System.out.println("[RECOVERED] Business Rule Violation: " + ibe.getMessage());
        } catch (Exception ex) {
            System.out.println("[RECOVERED] Unexpected error during bidding: " + ex.getMessage());
        }
    }

    /**
     * Menu Action 2: Fulfill item stock from OrderProcessor.
     * Demonstrates checked exception InsufficientStockException and unchecked InvalidQuantityException.
     */
    private static void menuOrderInventoryStock(Scanner scanner) {
        System.out.println("\n--- Option 2: Order Stock Fulfillment ---");
        System.out.println("Stock status:");
        System.out.println(" 1001. ThinkPad Laptop (Available: " + orderProcessor.getStock(1001) + ")");
        System.out.println(" 2001. Vintage Swiss Watch (Available: " + orderProcessor.getStock(2001) + ")");
        System.out.println(" 3001. DSLR Camera Kit (Available: " + orderProcessor.getStock(3001) + ")");

        try {
            System.out.print("Enter Item ID to order: ");
            int itemId = Integer.parseInt(scanner.nextLine().trim());

            AuctionItem item = (itemId == 1001) ? laptop : (itemId == 2001 ? watch : camera);

            System.out.print("Enter requested quantity: ");
            int qty = Integer.parseInt(scanner.nextLine().trim());

            int remaining = orderProcessor.processOrder(item, qty);
            System.out.println(">>> SUCCESS: Order fulfilled! Remaining stock for item " + itemId + ": " + remaining);

        } catch (NumberFormatException nfe) {
            System.out.println("[RECOVERED] Input error: Item ID and quantity must be valid integers.");
        } catch (InvalidQuantityException iqe) {
            // Unchecked exception caught cleanly
            System.out.println("[RECOVERED] Business Rule Violation: " + iqe.getMessage());
        } catch (InsufficientStockException ise) {
            // Checked exception caught cleanly
            System.out.println("[RECOVERED] Inventory Rule Violation: " + ise.getMessage());
        }
    }

    /**
     * Menu Action 3: Process order safely using OrderProcessingException with exception chaining.
     */
    private static void menuChainedOrderSafe(Scanner scanner) {
        System.out.println("\n--- Option 3: Process Order with Exception Chaining ---");
        try {
            System.out.print("Enter Item ID (1001, 2001, 3001): ");
            int itemId = Integer.parseInt(scanner.nextLine().trim());
            AuctionItem item = (itemId == 1001) ? laptop : (itemId == 2001 ? watch : camera);

            System.out.print("Enter requested quantity: ");
            int qty = Integer.parseInt(scanner.nextLine().trim());

            int remaining = orderProcessor.processOrderSafely(item, qty);
            System.out.println(">>> SUCCESS: Safe order completed! Remaining stock: " + remaining);

        } catch (NumberFormatException nfe) {
            System.out.println("[RECOVERED] Input error: Expected numeric input.");
        } catch (OrderProcessingException ope) {
            System.out.println("[RECOVERED] High-level OrderProcessingException caught!");
            System.out.println("Message: " + ope.getMessage());
            System.out.println("Original Cause Class: " + (ope.getCause() != null ? ope.getCause().getClass().getSimpleName() : "None"));
            System.out.println("Original Cause Message: " + (ope.getCause() != null ? ope.getCause().getMessage() : "None"));
        }
    }

    private static void menuViewInventoryAndAuctions() {
        System.out.println("\n--- Current Inventory & Auction Status ---");
        System.out.println(laptop.getSummary() + " | In Stock: " + orderProcessor.getStock(laptop.getId()));
        System.out.println(watch.getSummary() + " | In Stock: " + orderProcessor.getStock(watch.getId()));
        System.out.println(camera.getSummary() + " | In Stock: " + orderProcessor.getStock(camera.getId()));
    }

    private static void menuViewAuditLog() {
        System.out.println("\n--- Audit Log Trail (Logged via finally) ---");
        var records = auditService.getRecords();
        if (records.isEmpty()) {
            System.out.println("No audit records recorded yet.");
            return;
        }
        for (var rec : records) {
            System.out.println(rec);
        }
    }

    /**
     * Complete automated demonstration of all Day 6 requirements.
     */
    public static void runAutomatedDemonstration() {
        System.out.println("\n=================================================");
        System.out.println("   AUTOMATED EXCEPTION HANDLING DEMONSTRATION");
        System.out.println("=================================================");

        // 1. Business Rule 1: Invalid bid amount (unchecked InvalidBidException)
        System.out.println("\n[DEMO 1] Enforcing InvalidBidException (Negative or Zero Bid)");
        try {
            System.out.println("Attempting bid with 0.0 amount...");
            standardAuctionService.placeBid(1001, krishna, 0.0);
        } catch (InvalidBidException ex) {
            System.out.println("Caught InvalidBidException: " + ex.getMessage());
        }

        try {
            System.out.println("Attempting bid with -500.0 amount...");
            standardAuctionService.placeBid(1001, krishna, -500.0);
        } catch (InvalidBidException ex) {
            System.out.println("Caught InvalidBidException: " + ex.getMessage());
        }

        // 2. Business Rule 2: OrderProcessor invalid quantity (unchecked InvalidQuantityException)
        System.out.println("\n[DEMO 2] Enforcing InvalidQuantityException (Zero or Negative Quantity)");
        try {
            System.out.println("Attempting order with quantity 0...");
            orderProcessor.processOrder(laptop, 0);
        } catch (InvalidQuantityException ex) {
            System.out.println("Caught InvalidQuantityException: " + ex.getMessage());
        } catch (InsufficientStockException e) {
            System.out.println("Unexpected checked exception: " + e.getMessage());
        }

        // 3. Business Rule 3: Insufficient stock (checked InsufficientStockException)
        System.out.println("\n[DEMO 3] Enforcing InsufficientStockException (Checked Exception)");
        int available = orderProcessor.getStock(watch.getId());
        System.out.println("Current stock for Watch (Item 2001): " + available);
        try {
            System.out.println("Attempting order of " + (available + 5) + " units...");
            orderProcessor.processOrder(watch, available + 5);
        } catch (InsufficientStockException ex) {
            System.out.println("Caught checked InsufficientStockException: " + ex.getMessage());
        }

        // 4. Valid order fulfillment - stock reduces cleanly
        System.out.println("\n[DEMO 4] Valid Order Fulfillment");
        try {
            System.out.println("Ordering 1 unit of Watch (Item 2001)...");
            int remaining = orderProcessor.processOrder(watch, 1);
            System.out.println("Order successful! Remaining stock: " + remaining);
        } catch (InsufficientStockException ex) {
            System.out.println("Order failed: " + ex.getMessage());
        }

        // 5. Multi-catch and Exception Chaining demonstration
        demoExceptionChainingDetailed();
    }

    /**
     * Deep dive showing:
     * - Multi-catch block for compatible exceptions
     * - Wrapping and preserving cause with getCause()
     * - Finally block execution guarantee
     */
    public static void demoExceptionChainingDetailed() {
        System.out.println("\n--- Exception Chaining & Multi-Catch Demonstration ---");

        System.out.println("\nCase A: Wrapping InsufficientStockException into OrderProcessingException");
        try {
            // Asking for 99 items when only a few exist
            orderProcessor.processOrderSafely(watch, 99);
        } catch (OrderProcessingException ope) {
            System.out.println("Top-level exception caught: " + ope.getClass().getSimpleName());
            System.out.println("Top-level message: " + ope.getMessage());
            Throwable cause = ope.getCause();
            System.out.println("Original Cause Class: " + (cause != null ? cause.getClass().getName() : "None"));
            System.out.println("Original Cause Message: " + (cause != null ? cause.getMessage() : "None"));
            System.out.println("Was cause preserved? " + (cause instanceof InsufficientStockException));
        }

        System.out.println("\nCase B: Wrapping InvalidQuantityException into OrderProcessingException");
        try {
            orderProcessor.processOrderSafely(laptop, -10);
        } catch (OrderProcessingException ope) {
            System.out.println("Top-level exception caught: " + ope.getClass().getSimpleName());
            Throwable cause = ope.getCause();
            System.out.println("Original Cause Class: " + (cause != null ? cause.getClass().getName() : "None"));
            System.out.println("Was cause preserved? " + (cause instanceof InvalidQuantityException));
        }

        System.out.println("\nCase C: Demonstration of try-catch-finally control flow");
        boolean finallyRan = false;
        try {
            System.out.println("1. Inside try block: performing an operation that throws");
            throw new IllegalArgumentException("Deliberate test error for finally demonstration");
        } catch (IllegalArgumentException ex) {
            System.out.println("2. Inside catch block: handling " + ex.getMessage());
        } finally {
            finallyRan = true;
            System.out.println("3. Inside finally block: CLEANUP & AUDIT ALWAYS RUNS!");
        }
        System.out.println("Finally block executed as expected: " + finallyRan);
    }

    /**
     * Demonstrates that Days 1-5 domain entities (AuctionRole, Seller, Bidder, Payment)
     * are preserved and reused without conflict.
     */
    private static void demoRoleAndPaymentHierarchy() {
        System.out.println("\n--- Reused Days 1-5 Domain Components ---");

        // Role verification
        for (AuctionRole role : AuctionRole.values()) {
            System.out.println(role.getAuthority() + " | canBid=" + role.canBid() + " | canList=" + role.canListItems());
        }

        // Payment polymorphism
        Payment[] payments = {
            new CardPayment("Krishna", 52000.0, "4242"),
            new UpiPayment("Meera", 90000.0, "meera@upi"),
            new CashPayment("Ravi", 15000.0, "Counter Staff")
        };

        for (Payment p : payments) {
            p.pay();
            if (p instanceof Refundable ref) {
                System.out.println(" -> " + p.getPayerName() + " is refundable up to INR " + (long) ref.getRefundableAmount());
            }
        }
    }
}
