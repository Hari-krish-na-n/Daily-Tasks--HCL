package com.auction.model;

/**
 * Base class representing a User in the Online Auction System.
 * Demonstrates:
 * - Encapsulation (private fields, public getters/setters)
 * - Constructor and 'this' keyword
 * - Protected and package-private access modifiers
 */
public class User {

    // Private fields: accessible only within this class (Encapsulation)
    private int userId;
    private String name;
    private String email;
    private String role;

    // Default constructor
    public User() {
        this(0, "Unknown", "unknown@auction.com", "GUEST");
    }

    // Parameterized constructor using 'this' to distinguish instance variables from parameters
    public User(int userId, String name, String email, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    // Public Getters and Setters (Encapsulation interface)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null && email.contains("@")) {
            this.email = email;
        }
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Protected method: accessible within the same package and subclasses in other packages
    protected String getMaskedEmail() {
        if (email == null || !email.contains("@")) {
            return "***";
        }
        String[] parts = email.split("@");
        String prefix = parts[0];
        String domain = parts[1];
        if (prefix.length() <= 2) {
            return prefix + "***@" + domain;
        }
        return prefix.charAt(0) + "***" + prefix.charAt(prefix.length() - 1) + "@" + domain;
    }

    // Package-private method: accessible only within package 'com.auction.model'
    void displayRoleSummary() {
        System.out.println("User ID: " + userId + ", Role: " + role);
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", name=" + name + ", email=" + email + ", role=" + role + "]";
    }
}
