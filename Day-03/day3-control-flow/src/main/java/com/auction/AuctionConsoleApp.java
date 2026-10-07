package com.auction;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * AuctionConsoleApp.java
 * -----------------------------------------------------------
 * Day 3 — Control Flow + Maven Training
 *
 * Core Control-Flow Concepts Demonstrated:
 *  1. if / else / else-if (role validation, bid increment check, reserve price check)
 *  2. switch (main menu dispatch, admin moderation decisions)
 *  3. while (input validation and retry loops, proxy bidding increments)
 *  4. do-while (main console menu loop guaranteed to execute at least once)
 *  5. for (traditional indexed iteration over auction bid ledgers)
 *  6. enhanced for (collection iteration for listings and mini-statements)
 *  7. break (exiting 3-attempt PIN security loop upon success, switch cases)
 *  8. continue (skipping non-approved/rejected auction records during audit)
 *  9. labelled break (nested search exiting outer loop immediately upon match)
 * 10. safe input validation (resilient against non-numeric tokens, bounds, empty lines)
 *
 * Domain: Online Auction System (Java 21, In-Memory Data)
 */
public class AuctionConsoleApp {

    // In-memory collection of active auctions
    private static final List<Auction> auctions = new ArrayList<>();
    private static int nextAuctionId = 104;

    static {
        // Initialize in-memory sample data (Requirement 15)
        Auction a1 = new Auction(101, "iPhone 15", "Ravi", 50000.0, 60000.0);
        a1.addBid(new Bid("Krishna", 50000.0, "10:00 AM"));
        a1.addBid(new Bid("Vijay", 52000.0, "10:15 AM"));

        Auction a2 = new Auction(102, "Gaming Laptop", "Arun", 70000.0, 80000.0);
        a2.addBid(new Bid("Suresh", 70000.0, "11:00 AM"));

        Auction a3 = new Auction(103, "DSLR Camera", "Kumar", 40000.0, 45000.0);
        a3.addBid(new Bid("Ramesh", 40000.0, "09:30 AM"));

        auctions.add(a1);
        auctions.add(a2);
        auctions.add(a3);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String env = System.getProperty("app.environment", "development");

        System.out.println("========================================");
        System.out.println("       ONLINE AUCTION SYSTEM - DAY 3    ");
        System.out.println("       Active Profile: " + env.toUpperCase());
        System.out.println("========================================");

        int choice;

        /*
         * CONCEPT 4: DO-WHILE LOOP
         * The do-while loop ensures the main menu displays at least once.
         * The application keeps displaying the menu until the user selects 0.
         */
        do {
            displayMenu();
            choice = readValidatedMenuChoice(scanner);

            /*
             * CONCEPT 2: SWITCH STATEMENT
             * Normal switch syntax routing choices 0-8 to the corresponding
             * Functional Requirement handlers.
             */
            switch (choice) {
                case 1:
                    // FR1 — User Authentication
                    handleUserRegistrationAndLogin(scanner);
                    break;

                case 2:
                    // FR2 — Auction Listing
                    handleCreateAuctionListing(scanner);
                    break;

                case 3:
                    // FR3 — Admin Moderation
                    handleAdminModeration(scanner);
                    break;

                case 4:
                    // FR4 — Bidding
                    handlePlaceBid(scanner);
                    break;

                case 5:
                    // FR5 — Proxy / Auto Bidding
                    handleProxyBidding(scanner);
                    break;

                case 6:
                    // FR6 — Anti-Sniping
                    handleAntiSniping(scanner);
                    break;

                case 7:
                    // FR7 — Closing & Winner Declaration
                    handleCloseAuctionAndDeclareWinner(scanner);
                    break;

                case 8:
                    // FR8 — Settlement & Shipment
                    handleSettlementAndShipment(scanner);
                    break;

                case 0:
                    // Exit Application
                    System.out.println("\nThank you for using Online Auction System.");
                    break;

                default:
                    // Fallback for unexpected inputs
                    System.out.println("\nInvalid choice. Please choose between 0 and 8.");
                    break;
            }

            if (choice != 0) {
                System.out.println("\nPress Enter to return to main menu...");
                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                }
            }

        } while (choice != 0);

