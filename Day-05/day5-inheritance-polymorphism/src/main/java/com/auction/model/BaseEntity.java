package com.auction.model;

import java.time.LocalDateTime;

/**
 * Abstract base class for all domain entities in the Online Auction System.
 *
 * Demonstrates:
 * - Abstract classes: cannot be instantiated directly, only extended
 * - Common field inheritance: subclasses automatically get id, createdAt, updatedAt
 * - Template for entity identity and audit information
 *
 * Day 5 - Task A: BaseEntity / Inheritance
 */
public abstract class BaseEntity {

    // Every entity has a unique integer ID
    private int id;

    // Audit timestamps - when the entity was created and last modified
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Protected: subclasses can call this to initialize audit fields
    protected BaseEntity() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Protected: subclasses can call this when they already know the ID
    protected BaseEntity(int id) {
        this.id = id;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Call this whenever the entity is modified so the updatedAt timestamp stays fresh.
     */
    protected void markUpdated() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Abstract method: every entity must be able to describe itself.
     * Forces subclasses to implement a meaningful summary string.
     */
    public abstract String getSummary();
}
