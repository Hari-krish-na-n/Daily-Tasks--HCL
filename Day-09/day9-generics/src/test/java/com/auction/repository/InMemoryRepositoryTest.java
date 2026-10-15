package com.auction.repository;

import com.auction.model.Auction;
import com.auction.model.AuctionItem;
import com.auction.model.Bid;
import com.auction.model.Seller;
import com.auction.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests verifying InMemoryRepository<T, ID>:
 * - Repository save, lookup, listing, deletion, and duplicate-ID behavior
 * - Repository reuse for at least two different entity types (User, Auction, Bid)
 */
public class InMemoryRepositoryTest {

    private InMemoryRepository<User, Integer> userRepository;
    private InMemoryRepository<Auction, Integer> auctionRepository;
    private InMemoryRepository<Bid, Integer> bidRepository;

    @BeforeEach
    void setUp() {
        // Reuse for User
        userRepository = new InMemoryRepository<>(User::getId);
        // Reuse for Auction
        auctionRepository = new InMemoryRepository<>(Auction::getId);
        // Reuse for Bid
        bidRepository = new InMemoryRepository<>(Bid::getId);
    }

    @Test
    @DisplayName("Repository save and findById retrieves stored entity")
    void testSaveAndFindById() {
        User user = new User(1, "Aarav", "aarav@auction.com", com.auction.model.AuctionRole.ADMIN);
        userRepository.save(user);

        Optional<User> found = userRepository.findById(1);
        assertTrue(found.isPresent());
        assertEquals("Aarav", found.get().getName());

        Optional<User> missing = userRepository.findById(999);
        assertTrue(missing.isEmpty());
    }

    @Test
    @DisplayName("Duplicate ID save updates/replaces existing entity (Upsert semantics)")
    void testDuplicateIdReplacesEntity() {
        User original = new User(10, "Original", "orig@auction.com", com.auction.model.AuctionRole.BIDDER);
        userRepository.save(original);
        assertEquals(1, userRepository.count());

        User updated = new User(10, "Updated Name", "updated@auction.com", com.auction.model.AuctionRole.BIDDER);
        userRepository.save(updated);

        // Count should remain 1, entity should be updated
        assertEquals(1, userRepository.count());
        Optional<User> fetched = userRepository.findById(10);
        assertTrue(fetched.isPresent());
        assertEquals("Updated Name", fetched.get().getName());
        assertEquals("updated@auction.com", fetched.get().getEmail());
    }

    @Test
    @DisplayName("Repository findAll returns all stored entities in insertion order")
    void testFindAll() {
        userRepository.save(new User(1, "User1", "u1@auction.com", com.auction.model.AuctionRole.BIDDER));
        userRepository.save(new User(2, "User2", "u2@auction.com", com.auction.model.AuctionRole.SELLER));
        userRepository.save(new User(3, "User3", "u3@auction.com", com.auction.model.AuctionRole.ADMIN));

        List<User> all = userRepository.findAll();
        assertEquals(3, all.size());
        assertEquals(1, all.get(0).getId());
        assertEquals(2, all.get(1).getId());
        assertEquals(3, all.get(2).getId());
    }

    @Test
    @DisplayName("deleteById removes entity and returns true; missing ID returns false")
    void testDeleteById() {
        userRepository.save(new User(1, "User1", "u1@auction.com", com.auction.model.AuctionRole.BIDDER));
        assertEquals(1, userRepository.count());

        assertTrue(userRepository.deleteById(1));
        assertEquals(0, userRepository.count());
        assertTrue(userRepository.findById(1).isEmpty());

        // Deleting non-existent ID
        assertFalse(userRepository.deleteById(999));
        assertFalse(userRepository.deleteById(null));
    }

    @Test
    @DisplayName("Repository reuse: Auction entity CRUD operations")
    void testRepositoryReuseWithAuction() {
        Seller seller = new Seller(5, "sellerRavi", "pass", "Ravi", "ravi@auction.com", "Ravi Store", 4.9);
        Auction auction = new Auction(201, "MacBook Pro M3", seller, 120000.0, 140000.0,
                LocalDateTime.now(), LocalDateTime.now().plusDays(3));

        auctionRepository.save(auction);
        assertEquals(1, auctionRepository.count());

        Optional<Auction> found = auctionRepository.findById(201);
        assertTrue(found.isPresent());
        assertEquals("MacBook Pro M3", found.get().getProductName());

        assertTrue(auctionRepository.deleteById(201));
        assertEquals(0, auctionRepository.count());
    }

    @Test
    @DisplayName("Repository reuse: Bid entity CRUD operations")
    void testRepositoryReuseWithBid() {
        Bid bid = new Bid(301, 201, 1, "Meera", 125000.0, LocalDateTime.now());
        bidRepository.save(bid);

        assertEquals(1, bidRepository.count());
        Optional<Bid> found = bidRepository.findById(301);
        assertTrue(found.isPresent());
        assertEquals(125000.0, found.get().getAmount());
    }

    @Test
    @DisplayName("Saving null entity throws IllegalArgumentException")
    void testSaveNullEntityThrows() {
        assertThrows(IllegalArgumentException.class, () -> userRepository.save(null));
    }
}
