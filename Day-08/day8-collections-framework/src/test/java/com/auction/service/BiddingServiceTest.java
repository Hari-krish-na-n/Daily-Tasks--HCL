package com.auction.service;

import com.auction.model.Auction;
import com.auction.model.Bid;
import com.auction.model.BidResult;
import com.auction.model.Bidder;
import com.auction.model.Seller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests verifying FR4 (Bidding validation), FR5 (Proxy/Auto-bidding),
 * and FR6 (Anti-sniping auction deadline extension).
 */
public class BiddingServiceTest {

    private BiddingService biddingService;
    private Auction testAuction;
    private Bidder bidderKrishna;
    private Bidder bidderMeera;
    private Bidder bidderRohan;
    private LocalDateTime auctionStartTime;
    private LocalDateTime auctionEndTime;

    @BeforeEach
    void setUp() {
        biddingService = new BiddingService();
        Seller seller = new Seller(1, "seller1", "pass", "Ravi", "ravi@auction.com", "Ravi Store", 4.8);
        auctionStartTime = LocalDateTime.of(2026, 10, 15, 10, 0);
        auctionEndTime = LocalDateTime.of(2026, 10, 15, 12, 0); // 2 hours duration

        testAuction = new Auction(1001, "Vintage Camera", seller, 10000.0, 15000.0,
                auctionStartTime, auctionEndTime);

        biddingService.registerAuction(testAuction);

        bidderKrishna = new Bidder(201, "krishna", "pass", "Krishna", "krishna@auction.com", 100000.0);
        bidderMeera = new Bidder(202, "meera", "pass", "Meera", "meera@auction.com", 100000.0);
        bidderRohan = new Bidder(203, "rohan", "pass", "Rohan", "rohan@auction.com", 100000.0);
    }

    // =========================================================================
    // FR4: BIDDING VALIDATION TESTS
    // =========================================================================

    @Test
    @DisplayName("FR4: Valid first bid at or above starting price succeeds")
    void testValidFirstBid() {
        LocalDateTime bidTime = auctionStartTime.plusMinutes(10);
        BidResult result = biddingService.placeBid(1001, bidderKrishna, 10000.0, bidTime);

        assertTrue(result.isSuccess());
        assertEquals(10000.0, testAuction.getCurrentBid());
        assertEquals("Krishna", result.getCurrentHighestBidder());
    }

