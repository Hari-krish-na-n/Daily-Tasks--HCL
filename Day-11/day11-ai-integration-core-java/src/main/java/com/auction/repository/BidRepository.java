package com.auction.repository;

import com.auction.model.Bid;
import java.util.List;

/**
 * Specialized repository interface for Bid entities in the Online Auction System.
 * Reuses the generic CrudRepository<T, ID> design from Day 9 while adding
 * auction-specific query capabilities.
 */
public interface BidRepository extends CrudRepository<Bid, Integer> {

    /**
     * Finds all bids placed on a specific auction listing, ordered by timestamp or amount.
     *
     * @param auctionId the auction ID
     * @return List of bids for that auction
     */
    List<Bid> findByAuctionId(int auctionId);

    /**
     * Finds all bids submitted by a specific bidder.
     *
     * @param bidderId the bidder's user ID
     * @return List of bids placed by this bidder
     */
    List<Bid> findByBidderId(int bidderId);

    /**
     * Retrieves the highest active bid for a given auction.
     *
     * @param auctionId the auction ID
     * @return highest Bid or null if no bids exist
     */
    Bid findHighestBid(int auctionId);
}
