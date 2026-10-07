package com.auction;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AuctionConsoleAppTest.java
 * -----------------------------------------------------------
 * Unit tests validating control-flow decisions and domain rules
 * for the Online Auction System (Day 3).
 */
class AuctionConsoleAppTest {

    @Test
    @DisplayName("FR4 - Bid >= current + minimum increment must be accepted")
    void testBidValidation_Accept() {
        double currentBid = 50000.0;
        double minIncrement = AuctionConstants.MINIMUM_BID_INCREMENT; // 500.0
        double validBid = 50500.0;

        assertTrue(AuctionConsoleApp.validateBid(validBid, currentBid, minIncrement),
                "Bid of ₹50,500 on current ₹50,000 must be accepted.");

        double higherBid = 55000.0;
        assertTrue(AuctionConsoleApp.validateBid(higherBid, currentBid, minIncrement),
                "Bid of ₹55,000 must be accepted.");
    }

    @Test
    @DisplayName("FR4 - Bid < current + minimum increment must be rejected")
    void testBidValidation_Reject() {
        double currentBid = 50000.0;
        double minIncrement = AuctionConstants.MINIMUM_BID_INCREMENT; // 500.0

        double insufficientBid = 50400.0;
        assertFalse(AuctionConsoleApp.validateBid(insufficientBid, currentBid, minIncrement),
                "Bid of ₹50,400 does not meet the minimum increment of ₹500 and must be rejected.");

        double underBid = 49000.0;
        assertFalse(AuctionConsoleApp.validateBid(underBid, currentBid, minIncrement),
                "Bid of ₹49,000 is below current bid and must be rejected.");
    }

    @Test
    @DisplayName("FR7 - Highest bid >= reserve price declares winner")
    void testReservePrice_Met() {
        double reservePrice = 60000.0;
        double winningBid = 62000.0;

        assertTrue(AuctionConsoleApp.isReservePriceMet(winningBid, reservePrice),
                "Highest bid of ₹62,000 meets reserve price of ₹60,000.");
    }

    @Test
    @DisplayName("FR7 - Highest bid < reserve price marks auction unsold")
    void testReservePrice_NotMet() {
        double reservePrice = 60000.0;
        double losingBid = 55000.0;

        assertFalse(AuctionConsoleApp.isReservePriceMet(losingBid, reservePrice),
                "Highest bid of ₹55,000 fails reserve price of ₹60,000.");
    }

    @Test
    @DisplayName("FR6 - Anti-sniping triggers when minutes remaining <= threshold")
    void testAntiSnipingTrigger() {
        int threshold = AuctionConstants.ANTI_SNIPE_MINUTES; // 2 minutes

        assertTrue(AuctionConsoleApp.isAntiSnipeTriggered(1, threshold),
                "Bid placed with 1 minute remaining must trigger anti-sniping extension.");
        assertTrue(AuctionConsoleApp.isAntiSnipeTriggered(2, threshold),
                "Bid placed with 2 minutes remaining must trigger anti-sniping extension.");
        assertFalse(AuctionConsoleApp.isAntiSnipeTriggered(5, threshold),
                "Bid placed with 5 minutes remaining must not trigger anti-sniping extension.");
    }

    @Test
    @DisplayName("Control Flow - Safe input parser handles invalid inputs without crashing")
    void testSafeInputValidation_RecoversFromInvalidInput() {
        // Simulates invalid entries: 'abc', '-1', '99', empty line, and finally valid '4'
        String simulatedUserInput = "abc\n-1\n99\n\n4\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(simulatedUserInput.getBytes(StandardCharsets.UTF_8)));

        int result = AuctionConsoleApp.readValidatedMenuChoice(scanner);
        assertEquals(4, result, "Menu input validator must safely ignore invalid inputs and return 4.");
    }

    @Test
    @DisplayName("Domain - Initial sample auctions are correctly loaded")
    void testInitialSampleAuctions() {
        assertNotNull(AuctionConsoleApp.findAuctionById(101));
        assertNotNull(AuctionConsoleApp.findAuctionById(102));
        assertNotNull(AuctionConsoleApp.findAuctionById(103));

        Auction iphone = AuctionConsoleApp.findAuctionById(101);
        assertEquals("iPhone 15", iphone.getProductName());
        assertEquals(60000.0, iphone.getReservePrice());
        assertEquals("APPROVED", iphone.getStatus());
    }

    @Test
    @DisplayName("Domain - User PIN verification works properly")
    void testUserPinVerification() {
        User seller = new User("Ravi", "SELLER", 1234);
        assertTrue(seller.verifyPin(1234), "Correct PIN must return true.");
        assertFalse(seller.verifyPin(9999), "Incorrect PIN must return false.");
    }
}
