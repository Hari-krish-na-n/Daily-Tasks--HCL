package com.auction.app;

import com.auction.demo.CollectionsShowcase;
import com.auction.demo.LibraryWaitlistDemo;
import com.auction.demo.ListPerformanceBenchmark;
import com.auction.model.Admin;
import com.auction.model.Auction;
import com.auction.model.AuctionRole;
import com.auction.model.Bidder;
import com.auction.model.ListingStatus;
import com.auction.model.Seller;
import com.auction.model.User;
import com.auction.service.AuthService;
import com.auction.service.AuctionManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

/**
 * Main Interactive Application for Day 7: Collections Framework (Part 1).
 *
 * Implements:
 * - FR1: User Authentication (Registration & Login) using ArrayList and HashSet
 * - FR2: Auction Listing (Seller listing creation & validation) using ArrayList
 * - FR3: Admin Moderation (Approve/Reject pending listings) using ArrayDeque
 * - Collections Demonstration: List, Set, Queue, and safe Iteration patterns
 * - Library Waitlist Demonstration: Catalogue, De-duplication, FIFO & Priority Queues, Iterator.remove()
 * - ArrayList vs LinkedList Performance Benchmark: Measured with System.nanoTime()
 * - Robust error handling with graceful recovery (never crash on bad user input)
 */
public class AuctionApp {

    private static final AuthService authService = new AuthService();
    private static final AuctionManager auctionManager = new AuctionManager();

    // Session state
    private static User currentUser = null;

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("       ONLINE AUCTION SYSTEM - DAY 7");
        System.out.println(" Collections Framework Part 1 & Auction Workflow");
        System.out.println("=================================================");

        if (args.length > 0 && "--demo".equalsIgnoreCase(args[0])) {
            runAutomatedDemonstrations();
            return;
        }

