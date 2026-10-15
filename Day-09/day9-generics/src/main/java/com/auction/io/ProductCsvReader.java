package com.auction.io;

import com.auction.model.AuctionItem;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for reading CSV files using try-with-resources.
 * Demonstrates:
 * - try-with-resources with BufferedReader and custom AutoCloseable
 * - Reverse closing order of multiple resources
 * - Graceful handling of FileNotFoundException and malformed CSV rows
 */
public class ProductCsvReader {

    /**
     * Reads products from a CSV file into a list of AuctionItem objects.
     * Expects header: id,name,category,startingPrice,quantity
     */
    public List<AuctionItem> readProducts(String filePath) throws IOException {
        Path path = Path.of(filePath);
        if (!Files.exists(path)) {
            throw new FileNotFoundException("CSV file not found at: " + filePath);
        }

        List<AuctionItem> products = new ArrayList<>();

        // try-with-resources with BufferedReader and custom ResourceTracker
        try (ResourceTracker tracker = new ResourceTracker("FileSession(" + path.getFileName() + ")");
             BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            tracker.doWork();
            String line;
            boolean isHeader = true;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty()) {
                    continue; // Skip empty lines safely
                }

                if (isHeader) {
                    isHeader = false;
                    continue; // Skip header row
                }

                try {
                    AuctionItem item = parseCsvRow(line, lineNumber);
                    if (item != null) {
                        products.add(item);
                    }
                } catch (IllegalArgumentException e) {
                    // Log malformed row and continue reading without terminating
                    System.err.printf("  [WARN] Skipping malformed CSV row %d: '%s' (Reason: %s)%n",
                            lineNumber, line, e.getMessage());
                }
            }
        } // Both reader and tracker are automatically closed here in reverse order of declaration!

        return products;
    }

    /**
     * Parses a single CSV line into an AuctionItem.
     * Format: id,name,category,startingPrice,quantity
     */
    public AuctionItem parseCsvRow(String line, int lineNumber) {
        String[] parts = line.split(",");
        if (parts.length < 5) {
            throw new IllegalArgumentException("Expected 5 columns, found " + parts.length);
        }

        try {
            int id = Integer.parseInt(parts[0].trim());
            String name = parts[1].trim();
            String category = parts[2].trim();
            double price = Double.parseDouble(parts[3].trim());
            int quantity = Integer.parseInt(parts[4].trim());

            if (name.isEmpty()) {
                throw new IllegalArgumentException("Product name cannot be empty");
            }
            if (price < 0) {
                throw new IllegalArgumentException("Price cannot be negative: " + price);
            }
            if (quantity < 0) {
                throw new IllegalArgumentException("Quantity cannot be negative: " + quantity);
            }

            return new AuctionItem(id, name, price);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric value in row " + lineNumber + ": " + e.getMessage(), e);
        }
    }
}
