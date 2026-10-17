package com.auction.repository;

import com.auction.model.Bid;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of BidRepository extending the generic InMemoryRepository<Bid, Integer>.
 *
 * Demonstrates:
 * - Proper code reuse: Extends generic CRUD operations from InMemoryRepository instead of rewriting them.
 * - Entity identity extracted via method reference (Bid::getId).
 * - Specialized domain queries (findByAuctionId, findByBidderId, findHighestBid)
 *   implemented over the inherited storage safely.
 */
public class InMemoryBidRepository extends InMemoryRepository<Bid, Integer> implements BidRepository {

    public InMemoryBidRepository() {
        super(Bid::getId);
    }

    @Override
    public synchronized List<Bid> findByAuctionId(int auctionId) {
        List<Bid> results = new ArrayList<>();
        for (Bid bid : findAll()) {
            if (bid.getAuctionId() == auctionId) {
                results.add(bid);
            }
        }
        return results;
    }

    @Override
    public synchronized List<Bid> findByBidderId(int bidderId) {
        List<Bid> results = new ArrayList<>();
        for (Bid bid : findAll()) {
            if (bid.getBidderId() == bidderId) {
                results.add(bid);
            }
        }
        return results;
    }

    @Override
    public synchronized Bid findHighestBid(int auctionId) {
        Bid highest = null;
        for (Bid bid : findAll()) {
            if (bid.getAuctionId() == auctionId) {
                if (highest == null || bid.getAmount() > highest.getAmount()) {
                    highest = bid;
                }
            }
        }
        return highest;
    }
}
