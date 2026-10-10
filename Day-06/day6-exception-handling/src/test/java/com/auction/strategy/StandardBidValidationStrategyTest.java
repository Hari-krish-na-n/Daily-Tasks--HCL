package com.auction.strategy;

import com.auction.model.AuctionItem;
import com.auction.model.Bidder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for StandardBidValidationStrategy.
 *
 * Each test exercises one rule in isolation so failures are easy to diagnose.
 *
 * Day 5 - Task C tests
 */
class StandardBidValidationStrategyTest {

    private StandardBidValidationStrategy strategy;
    private AuctionItem activeItem;
    private AuctionItem closedItem;
    private Bidder bidder;

    @BeforeEach
    void setUp() {
        strategy = new StandardBidValidationStrategy();

        activeItem = new AuctionItem(1, "Laptop", 50000.0);
        closedItem = new AuctionItem(2, "Phone", 20000.0);
        closedItem.close(); // mark as inactive

        // Bidder has enough budget for most bids in these tests
        bidder = new Bidder(201, "Krishna", "krishna@auction.com", 100000.0);
    }

    @Test
    void valid_bid_above_minimum_increment_is_accepted() {
        // 50000 + 500 = 50500 minimum; 52000 is above that
        assertTrue(strategy.isValidBid(activeItem, bidder, 52000.0));
    }

    @Test
    void bid_exactly_at_minimum_increment_is_accepted() {
        assertTrue(strategy.isValidBid(activeItem, bidder, 50500.0));
    }

    @Test
    void bid_below_minimum_increment_is_rejected() {
        // 49000 < 50500 minimum required
        assertFalse(strategy.isValidBid(activeItem, bidder, 49000.0));
    }

    @Test
    void bid_on_closed_auction_is_rejected() {
        assertFalse(strategy.isValidBid(closedItem, bidder, 25000.0));
    }

    @Test
    void bid_exceeding_bidder_budget_is_rejected() {
        Bidder poorBidder = new Bidder(202, "LowFunds", "low@auction.com", 50000.0);
        // 50000 budget but bid of 55000 exceeds it
        assertFalse(strategy.isValidBid(activeItem, poorBidder, 55000.0));
    }

    @Test
    void strategy_name_is_not_empty() {
        assertNotNull(strategy.getStrategyName());
        assertFalse(strategy.getStrategyName().isBlank());
    }
}
