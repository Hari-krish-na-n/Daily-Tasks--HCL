package com.auction.service;

import com.auction.model.AuctionItem;
import com.auction.model.Bidder;
import com.auction.strategy.BidValidationStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * AuctionService manages auction items and bid placement.
 *
 * The key design change from Day 4:
 * - Bid validation is now delegated to a BidValidationStrategy.
 * - AuctionService depends on the interface, not a specific implementation.
 * - The strategy can be changed at construction time (runtime polymorphism).
 *
 * This avoids a large if/else chain inside placeBid and makes each
 * validation rule independently testable.
 *
 * Day 5 - Task C: Strategy Pattern in a service
 */
public class AuctionService {

    // Depends on the interface - not on StandardBidValidationStrategy or PremiumBidValidationStrategy
    private final BidValidationStrategy validationStrategy;

    private final List<AuctionItem> items = new ArrayList<>();

    /**
     * Constructor injection: the caller decides which strategy to use.
     * This is how Spring would inject it in a real Spring Boot application.
     */
    public AuctionService(BidValidationStrategy validationStrategy) {
        this.validationStrategy = validationStrategy;
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
     * Places a bid on an item.
     * The strategy decides whether the bid is valid - no validation logic lives here.
     *
     * @return true if the bid was accepted and recorded, false otherwise
     */
    public boolean placeBid(int itemId, Bidder bidder, double bidAmount) {
        AuctionItem item = findById(itemId);

        if (item == null) {
            System.out.println("Bid rejected: item ID " + itemId + " not found.");
            return false;
        }

        // Delegate to whatever strategy is injected - runtime polymorphism
        System.out.println("Using strategy: " + validationStrategy.getStrategyName());
        boolean valid = validationStrategy.isValidBid(item, bidder, bidAmount);

        if (valid) {
            item.setCurrentBid(bidAmount);
            bidder.incrementBidCount();
        }

        return valid;
    }

    /** Returns the name of the currently active validation strategy. */
    public String getActiveStrategyName() {
        return validationStrategy.getStrategyName();
    }

    public List<AuctionItem> getAllItems() {
        return new ArrayList<>(items);
    }
}