        scanner.close();
    }

    // =========================================================================
    // MENU DISPLAY & INPUT VALIDATION
    // =========================================================================

    private static void displayMenu() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("       ONLINE AUCTION SYSTEM            ");
        System.out.println("========================================");
        System.out.println("1. User Registration / Login");
        System.out.println("2. Create Auction Listing");
        System.out.println("3. Admin Approve / Reject Listing");
        System.out.println("4. Place Bid");
        System.out.println("5. Proxy / Auto Bidding");
        System.out.println("6. Anti-Sniping");
        System.out.println("7. Close Auction & Declare Winner");
        System.out.println("8. Settlement & Shipment");
        System.out.println("0. Exit");
        System.out.println("========================================");
        System.out.print("Enter your choice: ");
    }

    /**
     * CONCEPTS 3 & 14: WHILE LOOP & INPUT VALIDATION
     * Safely reads the user menu selection.
     * Prevents crashing when user enters text like 'abc', negative numbers,
     * out-of-range choices, empty strings, or EOF.
     */
    public static int readValidatedMenuChoice(Scanner scanner) {
        while (true) {
            if (!scanner.hasNextLine()) {
                return 0; // Graceful exit on end-of-stream
            }
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.print("Input cannot be empty. Enter choice (0-8): ");
                continue;
            }

            try {
                int choice = Integer.parseInt(input);

                /*
                 * WHILE validation condition check
                 */
                if (choice >= 0 && choice <= 8) {
                    return choice;
                } else {
                    System.out.print("Invalid choice. Enter 0-8: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    public static double readPositiveDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return 0.0;
            }
            String input = scanner.nextLine().trim();
            try {
                double val = Double.parseDouble(input);
                if (val > 0) {
                    return val;
                } else {
                    System.out.println("Invalid amount. Must be greater than 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    public static int readPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return 0;
            }
            String input = scanner.nextLine().trim();
            try {
                int val = Integer.parseInt(input);
                if (val > 0) {
                    return val;
                } else {
                    System.out.println("Invalid number. Must be greater than 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter an integer.");
            }
        }
    }

    // =========================================================================
    // FR1 — USER REGISTRATION / LOGIN
    // =========================================================================

    /**
     * Demonstrates:
     * - IF / ELSE IF / ELSE for role authorization
     * - BREAK statement in a 3-attempt PIN security loop
     */
    private static void handleUserRegistrationAndLogin(Scanner scanner) {
        System.out.println("\n----------------------------------------");
        System.out.println("  FR1 -- User Registration / Login      ");
        System.out.println("----------------------------------------");
        System.out.println("1. Seller");
        System.out.println("2. Bidder");
        System.out.println("3. Admin");
        System.out.print("Select role (1-3): ");

        int roleChoice = readPositiveInt(scanner, "");
        String roleName;

        /*
         * CONCEPT 1: IF / ELSE IF / ELSE
         * Validates role permissions
         */
        if (roleChoice == 1) {
            roleName = "SELLER";
            System.out.println("Role selected: SELLER (Permissions: Create Listings, Manage Inventory)");
        } else if (roleChoice == 2) {
            roleName = "BIDDER";
            System.out.println("Role selected: BIDDER (Permissions: Browse Auctions, Place Bids)");
        } else if (roleChoice == 3) {
            roleName = "ADMIN";
            System.out.println("Role selected: ADMIN (Permissions: Approve/Reject Listings, Moderate System)");
        } else {
            System.out.println("Invalid role selected. Defaulting to Guest BIDDER.");
            roleName = "BIDDER (Guest)";
        }

        /*
         * CONCEPT 7: BREAK STATEMENT
         * Login / PIN security check with maximum 3 login attempts.
         * Exits the loop immediately with 'break' once correct PIN is entered.
         */
        final int correctPin = AuctionConstants.DEFAULT_DEMO_PIN; // 1234
        boolean authenticated = false;

        System.out.println("\nEnter PIN (Maximum " + AuctionConstants.MAX_LOGIN_ATTEMPTS + " attempts, Demo PIN: 1234):");

        for (int attempt = 1; attempt <= AuctionConstants.MAX_LOGIN_ATTEMPTS; attempt++) {
            System.out.println("Attempt " + attempt);
            System.out.print("Enter PIN: ");

            if (!scanner.hasNextLine()) {
                break;
            }
            String line = scanner.nextLine().trim();
            int enteredPin;
            try {
                enteredPin = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid PIN format. Digits only.");
                continue;
            }

            if (enteredPin == correctPin) {
                System.out.println("Login successful!");
                authenticated = true;
                break; // CONCEPT 7: BREAK immediately exits the loop upon success
            } else {
                System.out.println("Incorrect PIN.");
            }
        }

        if (authenticated) {
            System.out.println("Access granted as " + roleName + ".");
        } else {
            System.out.println("\nMaximum attempts reached.");
            System.out.println("Access denied.");
        }
    }

    // =========================================================================
    // FR2 — CREATE AUCTION LISTING
    // =========================================================================

    /**
     * Demonstrates:
     * - IF / ELSE for positive starting and reserve price validation
     * - WHILE loop for user input gathering
     */
    private static void handleCreateAuctionListing(Scanner scanner) {
        System.out.println("\n----------------------------------------");
        System.out.println("  FR2 -- Create Auction Listing         ");
        System.out.println("----------------------------------------");

        System.out.print("Product name: ");
        String productName = scanner.nextLine().trim();
        while (productName.isEmpty()) {
            System.out.print("Product name cannot be empty. Enter product name: ");
            productName = scanner.nextLine().trim();
        }

        System.out.print("Seller name: ");
        String sellerName = scanner.nextLine().trim();
        if (sellerName.isEmpty()) {
            sellerName = "Self";
        }

        double startingPrice = readPositiveDouble(scanner, "Starting price: ");

        /*
         * CONCEPT 1: IF / ELSE
         * Validates minimum starting price
         */
        if (startingPrice < AuctionConstants.MINIMUM_STARTING_PRICE) {
            System.out.printf("Notice: Starting price adjusted to minimum required ₹%,.2f%n",
                    AuctionConstants.MINIMUM_STARTING_PRICE);
            startingPrice = AuctionConstants.MINIMUM_STARTING_PRICE;
        }

        double reservePrice = readPositiveDouble(scanner, "Reserve price: ");

        /*
         * CONCEPT 1: IF / ELSE
         * Reserve price validation: must be >= starting price
         */
        if (reservePrice < startingPrice) {
            System.out.println("Notice: Reserve price cannot be less than starting price. Adjusted to match starting price.");
            reservePrice = startingPrice;
        }

        Auction newListing = new Auction(nextAuctionId++, productName, sellerName, startingPrice, reservePrice);
        newListing.setStatus("PENDING"); // Pending admin approval
        auctions.add(newListing);

        System.out.println("\nAuction created successfully.");
        System.out.printf("Auction ID: %d | Product: %s | Starting: ₹%,.2f | Reserve: ₹%,.2f | Status: PENDING%n",
                newListing.getId(), newListing.getProductName(), newListing.getStartingPrice(), newListing.getReservePrice());
    }

    // =========================================================================
    // FR3 — ADMIN MODERATION (APPROVE / REJECT)
    // =========================================================================

    /**
     * Demonstrates:
     * - ENHANCED FOR loop for browsing pending listings
     * - SWITCH statement for moderation actions
     */
    private static void handleAdminModeration(Scanner scanner) {
        System.out.println("\n----------------------------------------");
        System.out.println("  FR3 -- Admin Approve / Reject Listing ");
        System.out.println("----------------------------------------");

        System.out.println("Current Auction Listings:");
        for (Auction auction : auctions) {
            System.out.printf("  [%d] %-16s | Seller: %-8s | Price: ₹%,.0f | Status: %s%n",
                    auction.getId(), auction.getProductName(), auction.getSellerName(),
                    auction.getCurrentBid(), auction.getStatus());
        }

        int targetId = readPositiveInt(scanner, "\nEnter Auction ID to moderate: ");
        Auction target = findAuctionById(targetId);

        if (target == null) {
            System.out.println("Auction ID " + targetId + " not found.");
            return;
        }

        System.out.println("\nAdmin Moderation:");
        System.out.println("1. Approve");
        System.out.println("2. Reject");
        int modChoice = readPositiveInt(scanner, "Select action (1-2): ");

        /*
         * CONCEPT 2: SWITCH STATEMENT
         * Handles admin decision
         */
        switch (modChoice) {
            case 1:
                target.setStatus("APPROVED");
                System.out.println("Auction " + targetId + " approved.");
                break;
            case 2:
                target.setStatus("REJECTED");
                System.out.println("Auction " + targetId + " rejected.");
                break;
            default:
                System.out.println("Invalid moderation choice.");
                break;
        }
    }

    // =========================================================================
    // FR4 — PLACE BID
    // =========================================================================

    /**
     * Demonstrates:
     * - IF / ELSE bid validation (bid >= current + increment)
     * - ENHANCED FOR loop mini statement
     */
    private static void handlePlaceBid(Scanner scanner) {
        System.out.println("\n----------------------------------------");
        System.out.println("  FR4 -- Place Bid                      ");
        System.out.println("----------------------------------------");

        displayCurrentAuctions();

        int auctionId = readPositiveInt(scanner, "\nEnter Auction ID to place bid: ");
        Auction auction = findAuctionById(auctionId);

        if (auction == null) {
            System.out.println("Auction ID " + auctionId + " not found.");
            return;
        }

        if (!"APPROVED".equalsIgnoreCase(auction.getStatus())) {
            System.out.println("Bidding not allowed. Auction status: " + auction.getStatus());
            return;
        }

        double currentBid = auction.getCurrentBid();
        double minNextBid = currentBid + AuctionConstants.MINIMUM_BID_INCREMENT;

        System.out.printf("Current bid: ₹%,.0f%n", currentBid);
        System.out.printf("Minimum next bid: ₹%,.0f%n", minNextBid);

        double newBid = readPositiveDouble(scanner, "Enter bid: ");

        /*
         * CONCEPT 1: IF / ELSE BID VALIDATION
         * If new bid >= current bid + minimum increment: accept bid
         * else: reject bid
         */
        if (validateBid(newBid, currentBid, AuctionConstants.MINIMUM_BID_INCREMENT)) {
            System.out.print("Enter bidder name: ");
            String bidderName = scanner.hasNextLine() ? scanner.nextLine().trim() : "Krishna";
            if (bidderName.isEmpty()) {
                bidderName = "Krishna";
            }

            auction.addBid(new Bid(bidderName, newBid, "Active"));
            System.out.println("Bid accepted.");
            System.out.printf("Current bid is now ₹%,.0f by %s.%n", newBid, bidderName);
        } else {
            System.out.println("Bid rejected.");
            System.out.printf("Bid must be at least ₹%,.0f.%n", minNextBid);
        }
    }

    public static boolean validateBid(double newBid, double currentBid, double minIncrement) {
        return newBid >= (currentBid + minIncrement);
    }

    // =========================================================================
    // FR5 — PROXY / AUTO BIDDING
    // =========================================================================

    /**
     * Demonstrates:
     * - WHILE loop simulating proxy counter-bidding increments
     * - IF / ELSE budget checking
     */
    private static void handleProxyBidding(Scanner scanner) {
        System.out.println("\n----------------------------------------");
        System.out.println("  FR5 -- Proxy / Auto Bidding           ");
        System.out.println("----------------------------------------");

        displayCurrentAuctions();

        int auctionId = readPositiveInt(scanner, "\nEnter Auction ID: ");
        Auction auction = findAuctionById(auctionId);

        if (auction == null) {
            System.out.println("Auction ID " + auctionId + " not found.");
            return;
        }

        System.out.printf("Current bid: ₹%,.0f%n", auction.getCurrentBid());
        double maxBid = readPositiveDouble(scanner, "Enter maximum bid: ");

        double minRequired = auction.getCurrentBid() + AuctionConstants.MINIMUM_BID_INCREMENT;

        /*
         * CONCEPT 1: IF / ELSE
         * Budget check
         */
        if (maxBid < minRequired) {
            System.out.printf("Maximum bid must be at least ₹%,.0f.%n", minRequired);
            return;
        }

        System.out.println("Proxy bidding configured.");
        System.out.printf("Auto-bidding activated up to ₹%,.0f.%n", maxBid);

        /*
         * CONCEPT 3: WHILE LOOP
         * Increments bid automatically up to ceiling
         */
        double simBid = auction.getCurrentBid();
        int step = 1;
        while ((simBid + AuctionConstants.MINIMUM_BID_INCREMENT) <= maxBid && step <= 3) {
            simBid += AuctionConstants.MINIMUM_BID_INCREMENT;
            auction.addBid(new Bid("AutoProxy_User", simBid, "Proxy step " + step));
            System.out.printf("Proxy step %d: Auto-bid placed at ₹%,.0f%n", step, simBid);
            step++;
        }

        System.out.printf("Current highest bid settled at ₹%,.0f by %s.%n",
                auction.getCurrentBid(), auction.getHighestBidder());
    }

    // =========================================================================
    // FR6 — ANTI-SNIPING
    // =========================================================================

    /**
     * Demonstrates:
     * - IF / ELSE anti-sniping extension rule
     * - Constant: AuctionConstants.ANTI_SNIPE_MINUTES
     */
    private static void handleAntiSniping(Scanner scanner) {
        System.out.println("\n----------------------------------------");
        System.out.println("  FR6 -- Anti-Sniping                   ");
        System.out.println("----------------------------------------");

        System.out.println("Checking auction end time...\n");
        System.out.printf("If bid occurs within final %d minutes:%n", AuctionConstants.ANTI_SNIPE_MINUTES);
        System.out.printf("Auction extended by %d minutes.%n", AuctionConstants.ANTI_SNIPE_EXTENSION_MINUTES);

        int minutesRemaining = readPositiveInt(scanner, "\nEnter minutes remaining before end: ");

        /*
         * CONCEPT 1: IF / ELSE
         * Checking if anti-sniping should trigger
         */
        if (isAntiSnipeTriggered(minutesRemaining, AuctionConstants.ANTI_SNIPE_MINUTES)) {
            int extendedRemaining = minutesRemaining + AuctionConstants.ANTI_SNIPE_EXTENSION_MINUTES;
            System.out.printf("\nBid occurred within final %d minutes.%n", AuctionConstants.ANTI_SNIPE_MINUTES);
            System.out.printf("Auction extended by %d minutes. New end time: %d minutes remaining.%n",
                    AuctionConstants.ANTI_SNIPE_EXTENSION_MINUTES, extendedRemaining);
        } else {
            System.out.printf("\nBid occurred with %d minutes remaining. No extension needed.%n", minutesRemaining);
        }
    }

    public static boolean isAntiSnipeTriggered(int minutesRemaining, int thresholdMinutes) {
        return minutesRemaining <= thresholdMinutes;
    }

    // =========================================================================
    // FR7 — CLOSE AUCTION & DECLARE WINNER
    // =========================================================================

    /**
     * Demonstrates:
     * - IF / ELSE reserve price validation
     * - Declares winner if highest bid >= reserve price
     */
    private static void handleCloseAuctionAndDeclareWinner(Scanner scanner) {
        System.out.println("\n----------------------------------------");
        System.out.println("  FR7 -- Close Auction & Declare Winner ");
        System.out.println("----------------------------------------");

        displayCurrentAuctions();

        int auctionId = readPositiveInt(scanner, "\nEnter Auction ID to close: ");
        Auction auction = findAuctionById(auctionId);

        if (auction == null) {
            System.out.println("Auction ID " + auctionId + " not found.");
            return;
        }

        double highestBid = auction.getCurrentBid();
        double reservePrice = auction.getReservePrice();

        System.out.println("\nClosing Auction...");
        System.out.printf("Highest bid: ₹%,.0f%n", highestBid);
        System.out.printf("Reserve price: ₹%,.0f%n", reservePrice);

        /*
         * CONCEPT 1: IF / ELSE RESERVE PRICE
         * If highest bid >= reserve price: winner can be declared
         * else: reserve price not met
         */
        if (isReservePriceMet(highestBid, reservePrice)) {
            auction.setStatus("SOLD");
            System.out.println("\nWinner: " + auction.getHighestBidder());
            System.out.println("Auction status: SOLD");
        } else {
            auction.setStatus("UNSOLD");
            System.out.println("\nReserve price not met.");
            System.out.println("Auction status: UNSOLD");
        }
    }

    public static boolean isReservePriceMet(double highestBid, double reservePrice) {
        return highestBid >= reservePrice;
    }

    // =========================================================================
    // FR8 — SETTLEMENT & SHIPMENT
    // =========================================================================

    /**
     * Demonstrates:
     * - Traditional FOR loop (Concept 9) for bid ledger inspection
     * - CONTINUE statement (Concept 12) for skipping invalid/rejected records
     * - LABELLED BREAK (Concept 13) for nested bidder search
     */
    private static void handleSettlementAndShipment(Scanner scanner) {
        System.out.println("\n----------------------------------------");
        System.out.println("  FR8 -- Settlement & Shipment          ");
        System.out.println("----------------------------------------");

        System.out.println("Buyer payment status: PAID");
        System.out.println("Seller shipment status: SHIPPED");
        System.out.println("Settlement status: READY");

        Auction sample = auctions.getFirst();
        double finalPrice = sample.getCurrentBid();
        double platformFee = finalPrice * (AuctionConstants.PLATFORM_FEE_PERCENTAGE / 100.0);
        double sellerPayout = finalPrice - platformFee;

        System.out.printf("%nSettlement Summary for Auction %d (%s):%n", sample.getId(), sample.getProductName());
        System.out.printf("  Final Price:  ₹%,.2f%n", finalPrice);
        System.out.printf("  Platform Fee: ₹%,.2f%n", platformFee);
        System.out.printf("  Seller Payout:₹%,.2f%n", sellerPayout);

        /*
         * CONCEPT 9: TRADITIONAL FOR LOOP
         * Demonstrates processing indexed bid records.
         */
        System.out.println("\nBid History (Traditional for loop):");
        List<Bid> sampleBids = sample.getBids();
        for (int i = 0; i < sampleBids.size(); i++) {
            System.out.printf("  Bid [%d]: %s%n", i, sampleBids.get(i));
        }

        /*
         * CONCEPT 12: CONTINUE STATEMENT
         * The continue statement skips the rest of the current loop iteration
         * when an auction record is in REJECTED state, moving directly to the
         * next record in the loop.
         */
        System.out.println("\nAuditing active auctions (Demonstrating CONTINUE):");
        for (int i = 0; i < auctions.size(); i++) {
            Auction a = auctions.get(i);
            if ("REJECTED".equalsIgnoreCase(a.getStatus())) {
                System.out.println("Skipping rejected auction record ID: " + a.getId());
                continue; // Skips current iteration, continues loop
            }
            System.out.printf("Processing auction %d: %s [Status: %s]%n",
                    a.getId(), a.getProductName(), a.getStatus());
        }

        /*
         * CONCEPT 13: LABELLED BREAK STATEMENT
         * The labelled break directly terminates the specified outer loop ('searchAuction')
         * from within the inner nested loop as soon as the target bidder is found.
         * Without a labelled break, an inner break would only exit the inner loop,
         * requiring extra boolean flags to exit the outer loop.
         */
        System.out.print("\nEnter bidder name to search across all auctions (e.g. Krishna): ");
        String targetBidderId = scanner.hasNextLine() ? scanner.nextLine().trim() : "Krishna";
        if (targetBidderId.isEmpty()) {
            targetBidderId = "Krishna";
        }

        System.out.println("\nSearching for bidder across auctions (Demonstrating LABELLED BREAK):");
        boolean found = false;

        searchAuction: // Label identifying the outer loop
        for (Auction auction : auctions) {
            for (Bid bid : auction.getBids()) {
                if (bid.getBidderId().equalsIgnoreCase(targetBidderId)) {
                    System.out.printf("Bidder found in Auction %d (%s) with bid of ₹%,.0f.%n",
                            auction.getId(), auction.getProductName(), bid.getAmount());
                    found = true;
                    break searchAuction; // Directly exits the outer searchAuction loop!
                }
            }
        }

        if (!found) {
            System.out.println("Bidder '" + targetBidderId + "' not found in any auctions.");
        }
    }

    // =========================================================================
    // ENHANCED FOR LOOP & MINI STATEMENT
    // =========================================================================

    /**
     * CONCEPT 10: ENHANCED FOR LOOP
     * Displays current auctions mini-statement.
     */
    public static void displayCurrentAuctions() {
        System.out.println("\n===== CURRENT AUCTIONS =====");
        for (Auction auction : auctions) {
            System.out.printf("Auction %d - %s - ₹%,.0f%n",
                    auction.getId(), auction.getProductName(), auction.getCurrentBid());
        }
    }

    public static Auction findAuctionById(int id) {
        for (Auction auction : auctions) {
            if (auction.getId() == id) {
                return auction;
            }
        }
        return null;
    }

    public static List<Auction> getAuctions() {
        return auctions;
    }
}
