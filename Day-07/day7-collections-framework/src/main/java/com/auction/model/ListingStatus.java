package com.auction.model;

/**
 * Status of an auction listing during its lifecycle and moderation workflow.
 *
 * FR2 & FR3 moderation states:
 * - PENDING: Newly submitted by a seller, awaiting admin approval.
 * - APPROVED: Moderated and approved by an admin; active for bidding.
 * - REJECTED: Reviewed and rejected by an admin; cannot be bid upon.
 * - CLOSED: Auction has completed its bidding window.
 */
public enum ListingStatus {
    PENDING("Awaiting Admin Review"),
    APPROVED("Approved & Active"),
    REJECTED("Rejected by Admin"),
    CLOSED("Closed / Ended");

    private final String displayLabel;

    ListingStatus(String displayLabel) {
        this.displayLabel = displayLabel;
    }

    public String getDisplayLabel() {
        return displayLabel;
    }
}
