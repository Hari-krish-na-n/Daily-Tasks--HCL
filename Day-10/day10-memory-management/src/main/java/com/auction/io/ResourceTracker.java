package com.auction.io;

/**
 * A custom AutoCloseable resource that logs when it is opened and closed.
 * Useful for demonstrating:
 * 1. try-with-resources closing guarantees
 * 2. Reverse closing order of multiple resources (LIFO - Last Opened, First Closed)
 */
public class ResourceTracker implements AutoCloseable {

    private final String resourceName;
    private boolean closed = false;

    public ResourceTracker(String resourceName) {
        this.resourceName = resourceName;
        System.out.println("  [ResourceTracker] OPENED resource: " + resourceName);
    }

    public void doWork() {
        if (closed) {
            throw new IllegalStateException("Cannot work on already closed resource: " + resourceName);
        }
        System.out.println("  [ResourceTracker] WORKING with resource: " + resourceName);
    }

    public String getResourceName() {
        return resourceName;
    }

    public boolean isClosed() {
        return closed;
    }

    @Override
    public void close() {
        this.closed = true;
        System.out.println("  [ResourceTracker] CLOSED resource: " + resourceName);
    }
}
