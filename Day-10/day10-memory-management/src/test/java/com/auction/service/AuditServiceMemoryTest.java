package com.auction.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AuditService Memory Management Tests")
class AuditServiceMemoryTest {

    @Test
    @DisplayName("AuditService enforces bounded memory capacity via FIFO eviction")
    void testBoundedCapacityEviction() {
        AuditService boundedService = new AuditService(20);
        assertEquals(20, boundedService.getMaxRecords());

        // Log 35 records
        for (int i = 1; i <= 35; i++) {
            boundedService.log("OPERATION_" + i, true, "Detail " + i);
        }

        // Must not exceed configured capacity
        assertEquals(20, boundedService.getRecordCount(),
                "Record count must be bounded to maxRecords to avoid memory leak");

        // Oldest records (1-15) were evicted; first remaining record should be OPERATION_16
        AuditService.AuditRecord oldestRemaining = boundedService.getRecords().get(0);
        assertEquals("OPERATION_16", oldestRemaining.getOperation(),
                "Oldest records should be evicted first under FIFO policy");
    }

    @Test
    @DisplayName("AuditService clear() frees all retained records")
    void testClearReleasesReferences() {
        AuditService service = new AuditService(50);
        for (int i = 0; i < 10; i++) {
            service.log("OP", true, "Detail");
        }
        assertEquals(10, service.getRecordCount());

        service.clear();
        assertEquals(0, service.getRecordCount());
        assertTrue(service.getRecords().isEmpty());
    }
}
