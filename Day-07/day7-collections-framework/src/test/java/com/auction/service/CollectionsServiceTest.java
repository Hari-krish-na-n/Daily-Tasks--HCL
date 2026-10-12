package com.auction.service;

import com.auction.model.Admin;
import com.auction.model.Auction;
import com.auction.model.AuctionRole;
import com.auction.model.Bidder;
import com.auction.model.ListingStatus;
import com.auction.model.Seller;
import com.auction.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests verifying Collections Framework implementation for FR1, FR2, and FR3.
 */
class CollectionsServiceTest {

    private AuthService authService;
    private AuctionManager auctionManager;
    private Seller testSeller;

    @BeforeEach
    void setUp() {
        authService = new AuthService();
        auctionManager = new AuctionManager();
        testSeller = new Seller(301, "testseller", "pass123", "Test Seller", "seller@test.com", "Gadgets", 4.8);
    }

    // -------------------------------------------------------------
    // FR1: Authentication & User Registration Tests
    // -------------------------------------------------------------
    @Test
    @DisplayName("FR1: Successfully registers Bidder and stores in list and set")
    void testRegisterUserSuccess() {
        User user = authService.registerUser("arjun", "pass123", "Arjun Kumar", "arjun@test.com",
                AuctionRole.BIDDER, "", 50000.0);

        assertNotNull(user);
        assertEquals("arjun", user.getUsername());
        assertEquals(AuctionRole.BIDDER, user.getRole());
        assertTrue(authService.isUsernameTaken("arjun"));
        assertTrue(authService.isUsernameTaken("ARJUN"), "Username check should be case-insensitive");
    }

    @Test
    @DisplayName("FR1: Duplicate username rejected via HashSet lookup")
    void testRegisterDuplicateUsernameRejected() {
        authService.registerUser("deepak", "pass123", "Deepak", "deepak@test.com",
                AuctionRole.BIDDER, "", 25000.0);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                authService.registerUser("deepak", "newpass", "Deepak Second", "deepak2@test.com",
                        AuctionRole.BIDDER, "", 30000.0)
        );

        assertTrue(ex.getMessage().contains("already registered"));
    }

    @Test
    @DisplayName("FR1: Passwords are not printed in User.toString() or getSummary()")
    void testPasswordSecurityInUserRepresentation() {
        User user = authService.registerUser("securityuser", "SuperSecret999", "Secure User",
                "secure@test.com", AuctionRole.BIDDER, "", 40000.0);

        assertFalse(user.toString().contains("SuperSecret999"), "Password must not be in toString()");
        assertFalse(user.getSummary().contains("SuperSecret999"), "Password must not be in getSummary()");
        assertTrue(user.verifyPassword("SuperSecret999"));
        assertFalse(user.verifyPassword("wrongpass"));
    }

    @Test
    @DisplayName("FR1: User login verifies valid credentials and rejects bad passwords")
    void testUserLogin() {
        User loggedIn = authService.login("ravi", "ravi123");
        assertNotNull(loggedIn, "Should succeed with seeded credentials");
        assertEquals("Ravi", loggedIn.getName());

        User badPass = authService.login("ravi", "wrongpass");
        assertNull(badPass, "Login should fail with invalid password");

        User nonExistent = authService.login("ghost_user", "pass");
        assertNull(nonExistent, "Login should fail for non-existent user");
    }

    // -------------------------------------------------------------
    // FR2: Auction Listing Creation Tests
    // -------------------------------------------------------------
    @Test
    @DisplayName("FR2: Seller creates auction listing which begins in PENDING status")
    void testCreateAuctionListingPendingStatus() {
        Auction auction = auctionManager.createListing(testSeller, "Mechanical Keyboard",
                "RGB hot-swappable keyboard", 4000.0, 5000.0,
                LocalDateTime.now(), LocalDateTime.now().plusDays(5));

        assertNotNull(auction);
        assertTrue(auction.getId() > 0);
        assertEquals(ListingStatus.PENDING, auction.getStatus());
        assertTrue(auction.isPending());
        assertFalse(auction.isApproved());
        assertFalse(auction.isRejected());

        // Verified present in pending queue
        assertTrue(auctionManager.getPendingAuctions().stream()
                .anyMatch(a -> a.getId() == auction.getId()));
    }

    @Test
    @DisplayName("FR2: Listing creation rejects invalid price parameters")
    void testCreateListingInvalidPrices() {
        // Zero or negative starting price
        assertThrows(IllegalArgumentException.class, () ->
                auctionManager.createListing(testSeller, "Item", "Desc", -10.0, 100.0,
                        LocalDateTime.now(), LocalDateTime.now().plusDays(2))
        );

        // Reserve price less than starting price
        assertThrows(IllegalArgumentException.class, () ->
                auctionManager.createListing(testSeller, "Item", "Desc", 500.0, 400.0,
                        LocalDateTime.now(), LocalDateTime.now().plusDays(2))
        );
    }

    // -------------------------------------------------------------
    // FR3: Admin Moderation Tests
    // -------------------------------------------------------------
    @Test
    @DisplayName("FR3: Admin approves pending listing, updating status to APPROVED")
    void testAdminApproveListing() {
        Auction auction = auctionManager.createListing(testSeller, "Gaming Monitor 144Hz",
                "IPS 1ms gaming monitor", 15000.0, 18000.0,
                LocalDateTime.now(), LocalDateTime.now().plusDays(3));

        boolean result = auctionManager.moderateListing(auction.getId(), "admin", true, "Hardware verified");
        assertTrue(result);

        Auction updated = auctionManager.findById(auction.getId());
        assertEquals(ListingStatus.APPROVED, updated.getStatus());
        assertTrue(updated.isApproved());
        assertEquals("admin", updated.getModeratedBy());

        // Removed from pending queue
        assertFalse(auctionManager.getPendingAuctions().stream()
                .anyMatch(a -> a.getId() == auction.getId()));
    }

    @Test
    @DisplayName("FR3: Admin rejects listing, preventing it from being treated as approved")
    void testAdminRejectListing() {
        Auction auction = auctionManager.createListing(testSeller, "Prohibited Laser Pointer",
                "High powered laser", 2000.0, 3000.0,
                LocalDateTime.now(), LocalDateTime.now().plusDays(3));

        boolean result = auctionManager.moderateListing(auction.getId(), "admin", false, "Policy violation");
        assertTrue(result);

        Auction updated = auctionManager.findById(auction.getId());
        assertEquals(ListingStatus.REJECTED, updated.getStatus());
        assertTrue(updated.isRejected());
        assertFalse(updated.isApproved(), "Rejected listing must never be treated as approved");
    }

    @Test
    @DisplayName("FR3: Moderated listing cannot be moderated repeatedly")
    void testRepeatedModerationForbidden() {
        Auction auction = auctionManager.createListing(testSeller, "Tablet Computer",
                "10 inch Android tablet", 8000.0, 10000.0,
                LocalDateTime.now(), LocalDateTime.now().plusDays(3));

        auctionManager.moderateListing(auction.getId(), "admin", true, "Approved first time");

        // Attempting to moderate again should throw IllegalStateException
        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                auctionManager.moderateListing(auction.getId(), "admin", false, "Trying to reject after approve")
        );

        assertTrue(ex.getMessage().contains("already been moderated"));
    }
}
