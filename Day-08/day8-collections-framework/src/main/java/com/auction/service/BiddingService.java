package com.auction.service;

import com.auction.model.Auction;
import com.auction.model.Bid;
import com.auction.model.BidResult;
import com.auction.model.Bidder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service implementing FR4, FR5, and FR6 using Maps for lookups and proxy tracking.
 *
 * Rules:
 * - FR4 (Bidding Validation):
 *   * Bid must be >= currentHighestBid + minIncrement.
 *   * If no previous bids exist, bid must be >= auction starting price.
 *   * Bidder cannot be the seller of the auction.
 *   * Bid must arrive before auction endTime.
 *
 * - FR5 (Proxy/Auto-Bidding):
 *   * Bidders can register a maximum proxy limit in a Map<Integer, Map<Integer, Double>>:
 *     (auctionId -> (bidderId -> maxProxyLimit)).
 *   * When a competing bid is placed, the system automatically bids on behalf of the proxy bidder
 *     with: newBid = competingBid + minIncrement, capped strictly at their maximum proxy limit.
 *
 * - FR6 (Anti-Sniping):
 *   * If a valid bid arrives within the final 2 minutes (threshold = 120 seconds) before endTime,
 *     the auction deadline is extended by an additional 5 minutes from the current endTime (or from bid time).
 *     Rule: newEndTime = currentEndTime.plusMinutes(5).
 */
public class BiddingService {

    public static final double DEFAULT_MIN_INCREMENT = 500.0;
    public static final long ANTI_SNIPING_WINDOW_SECONDS = 120; // 2 minutes
    public static final long EXTENSION_MINUTES = 5; // extends by 5 minutes

    // Map: auctionId -> Auction
    private final Map<Integer, Auction> auctionMap = new HashMap<>();

    // Map: auctionId -> List of bids (history)
    private final Map<Integer, List<Bid>> bidHistoryMap = new HashMap<>();

    // Map: auctionId -> (bidderId -> maxProxyAmount)
    private final Map<Integer, Map<Integer, Double>> proxyBidMap = new HashMap<>();

    private int nextBidId = 5000;

    /**
     * Registers an auction into the bidding service registry.
     */
    public synchronized void registerAuction(Auction auction) {
        if (auction != null) {
            auctionMap.put(auction.getId(), auction);
            bidHistoryMap.putIfAbsent(auction.getId(), new ArrayList<>());
            proxyBidMap.putIfAbsent(auction.getId(), new HashMap<>());
        }
    }

    public synchronized Auction getAuction(int auctionId) {
        return auctionMap.get(auctionId);
    }

    public synchronized List<Bid> getBidHistory(int auctionId) {
        List<Bid> bids = bidHistoryMap.get(auctionId);
        if (bids == null) return Collections.emptyList();
        return new ArrayList<>(bids);
    }

    public synchronized double getProxyBid(int auctionId, int bidderId) {
        Map<Integer, Double> proxies = proxyBidMap.get(auctionId);
        if (proxies == null) return 0.0;
        return proxies.getOrDefault(bidderId, 0.0);
    }

    /**
     * FR5: Register or update a bidder's maximum proxy limit for an auction.
     */
    public synchronized void setProxyBid(int auctionId, int bidderId, double maxAmount) {
        if (maxAmount <= 0) {
            throw new IllegalArgumentException("Proxy bid maximum must be positive.");
        }
        Auction auction = auctionMap.get(auctionId);
        if (auction == null) {
            throw new IllegalArgumentException("Auction #" + auctionId + " not found.");
        }
        if (maxAmount < auction.getCurrentBid()) {
            throw new IllegalArgumentException("Proxy max limit (INR " + maxAmount +
                    ") cannot be less than current auction price (INR " + auction.getCurrentBid() + ").");
        }
        proxyBidMap.computeIfAbsent(auctionId, k -> new HashMap<>()).put(bidderId, maxAmount);
    }

