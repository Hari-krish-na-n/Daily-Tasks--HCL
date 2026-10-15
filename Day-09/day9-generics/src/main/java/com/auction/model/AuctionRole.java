package com.auction.model;

/**
 * AuctionRole enum represents the possible roles a user can have.
 *
 * Day 4 stored role as a plain String ("BIDDER", "SELLER").
 * In Day 5 we improve the design using an enum so:
 * - Invalid role names are caught at compile time
 * - Each role carries its Spring Security authority string
 * - Each role knows what it can do (canBid, canListItems)
 *
 * Demonstrates:
 * - Enum-based role hierarchy (Task B)
 * - Polymorphism: each enum constant overrides getDescription()
 * - Keeping authority names compatible with Spring Security conventions
 *
 * Day 5 - Task B: Role Hierarchy
 */
public enum AuctionRole {

    ADMIN("ROLE_ADMIN") {
        @Override
        public String getDescription() {
            return "Administrator - manages users, auctions and platform settings.";
        }

        @Override
        public boolean canBid() {
            // Admins manage the platform; they do not bid to avoid conflict of interest
            return false;
        }

        @Override
        public boolean canListItems() {
            return true;
        }
    },

    MANAGER("ROLE_MANAGER") {
        @Override
        public String getDescription() {
            return "Manager - approves auctions and oversees bidding activity.";
        }

        @Override
        public boolean canBid() {
            return false;
        }

        @Override
        public boolean canListItems() {
            return true;
        }
    },

    SELLER("ROLE_SELLER") {
        @Override
        public String getDescription() {
            return "Seller - lists products for auction.";
        }

        @Override
        public boolean canBid() {
            // Sellers cannot bid on their own auctions (enforced in service)
            return false;
        }

        @Override
        public boolean canListItems() {
            return true;
        }
    },

    BIDDER("ROLE_BIDDER") {
        @Override
        public String getDescription() {
            return "Bidder - participates in auctions by placing bids.";
        }

        @Override
        public boolean canBid() {
            return true;
        }

        @Override
        public boolean canListItems() {
            return false;
        }
    },

    GUEST("ROLE_GUEST") {
        @Override
        public String getDescription() {
            return "Guest - can browse auctions but cannot bid or list items.";
        }

        @Override
        public boolean canBid() {
            return false;
        }

        @Override
        public boolean canListItems() {
            return false;
        }
    };

    // Authority string that maps to Spring Security GrantedAuthority
    private final String authority;

    // Enum constructor - each constant calls this
    AuctionRole(String authority) {
        this.authority = authority;
    }

    /**
     * Returns the authority string compatible with Spring Security naming conventions.
     * Example: "ROLE_BIDDER"
     */
    public String getAuthority() {
        return authority;
    }

    /**
     * Abstract-like method using abstract in enum - every constant overrides this
     * to provide its own human-readable description (runtime polymorphism in enum form).
     */
    public abstract String getDescription();

    /** Returns whether users with this role are allowed to place bids. */
    public abstract boolean canBid();

    /** Returns whether users with this role are allowed to list auction items. */
    public abstract boolean canListItems();
}
