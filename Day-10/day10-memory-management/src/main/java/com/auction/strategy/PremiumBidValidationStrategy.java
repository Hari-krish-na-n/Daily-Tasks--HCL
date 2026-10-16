package com.auction.strategy;

import com.auction.model.AuctionItem;
import com.auction.model.Bidder;

/**
 * PremiumBidValidationStrategy - stricter rules for high-value auctions.
 *
 * Rules applied (in addition to basic checks):
 * 1. Auction must still be active.
 * 2. Bid must exceed the current bid by at least the premium increment (5%).
 * 3. Bid amount must be at or above the configured reserve price floor.
 * 4. Bidder must have placed at least one previous bid (verified bidder).
 *
 * This strategy is used for rare or premium-listed items where the platform
 * wants to filter out low-effort or unserious bidders.
 *
 * Day 5 - Task C: Strategy Pattern - Implementation 2
 */
public class PremiumBidValidationStrategy implements BidValidationStrategy {

    // Minimum percentage increment for premium auctions
    private static final double INCREMENT_PERCENT = 0.05; // 5%

    // Minimum bid floor for premium items
    private final double reservePriceFloor;

    public PremiumBidValidationStrategy(double reservePriceFloor) {
        this.reservePriceFloor = reservePriceFloor;
    }

    @Override
    public boolean isValidBid(AuctionItem item, Bidder bidder, double bidAmount) {
        // Rule 1: Auction must be active
        if (!item.isActive()) {
            System.out.println("[Premium] Rejected: auction is closed.");
            return false;
        }

        // Rule 2: Bid must exceed current bid by at least 5%
        double minimumRequired = item.getCurrentBid() * (1 + INCREMENT_PERCENT);
        if (bidAmount < minimumRequired) {
            System.out.println("[Premium] Rejected: bid INR " + (long) bidAmount
                    + " does not meet 5% increment requirement. Minimum: INR "
                    + (long) minimumRequired);
            return false;
        }

        // Rule 3: Bid must be at or above the reserve floor
        if (bidAmount < reservePriceFloor) {
            System.out.println("[Premium] Rejected: bid INR " + (long) bidAmount
                    + " is below the reserve floor INR " + (long) reservePriceFloor);
            return false;
        }

        // Rule 4: Only verified bidders (those who have bid before) can participate
        if (bidder.getTotalBidsPlaced() == 0) {
            System.out.println("[Premium] Rejected: bidder " + bidder.getName()
                    + " has no prior bid history. Premium auctions require a verified bidder.");
            return false;
        }

        System.out.println("[Premium] Accepted: bid INR " + (long) bidAmount
                + " by verified bidder " + bidder.getName());
        return true;
    }

    @Override
    public String getStrategyName() {
        return "PremiumBidValidation(floor=" + (long) reservePriceFloor + ")";
    }
}
