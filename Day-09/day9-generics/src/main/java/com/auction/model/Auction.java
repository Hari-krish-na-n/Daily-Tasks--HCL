package com.auction.model;

import java.time.LocalDateTime;

/**
 * Auction entity in the Online Auction System.
 * Extends BaseEntity to inherit id, createdAt, and updatedAt.
 *
 * Implements FR2 (Auction Listing) and FR3 (Admin Moderation) properties:
 * - Product details
 * - Seller association
 * - Starting price and reserve price
 * - Start time and end time
 * - ListingStatus (PENDING, APPROVED, REJECTED, CLOSED)
 * - Moderation remarks and moderatedBy admin username
 */
public class Auction extends BaseEntity implements Comparable<Auction> {

    private String productName;
    private String description;
    private Seller seller;
    private double startingPrice;
    private double reservePrice;
    private double currentBid;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ListingStatus status;
    private String moderationNote;
    private String moderatedBy;

    // Default constructor
    public Auction() {
        this(0, "Unnamed Auction", null, 0.0, 0.0,
                LocalDateTime.now(), LocalDateTime.now().plusDays(7));
    }

    // Common constructor for seller listing creation
    public Auction(int id, String productName, Seller seller,
                   double startingPrice, double reservePrice,
                   LocalDateTime startTime, LocalDateTime endTime) {
        super(id);
        this.productName = productName;
        this.description = productName;
        this.seller = seller;
        this.startingPrice = startingPrice;
        this.reservePrice = reservePrice;
        this.currentBid = startingPrice;
        this.startTime = startTime != null ? startTime : LocalDateTime.now();
        this.endTime = endTime != null ? endTime : LocalDateTime.now().plusDays(7);
        // FR2 requirement: newly submitted listings start in PENDING state
        this.status = ListingStatus.PENDING;
        this.moderationNote = "Awaiting review";
        this.moderatedBy = null;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        if (productName != null && !productName.trim().isEmpty()) {
            this.productName = productName;
            markUpdated();
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        markUpdated();
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
        markUpdated();
    }

    public double getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(double startingPrice) {
        if (startingPrice >= 0) {
            this.startingPrice = startingPrice;
            markUpdated();
        }
    }

    public double getReservePrice() {
        return reservePrice;
    }

    public void setReservePrice(double reservePrice) {
        if (reservePrice >= 0) {
            this.reservePrice = reservePrice;
            markUpdated();
        }
    }

    public double getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(double currentBid) {
        if (currentBid >= this.currentBid) {
            this.currentBid = currentBid;
            markUpdated();
        }
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        markUpdated();
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
        markUpdated();
    }

    public ListingStatus getStatus() {
        return status;
    }

    public void setStatus(ListingStatus status) {
        this.status = status;
        markUpdated();
    }

    public String getModerationNote() {
        return moderationNote;
    }

    public void setModerationNote(String moderationNote) {
        this.moderationNote = moderationNote;
        markUpdated();
    }

    public String getModeratedBy() {
        return moderatedBy;
    }

    public void setModeratedBy(String moderatedBy) {
        this.moderatedBy = moderatedBy;
        markUpdated();
    }

    public boolean isApproved() {
        return status == ListingStatus.APPROVED;
    }

    public boolean isPending() {
        return status == ListingStatus.PENDING;
    }

    public boolean isRejected() {
        return status == ListingStatus.REJECTED;
    }

    /**
     * Natural order by ID for TreeSet demonstrations.
     */
    @Override
    public int compareTo(Auction other) {
        if (other == null) {
            return 1;
        }
        return Integer.compare(this.getId(), other.getId());
    }

    @Override
    public String getSummary() {
        String sellerName = (seller != null) ? seller.getName() : "Unknown";
        return String.format("Auction #%d: '%s' | Seller: %s | Start: INR %.2f | Reserve: INR %.2f | Status: %s",
                getId(), productName, sellerName, startingPrice, reservePrice, status.getDisplayLabel());
    }

    @Override
    public String toString() {
        return getSummary();
    }
}
