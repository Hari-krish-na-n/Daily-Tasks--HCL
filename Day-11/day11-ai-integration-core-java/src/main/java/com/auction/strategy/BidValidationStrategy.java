package com.auction.strategy;

import com.auction.model.AuctionItem;
import com.auction.model.Bidder;

/**
 * BidValidationStrategy is the Strategy interface for bid validation.
 *
 * Using an interface here means we can swap validation rules at runtime
 * without changing any of the calling code (runtime polymorphism).
 *
 * Concrete strategies implement their own validation logic
 * independently, making them easy to test in isolation.
 *
 * Day 5 - Task C: Strategy Pattern
 */
public interface BidValidationStrategy {

    /**
     * Validates whether the given bid amount is acceptable for the auction.
     *
     * @param item      the auction item being bid on
     * @param bidder    the bidder placing the bid
     * @param bidAmount the amount the bidder wants to bid
     * @return true if the bid should be accepted, false otherwise
     */
    boolean isValidBid(AuctionItem item, Bidder bidder, double bidAmount);

    /**
     * Returns a short description of this strategy, useful for logging.
     */
    String getStrategyName();
}
