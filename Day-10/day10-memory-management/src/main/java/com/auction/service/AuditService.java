package com.auction.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * In-memory audit logger for tracking operations, their outcomes, and error reasons.
 * Typically invoked inside finally blocks to ensure execution regardless of success or exception.
 */
public class AuditService {

    public static class AuditRecord {
        private final LocalDateTime timestamp;
        private final String operation;
        private final boolean success;
        private final String details;

        public AuditRecord(String operation, boolean success, String details) {
            this.timestamp = LocalDateTime.now();
            this.operation = operation;
            this.success = success;
            this.details = details;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public String getOperation() {
            return operation;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getDetails() {
            return details;
        }

        @Override
        public String toString() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return String.format("[%s] %-25s | Status: %-7s | Details: %s",
                    timestamp.format(formatter),
                    operation,
                    (success ? "SUCCESS" : "FAILED"),
                    details);
        }
    }

    public static final int DEFAULT_MAX_RECORDS = 500;
    private final int maxRecords;
    private final List<AuditRecord> records = new ArrayList<>();

    public AuditService() {
        this(DEFAULT_MAX_RECORDS);
    }

    public AuditService(int maxRecords) {
        this.maxRecords = Math.max(10, maxRecords);
    }

    public synchronized void log(String operation, boolean success, String details) {
        // Enforce bounded memory retention to prevent memory leaks
        if (records.size() >= maxRecords) {
            records.remove(0); // Evict oldest record (FIFO)
        }
        AuditRecord record = new AuditRecord(operation, success, details);
        records.add(record);
        System.out.println("[AUDIT LOG] " + record);
    }

    public synchronized List<AuditRecord> getRecords() {
        return Collections.unmodifiableList(new ArrayList<>(records));
    }

    public synchronized int getRecordCount() {
        return records.size();
    }

    public int getMaxRecords() {
        return maxRecords;
    }

    public synchronized void clear() {
        records.clear();
    }
}
