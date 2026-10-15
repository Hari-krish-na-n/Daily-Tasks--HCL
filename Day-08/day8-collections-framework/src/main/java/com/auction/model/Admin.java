package com.auction.model;

/**
 * Admin extends User and adds admin-specific fields.
 *
 * Demonstrates:
 * - Specialization of User for platform moderation (FR3)
 * - Constructor chaining
 * - Overriding getSummary()
 */
public class Admin extends User {

    private String department;

    public Admin() {
        this(0, "admin", "admin123", "Default Admin", "admin@auction.com", "Operations");
    }

    public Admin(int id, String name, String email) {
        this(id, name != null ? name.toLowerCase() : "admin", "admin123", name, email, "Operations");
    }

    public Admin(int id, String name, String email, String department) {
        this(id, name != null ? name.toLowerCase() : "admin", "admin123", name, email, department);
    }

    public Admin(int id, String username, String password, String name, String email, String department) {
        super(id, username, password, name, email, AuctionRole.ADMIN);
        this.department = department != null ? department : "Operations";
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        if (department != null && !department.trim().isEmpty()) {
            this.department = department;
            markUpdated();
        }
    }

    @Override
    public String getSummary() {
        return super.getSummary() + ", department=" + department;
    }

    @Override
    public String toString() {
        return "Admin[id=" + getId() + ", username=" + getUsername()
                + ", name=" + getName() + ", email=" + getEmail()
                + ", department=" + department + "]";
    }
}
