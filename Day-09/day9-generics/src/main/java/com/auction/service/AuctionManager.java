package com.auction.service;

import com.auction.model.Admin;
import com.auction.model.Auction;
import com.auction.model.ListingStatus;
import com.auction.model.Seller;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

/**
 * Auction Listing and Moderation Manager implementing FR2 and FR3.
 *
 * Collection Choices Justified:
 * 1. List<Auction> (implemented as ArrayList<Auction>):
 *    - Master catalogue storing all listings created on the platform.
 *    - ArrayList provides O(1) indexed lookups and fast iteration when displaying all auctions.
 *
 * 2. Deque<Auction> (implemented as ArrayDeque<Auction>):
 *    - Pending moderation queue.
 *    - FIFO ordering: Sellers who submit listings first get moderated first (fair queuing).
 *    - ArrayDeque offers amortized O(1) insertion at tail (offer) and extraction from head (poll).
 *    - Memory efficient: backed by a circular array without node allocation overhead.
 */
public class AuctionManager {

    // MASTER CATALOGUE: ArrayList stores all created auctions in creation order
    private final List<Auction> allAuctions = new ArrayList<>();

    // MODERATION QUEUE: ArrayDeque provides a FIFO moderation queue for pending listings
    private final Deque<Auction> pendingModerationQueue = new ArrayDeque<>();

    private int nextAuctionId = 1000;

    public AuctionManager() {
        seedSampleAuctions();
    }

    private void seedSampleAuctions() {
        Seller sellerRavi = new Seller(101, "ravi", "ravi123", "Ravi", "ravi@auction.com", "Ravi Electronics", 4.9);
        
        // 1. Approved listing
        Auction a1 = createListing(sellerRavi, "MacBook Pro M3", "16GB RAM, 512GB SSD Space Gray",
                120000.0, 140000.0, LocalDateTime.now(), LocalDateTime.now().plusDays(5));
        moderateListing(a1.getId(), "admin", true, "High demand genuine item approved.");

        // 2. Pending listing awaiting admin
        createListing(sellerRavi, "Sony WH-1000XM5 Headphones", "Active noise cancellation wireless headphones",
                18000.0, 22000.0, LocalDateTime.now(), LocalDateTime.now().plusDays(3));

        // 3. Rejected listing
        Auction a3 = createListing(sellerRavi, "Replica Luxury Watch", "First copy designer watch",
                3000.0, 5000.0, LocalDateTime.now(), LocalDateTime.now().plusDays(2));
        moderateListing(a3.getId(), "admin", false, "Policy violation: counterfeit or replica goods not permitted.");
    }

    /**
     * FR2: Create Auction Listing
     * Validates seller authority, prices, and dates. Newly submitted listings
     * are stored in the master list and added to the pending FIFO queue.
     */
    public synchronized Auction createListing(Seller seller, String productName, String description,
                                             double startingPrice, double reservePrice,
                                             LocalDateTime startTime, LocalDateTime endTime) {
        if (seller == null) {
            throw new IllegalArgumentException("Listing error: A valid seller is required.");
        }
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("Listing error: Product name cannot be empty.");
        }
        if (startingPrice <= 0) {
            throw new IllegalArgumentException("Listing error: Starting price must be strictly positive (greater than 0).");
        }
        if (reservePrice < startingPrice) {
            throw new IllegalArgumentException("Listing error: Reserve price (INR " + reservePrice
                    + ") cannot be lower than starting price (INR " + startingPrice + ").");
        }
        if (startTime == null) {
            startTime = LocalDateTime.now();
        }
        if (endTime == null || !endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("Listing error: Auction end time must be after the start time.");
        }

        int auctionId = ++nextAuctionId;
        Auction auction = new Auction(auctionId, productName.trim(), seller,
                startingPrice, reservePrice, startTime, endTime);
        if (description != null && !description.trim().isEmpty()) {
            auction.setDescription(description.trim());
        }

        // Listing begins in PENDING status
        auction.setStatus(ListingStatus.PENDING);

        // Add to master catalogue
        allAuctions.add(auction);

        // Add to FIFO pending moderation queue
        pendingModerationQueue.offer(auction);

        return auction;
    }

    /**
     * FR3: Admin Moderation
     * Allows an admin to approve or reject a listing by ID.
     * Prevents repeated moderation of already decided listings.
     */
    public synchronized boolean moderateListing(int auctionId, String adminUsername, boolean approve, String remarks) {
        Auction auction = findById(auctionId);
        if (auction == null) {
            throw new IllegalArgumentException("Moderation error: Auction with ID " + auctionId + " not found.");
        }

        // Business Rule: moderated listings cannot be moderated repeatedly
        if (auction.getStatus() != ListingStatus.PENDING) {
            throw new IllegalStateException("Moderation error: Auction #" + auctionId
                    + " has already been moderated with status '" + auction.getStatus().getDisplayLabel() + "'.");
        }

        if (approve) {
            auction.setStatus(ListingStatus.APPROVED);
        } else {
            auction.setStatus(ListingStatus.REJECTED);
        }

        auction.setModeratedBy(adminUsername != null ? adminUsername : "admin");
        auction.setModerationNote(remarks != null ? remarks.trim() : (approve ? "Approved by admin" : "Rejected by admin"));

        // Remove from pending queue
        pendingModerationQueue.removeIf(a -> a.getId() == auctionId);

        return true;
    }

    /**
     * Finds an auction by ID.
     */
    public Auction findById(int auctionId) {
        for (Auction a : allAuctions) {
            if (a.getId() == auctionId) {
                return a;
            }
        }
        return null;
    }

    /**
     * Returns all auctions (Master ArrayList snapshot).
     */
    public List<Auction> getAllAuctions() {
        return new ArrayList<>(allAuctions);
    }

    /**
     * Returns approved active auctions available for bidding.
     */
    public List<Auction> getApprovedAuctions() {
        List<Auction> approved = new ArrayList<>();
        for (Auction a : allAuctions) {
            if (a.getStatus() == ListingStatus.APPROVED) {
                approved.add(a);
            }
        }
        return approved;
    }

    /**
     * Returns pending moderation auctions as a list snapshot.
     */
    public List<Auction> getPendingAuctions() {
        return new ArrayList<>(pendingModerationQueue);
    }

    /**
     * Returns the head of the FIFO pending queue without removing it.
     */
    public Auction peekNextPending() {
        return pendingModerationQueue.peek();
    }

    public int getPendingCount() {
        return pendingModerationQueue.size();
    }
}
