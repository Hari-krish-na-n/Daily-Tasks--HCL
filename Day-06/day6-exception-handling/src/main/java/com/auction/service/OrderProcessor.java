package com.auction.service;

import com.auction.exception.InsufficientStockException;
import com.auction.exception.InvalidQuantityException;
import com.auction.exception.OrderProcessingException;
import com.auction.model.AuctionItem;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * OrderProcessor manages auction inventory stock and order fulfillment.
 *
 * Demonstrates:
 * - Checked Exception: InsufficientStockException (declared with throws, checked at compile time).
 * - Unchecked Exception: InvalidQuantityException (extends RuntimeException, raised with throw).
 * - Exception Chaining: Wrapping exceptions in OrderProcessingException preserving original cause.
 * - Atomic Updates: Available stock is never partially deducted if validation fails.
 * - Audit recording in finally blocks.
 */
public class OrderProcessor {

    // In-memory stock mapping item ID -> available quantity
    private final Map<Integer, Integer> inventory = new HashMap<>();
    private final AuditService auditService;

    public OrderProcessor(AuditService auditService) {
        this.auditService = auditService;
    }

    /**
     * Initializes or updates stock for a given auction item ID.
     */
    public void setStock(int itemId, int quantity) {
        if (quantity < 0) {
            throw new InvalidQuantityException("Initial stock cannot be negative. Given: " + quantity);
        }
        inventory.put(itemId, quantity);
    }

    /**
     * Retrieves current stock for an item ID. Returns 0 if item is not mapped.
     */
    public int getStock(int itemId) {
        return inventory.getOrDefault(itemId, 0);
    }

    /**
     * Core business operation: process an order for an auction item with requested quantity.
     *
     * @param item the auction item being ordered
     * @param requestedQty number of units requested
     * @return remaining stock after fulfillment
     * @throws InvalidQuantityException unchecked exception if requestedQty <= 0
     * @throws InsufficientStockException checked exception if requestedQty > available stock
     */
    public int processOrder(AuctionItem item, int requestedQty) throws InsufficientStockException {
        if (item == null) {
            throw new IllegalArgumentException("Auction item cannot be null.");
        }

        int itemId = item.getId();
        int currentStock = getStock(itemId);
        boolean success = false;
        String auditDetail = "";

        try {
            // Business Rule 1 (Unchecked): requested quantity must be positive
            if (requestedQty <= 0) {
                auditDetail = "Invalid quantity rejected: " + requestedQty;
                throw new InvalidQuantityException(
                        "Order quantity must be strictly greater than 0. Requested: " + requestedQty
                );
            }

            // Business Rule 2 (Checked): quantity requested cannot exceed available stock
            if (requestedQty > currentStock) {
                auditDetail = String.format("Insufficient stock for item %d (%s): requested %d, available %d",
                        itemId, item.getProductName(), requestedQty, currentStock);
                throw new InsufficientStockException(auditDetail);
            }

            // Successful fulfillment - atomic stock reduction
            int newStock = currentStock - requestedQty;
            inventory.put(itemId, newStock);
            success = true;
            auditDetail = String.format("Fulfilled %d unit(s) of '%s' (Item %d). Remaining stock: %d",
                    requestedQty, item.getProductName(), itemId, newStock);
            return newStock;

        } finally {
            // Finally block always executes to log audit trail
            if (auditService != null) {
                auditService.log("PROCESS_ORDER [Item " + itemId + "]", success, auditDetail);
            }
        }
    }

    /**
     * High-level wrapper demonstrating Exception Chaining.
     * Catches lower-level exceptions (both checked InsufficientStockException
     * and unchecked InvalidQuantityException) and wraps them into OrderProcessingException,
     * preserving the root cause using constructor chaining.
     */
    public int processOrderSafely(AuctionItem item, int requestedQty) throws OrderProcessingException {
        try {
            return processOrder(item, requestedQty);
        } catch (InsufficientStockException | InvalidQuantityException ex) {
            // Multi-catch block handling both compatible exception types
            // Wraps into high-level OrderProcessingException preserving original cause
            throw new OrderProcessingException(
                    "Order processing failed for item '" + (item != null ? item.getProductName() : "null")
                            + "': " + ex.getMessage(),
                    ex
            );
        }
    }

    public Map<Integer, Integer> getAllInventory() {
        return Collections.unmodifiableMap(inventory);
    }
}
