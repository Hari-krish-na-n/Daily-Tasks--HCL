package com.auction.model;

/**
 * Seller extends User and adds selling-specific fields.
 *
 * Demonstrates:
 * - Multi-level inheritance: Seller -> User -> BaseEntity
 * - super() constructor chaining
 * - @Override on getSummary()
 */
public class Seller extends User {

    private String storeName;
    private double sellerRating;

    // Default constructor
    public Seller() {
        this(0, "Default Seller", "seller@auction.com", "General Store", 5.0);
    }

    // 3-arg constructor
    public Seller(int id, String name, String email) {
        this(id, name, email, name + "'s Store", 5.0);
    }

    // Legacy constructor
    public Seller(int id, String name, String email, String storeName, double sellerRating) {
        super(id, name, email, AuctionRole.SELLER);
        this.storeName = storeName;
        this.sellerRating = sellerRating;
    }

    // Full constructor including username and password
    public Seller(int id, String username, String password, String name, String email, String storeName, double sellerRating) {
        super(id, username, password, name, email, AuctionRole.SELLER);
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

    @Override
    public String getSummary() {
        return super.getSummary() + ", store=" + storeName
                + ", rating=" + sellerRating;
    }

    @Override
    public String toString() {
        return "Seller[id=" + getId() + ", username=" + getUsername()
                + ", name=" + getName() + ", email=" + getEmail()
                + ", store=" + storeName + ", rating=" + sellerRating + "]";
    }
}
