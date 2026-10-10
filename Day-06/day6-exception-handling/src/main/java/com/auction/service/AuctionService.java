package com.auction.service;

import com.auction.exception.InvalidBidException;
import com.auction.model.AuctionItem;
import com.auction.model.Bidder;
import com.auction.strategy.BidValidationStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * AuctionService manages auction items and bid placement.
 *
 * Day 6 Enhancements:
 * - Business Rule 1 (Invalid Bid Amount): A bid must be positive.
 *   Throws InvalidBidException (unchecked) when bidAmount <= 0.
 * - Integrates with AuditService in a finally block to record every bid attempt.
 * - Ensures current bid is NOT updated when validation fails or exception is thrown.
 */
public class AuctionService {

    // Depends on the interface - not on StandardBidValidationStrategy or PremiumBidValidationStrategy
    private final BidValidationStrategy validationStrategy;
    private final AuditService auditService;
    private final List<AuctionItem> items = new ArrayList<>();

    public AuctionService(BidValidationStrategy validationStrategy) {
        this(validationStrategy, null);
    }

    public AuctionService(BidValidationStrategy validationStrategy, AuditService auditService) {
        this.validationStrategy = validationStrategy;
        this.auditService = auditService;
    }

    /** Adds an auction item to the service. */
    public void addItem(AuctionItem item) {
        if (item != null) {
            items.add(item);
        }
    }

    /** Finds an item by its ID. */
    public AuctionItem findById(int id) {
        for (AuctionItem item : items) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    /**
     * Places a bid on an item with robust exception validation and auditing.
     *
     * @param itemId id of the auction item
     * @param bidder bidder placing the bid
     * @param bidAmount amount offered
     * @return true if accepted, false if rejected by strategy rules
     * @throws InvalidBidException if bidAmount <= 0
     * @throws IllegalArgumentException if item ID is not found or bidder is null
     */
    public boolean placeBid(int itemId, Bidder bidder, double bidAmount) {
        boolean success = false;
        String auditDetail = "";

        try {
            // Business Rule: bid amount must be strictly positive
            if (bidAmount <= 0) {
                auditDetail = "Invalid bid amount rejected: " + bidAmount;
                throw new InvalidBidException(
                        "Bid amount must be strictly positive. Given: INR " + bidAmount
                );
            }

            if (bidder == null) {
                auditDetail = "Bid rejected: Bidder cannot be null.";
                throw new IllegalArgumentException("Bidder cannot be null.");
            }

            AuctionItem item = findById(itemId);
            if (item == null) {
                auditDetail = "Bid rejected: Item ID " + itemId + " not found.";
                System.out.println("Bid rejected: item ID " + itemId + " not found.");
                return false;
            }

            // Delegate to strategy
            System.out.println("Using strategy: " + validationStrategy.getStrategyName());
            boolean valid = validationStrategy.isValidBid(item, bidder, bidAmount);

            if (valid) {
                item.setCurrentBid(bidAmount);
                bidder.incrementBidCount();
                success = true;
                auditDetail = String.format("Bid accepted: INR %.2f on item %d (%s) by %s",
                        bidAmount, itemId, item.getProductName(), bidder.getName());
            } else {
                auditDetail = String.format("Bid rejected by strategy: INR %.2f on item %d (%s)",
                        bidAmount, itemId, item.getProductName());
            }

            return valid;

        } finally {
            // Auditing executed unconditionally whether an exception was thrown or bid accepted/rejected
            if (auditService != null) {
                auditService.log("PLACE_BID [Item " + itemId + "]", success, auditDetail);
            }
        }
    }

    /** Returns the name of the currently active validation strategy. */
    public String getActiveStrategyName() {
        return validationStrategy.getStrategyName();
    }

    public List<AuctionItem> getAllItems() {
        return new ArrayList<>(items);
    }
}
