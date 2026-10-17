package com.auction.repository;

import com.auction.model.Bid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BidRepository and Generic InMemoryRepository Tests")
public class BidRepositoryTest {

    private BidRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryBidRepository();
    }

    @Test
    @DisplayName("Save and FindById using inherited Generic CrudRepository methods")
    void testSaveAndFindById() {
        Bid bid = new Bid(1, 100, 201, "Bidder1", 5000.0, LocalDateTime.now());
        repository.save(bid);

        Optional<Bid> found = repository.findById(1);
        assertTrue(found.isPresent());
        assertEquals(5000.0, found.get().getAmount());
    }

    @Test
    @DisplayName("Duplicate ID Handling - Upsert overwrites existing entity")
    void testDuplicateIdUpsert() {
        Bid b1 = new Bid(1, 100, 201, "Bidder1", 5000.0, LocalDateTime.now());
        Bid b2 = new Bid(1, 100, 201, "Bidder1", 6000.0, LocalDateTime.now());

        repository.save(b1);
        repository.save(b2);

        assertEquals(1, repository.findAll().size());
        assertEquals(6000.0, repository.findById(1).get().getAmount());
    }

    @Test
    @DisplayName("Specialized Queries - findByAuctionId and findByBidderId")
    void testSpecializedQueries() {
        repository.save(new Bid(1, 100, 201, "BidderA", 5000.0, LocalDateTime.now()));
        repository.save(new Bid(2, 100, 202, "BidderB", 6000.0, LocalDateTime.now()));
        repository.save(new Bid(3, 200, 201, "BidderA", 7000.0, LocalDateTime.now()));

        List<Bid> auctionBids = repository.findByAuctionId(100);
        assertEquals(2, auctionBids.size());

        List<Bid> bidderBids = repository.findByBidderId(201);
        assertEquals(2, bidderBids.size());
    }

    @Test
    @DisplayName("Specialized Query - findHighestBid")
    void testFindHighestBid() {
        repository.save(new Bid(1, 100, 201, "BidderA", 5000.0, LocalDateTime.now()));
        repository.save(new Bid(2, 100, 202, "BidderB", 7500.0, LocalDateTime.now()));
        repository.save(new Bid(3, 100, 203, "BidderC", 6200.0, LocalDateTime.now()));

        Bid highest = repository.findHighestBid(100);
        assertNotNull(highest);
        assertEquals(7500.0, highest.getAmount());
        assertEquals("BidderB", highest.getBidderName());

        assertNull(repository.findHighestBid(999));
    }
}
