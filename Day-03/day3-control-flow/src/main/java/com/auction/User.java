package com.auction;

/**
 * User.java
 * -----------------------------------------------------------
 * Domain model representing a system user.
 */
public class User {
    private final String username;
    private final String role; // SELLER, BIDDER, ADMIN
    private final int pin;

    public User(String username, String role, int pin) {
        this.username = username;
        this.role = role;
        this.pin = pin;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public int getPin() {
        return pin;
    }

    public boolean verifyPin(int inputPin) {
        return this.pin == inputPin;
    }

    @Override
    public String toString() {
        return String.format("User[username=%s, role=%s]", username, role);
    }
}
