package com.auction.app;

import com.auction.model.Auction;
import com.auction.model.AuctionCloseResult;
import com.auction.model.Bid;
import com.auction.model.ListingStatus;
import com.auction.model.Seller;
import com.auction.model.Settlement;
import com.auction.model.Student;
import com.auction.repository.BidRepository;
import com.auction.repository.CrudRepository;
import com.auction.repository.InMemoryBidRepository;
import com.auction.repository.InMemoryRepository;
import com.auction.service.AuctionFulfillmentService;
import com.auction.service.StudentService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Scanner;

/**
 * Main Interactive Application for Day 11: AI Integration (Core Java).
 *
 * Demonstrates:
 * 1. Student POJO & StudentService (prompt review, validation, defensive copying).
 * 2. Specialized BidRepository (reusing Day 9 generic CrudRepository architecture).
 * 3. FR7: Auction Closing & Winner Declaration (reserve price verification).
 * 4. FR8: Settlement & Shipment (5% platform fee deduction, courier tracking, seller payout).
 */
public class Day11App {

    private static final StudentService studentService = new StudentService();
    private static final CrudRepository<Auction, Integer> auctionRepository =
            new InMemoryRepository<>(Auction::getId);
    private static final BidRepository bidRepository = new InMemoryBidRepository();
    private static final AuctionFulfillmentService fulfillmentService =
            new AuctionFulfillmentService(auctionRepository, bidRepository);

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("       ONLINE AUCTION SYSTEM - DAY 11");
        System.out.println("   AI Integration (Core Java) & FR7-FR8");
        System.out.println("=================================================");

        seedDemoData();

        if (args.length > 0 && "--demo".equalsIgnoreCase(args[0])) {
            runAutomatedDemonstration();
            return;
        }

