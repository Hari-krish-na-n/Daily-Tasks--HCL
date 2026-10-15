package com.auction.toolkit;

import com.auction.io.ProductCsvReader;
import com.auction.io.ResourceTracker;
import com.auction.model.Auction;
import com.auction.model.AuctionItem;
import com.auction.model.Bid;
import com.auction.model.Seller;
import com.auction.model.User;
import com.auction.repository.CrudRepository;
import com.auction.repository.InMemoryRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Main demonstration class for Day 9: Generics.
 * Demonstrates:
 * A. GenericToolkit (Pair, Box<T extends Number>, max(), bounded wildcards, PECS, Type Erasure)
 * B. try-with-resources, AutoCloseable closing order, CSV file parsing
 * C. Generic CrudRepository and InMemoryRepository reused across multiple domain entities
 */
public class GenericToolkitDemo {

    public static void main(String[] args) {
        System.out.println("===============================================================================");
        System.out.println("               DAY 9: GENERICS & TRY-WITH-RESOURCES DEMO                       ");
        System.out.println("===============================================================================\n");

        demonstrateGenericToolkit();
        demonstrateTryWithResourcesAndCsv();
        demonstrateGenericRepository();
    }

    // =========================================================================
    // SECTION A: GENERIC TOOLKIT DEMO
    // =========================================================================

    public static void demonstrateGenericToolkit() {
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("1. GENERIC TOOLKIT DEMONSTRATION");
        System.out.println("-------------------------------------------------------------------------------");

        // 1. Pair<K, V>
        GenericToolkit.Pair<String, Double> productPrice = new GenericToolkit.Pair<>("MacBook Pro", 125000.0);
        GenericToolkit.Pair<Integer, String> userBadge = new GenericToolkit.Pair<>(101, "ADMIN");
        System.out.println("Pair 1: " + productPrice + " (Key: " + productPrice.getFirst() + ", Val: " + productPrice.getSecond() + ")");
        System.out.println("Pair 2: " + userBadge + " (Key: " + userBadge.getFirst() + ", Val: " + userBadge.getSecond() + ")");

        // 2. Box<T extends Number>
        GenericToolkit.Box<Integer> intBox = new GenericToolkit.Box<>(500);
        GenericToolkit.Box<Double> doubleBox = new GenericToolkit.Box<>(499.95);
        // GenericToolkit.Box<String> stringBox = new GenericToolkit.Box<>("Invalid"); // Compile-time error!
        System.out.println("intBox: " + intBox + ", doubleBox: " + doubleBox);
        System.out.println("Is intBox > doubleBox? " + intBox.isGreaterThan(doubleBox));

        // 3. Generic max() with <T extends Comparable<T>>
        Integer maxInt = GenericToolkit.max(45, 99);
        String maxStr = GenericToolkit.max("Apple", "Banana");
        System.out.println("max(45, 99): " + maxInt);
        System.out.println("max('Apple', 'Banana'): " + maxStr);

        // 4. Wildcard (? extends Number) - Reading / Producer
        List<Integer> intList = List.of(10, 20, 30, 40);
        List<Double> doubleList = List.of(1.5, 2.5, 3.5);
        System.out.println("sumOfNumbers(intList): " + GenericToolkit.sumOfNumbers(intList));
        System.out.println("sumOfNumbers(doubleList): " + GenericToolkit.sumOfNumbers(doubleList));

        // 5. Wildcard (? super Integer) - Writing / Consumer
        List<Number> numList = new ArrayList<>();
        GenericToolkit.appendIntegers(numList, 1, 5);
        System.out.println("numList after appendIntegers(? super Integer): " + numList);

        // 6. PECS Principle: Producer Extends, Consumer Super
        List<Integer> source = List.of(100, 200, 300);
        List<Number> destination = new ArrayList<>();
        GenericToolkit.copy(source, destination);
        System.out.println("PECS copy (List<Integer> extends -> List<Number> super): " + destination);

        // 7. Type Erasure Note
        System.out.println("\n--- Type Erasure Concept ---");
        System.out.println("In Java, generics exist only at compile time. At runtime, the JVM erases generic type");
        System.out.println("parameters (e.g., List<String> and List<Integer> both become raw List).");
        System.out.println("Hence: intList.getClass() == doubleList.getClass() is: "
                + (intList.getClass().equals(doubleList.getClass())));
        System.out.println();
    }

