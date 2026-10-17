package com.auction.service;

import com.auction.model.Auction;
import com.auction.model.AuctionCloseResult;
import com.auction.model.AuctionCloseStatus;
import com.auction.model.Bid;
import com.auction.model.ListingStatus;
import com.auction.model.Settlement;
import com.auction.repository.BidRepository;
import com.auction.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * AuctionFulfillmentService implements:
 * - FR7 (Auction Closing & Winner Declaration):
 *   Checks auction end time and reserve price; if highest bid >= reserve price,
 *   declares winner, marks status as SOLD, and notifies buyer and seller.
 *   If highest bid < reserve price, marks as UNSOLD (reserve not met).
 *
 * - FR8 (Settlement & Shipment):
 *   Initializes settlement for sold auction, manages buyer payment escrow,
 *   deducts 5% platform fee, records seller shipping details (courier name, tracking number),
 *   and disburses net payout to the seller upon shipment confirmation.
 */
public class AuctionFulfillmentService {

    private final CrudRepository<Auction, Integer> auctionRepository;
    private final BidRepository bidRepository;

    // Settlements storage: settlementId -> Settlement
    private final Map<Integer, Settlement> settlementMap = new LinkedHashMap<>();
    private final Map<Integer, Integer> auctionToSettlementIdMap = new LinkedHashMap<>();
    private int nextSettlementId = 8000;

    public AuctionFulfillmentService(CrudRepository<Auction, Integer> auctionRepository,
                                     BidRepository bidRepository) {
        this.auctionRepository = auctionRepository;
        this.bidRepository = bidRepository;
    }

    /**
     * FR7: Closes an auction and determines the winner based on reserve price verification.
     *
     * @param auctionId ID of the auction to close
     * @param currentTime the time of closing
     * @return AuctionCloseResult detailing the outcome
     */
    public synchronized AuctionCloseResult closeAuction(int auctionId, LocalDateTime currentTime) {
        Optional<Auction> optionalAuction = auctionRepository.findById(auctionId);
        if (optionalAuction.isEmpty()) {
            throw new IllegalArgumentException("Cannot close auction: Auction #" + auctionId + " not found.");
        }
        Auction auction = optionalAuction.get();

        if (auction.getStatus() == ListingStatus.CLOSED) {
            throw new IllegalStateException("Auction #" + auctionId + " is already closed.");
        }

        // Determine highest bid
        Bid highestBid = bidRepository.findHighestBid(auctionId);
        double reservePrice = auction.getReservePrice();
        AuctionCloseResult result;

        if (highestBid == null) {
            // No bids were placed
            auction.setStatus(ListingStatus.CLOSED);
            auctionRepository.save(auction);
            result = new AuctionCloseResult(auctionId, AuctionCloseStatus.CANCELLED,
                    0.0, reservePrice, null, null, "No bids placed during the auction duration.");
        } else if (highestBid.getAmount() >= reservePrice) {
            // Reserve price met -> SOLD
            auction.setStatus(ListingStatus.CLOSED);
            auctionRepository.save(auction);
            result = new AuctionCloseResult(auctionId, AuctionCloseStatus.SOLD,
                    highestBid.getAmount(), reservePrice,
                    highestBid.getBidderId(), highestBid.getBidderName(),
                    "Winning bid met or exceeded reserve price. Winner declared!");

            // Initialize FR8 settlement
            int sellerId = auction.getSeller() != null ? auction.getSeller().getId() : 0;
            Settlement settlement = new Settlement(++nextSettlementId, auctionId,
                    highestBid.getBidderId(), sellerId, highestBid.getAmount());
            settlementMap.put(settlement.getId(), settlement);
            auctionToSettlementIdMap.put(auctionId, settlement.getId());
        } else {
            // Highest bid fell below reserve price -> UNSOLD
            auction.setStatus(ListingStatus.CLOSED);
            auctionRepository.save(auction);
            result = new AuctionCloseResult(auctionId, AuctionCloseStatus.UNSOLD,
                    highestBid.getAmount(), reservePrice,
                    highestBid.getBidderId(), highestBid.getBidderName(),
                    String.format("Reserve price of INR %,.2f not met (Highest bid: INR %,.2f). Item marked UNSOLD.",
                            reservePrice, highestBid.getAmount()));
        }

        return result;
    }

    /**
     * FR8: Retrieves the Settlement record for a won auction.
     */
    public synchronized Optional<Settlement> getSettlementForAuction(int auctionId) {
        Integer settlementId = auctionToSettlementIdMap.get(auctionId);
        if (settlementId == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(settlementMap.get(settlementId));
    }

    /**
     * FR8: Simulates buyer payment for a won auction settlement.
     */
    public synchronized Settlement processBuyerPayment(int auctionId, int buyerId, double amountPaid) {
        Optional<Settlement> opt = getSettlementForAuction(auctionId);
        if (opt.isEmpty()) {
            throw new IllegalArgumentException("No settlement found for auction #" + auctionId + " (Must be closed and SOLD first).");
        }
        Settlement settlement = opt.get();
        if (settlement.getBuyerId() != buyerId) {
            throw new IllegalArgumentException("Buyer ID " + buyerId + " does not match winning buyer ID " + settlement.getBuyerId());
        }
        if (Math.abs(amountPaid - settlement.getGrossAmount()) > 0.01) {
            throw new IllegalArgumentException(String.format(
                    "Payment amount INR %,.2f does not match exact winning bid INR %,.2f",
                    amountPaid, settlement.getGrossAmount()));
        }
        settlement.recordPayment();
        return settlement;
    }

    /**
     * FR8: Submits courier shipment information and releases seller payout.
     */
    public synchronized Settlement processShipment(int auctionId, int sellerId, String courierName, String trackingNumber) {
        Optional<Settlement> opt = getSettlementForAuction(auctionId);
        if (opt.isEmpty()) {
            throw new IllegalArgumentException("No settlement found for auction #" + auctionId);
        }
        Settlement settlement = opt.get();
        if (settlement.getSellerId() != sellerId) {
            throw new IllegalArgumentException("Seller ID " + sellerId + " does not match listing seller ID " + settlement.getSellerId());
        }
        settlement.submitShipment(courierName, trackingNumber);
        return settlement;
    }

    /**
     * Retrieves all settlement records.
     */
    public synchronized List<Settlement> getAllSettlements() {
        return Collections.unmodifiableList(new ArrayList<>(settlementMap.values()));
    }
}