        runInteractiveMenu();
    }

    private static void seedDemoData() {
        // Seed sample students
        studentService.addStudent(new Student(1, "Hari Krishnan", "hari@training.hcl.com"));
        studentService.addStudent(new Student(2, "Ravi Kumar", "ravi.kumar@example.com"));

        // Seed sample auctions
        Seller seller = new Seller(101, "tech_seller", "pass123", "Tech Store", "tech@seller.com", "Tech Hub", 4.8);
        Auction a1 = new Auction(1001, "MacBook Pro M3", seller, 120000.0, 140000.0,
                LocalDateTime.now().minusHours(2), LocalDateTime.now().minusMinutes(5));
        a1.setStatus(ListingStatus.APPROVED);
        auctionRepository.save(a1);

        Auction a2 = new Auction(1002, "Vintage Mechanical Watch", seller, 15000.0, 25000.0,
                LocalDateTime.now().minusHours(2), LocalDateTime.now().minusMinutes(5));
        a2.setStatus(ListingStatus.APPROVED);
        auctionRepository.save(a2);

        // Seed bids
        bidRepository.save(new Bid(501, 1001, 201, "BidderAnita", 130000.0, LocalDateTime.now().minusMinutes(30)));
        bidRepository.save(new Bid(502, 1001, 202, "BidderKarthik", 145000.0, LocalDateTime.now().minusMinutes(10)));

        bidRepository.save(new Bid(503, 1002, 203, "BidderPooja", 18000.0, LocalDateTime.now().minusMinutes(15)));
    }

    public static void runAutomatedDemonstration() {
        System.out.println("\n--- RUNNING AUTOMATED DEMONSTRATION OF DAY 11 ---");

        System.out.println("\n1. Student POJO & StudentService verification:");
        for (Student s : studentService.getAllStudents()) {
            System.out.println("   " + s);
        }

        System.out.println("\n2. Specialized BidRepository verification:");
        Bid highestA1 = bidRepository.findHighestBid(1001);
        System.out.printf("   Highest bid on Auction 1001: INR %,.2f by %s%n",
                highestA1.getAmount(), highestA1.getBidderName());

        System.out.println("\n3. FR7 - Auction Closing & Winner Declaration (Reserve Met):");
        AuctionCloseResult res1 = fulfillmentService.closeAuction(1001, LocalDateTime.now());
        System.out.println("   " + res1);

        System.out.println("\n4. FR7 - Auction Closing (Reserve NOT Met):");
        AuctionCloseResult res2 = fulfillmentService.closeAuction(1002, LocalDateTime.now());
        System.out.println("   " + res2);

        System.out.println("\n5. FR8 - Settlement & Shipment Workflow for Auction 1001:");
        Optional<Settlement> opt = fulfillmentService.getSettlementForAuction(1001);
        if (opt.isPresent()) {
            Settlement s = opt.get();
            System.out.printf("   Created Settlement: Gross=INR %,.2f | Platform Fee (5%%)=INR %,.2f | Net Payout=INR %,.2f%n",
                    s.getGrossAmount(), s.getPlatformFee(), s.getNetSellerPayout());

            System.out.println("   Step A: Buyer payment simulation...");
            fulfillmentService.processBuyerPayment(1001, s.getBuyerId(), s.getGrossAmount());
            System.out.println("   Buyer payment received! Escrow locked.");

            System.out.println("   Step B: Seller shipment submission...");
            fulfillmentService.processShipment(1001, s.getSellerId(), "BlueDart Express", "BD-98234-IN");
            System.out.printf("   Shipment confirmed! Payout released to seller: INR %,.2f%n", s.getNetSellerPayout());
            System.out.println("   Final Settlement Status: " + s);
        }
        System.out.println("\n--- DEMONSTRATION COMPLETE ---");
    }

    private static void runInteractiveMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n----------------- DAY 11 MENU -----------------");
            System.out.println(" 1. View Registered Students (StudentService)");
            System.out.println(" 2. Add New Student (POJO Validation)");
            System.out.println(" 3. View Bids via BidRepository");
            System.out.println(" 4. Close Auction & Declare Winner (FR7)");
            System.out.println(" 5. Process Settlement & Shipment (FR8)");
            System.out.println(" 6. Run Complete Automated End-to-End Demo");
            System.out.println(" 0. Exit");
            System.out.print("Select an option (0-6): ");

            String input = scanner.nextLine();
            int choice;
            try {
                choice = Integer.parseInt(input.trim());
            } catch (NumberFormatException nfe) {
                System.out.println("[ERROR] Please enter a valid numeric option.");
                continue;
            }

            switch (choice) {
                case 1 -> {
                    System.out.println("\n--- Registered Students ---");
                    studentService.getAllStudents().forEach(s -> System.out.println("  " + s));
                }
                case 2 -> {
                    try {
                        System.out.print("Enter Student ID: ");
                        int id = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Enter Student Name: ");
                        String name = scanner.nextLine().trim();
                        System.out.print("Enter Student Email: ");
                        String email = scanner.nextLine().trim();
                        studentService.addStudent(new Student(id, name, email));
                        System.out.println("[SUCCESS] Student registered successfully.");
                    } catch (Exception ex) {
                        System.out.println("[ERROR] Failed to add student: " + ex.getMessage());
                    }
                }
                case 3 -> {
                    System.out.print("Enter Auction ID (e.g. 1001): ");
                    try {
                        int aId = Integer.parseInt(scanner.nextLine().trim());
                        var bids = bidRepository.findByAuctionId(aId);
                        System.out.printf("\n--- Bids for Auction #%d (%d bids) ---%n", aId, bids.size());
                        bids.forEach(b -> System.out.printf("  Bid #%d: INR %,.2f by %s (Bidder #%d)%n",
                                b.getId(), b.getAmount(), b.getBidderName(), b.getBidderId()));
                    } catch (Exception ex) {
                        System.out.println("[ERROR] " + ex.getMessage());
                    }
                }
                case 4 -> {
                    System.out.print("Enter Auction ID to close (e.g. 1001 or 1002): ");
                    try {
                        int aId = Integer.parseInt(scanner.nextLine().trim());
                        AuctionCloseResult res = fulfillmentService.closeAuction(aId, LocalDateTime.now());
                        System.out.println("\n[FR7 RESULT] " + res);
                    } catch (Exception ex) {
                        System.out.println("[ERROR] " + ex.getMessage());
                    }
                }
                case 5 -> {
                    System.out.print("Enter Auction ID for settlement: ");
                    try {
                        int aId = Integer.parseInt(scanner.nextLine().trim());
                        var opt = fulfillmentService.getSettlementForAuction(aId);
                        if (opt.isEmpty()) {
                            System.out.println("[INFO] No active settlement found. (Auction must be closed and SOLD first).");
                        } else {
                            Settlement s = opt.get();
                            System.out.println("Current Settlement: " + s);
                            if (!s.isPaid()) {
                                System.out.printf("Winning buyer (#%d) owes: INR %,.2f. Pay now? (y/n): ",
                                        s.getBuyerId(), s.getGrossAmount());
                                if ("y".equalsIgnoreCase(scanner.nextLine().trim())) {
                                    fulfillmentService.processBuyerPayment(aId, s.getBuyerId(), s.getGrossAmount());
                                    System.out.println("[SUCCESS] Payment recorded!");
                                }
                            } else if (!s.isShipped()) {
                                System.out.print("Enter Courier Name (e.g. BlueDart): ");
                                String courier = scanner.nextLine().trim();
                                System.out.print("Enter Tracking Number: ");
                                String track = scanner.nextLine().trim();
                                fulfillmentService.processShipment(aId, s.getSellerId(), courier, track);
                                System.out.printf("[SUCCESS] Shipment recorded and INR %,.2f payout released to Seller #%d!%n",
                                        s.getNetSellerPayout(), s.getSellerId());
                            } else {
                                System.out.println("[INFO] Settlement is already paid, shipped, and completed!");
                            }
                        }
                    } catch (Exception ex) {
                        System.out.println("[ERROR] " + ex.getMessage());
                    }
                }
                case 6 -> runAutomatedDemonstration();
                case 0 -> {
                    System.out.println("Exiting Day 11 Application. Goodbye!");
                    running = false;
                }
                default -> System.out.println("[WARNING] Invalid option.");
            }
        }
    }
}
