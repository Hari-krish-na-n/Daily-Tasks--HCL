package com.auction.strategy;

import com.auction.model.AuctionItem;
import com.auction.model.Bidder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PremiumBidValidationStrategy.
 *
 * The premium strategy has stricter rules:
 * 1. Auction must be active.
 * 2. Bid must exceed current bid by at least 5%.
 * 3. Bid must be at or above the reserve floor.
 * 4. Only verified bidders (totalBidsPlaced > 0) may bid.
 *
 * Day 5 - Task C tests
 */
class PremiumBidValidationStrategyTest {

    private PremiumBidValidationStrategy strategy;
    private AuctionItem item;      // currentBid = 80000
    private Bidder newBidder;      // totalBidsPlaced = 0
    private Bidder verifiedBidder; // totalBidsPlaced = 1

    @BeforeEach
    void setUp() {
        strategy = new PremiumBidValidationStrategy(80000.0); // floor = 80000

        item = new AuctionItem(2001, "Vintage Watch", 80000.0);

        newBidder = new Bidder(201, "Krishna", "krishna@auction.com", 200000.0);

        verifiedBidder = new Bidder(202, "Meera", "meera@auction.com", 200000.0);
        verifiedBidder.incrementBidCount(); // simulate prior participation
    }

    @Test
    void unverified_bidder_is_rejected_regardless_of_amount() {
        // 80000 * 1.05 = 84000 minimum; bid of 90000 would otherwise qualify
        assertFalse(strategy.isValidBid(item, newBidder, 90000.0),
                "New bidder with no bid history should be rejected");
    }

    @Test
    void bid_below_5_percent_increment_is_rejected() {
        // Current bid 80000; 5% increment requires >= 84000
        // 82000 < 84000, so should fail
        assertFalse(strategy.isValidBid(item, verifiedBidder, 82000.0));
    }

    @Test
    void bid_below_reserve_floor_is_rejected() {
        // Floor is 80000; bid at 75000 is below floor
        assertFalse(strategy.isValidBid(item, verifiedBidder, 75000.0));
    }

    @Test
    void valid_bid_from_verified_bidder_above_all_thresholds_is_accepted() {
        // 80000 * 1.05 = 84000 minimum; 90000 >= 84000, >= 80000 floor, verified bidder
        assertTrue(strategy.isValidBid(item, verifiedBidder, 90000.0));
    }

    @Test
    void bid_on_closed_auction_is_rejected() {
        item.close();
        assertFalse(strategy.isValidBid(item, verifiedBidder, 90000.0));
    }

    @Test
    void strategy_name_includes_floor_amount() {
        String name = strategy.getStrategyName();
        assertNotNull(name);
        assertTrue(name.contains("80000"), "Strategy name should mention the floor amount");
    }
}
