package com.auction;

/**
 * AuctionConstants.java
 * ----------------------
 * Day 2 — Language Fundamentals: Constants
 *
 * Constants are values that NEVER change during the program.
 * In Java we declare constants using:
 *   public static final TYPE NAME = value;
 *
 *   - public  : accessible from any class
 *   - static  : belongs to the class itself, not an object instance
 *   - final   : once set, the value cannot be changed
 *
 * WHY USE CONSTANTS?
 *   ✓ Avoid "magic numbers" scattered through the code
 *   ✓ Change the rule in ONE place — applies everywhere
 *   ✓ Self-documenting: the name tells you what the number means
 *
 * These constants represent the business rules of our
 * Online Auction System.
 */
public class AuctionConstants {

    // ----------------------------------------------------------------
    // Bidding Rules
    // ----------------------------------------------------------------

    /**
     * The smallest amount by which a new bid must exceed the current
     * highest bid. (FR4 — Bidding requirement)
     * Example: if current highest bid = 50,000 then
     *          minimum next bid = 50,000 + 500 = 50,500
     */
    public static final double MINIMUM_BID_INCREMENT = 500.0;

    /**
     * If a valid bid arrives within this many minutes before the auction
     * ends, the end time is extended. (FR6 — Anti-Sniping requirement)
     */
    public static final int ANTI_SNIPE_MINUTES = 2;

    /**
     * How many minutes the end time is extended when anti-sniping triggers.
     */
    public static final int ANTI_SNIPE_EXTENSION_MINUTES = 5;

    // ----------------------------------------------------------------
    // Financial Rules
    // ----------------------------------------------------------------

    /**
     * The percentage the auction platform keeps from the final sale price.
     * Example: sale price = 100,000 → platform fee = 5,000
     */
    public static final double PLATFORM_FEE_PERCENTAGE = 5.0;

    /**
     * The minimum starting price allowed for any auction listing.
     */
    public static final double MINIMUM_STARTING_PRICE = 100.0;

    // ----------------------------------------------------------------
    // Auction Grading Thresholds
    // ----------------------------------------------------------------

    /**
     * If the winning bid is at or above this amount, the auction is
     * graded 'A' (Premium). Otherwise it is graded 'B' (Standard).
     */
    public static final double GRADE_A_THRESHOLD = 100_000.0;  // underscores improve readability

    // ----------------------------------------------------------------
    // Auction Limits
    // ----------------------------------------------------------------

    /**
     * Maximum number of items a single seller can have in active auctions
     * at the same time.
     */
    public static final int MAX_ACTIVE_AUCTIONS_PER_SELLER = 10;

    /**
     * Maximum number of bids stored per auction in our demo array.
     */
    public static final int MAX_BIDS_PER_AUCTION = 20;

    // ----------------------------------------------------------------
    // Constructor is private — this is a utility class.
    // You should NOT create objects of this class.
    // Just use:  AuctionConstants.MINIMUM_BID_INCREMENT
    // ----------------------------------------------------------------
    private AuctionConstants() {
        // Utility class — no instances allowed
    }
}
