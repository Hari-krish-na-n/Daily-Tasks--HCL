package com.auction.service;

import com.auction.model.Auction;
import com.auction.model.AuctionCloseResult;
import com.auction.model.AuctionCloseStatus;
import com.auction.model.Bid;
import com.auction.model.ListingStatus;
import com.auction.model.Seller;
import com.auction.model.Settlement;
import com.auction.repository.BidRepository;
import com.auction.repository.CrudRepository;
import com.auction.repository.InMemoryBidRepository;
import com.auction.repository.InMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FR7 and FR8 Functional Requirements Tests")
public class AuctionFulfillmentServiceTest {

    private CrudRepository<Auction, Integer> auctionRepository;
    private BidRepository bidRepository;
    private AuctionFulfillmentService fulfillmentService;
    private Seller seller;

    @BeforeEach
    void setUp() {
        auctionRepository = new InMemoryRepository<>(Auction::getId);
        bidRepository = new InMemoryBidRepository();
        fulfillmentService = new AuctionFulfillmentService(auctionRepository, bidRepository);

        seller = new Seller(101, "seller1", "pass", "Tech Store", "seller@store.com", "Tech Store LLC", 4.9);
    }

    @Test
    @DisplayName("FR7 - Auction Closes as SOLD when highest bid >= reserve price")
    void testCloseAuctionReservePriceMet() {
        // Auction starting 100, reserve 150
        Auction auction = new Auction(1, "Laptop", seller, 100.0, 150.0,
                LocalDateTime.now().minusHours(1), LocalDateTime.now());
        auction.setStatus(ListingStatus.APPROVED);
        auctionRepository.save(auction);

        // Bid of 160 placed
        bidRepository.save(new Bid(10, 1, 301, "WinnerBob", 160.0, LocalDateTime.now().minusMinutes(5)));

        AuctionCloseResult result = fulfillmentService.closeAuction(1, LocalDateTime.now());

        assertTrue(result.isSold());
        assertEquals(AuctionCloseStatus.SOLD, result.getStatus());
        assertEquals(301, result.getWinnerId());
        assertEquals("WinnerBob", result.getWinnerName());
        assertEquals(160.0, result.getFinalPrice());

        // Verify auction status transitioned to CLOSED
        Auction updated = auctionRepository.findById(1).orElseThrow();
        assertEquals(ListingStatus.CLOSED, updated.getStatus());

        // Verify settlement automatically initialized for FR8
        Optional<Settlement> settlement = fulfillmentService.getSettlementForAuction(1);
        assertTrue(settlement.isPresent());
        assertEquals(160.0, settlement.get().getGrossAmount());
    }

    @Test
    @DisplayName("FR7 - Auction Closes as UNSOLD when highest bid < reserve price")
    void testCloseAuctionReservePriceNotMet() {
        // Auction starting 100, reserve 200
        Auction auction = new Auction(2, "Rare Painting", seller, 100.0, 200.0,
                LocalDateTime.now().minusHours(1), LocalDateTime.now());
        auction.setStatus(ListingStatus.APPROVED);
        auctionRepository.save(auction);

        // Highest bid is only 150
        bidRepository.save(new Bid(11, 2, 302, "BidderAlice", 150.0, LocalDateTime.now().minusMinutes(5)));

        AuctionCloseResult result = fulfillmentService.closeAuction(2, LocalDateTime.now());

        assertFalse(result.isSold());
        assertEquals(AuctionCloseStatus.UNSOLD, result.getStatus());
        assertEquals(ListingStatus.CLOSED, auctionRepository.findById(2).get().getStatus());

        // No settlement should exist for unsold auction
        assertTrue(fulfillmentService.getSettlementForAuction(2).isEmpty());
    }

    @Test
    @DisplayName("FR7 - Closing an already closed auction throws IllegalStateException")
    void testCloseAlreadyClosedAuction() {
        Auction auction = new Auction(3, "Item", seller, 50.0, 50.0,
                LocalDateTime.now().minusHours(1), LocalDateTime.now());
        auction.setStatus(ListingStatus.APPROVED);
        auctionRepository.save(auction);

        fulfillmentService.closeAuction(3, LocalDateTime.now());
        assertThrows(IllegalStateException.class, () -> fulfillmentService.closeAuction(3, LocalDateTime.now()));
    }

    @Test
    @DisplayName("FR8 - Complete Settlement Workflow: Payment (5% fee) -> Shipment -> Payout")
    void testCompleteSettlementWorkflow() {
        // Setup sold auction
        Auction auction = new Auction(4, "Smartphone", seller, 1000.0, 1000.0,
                LocalDateTime.now().minusHours(1), LocalDateTime.now());
        auction.setStatus(ListingStatus.APPROVED);
        auctionRepository.save(auction);
        bidRepository.save(new Bid(20, 4, 303, "BuyerCharlie", 1000.0, LocalDateTime.now().minusMinutes(10)));

        fulfillmentService.closeAuction(4, LocalDateTime.now());
        Settlement settlement = fulfillmentService.getSettlementForAuction(4).orElseThrow();

        // Check platform fee (5% of 1000 = 50) and net payout (950)
        assertEquals(1000.0, settlement.getGrossAmount());
        assertEquals(50.0, settlement.getPlatformFee(), 0.001);
        assertEquals(950.0, settlement.getNetSellerPayout(), 0.001);
        assertFalse(settlement.isPaid());
        assertFalse(settlement.isShipped());
        assertFalse(settlement.isPayoutReleased());

        // Step 1: Shipment cannot occur before buyer pays
        assertThrows(IllegalStateException.class, () ->
                fulfillmentService.processShipment(4, seller.getId(), "FedEx", "TRACK123"));

        // Step 2: Buyer pays exact gross amount
        assertThrows(IllegalArgumentException.class, () ->
                fulfillmentService.processBuyerPayment(4, 303, 500.0)); // Underpayment rejected
        assertThrows(IllegalArgumentException.class, () ->
                fulfillmentService.processBuyerPayment(4, 999, 1000.0)); // Wrong buyer rejected

        Settlement paidSettlement = fulfillmentService.processBuyerPayment(4, 303, 1000.0);
        assertTrue(paidSettlement.isPaid());
        assertNotNull(paidSettlement.getPaidAt());

        // Step 3: Seller submits shipment tracking details
        Settlement shippedSettlement = fulfillmentService.processShipment(4, seller.getId(), "FedEx Express", "FDX-9988-IN");
        assertTrue(shippedSettlement.isShipped());
        assertEquals("FedEx Express", shippedSettlement.getCourierName());
        assertEquals("FDX-9988-IN", shippedSettlement.getTrackingNumber());
        assertNotNull(shippedSettlement.getShippedAt());

        // Step 4: Seller payout released
        assertTrue(shippedSettlement.isPayoutReleased());
    }
}
