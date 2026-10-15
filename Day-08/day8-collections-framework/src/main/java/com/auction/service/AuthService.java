package com.auction.service;

import com.auction.model.Admin;
import com.auction.model.AuctionRole;
import com.auction.model.Bidder;
import com.auction.model.Seller;
import com.auction.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Authentication and User Management Service implementing FR1.
 *
 * Collection Choices Justified:
 * 1. List<User> (implemented as ArrayList<User>):
 *    - Preserves user registration order.
 *    - Supports efficient iteration and indexed access when browsing user lists.
 *    - Represents our primary in-memory user registry.
 *
 * 2. Set<String> (implemented as HashSet<String>):
 *    - Tracks registered usernames in lowercase.
 *    - Provides O(1) average time complexity for duplicate username lookups during registration.
 *    - Rejects duplicate registrations instantly before creating User objects.
 */
public class AuthService {

    // PRIMARY REGISTRY: ArrayList preserves insertion order and allows ordered iteration
    private final List<User> registeredUsers = new ArrayList<>();

    // DEDUPLICATION INDEX: HashSet provides O(1) average membership check for unique usernames
    private final Set<String> registeredUsernames = new HashSet<>();

    private int nextUserId = 100;

    public AuthService() {
        // Seed default users for testing and demonstration
        seedDefaultUsers();
    }

    private void seedDefaultUsers() {
        registerUser("admin", "admin123", "System Admin", "admin@auction.com", AuctionRole.ADMIN, "Operations", 0.0);
        registerUser("ravi", "ravi123", "Ravi", "ravi@auction.com", AuctionRole.SELLER, "Ravi Electronics", 0.0);
        registerUser("krishna", "krishna123", "Krishna", "krishna@auction.com", AuctionRole.BIDDER, "", 75000.0);
        registerUser("meera", "meera123", "Meera", "meera@auction.com", AuctionRole.BIDDER, "", 200000.0);
    }

    /**
     * FR1: User Registration
     * Validates required details, checks for duplicate usernames using HashSet,
     * creates the concrete role instance, and stores in ArrayList.
     */
    public synchronized User registerUser(String username, String password, String name,
                                         String email, AuctionRole role,
                                         String extraDetail, double budget) {
        // Validation: required fields
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Registration error: Username cannot be empty.");
        }
        if (password == null || password.trim().length() < 4) {
            throw new IllegalArgumentException("Registration error: Password must be at least 4 characters.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Registration error: Full name cannot be empty.");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Registration error: Valid email address is required.");
        }
        if (role == null) {
            throw new IllegalArgumentException("Registration error: User role must be specified.");
        }

        String normalizedUsername = username.trim().toLowerCase();

        // HashSet check: O(1) instant duplicate check
        if (registeredUsernames.contains(normalizedUsername)) {
            throw new IllegalArgumentException("Registration error: Username '" + username + "' is already registered.");
        }

        int userId = ++nextUserId;
        User newUser;

        // Instantiate appropriate role hierarchy subclass
        switch (role) {
            case ADMIN -> newUser = new Admin(userId, normalizedUsername, password, name.trim(), email.trim(),
                    (extraDetail != null && !extraDetail.trim().isEmpty()) ? extraDetail.trim() : "General Operations");
            case SELLER -> newUser = new Seller(userId, normalizedUsername, password, name.trim(), email.trim(),
                    (extraDetail != null && !extraDetail.trim().isEmpty()) ? extraDetail.trim() : name + "'s Store", 5.0);
            case BIDDER -> newUser = new Bidder(userId, normalizedUsername, password, name.trim(), email.trim(),
                    budget > 0 ? budget : 10000.0);
            default -> newUser = new User(userId, normalizedUsername, password, name.trim(), email.trim(), role);
        }

        // Add to collections
        registeredUsernames.add(normalizedUsername);
        registeredUsers.add(newUser);

        return newUser;
    }

    /**
     * FR1: User Login
     * Iterates registered users to locate matching username and validates password.
     * Password is never logged or printed.
     */
    public User login(String username, String password) {
        if (username == null || password == null) {
            return null;
        }

        String normalized = username.trim().toLowerCase();

        for (User user : registeredUsers) {
            if (user.getUsername() != null && user.getUsername().equalsIgnoreCase(normalized)) {
                if (user.verifyPassword(password)) {
                    return user; // Login success
                } else {
                    return null; // Invalid credentials
                }
            }
        }
        return null; // User not found
    }

    /**
     * Checks if a username already exists.
     * Uses HashSet for O(1) lookup.
     */
    public boolean isUsernameTaken(String username) {
        if (username == null) {
            return false;
        }
        return registeredUsernames.contains(username.trim().toLowerCase());
    }

    /**
     * Returns an unmodifiable snapshot of the registered users list.
     */
    public List<User> getRegisteredUsers() {
        return new ArrayList<>(registeredUsers);
    }

    /**
     * Returns the set of registered usernames.
     */
    public Set<String> getRegisteredUsernames() {
        return new HashSet<>(registeredUsernames);
    }

    /**
     * Finds a user by username.
     */
    public User findByUsername(String username) {
        if (username == null) return null;
        String normalized = username.trim().toLowerCase();
        for (User user : registeredUsers) {
            if (user.getUsername().equalsIgnoreCase(normalized)) {
                return user;
            }
        }
        return null;
    }
}
