package com.auction.service;

import com.auction.model.Auction;
import com.auction.model.Bid;
import com.auction.model.Bidder;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing auctions and bids in-memory.
 * Demonstrates:
 * - Separation of concerns (Service layer in com.auction.service)
 * - Encapsulated business logic and validation
 * - Debugging focus: bid validation logic
 */
public class AuctionService {

    public static final double DEFAULT_MINIMUM_INCREMENT = 500.0;

    // In-memory storage for auctions and bids
    private final List<Auction> auctions = new ArrayList<>();
    private final List<Bid> bids = new ArrayList<>();

    /**
     * Creates and adds a new auction to the system.
     */
    public void createAuction(Auction auction) {
        if (auction != null) {
            auctions.add(auction);
        }
    }

    /**
     * Finds an auction by its unique ID.
     */
    public Auction findAuctionById(int auctionId) {
        for (Auction a : auctions) {
            if (a.getAuctionId() == auctionId) {
                return a;
            }
        }
        return null;
    }

    /**
     * Validates whether a new bid meets the minimum increment requirement.
     *
     * DEBUGGING NOTE:
     * Planted bug originally had:
     *   return newBid > (currentBid - minimumIncrement); // BUG: accepted ₹49,000 when current was ₹50,000!
     *
     * Fixed logic:
     *   return newBid >= (currentBid + minimumIncrement);
     */
    public boolean validateBid(double currentBid, double newBid, double minimumIncrement) {
        double minimumRequiredBid = currentBid + minimumIncrement;
        return newBid >= minimumRequiredBid;
    }

    /**
     * Places a bid on an auction with proper validation.
     */
    public boolean placeBid(int auctionId, Bidder bidder, double bidAmount, int bidId) {
        Auction auction = findAuctionById(auctionId);

        if (auction == null) {
            System.out.println("Bid rejected: Auction ID " + auctionId + " not found.");
            return false;
        }

        if (!auction.isActive()) {
            System.out.println("Bid rejected: Auction ID " + auctionId + " is closed.");
            return false;
        }

        double currentBid = auction.getCurrentBid();
        double minimumIncrement = DEFAULT_MINIMUM_INCREMENT;
        double minimumBid = currentBid + minimumIncrement;

        // Validate bid using the debugged validation method
        if (!validateBid(currentBid, bidAmount, minimumIncrement)) {
            System.out.println("Bid rejected: INR " + (long) bidAmount + " is below minimum required bid of INR " + (long) minimumBid);
            return false;
        }

        // Accept bid, update auction current bid, and record bid
        auction.setCurrentBid(bidAmount);
        Bid bid = new Bid(bidId, bidder, auction, bidAmount);
        bids.add(bid);

        System.out.println("Bid accepted.");
        return true;
    }

    /**
     * Displays details of a specific auction.
     */
    public void displayAuction(int auctionId) {
        Auction auction = findAuctionById(auctionId);
        if (auction != null) {
            System.out.println(auction);
        } else {
            System.out.println("Auction ID " + auctionId + " not found.");
        }
    }

    /**
     * Closes an active auction.
     */
    public void closeAuction(int auctionId) {
        Auction auction = findAuctionById(auctionId);
        if (auction != null) {
            auction.closeAuction();
            System.out.println("Auction ID " + auctionId + " (" + auction.getProductName() + ") is now CLOSED.");
        }
    }

    /**
     * Returns all registered auctions.
     */
    public List<Auction> getAllAuctions() {
        return new ArrayList<>(auctions);
    }

    /**
     * Returns all bids recorded.
     */
    public List<Bid> getAllBids() {
        return new ArrayList<>(bids);
    }
}
