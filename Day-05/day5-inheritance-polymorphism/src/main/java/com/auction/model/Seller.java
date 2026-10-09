package com.auction.model;

/**
 * Seller extends User and adds selling-specific fields.
 *
 * Demonstrates:
 * - Multi-level inheritance: Seller -> User -> BaseEntity
 * - super() constructor chaining
 * - @Override on getSummary()
 *
 * Day 5 - Task A: Inheritance
 */
public class Seller extends User {

    private String storeName;
    private double sellerRating;

    // Default constructor
    public Seller() {
        this(0, "Default Seller", "seller@auction.com", "General Store", 5.0);
    }

    // 3-arg constructor - derives store name from user name
    public Seller(int id, String name, String email) {
        this(id, name, email, name + "'s Store", 5.0);
    }

    // Full constructor: calls super(User), which calls super(BaseEntity)
    public Seller(int id, String name, String email, String storeName, double sellerRating) {
        super(id, name, email, AuctionRole.SELLER); // chains up to User
        this.storeName = storeName;
        this.sellerRating = sellerRating;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
        markUpdated();
    }

    public double getSellerRating() {
        return sellerRating;
    }

    public void setSellerRating(double sellerRating) {
        if (sellerRating >= 0.0 && sellerRating <= 5.0) {
            this.sellerRating = sellerRating;
            markUpdated();
        }
    }

    /**
     * Overrides User's getSummary().
     * Calls super.getSummary() then appends seller-specific info.
     */
    @Override
    public String getSummary() {
        return super.getSummary() + ", store=" + storeName
                + ", rating=" + sellerRating;
    }

    @Override
    public String toString() {
        return "Seller[id=" + getId() + ", name=" + getName()
                + ", email=" + getEmail()
                + ", store=" + storeName
                + ", rating=" + sellerRating + "]";
    }
}
