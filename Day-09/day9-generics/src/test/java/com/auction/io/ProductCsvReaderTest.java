package com.auction.io;

import com.auction.model.AuctionItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests verifying:
 * - try-with-resources and AutoCloseable closing order
 * - CSV file loading, parsing, and malformed-row handling
 * - FileNotFoundException handling
 */
public class ProductCsvReaderTest {

    @Test
    @DisplayName("AutoCloseable closing order: Multiple resources close in reverse order of declaration (LIFO)")
    void testResourceClosingOrder() {
        List<String> closeOrder = new ArrayList<>();

        class TrackedResource implements AutoCloseable {
            private final String name;

            TrackedResource(String name) {
                this.name = name;
            }

            @Override
            public void close() {
                closeOrder.add(name);
            }
        }

        try (TrackedResource first = new TrackedResource("FIRST");
             TrackedResource second = new TrackedResource("SECOND")) {
            // Both are open
            assertTrue(closeOrder.isEmpty());
        }

        // Must close in reverse order: SECOND first, then FIRST
        assertEquals(2, closeOrder.size());
        assertEquals("SECOND", closeOrder.get(0));
        assertEquals("FIRST", closeOrder.get(1));
    }

    @Test
    @DisplayName("CSV reading loads valid rows and safely ignores malformed rows")
    void testCsvReadingWithMalformedRows(@TempDir Path tempDir) throws IOException {
        Path csvFile = tempDir.resolve("test_products.csv");
        String content = "id,name,category,startingPrice,quantity\n" +
                "101,Laptop,Electronics,50000.0,10\n" +
                "corrupted_line_without_enough_commas\n" +
                "102,Phone,Electronics,25000.0,5\n" +
                "103,BadPrice,Audio,not_a_number,2\n" +
                "104,Headphones,Audio,3000.0,20\n";

        Files.writeString(csvFile, content);

        ProductCsvReader reader = new ProductCsvReader();
        List<AuctionItem> items = reader.readProducts(csvFile.toString());

        // There were 3 valid rows: 101, 102, 104
        assertEquals(3, items.size());
        assertEquals(101, items.get(0).getId());
        assertEquals("Laptop", items.get(0).getProductName());
        assertEquals(102, items.get(1).getId());
        assertEquals(104, items.get(2).getId());
    }

    @Test
    @DisplayName("CSV reading throws FileNotFoundException when file does not exist")
    void testCsvFileNotFound() {
        ProductCsvReader reader = new ProductCsvReader();
        assertThrows(FileNotFoundException.class, () -> {
            reader.readProducts("non_existent_path_to_products_xyz.csv");
        });
    }
}
