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

    private final List<AuditRecord> records = new ArrayList<>();

    public void log(String operation, boolean success, String details) {
        AuditRecord record = new AuditRecord(operation, success, details);
        records.add(record);
        System.out.println("[AUDIT LOG] " + record);
    }

    public List<AuditRecord> getRecords() {
        return Collections.unmodifiableList(records);
    }

    public int getRecordCount() {
        return records.size();
    }

    public void clear() {
        records.clear();
    }
}
