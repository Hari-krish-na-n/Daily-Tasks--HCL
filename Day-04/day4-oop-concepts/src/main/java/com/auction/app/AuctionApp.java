package com.auction.app;

import com.auction.model.Auction;
import com.auction.model.Bid;
import com.auction.model.Bidder;
import com.auction.model.Seller;
import com.auction.service.AuctionService;

/**
 * Main application demonstrating Day 4 OOP concepts and Debugging scenarios:
 * - Classes, Objects, Fields, Methods
 * - Encapsulation (private fields, getters/setters)
 * - Inheritance (Seller and Bidder extending User)
 * - 'this' and 'super' keywords
 * - Constructor overloading & Constructor chaining using this()
 * - static vs instance members (Auction.getAuctionCount())
 * - equals() and hashCode() (Bid identity comparison)
 * - Package organization (model, service, app)
 */
public class AuctionApp {

    public static void main(String[] args) {
        System.out.println("=================================");
        System.out.println("ONLINE AUCTION - DAY 4");
        System.out.println("=================================");

        // 1. Inheritance and super() demonstration
        // Seller extends User, constructor calls super(userId, name, email, role)
        Seller seller = new Seller(101, "Ravi", "ravi@auction.com", "Ravi Electronics", 4.9);
        System.out.println("\nSeller:");
        System.out.println("ID: " + seller.getUserId());
        System.out.println("Name: " + seller.getName());
        System.out.println("Role: " + seller.getRole());

        // Bidder extends User, constructor calls super(userId, name, email, role)
        Bidder bidder = new Bidder(201, "Krishna", "krishna@auction.com", 75000.0);
        System.out.println("\nBidder:");
        System.out.println("ID: " + bidder.getUserId());
        System.out.println("Name: " + bidder.getName());
        System.out.println("Role: " + bidder.getRole());

        // 2. Constructor Overloading and Chaining with this()
        // Auction() -> calls this(0, "Unnamed") -> calls this(id, name, seller, startPrice, reservePrice)
        Auction auction = new Auction(1001, "Laptop", seller, 50000.0, 55000.0);
        System.out.println("\nAuction:");
        System.out.println("ID: " + auction.getAuctionId());
        System.out.println("Product: " + auction.getProductName());
        System.out.println("Starting Price: " + (long) auction.getStartingPrice());

        // Service to manage auctions
        AuctionService service = new AuctionService();
        service.createAuction(auction);

        // 3. Debugging scenario: Attempting invalid bid vs valid bid
        System.out.println("\n--- Debugging Planted Bug Scenario ---");
        System.out.println("Current Bid: " + (long) auction.getCurrentBid());
        System.out.println("Minimum Increment: " + (long) AuctionService.DEFAULT_MINIMUM_INCREMENT);
        System.out.println("Minimum Valid Bid: " + (long) (auction.getCurrentBid() + AuctionService.DEFAULT_MINIMUM_INCREMENT));

        System.out.println("\nAttempt 1 (Invalid Bid - Underbid):");
        System.out.println("Bid Amount: 49000");
        service.placeBid(auction.getAuctionId(), bidder, 49000.0, 301);

        System.out.println("\nAttempt 2 (Valid Bid):");
        System.out.println("Bid:");
        System.out.println("Amount: 52000");
        service.placeBid(auction.getAuctionId(), bidder, 52000.0, 302);

        // 4. Static vs Instance Members Demonstration
        System.out.println("\n--- Static vs Instance Demo ---");
        System.out.println("Instance Member (Auction ID): " + auction.getAuctionId());
        System.out.println("Instance Member (Product Name): " + auction.getProductName());
        System.out.println("Static Member (Total Auctions): " + Auction.getAuctionCount());

        // 5. equals() and hashCode() Demonstration
        System.out.println("\n--- equals() and hashCode() Demo ---");
        Bid bidA = new Bid(302, bidder, auction, 52000.0);
        Bid bidB = new Bid(302, bidder, auction, 52000.0); // same bidId
        Bid bidC = new Bid(303, bidder, auction, 54000.0); // different bidId

        System.out.println("bidA.equals(bidB) [Same ID 302]: " + bidA.equals(bidB));
        System.out.println("bidA.equals(bidC) [Different IDs]: " + bidA.equals(bidC));
        System.out.println("bidA.hashCode() == bidB.hashCode(): " + (bidA.hashCode() == bidB.hashCode()));

        // Final Summary
        System.out.println("\n=================================");
        System.out.println("Total Auctions: " + Auction.getAuctionCount());
        System.out.println("=================================");
    }
}
