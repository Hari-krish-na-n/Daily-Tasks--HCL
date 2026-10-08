package com.auction.model;

/**
 * Seller class extending User.
 * Demonstrates:
 * - Inheritance ('extends User')
 * - 'super()' to invoke the parent class constructor
 * - 'super.toString()' to reuse parent behavior
 */
public class Seller extends User {

    private String storeName;
    private double sellerRating;

    // Default constructor
    public Seller() {
        super(0, "Default Seller", "seller@auction.com", "SELLER");
        this.storeName = "General Store";
        this.sellerRating = 5.0;
    }

    // Constructor with basic user info - demonstrates super()
    public Seller(int userId, String name, String email) {
        super(userId, name, email, "SELLER");
        this.storeName = name + "'s Store";
        this.sellerRating = 5.0;
    }

    // Overloaded constructor with full seller details
    public Seller(int userId, String name, String email, String storeName, double sellerRating) {
        super(userId, name, email, "SELLER");
        this.storeName = storeName;
        this.sellerRating = sellerRating;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public double getSellerRating() {
        return sellerRating;
    }

    public void setSellerRating(double sellerRating) {
        if (sellerRating >= 0.0 && sellerRating <= 5.0) {
            this.sellerRating = sellerRating;
        }
    }

    @Override
    public String toString() {
        return "Seller [userId=" + getUserId() + ", name=" + getName() +
               ", email=" + getEmail() + ", role=" + getRole() +
               ", storeName=" + storeName + ", sellerRating=" + sellerRating + "]";
    }
}
