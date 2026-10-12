package com.auction.service;

import com.auction.exception.InsufficientStockException;
import com.auction.exception.InvalidBidException;
import com.auction.exception.InvalidQuantityException;
import com.auction.exception.OrderProcessingException;
import com.auction.model.AuctionItem;
import com.auction.model.Bidder;
import com.auction.strategy.StandardBidValidationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests validating all Day 6 Exception Handling requirements:
 * 1. Valid quantity reduces stock accurately.
 * 2. Zero quantity triggers InvalidQuantityException (unchecked).
 * 3. Negative quantity triggers InvalidQuantityException (unchecked).
 * 4. Request exceeding available stock triggers InsufficientStockException (checked).
 * 5. Failed requests do not change stock.
 * 6. Valid positive bid succeeds.
 * 7. Zero or negative bid triggers InvalidBidException and does not change current bid.
 * 8. Failed operations are audited through the finally path.
 * 9. Exception cause is preserved during exception chaining and verifiable via getCause().
 */
class ExceptionHandlingTest {

    private AuditService auditService;
    private OrderProcessor orderProcessor;
    private AuctionService auctionService;
    private AuctionItem laptop;
    private Bidder bidder;

    @BeforeEach
    void setUp() {
        auditService = new AuditService();
        orderProcessor = new OrderProcessor(auditService);
        auctionService = new AuctionService(new StandardBidValidationStrategy(), auditService);

        laptop = new AuctionItem(1001, "MacBook Pro", 50000.0);
        bidder = new Bidder(201, "Krishna", "krishna@auction.com", 80000.0);

        orderProcessor.setStock(laptop.getId(), 10);
        auctionService.addItem(laptop);
    }

    @Test
    @DisplayName("1. Valid quantity succeeds and stock decreases accurately")
    void testValidOrderDecreasesStock() throws InsufficientStockException {
        int initialStock = orderProcessor.getStock(laptop.getId());
        int remaining = orderProcessor.processOrder(laptop, 3);

        assertEquals(7, remaining);
        assertEquals(7, orderProcessor.getStock(laptop.getId()));
        assertEquals(initialStock - 3, orderProcessor.getStock(laptop.getId()));
    }

    @Test
    @DisplayName("2. Zero quantity triggers InvalidQuantityException")
    void testZeroQuantityThrowsInvalidQuantityException() {
        InvalidQuantityException ex = assertThrows(InvalidQuantityException.class, () -> {
            orderProcessor.processOrder(laptop, 0);
        });

        assertTrue(ex.getMessage().contains("strictly greater than 0"));
    }

    @Test
    @DisplayName("3. Negative quantity triggers InvalidQuantityException")
    void testNegativeQuantityThrowsInvalidQuantityException() {
        InvalidQuantityException ex = assertThrows(InvalidQuantityException.class, () -> {
            orderProcessor.processOrder(laptop, -5);
        });

        assertTrue(ex.getMessage().contains("Requested: -5"));
    }

    @Test
    @DisplayName("4. Request exceeding available stock triggers InsufficientStockException (checked)")
    void testExcessQuantityThrowsInsufficientStockException() {
        int currentStock = orderProcessor.getStock(laptop.getId()); // 10

        InsufficientStockException ex = assertThrows(InsufficientStockException.class, () -> {
            orderProcessor.processOrder(laptop, currentStock + 1);
        });

        assertTrue(ex.getMessage().contains("Insufficient stock"));
    }

    @Test
    @DisplayName("5. Failed requests do not change stock")
    void testFailedRequestsDoNotMutateStock() {
        int initialStock = orderProcessor.getStock(laptop.getId());

        // Attempt invalid quantity
        assertThrows(InvalidQuantityException.class, () -> orderProcessor.processOrder(laptop, 0));
        assertEquals(initialStock, orderProcessor.getStock(laptop.getId()));

        // Attempt excessive quantity
        assertThrows(InsufficientStockException.class, () -> orderProcessor.processOrder(laptop, 999));
        assertEquals(initialStock, orderProcessor.getStock(laptop.getId()));
    }

    @Test
    @DisplayName("6. Valid positive bid succeeds if it satisfies bidding rules")
    void testValidPositiveBidSucceeds() {
        double validBidAmount = 52000.0; // Starting is 50000, min increment is 500
        boolean placed = auctionService.placeBid(laptop.getId(), bidder, validBidAmount);

        assertTrue(placed);
        assertEquals(validBidAmount, laptop.getCurrentBid());
        assertEquals(1, bidder.getTotalBidsPlaced());
    }

    @Test
    @DisplayName("7. Zero or negative bid triggers InvalidBidException and preserves current bid")
    void testZeroOrNegativeBidThrowsExceptionAndPreservesBid() {
        double originalBid = laptop.getCurrentBid();

        // Zero bid
        InvalidBidException exZero = assertThrows(InvalidBidException.class, () -> {
            auctionService.placeBid(laptop.getId(), bidder, 0.0);
        });
        assertTrue(exZero.getMessage().contains("strictly positive"));
        assertEquals(originalBid, laptop.getCurrentBid());

        // Negative bid
        InvalidBidException exNeg = assertThrows(InvalidBidException.class, () -> {
            auctionService.placeBid(laptop.getId(), bidder, -1500.0);
        });
        assertTrue(exNeg.getMessage().contains("Given: INR -1500.0"));
        assertEquals(originalBid, laptop.getCurrentBid());
    }

    @Test
    @DisplayName("8. Failed operation is audited through finally path")
    void testFinallyAuditExecutionOnSuccessAndFailure() throws InsufficientStockException {
        int initialAuditCount = auditService.getRecordCount();

        // 1. Success path
        try {
            orderProcessor.processOrder(laptop, 2);
        } catch (InsufficientStockException ignored) {}

        assertEquals(initialAuditCount + 1, auditService.getRecordCount());
        assertTrue(auditService.getRecords().get(auditService.getRecordCount() - 1).isSuccess());

        // 2. Failure path with checked exception
        try {
            orderProcessor.processOrder(laptop, 500);
        } catch (InsufficientStockException expected) {}

        assertEquals(initialAuditCount + 2, auditService.getRecordCount());
        assertFalse(auditService.getRecords().get(auditService.getRecordCount() - 1).isSuccess());

        // 3. Failure path with unchecked exception
        try {
            orderProcessor.processOrder(laptop, -1);
        } catch (InvalidQuantityException expected) {}

        assertEquals(initialAuditCount + 3, auditService.getRecordCount());
        assertFalse(auditService.getRecords().get(auditService.getRecordCount() - 1).isSuccess());
    }

    @Test
    @DisplayName("9. Exception cause is preserved when wrapped in OrderProcessingException")
    void testExceptionChainingPreservesCause() {
        // Wrap InsufficientStockException
        OrderProcessingException opeStock = assertThrows(OrderProcessingException.class, () -> {
            orderProcessor.processOrderSafely(laptop, 100);
        });

        assertNotNull(opeStock.getCause());
        assertInstanceOf(InsufficientStockException.class, opeStock.getCause());
        assertTrue(opeStock.getCause().getMessage().contains("Insufficient stock"));

        // Wrap InvalidQuantityException
        OrderProcessingException opeQty = assertThrows(OrderProcessingException.class, () -> {
            orderProcessor.processOrderSafely(laptop, -4);
        });

        assertNotNull(opeQty.getCause());
        assertInstanceOf(InvalidQuantityException.class, opeQty.getCause());
        assertTrue(opeQty.getCause().getMessage().contains("strictly greater than 0"));
    }
}
