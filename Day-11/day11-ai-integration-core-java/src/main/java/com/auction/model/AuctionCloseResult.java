package com.auction.model;

/**
 * Result model representing the outcome of closing an auction (FR7).
 */
public class AuctionCloseResult {

    private final int auctionId;
    private final AuctionCloseStatus status;
    private final double finalPrice;
    private final double reservePrice;
    private final Integer winnerId;
    private final String winnerName;
    private final String message;

    public AuctionCloseResult(int auctionId, AuctionCloseStatus status, double finalPrice,
                              double reservePrice, Integer winnerId, String winnerName, String message) {
        this.auctionId = auctionId;
        this.status = status;
        this.finalPrice = finalPrice;
        this.reservePrice = reservePrice;
        this.winnerId = winnerId;
        this.winnerName = winnerName;
        this.message = message;
    }

    public int getAuctionId() {
        return auctionId;
    }

    public AuctionCloseStatus getStatus() {
        return status;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public double getReservePrice() {
        return reservePrice;
    }

    public Integer getWinnerId() {
        return winnerId;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSold() {
        return status == AuctionCloseStatus.SOLD;
    }

    @Override
    public String toString() {
        if (isSold()) {
            return String.format("Auction #%d CLOSED [SOLD]: Winner=%s (ID: %d), Winning Bid=INR %,.2f (Reserve: INR %,.2f)",
                    auctionId, winnerName, winnerId, finalPrice, reservePrice);
        } else {
            return String.format("Auction #%d CLOSED [%s]: Final Bid=INR %,.2f, Reserve=INR %,.2f. Message: %s",
                    auctionId, status, finalPrice, reservePrice, message);
        }
    }
}