        runInteractiveMenu();
    }

    public static void runInteractiveMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            displayMenu();
            System.out.print("Select an option (0-9): ");
            String input = scanner.nextLine();

            int choice;
            try {
                choice = Integer.parseInt(input.trim());
            } catch (NumberFormatException nfe) {
                System.out.println("\n[ERROR] Invalid menu selection! '" + input + "' is not a valid number.");
                System.out.println("Please enter a numeric option between 0 and 9.\n");
                continue;
            }

            switch (choice) {
                case 1 -> menuRegisterUser(scanner);
                case 2 -> menuLoginUser(scanner);
                case 3 -> menuLogoutUser();
                case 4 -> menuCreateAuctionListing(scanner);
                case 5 -> menuViewAuctionListings();
                case 6 -> menuAdminModeration(scanner);
                case 7 -> CollectionsShowcase.runAllDemonstrations();
                case 8 -> LibraryWaitlistDemo.runDemo();
                case 9 -> ListPerformanceBenchmark.runBenchmark(50_000);
                case 0 -> {
                    System.out.println("\nThank you for using the Online Auction System. Goodbye!");
                    running = false;
                }
                default -> System.out.println("\n[WARNING] Choice " + choice + " is out of range. Please choose between 0 and 9.");
            }
        }
    }

    private static void displayMenu() {
        String userBanner = (currentUser == null)
                ? "[Not Logged In - Guest]"
                : "[" + currentUser.getName() + " (" + currentUser.getRole().getAuthority() + ")]";

        System.out.println("\n----------------- MAIN MENU " + userBanner + " -----------------");
        System.out.println(" 1. Register User (FR1 - ArrayList + HashSet)");
        System.out.println(" 2. Log In (FR1)");
        System.out.println(" 3. Log Out");
        System.out.println(" 4. Create Auction Listing (FR2 - Seller)");
        System.out.println(" 5. View Auction Listings (FR2/FR3 - Master Catalogue)");
        System.out.println(" 6. Moderate Pending Listings (FR3 - Admin FIFO Queue)");
        System.out.println(" 7. Run Java Collections Demonstration (List, Set, Queue, CME)");
        System.out.println(" 8. Run Library Waitlist Exercise (ArrayList, Deque, PriorityQueue)");
        System.out.println(" 9. Run ArrayList vs LinkedList Performance Benchmark");
        System.out.println(" 0. Exit");
        System.out.println("-----------------------------------------------------------------");
    }

    // -------------------------------------------------------------
    // FR1: User Registration
    // -------------------------------------------------------------
    private static void menuRegisterUser(Scanner scanner) {
        System.out.println("\n--- Option 1: Register New User (FR1) ---");
        try {
            System.out.print("Enter desired username: ");
            String username = scanner.nextLine().trim();

            if (authService.isUsernameTaken(username)) {
                System.out.println("[RECOVERED] Registration failed: Username '" + username
                        + "' is already taken! (Detected via HashSet O(1) check).");
                return;
            }

            System.out.print("Enter password: ");
            String password = scanner.nextLine().trim();

            System.out.print("Enter full name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter email address: ");
            String email = scanner.nextLine().trim();

            System.out.println("Select Role: 1. BIDDER | 2. SELLER | 3. ADMIN");
            int roleOption = Integer.parseInt(scanner.nextLine().trim());
            AuctionRole role = switch (roleOption) {
                case 1 -> AuctionRole.BIDDER;
                case 2 -> AuctionRole.SELLER;
                case 3 -> AuctionRole.ADMIN;
                default -> AuctionRole.BIDDER;
            };

            String extra = "";
            double budget = 0.0;
            if (role == AuctionRole.SELLER) {
                System.out.print("Enter Store Name: ");
                extra = scanner.nextLine().trim();
            } else if (role == AuctionRole.ADMIN) {
                System.out.print("Enter Department: ");
                extra = scanner.nextLine().trim();
            } else {
                System.out.print("Enter initial bidding budget in INR (e.g. 50000): ");
                budget = Double.parseDouble(scanner.nextLine().trim());
            }

            User registered = authService.registerUser(username, password, name, email, role, extra, budget);
            System.out.println(">>> SUCCESS: User registered successfully!");
            System.out.println("    Details: " + registered.getSummary());
            System.out.println("    Note: Password was securely validated and not printed.");

        } catch (NumberFormatException nfe) {
            System.out.println("[RECOVERED] Input error: Numeric value expected for role or budget.");
        } catch (IllegalArgumentException iae) {
            System.out.println("[RECOVERED] Validation Error: " + iae.getMessage());
        } catch (Exception ex) {
            System.out.println("[RECOVERED] Unexpected error during registration: " + ex.getMessage());
        }
    }

    // -------------------------------------------------------------
    // FR1: User Login
    // -------------------------------------------------------------
    private static void menuLoginUser(Scanner scanner) {
        System.out.println("\n--- Option 2: User Login (FR1) ---");
        try {
            System.out.print("Enter username: ");
            String username = scanner.nextLine().trim();

            System.out.print("Enter password: ");
            String password = scanner.nextLine().trim();

            User user = authService.login(username, password);
            if (user != null) {
                currentUser = user;
                System.out.println(">>> LOGIN SUCCESSFUL! Welcome back, " + user.getName() + " (" + user.getRole() + ").");
            } else {
                System.out.println(">>> LOGIN FAILED: Invalid username or incorrect password.");
            }
        } catch (Exception ex) {
            System.out.println("[RECOVERED] Error during login: " + ex.getMessage());
        }
    }

    private static void menuLogoutUser() {
        if (currentUser != null) {
            System.out.println(">>> Logged out user: " + currentUser.getName());
            currentUser = null;
        } else {
            System.out.println(">>> No user is currently logged in.");
        }
    }

    // -------------------------------------------------------------
    // FR2: Create Auction Listing
    // -------------------------------------------------------------
    private static void menuCreateAuctionListing(Scanner scanner) {
        System.out.println("\n--- Option 4: Create Auction Listing (FR2) ---");

        Seller seller;
        if (currentUser instanceof Seller s) {
            seller = s;
        } else {
            System.out.println("[Notice] You are not currently logged in as a Seller.");
            System.out.println("Available registered sellers:");
            List<User> sellers = authService.getRegisteredUsers().stream()
                    .filter(u -> u.getRole() == AuctionRole.SELLER)
                    .toList();
            for (int i = 0; i < sellers.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + sellers.get(i).getName() + " (" + sellers.get(i).getUsername() + ")");
            }
            System.out.print("Select seller number (or 0 to cancel): ");
            int idx = Integer.parseInt(scanner.nextLine().trim());
            if (idx <= 0 || idx > sellers.size()) {
                System.out.println("Listing creation cancelled.");
                return;
            }
            seller = (Seller) sellers.get(idx - 1);
        }

        try {
            System.out.print("Enter product name: ");
            String productName = scanner.nextLine().trim();

            System.out.print("Enter item description: ");
            String desc = scanner.nextLine().trim();

            System.out.print("Enter starting price in INR: ");
            double startingPrice = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Enter reserve price in INR (minimum price to win): ");
            double reservePrice = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Enter auction duration in days (e.g. 7): ");
            int days = Integer.parseInt(scanner.nextLine().trim());
            if (days <= 0) days = 7;

            LocalDateTime start = LocalDateTime.now();
            LocalDateTime end = start.plusDays(days);

            Auction listing = auctionManager.createListing(seller, productName, desc, startingPrice, reservePrice, start, end);
            System.out.println(">>> SUCCESS: Auction listing submitted!");
            System.out.println("    Listing ID: " + listing.getId());
            System.out.println("    Status: " + listing.getStatus().getDisplayLabel());
            System.out.println("    Note: As per FR2 and FR3, the listing is currently PENDING until an Admin approves it.");

        } catch (NumberFormatException nfe) {
            System.out.println("[RECOVERED] Input error: Price and duration must be valid numbers.");
        } catch (IllegalArgumentException iae) {
            System.out.println("[RECOVERED] Validation Error: " + iae.getMessage());
        } catch (Exception ex) {
            System.out.println("[RECOVERED] Error creating listing: " + ex.getMessage());
        }
    }

    // -------------------------------------------------------------
    // FR2/FR3: View Auction Listings
    // -------------------------------------------------------------
    private static void menuViewAuctionListings() {
        System.out.println("\n--- Option 5: View Auction Listings ---");
        List<Auction> all = auctionManager.getAllAuctions();
        if (all.isEmpty()) {
            System.out.println("No auction listings found in the catalogue.");
            return;
        }

        System.out.println(String.format("%-6s | %-28s | %-12s | %-12s | %-12s | %-16s",
                "ID", "Product Name", "Start Price", "Reserve", "Seller", "Status"));
        System.out.println("-------------------------------------------------------------------------------------------------");
        for (Auction a : all) {
            System.out.println(String.format("%-6d | %-28s | INR %-8.0f | INR %-8.0f | %-12s | %-16s",
                    a.getId(),
                    truncate(a.getProductName(), 28),
                    a.getStartingPrice(),
                    a.getReservePrice(),
                    (a.getSeller() != null ? a.getSeller().getName() : "None"),
                    a.getStatus().name()));
        }
    }

    // -------------------------------------------------------------
    // FR3: Admin Moderation
    // -------------------------------------------------------------
    private static void menuAdminModeration(Scanner scanner) {
        System.out.println("\n--- Option 6: Admin Moderation (FR3) ---");

        String adminUsername = "admin";
        if (currentUser instanceof Admin admin) {
            adminUsername = admin.getUsername();
        } else if (currentUser != null && currentUser.getRole() != AuctionRole.ADMIN) {
            System.out.println("[Notice] Current user (" + currentUser.getName()
                    + ") does not have ROLE_ADMIN. Falling back to default system admin for review.");
        }

        List<Auction> pending = auctionManager.getPendingAuctions();
        if (pending.isEmpty()) {
            System.out.println("No pending auctions awaiting moderation in the queue!");
            return;
        }

        System.out.println("Currently Pending Listings (" + pending.size() + " in FIFO queue):");
        for (Auction a : pending) {
            System.out.println("  * " + a.getSummary());
        }

        try {
            System.out.print("\nEnter Auction ID to moderate (or 0 to cancel): ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            if (id == 0) return;

            Auction target = auctionManager.findById(id);
            if (target == null) {
                System.out.println("[RECOVERED] Auction ID " + id + " was not found.");
                return;
            }

            if (!target.isPending()) {
                System.out.println("[RECOVERED] Cannot moderate: Auction #" + id
                        + " is already " + target.getStatus().name() + " and cannot be moderated repeatedly.");
                return;
            }

            System.out.print("Enter decision (1 = APPROVE, 2 = REJECT): ");
            int decision = Integer.parseInt(scanner.nextLine().trim());
            boolean approve = (decision == 1);

            System.out.print("Enter moderation note / remarks: ");
            String note = scanner.nextLine().trim();

            auctionManager.moderateListing(id, adminUsername, approve, note);
            System.out.println(">>> SUCCESS: Listing #" + id + " is now " + (approve ? "APPROVED" : "REJECTED") + ".");
            System.out.println("    Moderated by: " + adminUsername);
            System.out.println("    Remarks: " + note);

        } catch (NumberFormatException nfe) {
            System.out.println("[RECOVERED] Input error: Please enter valid numbers.");
        } catch (IllegalStateException | IllegalArgumentException ex) {
            System.out.println("[RECOVERED] Business Rule Violation: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("[RECOVERED] Error during moderation: " + ex.getMessage());
        }
    }

    private static void runAutomatedDemonstrations() {
        System.out.println("\nRunning automated test demonstrations...");
        CollectionsShowcase.runAllDemonstrations();
        System.out.println();
        LibraryWaitlistDemo.runDemo();
        System.out.println();
        ListPerformanceBenchmark.runBenchmark(20_000);
    }

    private static String truncate(String text, int max) {
        if (text == null) return "";
        return text.length() <= max ? text : text.substring(0, max - 3) + "...";
    }
}