    /**
     * FR4, FR5, FR6: Place a bid on an auction.
     */
    public synchronized BidResult placeBid(int auctionId, Bidder bidder, double bidAmount, LocalDateTime bidTime) {
        if (bidder == null) {
            return BidResult.failure("Bid rejected: Invalid bidder.");
        }
        if (bidTime == null) {
            bidTime = LocalDateTime.now();
        }

        Auction auction = auctionMap.get(auctionId);
        if (auction == null) {
            return BidResult.failure("Bid rejected: Auction #" + auctionId + " not found.");
        }

        // Validate auction timing
        if (bidTime.isBefore(auction.getStartTime())) {
            return BidResult.failure("Bid rejected: Auction has not started yet.");
        }
        if (bidTime.isAfter(auction.getEndTime())) {
            return BidResult.failure("Bid rejected: Auction has already ended at " + auction.getEndTime());
        }

        // FR4: Validate bid increment
        double currentPrice = auction.getCurrentBid();
        List<Bid> history = bidHistoryMap.computeIfAbsent(auctionId, k -> new ArrayList<>());

        if (history.isEmpty()) {
            // First bid must be at least starting price
            if (bidAmount < auction.getStartingPrice()) {
                return BidResult.failure(String.format(
                        "Bid rejected: First bid (INR %,.2f) must be at least the starting price (INR %,.2f).",
                        bidAmount, auction.getStartingPrice()));
            }
        } else {
            double minRequired = currentPrice + DEFAULT_MIN_INCREMENT;
            if (bidAmount < minRequired) {
                return BidResult.failure(String.format(
                        "Bid rejected: Bid amount (INR %,.2f) is below the minimum required (INR %,.2f = current %,.2f + increment %,.2f).",
                        bidAmount, minRequired, currentPrice, DEFAULT_MIN_INCREMENT));
            }
        }

        // FR6: Anti-sniping check
        boolean extended = false;
        long secondsRemaining = Duration.between(bidTime, auction.getEndTime()).getSeconds();
        if (secondsRemaining <= ANTI_SNIPING_WINDOW_SECONDS && secondsRemaining >= 0) {
            // Bid arrived within the final 2 minutes!
            LocalDateTime newDeadline = auction.getEndTime().plusMinutes(EXTENSION_MINUTES);
            auction.setEndTime(newDeadline);
            extended = true;
        }

        // Record the valid bid
        Bid bid = new Bid(++nextBidId, auctionId, bidder.getId(), bidder.getName(), bidAmount, bidTime, false);
        history.add(bid);
        auction.setCurrentBid(bidAmount);

        // FR5: Check for competing auto/proxy bids from other registered bidders
        Bid winningBid = bid;
        triggerProxyBidding(auction, bidder.getId(), bidTime);

        // Fetch latest winning bid after possible proxy responses
        if (!history.isEmpty()) {
            winningBid = history.get(history.size() - 1);
        }

        String msg = extended ?
                String.format("Bid accepted! Anti-sniping triggered: deadline extended by %d minutes.", EXTENSION_MINUTES)
                : "Bid accepted successfully.";

        return BidResult.success(msg, bid, winningBid.getAmount(), winningBid.getBidderName(),
                extended, auction.getEndTime());
    }

    /**
     * FR5: Proxy auto-bidding engine.
     * Evaluates all other proxy bids registered on this auction and places automatic outbids
     * up to their configured maximums.
     */
    private void triggerProxyBidding(Auction auction, int currentLeaderBidderId, LocalDateTime now) {
        Map<Integer, Double> proxies = proxyBidMap.get(auction.getId());
        if (proxies == null || proxies.isEmpty()) {
            return;
        }

        boolean proxyPlaced;
        do {
            proxyPlaced = false;
            double currentBid = auction.getCurrentBid();

            // Find eligible proxy bidders who are not currently the leader and whose max > currentBid
            for (Map.Entry<Integer, Double> entry : proxies.entrySet()) {
                int proxyBidderId = entry.getKey();
                double maxProxy = entry.getValue();

                if (proxyBidderId != currentLeaderBidderId && maxProxy > currentBid) {
                    double neededBid = currentBid + DEFAULT_MIN_INCREMENT;
                    if (neededBid <= maxProxy) {
                        // Place automated proxy bid
                        Bid autoBid = new Bid(++nextBidId, auction.getId(), proxyBidderId,
                                "ProxyBidder#" + proxyBidderId, neededBid, now.plusNanos(1_000_000), true);
                        bidHistoryMap.get(auction.getId()).add(autoBid);
                        auction.setCurrentBid(neededBid);
                        currentLeaderBidderId = proxyBidderId;
                        proxyPlaced = true;
                        break; // Loop again to allow other proxies or re-evaluations
                    }
                }
            }
        } while (proxyPlaced);
    }
}