    // =========================================================================
    // SECTION B: TRY-WITH-RESOURCES & AUTOCLOSEABLE
    // =========================================================================

    public static void demonstrateTryWithResourcesAndCsv() {
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("2. TRY-WITH-RESOURCES & AUTOCLOSEABLE DEMONSTRATION");
        System.out.println("-------------------------------------------------------------------------------");

        System.out.println("Demonstrating AutoCloseable and resource closing order:");
        try (ResourceTracker r1 = new ResourceTracker("DatabaseConnection");
             ResourceTracker r2 = new ResourceTracker("NetworkSocket")) {

            r1.doWork();
            r2.doWork();
            System.out.println("  [Execution] Exiting try-with-resources block...");
        } // r2 closes first, then r1 closes (LIFO reverse order)

        System.out.println("\nReading seed products from CSV file with try-with-resources:");
        ProductCsvReader csvReader = new ProductCsvReader();
        String csvPath = "src/main/resources/products.csv";

        try {
            List<AuctionItem> items = csvReader.readProducts(csvPath);
            System.out.println("Successfully loaded " + items.size() + " valid products from CSV:");
            for (AuctionItem item : items) {
                System.out.printf("   - %s%n", item.getSummary());
            }
        } catch (IOException e) {
            System.err.println("Failed to read CSV: " + e.getMessage());
        }
        System.out.println();
    }

    // =========================================================================
    // SECTION C: GENERIC CRUD REPOSITORY REUSE
    // =========================================================================

    public static void demonstrateGenericRepository() {
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("3. GENERIC REPOSITORY REUSE (CrudRepository<T, ID>)");
        System.out.println("-------------------------------------------------------------------------------");

        // Reuse 1: User Repository (ID = Integer)
        CrudRepository<User, Integer> userRepo = new InMemoryRepository<>(User::getId);
        userRepo.save(new User(1, "Aarav", "aarav@auction.com", com.auction.model.AuctionRole.ADMIN));
        userRepo.save(new User(2, "Meera", "meera@auction.com", com.auction.model.AuctionRole.BIDDER));
        userRepo.save(new User(3, "Rohan", "rohan@auction.com", com.auction.model.AuctionRole.SELLER));

        System.out.println("a) User Repository (Count = " + userRepo.findAll().size() + "):");
        userRepo.findAll().forEach(u -> System.out.println("   " + u));

        // Duplicate ID Handling Demo:
        System.out.println("\nTesting Duplicate ID Save on User Repo:");
        User updatedMeera = new User(2, "Meera Nair", "meera.nair@auction.com", com.auction.model.AuctionRole.BIDDER);
        userRepo.save(updatedMeera);
        System.out.println("After saving User with existing ID #2: " + userRepo.findById(2).orElse(null));

        // Deletion Demo:
        boolean deleted = userRepo.deleteById(3);
        System.out.println("Deleted User #3: " + deleted + ", Current count: " + userRepo.findAll().size());

        // Reuse 2: Auction Repository (ID = Integer)
        CrudRepository<Auction, Integer> auctionRepo = new InMemoryRepository<>(Auction::getId);
        Seller seller = new Seller(10, "sellerRavi", "pass", "Ravi", "ravi@auction.com", "Ravi Store", 4.9);
        auctionRepo.save(new Auction(1001, "MacBook Pro M3", seller, 120000.0, 140000.0, LocalDateTime.now(), LocalDateTime.now().plusDays(5)));
        auctionRepo.save(new Auction(1002, "Sony WH-1000XM5", seller, 18000.0, 22000.0, LocalDateTime.now(), LocalDateTime.now().plusDays(3)));

        System.out.println("\nb) Auction Repository (Count = " + auctionRepo.findAll().size() + "):");
        auctionRepo.findAll().forEach(a -> System.out.println("   " + a.getSummary()));

        // Reuse 3: Bid Repository (ID = Integer)
        CrudRepository<Bid, Integer> bidRepo = new InMemoryRepository<>(Bid::getId);
        bidRepo.save(new Bid(501, 1001, 2, "Meera", 125000.0, LocalDateTime.now()));
        bidRepo.save(new Bid(502, 1001, 1, "Aarav", 130000.0, LocalDateTime.now().plusMinutes(5)));

        System.out.println("\nc) Bid Repository (Count = " + bidRepo.findAll().size() + "):");
        bidRepo.findAll().forEach(b -> System.out.println("   " + b.getSummary()));

        System.out.println("===============================================================================\n");
    }
}
