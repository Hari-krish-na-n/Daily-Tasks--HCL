package com.auction.strategy;

import com.auction.model.AuctionItem;
import com.auction.model.Bidder;

/**
 * StandardBidValidationStrategy - the default validation rules.
 *
 * Rules applied:
 * 1. Auction must still be active.
 * 2. Bid must be above the current bid by at least the minimum increment.
 * 3. Bidder must have enough budget to cover the bid.
 *
 * This is used for regular open auctions where anyone with a budget can bid.
 *
 * Day 5 - Task C: Strategy Pattern - Implementation 1
 */
public class StandardBidValidationStrategy implements BidValidationStrategy {

    // Minimum amount a bid must exceed the current bid
    public static final double MINIMUM_INCREMENT = 500.0;

    @Override
    public boolean isValidBid(AuctionItem item, Bidder bidder, double bidAmount) {
        // Rule 1: Auction must be active
        if (!item.isActive()) {
            System.out.println("[Standard] Rejected: auction is closed.");
            return false;
        }

        // Rule 2: Bid must meet the minimum increment
        double minimumRequired = item.getCurrentBid() + MINIMUM_INCREMENT;
        if (bidAmount < minimumRequired) {
            System.out.println("[Standard] Rejected: bid INR " + (long) bidAmount
                    + " is below minimum required INR " + (long) minimumRequired);
            return false;
        }

        // Rule 3: Bidder must have sufficient budget
        if (bidder.getMaxBudget() < bidAmount) {
            System.out.println("[Standard] Rejected: bidder budget INR "
                    + (long) bidder.getMaxBudget() + " is less than bid amount.");
            return false;
        }

        System.out.println("[Standard] Accepted: bid INR " + (long) bidAmount);
        return true;
    }

    @Override
    public String getStrategyName() {
        return "StandardBidValidation";
    }
}
