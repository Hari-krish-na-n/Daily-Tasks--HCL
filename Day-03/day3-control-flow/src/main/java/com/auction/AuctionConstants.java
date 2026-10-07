package com.auction;

/**
 * AuctionConstants.java
 * -----------------------------------------------------------
 * Day 3 — Business Rule Constants for Online Auction System
 *
 * Eliminates magic numbers across the application.
 */
public final class AuctionConstants {

    // Bidding Rules
    public static final double MINIMUM_BID_INCREMENT = 500.0;
    public static final int ANTI_SNIPE_MINUTES = 2;
    public static final int ANTI_SNIPE_EXTENSION_MINUTES = 2;

    // Security & Authentication Rules
    public static final int MAX_LOGIN_ATTEMPTS = 3;
    public static final int DEFAULT_DEMO_PIN = 1234;

    // Financial & Listing Rules
    public static final double MINIMUM_STARTING_PRICE = 100.0;
    public static final double PLATFORM_FEE_PERCENTAGE = 5.0;
    public static final int MAX_ACTIVE_AUCTIONS_PER_SELLER = 10;

    private AuctionConstants() {
        // Utility class: prevent instantiation
    }
}
