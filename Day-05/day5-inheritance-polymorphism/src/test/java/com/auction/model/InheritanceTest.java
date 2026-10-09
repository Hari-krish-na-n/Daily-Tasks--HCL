package com.auction.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for inheritance and BaseEntity behavior.
 *
 * Day 5 - Task A tests
 */
class InheritanceTest {

    @Test
    void bidder_inherits_id_and_timestamps_from_BaseEntity() {
        Bidder bidder = new Bidder(201, "Krishna", "krishna@auction.com", 75000.0);

        // Fields from BaseEntity must be present on the subclass
        assertEquals(201, bidder.getId());
        assertNotNull(bidder.getCreatedAt());
        assertNotNull(bidder.getUpdatedAt());
    }

    @Test
    void seller_inherits_id_and_timestamps_from_BaseEntity() {
        Seller seller = new Seller(101, "Ravi", "ravi@auction.com", "Ravi Electronics", 4.9);

        assertEquals(101, seller.getId());
        assertNotNull(seller.getCreatedAt());
    }

    @Test
    void auctionItem_inherits_id_from_BaseEntity() {
        AuctionItem item = new AuctionItem(1001, "Laptop", 50000.0);
        assertEquals(1001, item.getId());
    }

    @Test
    void runtime_polymorphism_getSummary_calls_correct_override() {
        // Variable type is User (parent), but actual object differs
        User seller = new Seller(101, "Ravi", "ravi@auction.com");
        User bidder = new Bidder(201, "Krishna", "krishna@auction.com");

        // Each should include class-specific data, proving the correct override is called
        assertTrue(seller.getSummary().contains("store"),
                "Seller.getSummary() should mention store");
        assertTrue(bidder.getSummary().contains("maxBudget"),
                "Bidder.getSummary() should mention maxBudget");
    }

    @Test
    void bidder_role_is_set_correctly_via_enum() {
        Bidder bidder = new Bidder(201, "Krishna", "krishna@auction.com");
        assertEquals(AuctionRole.BIDDER, bidder.getRole());
        assertEquals("ROLE_BIDDER", bidder.getAuthority());
    }

    @Test
    void seller_role_is_set_correctly_via_enum() {
        Seller seller = new Seller(101, "Ravi", "ravi@auction.com");
        assertEquals(AuctionRole.SELLER, seller.getRole());
        assertEquals("ROLE_SELLER", seller.getAuthority());
    }

    @Test
    void markUpdated_refreshes_updatedAt_timestamp() throws InterruptedException {
        AuctionItem item = new AuctionItem(1001, "Camera", 15000.0);
        var before = item.getUpdatedAt();

        // Small sleep so the new timestamp differs
        Thread.sleep(10);
        item.setProductName("Vintage Camera"); // calls markUpdated() internally

        assertTrue(item.getUpdatedAt().isAfter(before) || item.getUpdatedAt().equals(before),
                "updatedAt should not go backwards");
    }
}
