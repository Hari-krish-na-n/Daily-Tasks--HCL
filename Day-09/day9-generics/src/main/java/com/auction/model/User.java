package com.auction.model;

/**
 * User represents a participant in the Online Auction System.
 * Extends BaseEntity to inherit id and audit timestamps.
 *
 * Updated in Day 7:
 * - Added username field for authentication (FR1)
 * - Added password field (stored in-memory, excluded from toString/logs)
 * - Backwards-compatible constructors for existing tests and components
 */
public class User extends BaseEntity {

    private String username;
    private String password;
    private String name;
    private String email;
    // Role is stored as the AuctionRole enum
    private AuctionRole role;

    // Default constructor - creates a guest user
    public User() {
        this(0, "guest", "guest123", "Unknown", "unknown@auction.com", AuctionRole.GUEST);
    }

    // 4-arg constructor: legacy compatibility with Days 5 & 6
    public User(int id, String name, String email, AuctionRole role) {
        this(id, name != null ? name.toLowerCase().replaceAll("\\s+", "") : "user" + id,
                "password123", name, email, role);
    }

    // Full constructor with username and password
    public User(int id, String username, String password, String name, String email, AuctionRole role) {
        super(id);
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.role = role != null ? role : AuctionRole.GUEST;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username != null && !username.trim().isEmpty()) {
            this.username = username.trim();
            markUpdated();
        }
    }

    /**
     * Password is kept private for validation.
     * Never printed in toString() or audit logs.
     */
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password != null && !password.trim().isEmpty()) {
            this.password = password;
            markUpdated();
        }
    }

    public boolean verifyPassword(String rawPassword) {
        return this.password != null && this.password.equals(rawPassword);
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

    public String getAuthority() {
        return role != null ? role.getAuthority() : "ROLE_GUEST";
    }

    @Override
    public String getSummary() {
        return "User[id=" + getId() + ", username=" + username + ", name=" + name
                + ", role=" + (role != null ? role.getAuthority() : "GUEST") + "]";
    }

    /**
     * Crucial security rule: Password is NEVER printed in toString() or logs.
     */
    @Override
    public String toString() {
        return "User[id=" + getId() + ", username=" + username + ", name=" + name
                + ", email=" + email + ", role=" + role + "]";
    }
}
