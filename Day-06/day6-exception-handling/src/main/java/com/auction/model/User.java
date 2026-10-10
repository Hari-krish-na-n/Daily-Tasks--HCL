package com.auction.model;

/**
 * User represents a participant in the Online Auction System.
 * Extends BaseEntity to inherit id and audit timestamps.
 *
 * Day 4 had User as a standalone class. In Day 5 we extend BaseEntity
 * so that every entity in the system shares common identity fields.
 *
 * Demonstrates:
 * - Inheritance from abstract BaseEntity
 * - Constructor chaining with super()
 * - Overriding abstract getSummary()
 * - Protected vs private access modifiers
 *
 * Day 5 - Task A: Inheritance
 */
public class User extends BaseEntity {

    private String name;
    private String email;
    // Role is stored as the AuctionRole enum (Task B)
    private AuctionRole role;

    // Default constructor - creates a guest user
    public User() {
        this(0, "Unknown", "unknown@auction.com", AuctionRole.GUEST);
    }

    // Constructor used most often
    public User(int id, String name, String email, AuctionRole role) {
        super(id);
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
            markUpdated();
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null && email.contains("@")) {
            this.email = email;
            markUpdated();
        }
    }

    public AuctionRole getRole() {
        return role;
    }

    public void setRole(AuctionRole role) {
        this.role = role;
        markUpdated();
    }

    /**
     * Returns the Spring Security style authority name for the role.
     * For example: ROLE_ADMIN, ROLE_SELLER, ROLE_BIDDER
     */
    public String getAuthority() {
        return role.getAuthority();
    }

    // Demonstrates runtime polymorphism: subclasses override this differently
    @Override
    public String getSummary() {
        return "User[id=" + getId() + ", name=" + name
                + ", role=" + role.getAuthority() + "]";
    }

    @Override
    public String toString() {
        return "User[id=" + getId() + ", name=" + name
                + ", email=" + email + ", role=" + role + "]";
    }
}