    @Test
    @DisplayName("FR4: First bid below starting price is rejected")
    void testFirstBidBelowStartingPriceRejected() {
        LocalDateTime bidTime = auctionStartTime.plusMinutes(10);
        BidResult result = biddingService.placeBid(1001, bidderKrishna, 9500.0, bidTime);

        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("must be at least the starting price"));
        assertEquals(10000.0, testAuction.getCurrentBid());
    }

    @Test
    @DisplayName("FR4: Subsequent bid meeting min increment (>= current + 500) succeeds")
    void testSubsequentBidValidIncrement() {
        LocalDateTime t1 = auctionStartTime.plusMinutes(10);
        biddingService.placeBid(1001, bidderKrishna, 10000.0, t1);

        LocalDateTime t2 = auctionStartTime.plusMinutes(15);
        BidResult result2 = biddingService.placeBid(1001, bidderMeera, 10500.0, t2);

        assertTrue(result2.isSuccess());
        assertEquals(10500.0, testAuction.getCurrentBid());
        assertEquals("Meera", result2.getCurrentHighestBidder());
    }

    @Test
    @DisplayName("FR4: Subsequent bid below minimum increment is rejected")
    void testSubsequentBidBelowMinIncrementRejected() {
        LocalDateTime t1 = auctionStartTime.plusMinutes(10);
        biddingService.placeBid(1001, bidderKrishna, 10000.0, t1);

        LocalDateTime t2 = auctionStartTime.plusMinutes(15);
        // Minimum required is 10000 + 500 = 10500. Placing 10200 should fail.
        BidResult result2 = biddingService.placeBid(1001, bidderMeera, 10200.0, t2);

        assertFalse(result2.isSuccess());
        assertTrue(result2.getMessage().contains("below the minimum required"));
        assertEquals(10000.0, testAuction.getCurrentBid());
    }

    @Test
    @DisplayName("FR4: Bid placed after auction end time is rejected")
    void testBidAfterAuctionEndedRejected() {
        LocalDateTime pastEndTime = auctionEndTime.plusMinutes(1);
        BidResult result = biddingService.placeBid(1001, bidderKrishna, 15000.0, pastEndTime);

        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("already ended"));
    }

    // =========================================================================
    // FR5: PROXY / AUTO-BIDDING TESTS
    // =========================================================================

    @Test
    @DisplayName("FR5: Proxy bidder automatically counters competing bids up to maximum limit")
    void testProxyBiddingAutoOutbidsCompetitor() {
        // Meera sets a proxy bid maximum of 13,000 INR
        biddingService.setProxyBid(1001, bidderMeera.getId(), 13000.0);
        assertEquals(13000.0, biddingService.getProxyBid(1001, bidderMeera.getId()));

        // Krishna places a manual bid of 10,000 INR
        LocalDateTime t1 = auctionStartTime.plusMinutes(10);
        BidResult result = biddingService.placeBid(1001, bidderKrishna, 10000.0, t1);

        assertTrue(result.isSuccess());
        // Since Meera had proxy up to 13,000, system automatically outbids Krishna:
        // Needed: 10,000 + 500 = 10,500 INR
        assertEquals(10500.0, testAuction.getCurrentBid());
        assertEquals(10500.0, result.getCurrentHighestBid());
        assertTrue(result.getCurrentHighestBidder().contains("ProxyBidder#202"));

        // Krishna now places a higher bid of 12,000 INR
        LocalDateTime t2 = auctionStartTime.plusMinutes(20);
        BidResult result2 = biddingService.placeBid(1001, bidderKrishna, 12000.0, t2);
        assertTrue(result2.isSuccess());
        // Meera's proxy triggers again: 12,000 + 500 = 12,500 <= 13,000
        assertEquals(12500.0, testAuction.getCurrentBid());

        // Now Krishna bids 13,500 INR (exceeding Meera's proxy limit of 13,000)
        LocalDateTime t3 = auctionStartTime.plusMinutes(30);
        BidResult result3 = biddingService.placeBid(1001, bidderKrishna, 13500.0, t3);
        assertTrue(result3.isSuccess());
        // Meera cannot outbid anymore
        assertEquals(13500.0, testAuction.getCurrentBid());
        assertEquals("Krishna", result3.getCurrentHighestBidder());
    }

    // =========================================================================
    // FR6: ANTI-SNIPING TESTS
    // =========================================================================

    @Test
    @DisplayName("FR6: Bid arriving within final 2 minutes triggers 5-minute deadline extension")
    void testAntiSnipingExtendsDeadlineInFinalTwoMinutes() {
        // Auction ends at 12:00:00.
        // Bid placed at 11:58:30 (90 seconds before end <= 120s anti-sniping window)
        LocalDateTime lateBidTime = auctionEndTime.minusSeconds(90);

        BidResult result = biddingService.placeBid(1001, bidderKrishna, 10000.0, lateBidTime);

        assertTrue(result.isSuccess());
        assertTrue(result.isDeadlineExtended(), "Deadline should be marked as extended");

        LocalDateTime expectedNewEnd = auctionEndTime.plusMinutes(5);
        assertEquals(expectedNewEnd, testAuction.getEndTime(), "End time should be extended by exactly 5 minutes");
        assertEquals(expectedNewEnd, result.getNewDeadline());
    }

    @Test
    @DisplayName("FR6: Bid arriving outside final 2 minutes does NOT extend deadline")
    void testBidOutsideAntiSnipingWindowDoesNotExtendDeadline() {
        // Auction ends at 12:00:00.
        // Bid placed at 11:55:00 (5 minutes before end > 120s anti-sniping window)
        LocalDateTime earlyBidTime = auctionEndTime.minusMinutes(5);

        BidResult result = biddingService.placeBid(1001, bidderKrishna, 10000.0, earlyBidTime);

        assertTrue(result.isSuccess());
        assertFalse(result.isDeadlineExtended(), "Deadline should NOT be extended");
        assertEquals(auctionEndTime, testAuction.getEndTime(), "Auction end time should remain unchanged");
    }
}
