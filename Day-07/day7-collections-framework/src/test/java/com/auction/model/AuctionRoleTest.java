package com.auction.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for AuctionRole enum behavior.
 *
 * Day 5 - Task B tests
 */
class AuctionRoleTest {

    @Test
    void bidder_can_bid_and_cannot_list_items() {
        assertTrue(AuctionRole.BIDDER.canBid());
        assertFalse(AuctionRole.BIDDER.canListItems());
    }

    @Test
    void seller_cannot_bid_but_can_list_items() {
        assertFalse(AuctionRole.SELLER.canBid());
        assertTrue(AuctionRole.SELLER.canListItems());
    }

    @Test
    void admin_cannot_bid_but_can_list_items() {
        assertFalse(AuctionRole.ADMIN.canBid());
        assertTrue(AuctionRole.ADMIN.canListItems());
    }

    @Test
    void manager_cannot_bid_but_can_list_items() {
        assertFalse(AuctionRole.MANAGER.canBid());
        assertTrue(AuctionRole.MANAGER.canListItems());
    }

    @Test
    void guest_has_no_permissions() {
        assertFalse(AuctionRole.GUEST.canBid());
        assertFalse(AuctionRole.GUEST.canListItems());
    }

    @Test
    void authority_names_follow_spring_security_convention() {
        assertEquals("ROLE_ADMIN",   AuctionRole.ADMIN.getAuthority());
        assertEquals("ROLE_MANAGER", AuctionRole.MANAGER.getAuthority());
        assertEquals("ROLE_SELLER",  AuctionRole.SELLER.getAuthority());
        assertEquals("ROLE_BIDDER",  AuctionRole.BIDDER.getAuthority());
        assertEquals("ROLE_GUEST",   AuctionRole.GUEST.getAuthority());
    }

    @Test
    void each_role_has_a_non_empty_description() {
        for (AuctionRole role : AuctionRole.values()) {
            assertNotNull(role.getDescription(),
                    role + " should have a description");
            assertFalse(role.getDescription().isBlank(),
                    role + " description should not be blank");
        }
    }
}
