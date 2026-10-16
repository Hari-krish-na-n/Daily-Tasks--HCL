package com.auction.model;

import java.time.LocalDateTime;

/**
 * Result object returned when a bid is processed.
 */
public class BidResult {

    private final boolean success;
    private final String message;
    private final Bid placedBid;
    private final double currentHighestBid;
    private final String currentHighestBidder;
    private final boolean deadlineExtended;
    private final LocalDateTime newDeadline;

    public BidResult(boolean success, String message, Bid placedBid,
                     double currentHighestBid, String currentHighestBidder,
                     boolean deadlineExtended, LocalDateTime newDeadline) {
        this.success = success;
        this.message = message;
        this.placedBid = placedBid;
        this.currentHighestBid = currentHighestBid;
        this.currentHighestBidder = currentHighestBidder;
        this.deadlineExtended = deadlineExtended;
        this.newDeadline = newDeadline;
    }

    public static BidResult failure(String message) {
        return new BidResult(false, message, null, 0.0, null, false, null);
    }

    public static BidResult success(String message, Bid bid, double currentHighest, String highestBidder,
                                    boolean extended, LocalDateTime deadline) {
        return new BidResult(true, message, bid, currentHighest, highestBidder, extended, deadline);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Bid getPlacedBid() {
        return placedBid;
    }

    public double getCurrentHighestBid() {
        return currentHighestBid;
    }

    public String getCurrentHighestBidder() {
        return currentHighestBidder;
    }

    public boolean isDeadlineExtended() {
        return deadlineExtended;
    }

    public LocalDateTime getNewDeadline() {
        return newDeadline;
    }

    @Override
    public String toString() {
        return String.format("BidResult[success=%s, msg='%s', highest=INR %,.2f by %s, extended=%s]",
                success, message, currentHighestBid, currentHighestBidder, deadlineExtended);
    }
}
