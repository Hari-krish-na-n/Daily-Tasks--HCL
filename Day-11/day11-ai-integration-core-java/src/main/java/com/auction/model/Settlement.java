package com.auction.model;

import java.time.LocalDateTime;

/**
 * Settlement and shipment record for a won auction (FR8).
 *
 * Implements acceptance criteria:
 * - Payment simulation from winner to platform.
 * - Platform fee deduction (5%).
 * - Seller shipment details (courier name, tracking number).
 * - Seller payout calculation.
 * - Buyer shipment notification tracking.
 */
public class Settlement extends BaseEntity {

    public static final double DEFAULT_PLATFORM_FEE_PERCENTAGE = 5.0;

    private int auctionId;
    private int buyerId;
    private int sellerId;
    private double grossAmount;
    private double platformFee;
    private double netSellerPayout;
    private boolean paid;
    private LocalDateTime paidAt;
    private boolean shipped;
    private String courierName;
    private String trackingNumber;
    private LocalDateTime shippedAt;
    private boolean payoutReleased;

    public Settlement() {
        super(0);
    }

    public Settlement(int id, int auctionId, int buyerId, int sellerId, double grossAmount) {
        this(id, auctionId, buyerId, sellerId, grossAmount, DEFAULT_PLATFORM_FEE_PERCENTAGE);
    }

    public Settlement(int id, int auctionId, int buyerId, int sellerId, double grossAmount, double feePercentage) {
        super(id);
        if (grossAmount <= 0) {
            throw new IllegalArgumentException("Gross settlement amount must be strictly positive.");
        }
        if (feePercentage < 0 || feePercentage >= 100) {
            throw new IllegalArgumentException("Invalid fee percentage: " + feePercentage);
        }
        this.auctionId = auctionId;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.grossAmount = grossAmount;
        this.platformFee = (grossAmount * feePercentage) / 100.0;
        this.netSellerPayout = this.grossAmount - this.platformFee;
        this.paid = false;
        this.shipped = false;
        this.payoutReleased = false;
    }

    public int getAuctionId() {
        return auctionId;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public double getGrossAmount() {
        return grossAmount;
    }

    public double getPlatformFee() {
        return platformFee;
    }

    public double getNetSellerPayout() {
        return netSellerPayout;
    }

    public boolean isPaid() {
        return paid;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public boolean isShipped() {
        return shipped;
    }

    public String getCourierName() {
        return courierName;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public LocalDateTime getShippedAt() {
        return shippedAt;
    }

    public boolean isPayoutReleased() {
        return payoutReleased;
    }

    /**
     * Records payment from buyer to the platform escrow.
     */
    public void recordPayment() {
        if (this.paid) {
            throw new IllegalStateException("Payment has already been recorded for this settlement.");
        }
        this.paid = true;
        this.paidAt = LocalDateTime.now();
        markUpdated();
    }

    /**
     * Submits shipment details once buyer has paid.
     * Releasing seller payout is triggered upon shipment confirmation.
     */
    public void submitShipment(String courierName, String trackingNumber) {
        if (!this.paid) {
            throw new IllegalStateException("Cannot ship item before buyer payment is completed.");
        }
        if (courierName == null || courierName.trim().isEmpty()) {
            throw new IllegalArgumentException("Courier name cannot be blank.");
        }
        if (trackingNumber == null || trackingNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Tracking number cannot be blank.");
        }
        this.courierName = courierName.trim();
        this.trackingNumber = trackingNumber.trim();
        this.shipped = true;
        this.shippedAt = LocalDateTime.now();
        // Release seller payout upon shipment confirmation
        this.payoutReleased = true;
        markUpdated();
    }

    @Override
    public String getSummary() {
        return String.format("Settlement #%d [Auction #%d]: Gross INR %,.2f, Net Payout INR %,.2f, Paid=%s, Shipped=%s",
                getId(), auctionId, grossAmount, netSellerPayout, paid, shipped);
    }

    @Override
    public String toString() {
        return String.format("Settlement #%d [Auction #%d] Gross: INR %,.2f | Fee: INR %,.2f | Net Payout: INR %,.2f | Paid: %s | Shipped: %s (Courier: %s, Track: %s) | Payout Released: %s",
                getId(), auctionId, grossAmount, platformFee, netSellerPayout, paid, shipped,
                courierName != null ? courierName : "N/A",
                trackingNumber != null ? trackingNumber : "N/A",
                payoutReleased);
    }
}
