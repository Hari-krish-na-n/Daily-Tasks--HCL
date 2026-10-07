package com.auction;

/**
 * AuctionDataDemo.java
 * ---------------------
 * Day 2 - Java Language Fundamentals
 *
 * This program demonstrates all core Java language fundamentals
 * using the Online Auction System domain.
 *
 * Topics covered in order:
 *   1. Variables and all 8 primitive data types
 *   2. 1-D array (single auction bids)
 *   3. 2-D array (bids across multiple auctions)
 *   4. Constants (via AuctionConstants class)
 *   5. Operators (arithmetic, relational, logical, assignment, ternary)
 *   6. Type casting (widening and narrowing)
 *   7. Integer overflow - problem and fix
 *   8. Floating-point precision - known limitation of double
 *
 * HOW TO RUN:
 *   mvn clean compile
 *   mvn exec:java -Dexec.mainClass="com.auction.AuctionDataDemo"
 */
public class AuctionDataDemo {

    public static void main(String[] args) {

        printSectionHeader("ONLINE AUCTION SYSTEM - Java Language Fundamentals Demo");

        demonstratePrimitiveTypes();
        demonstrateOneDimensionalArray();
        demonstrateTwoDimensionalArray();
        demonstrateOperators();
        demonstrateTypeCasting();
        demonstrateIntegerOverflow();
        demonstrateFloatingPointPrecision();
    }

    // ================================================================
    //  SECTION 1 - Variables and the 8 Primitive Data Types
    // ================================================================
    static void demonstratePrimitiveTypes() {
        printSectionHeader("1. Variables and the 8 Primitive Data Types");

        /*
         * Java has exactly 8 primitive (built-in) data types.
         * They are NOT objects - they store raw values directly in memory.
         * Each type has a fixed size and a specific range.
         */

        // ---- byte ----
        // Size: 1 byte (8 bits)
        // Range: -128 to 127
        // Use: small counters, flags, low-level data
        byte auctionStatus = 1;   // 0=Pending, 1=Active, 2=Closed, 3=Cancelled
        System.out.println("[byte]   auctionStatus     = " + auctionStatus
                + "  (1 = Active)   | range: -128 to 127");

        // ---- short ----
        // Size: 2 bytes (16 bits)
        // Range: -32,768 to 32,767
        // Use: moderate-range integers
        short totalAuctions = 1_250;   // total auctions on the platform
        System.out.println("[short]  totalAuctions     = " + totalAuctions
                + "           | range: -32768 to 32767");

        // ---- int ----
        // Size: 4 bytes (32 bits)
        // Range: ~ -2.1 billion to ~ 2.1 billion
        // Use: most common integer type
        int auctionId = 100_001;          // unique auction identifier
        int registeredUsers = 45_230;     // total registered users
        System.out.println("[int]    auctionId         = " + auctionId
                + "        | range: ~ -2.1B to 2.1B");
        System.out.println("[int]    registeredUsers   = " + registeredUsers);

        // ---- long ----
        // Size: 8 bytes (64 bits)
        // Range: ~ -9.2 quintillion to 9.2 quintillion
        // Use: very large integers (timestamps, total transaction values)
        // Note: the letter 'L' at the end tells Java this is a long literal
        long totalTransactionValue = 98_765_432_100L;   // platform total in cents
        long auctionTimestamp      = System.currentTimeMillis(); // milliseconds since 1970
        System.out.println("[long]   totalTransaction  = " + totalTransactionValue
                + " | range: ~ -9.2 quintillion to 9.2 quintillion");
        System.out.println("[long]   auctionTimestamp  = " + auctionTimestamp + " ms");

        // ---- float ----
        // Size: 4 bytes (32 bits)
        // Precision: ~7 decimal digits
        // Note: the letter 'f' or 'F' at the end tells Java this is a float literal
        // Use: when memory is tight and approximate decimals are OK
        float commissionRate = 2.5f;   // 2.5% commission
        System.out.println("[float]  commissionRate    = " + commissionRate
                + "f              | ~7 decimal digits precision");

        // ---- double ----
        // Size: 8 bytes (64 bits)
        // Precision: ~15-16 decimal digits
        // Use: the default type for decimal numbers in Java
        double startingPrice  = 50_000.00;   // auction starting price in INR
        double reservePrice   = 75_000.00;   // minimum price seller will accept
        double currentHighBid = 72_500.50;   // highest bid placed so far
        System.out.println("[double] startingPrice     = " + startingPrice
                + "          | ~15 decimal digits precision");
        System.out.println("[double] reservePrice      = " + reservePrice);
        System.out.println("[double] currentHighBid    = " + currentHighBid);

        // ---- char ----
        // Size: 2 bytes (16 bits, stores a Unicode character)
        // Use: a single character
        char auctionGrade = 'B';   // 'A' = Premium, 'B' = Standard
        System.out.println("[char]   auctionGrade      = " + auctionGrade
                + "               | stores a single character");

        // ---- boolean ----
        // Size: not precisely defined by JVM (typically 1 bit of meaning)
        // Values: true or false - nothing else
        boolean isApproved    = false;   // admin has not approved this listing yet
        boolean isReserveMet  = false;   // reserve price not yet reached
        boolean isAutoProxy   = true;    // bidder has enabled proxy/auto-bidding
        System.out.println("[boolean] isApproved       = " + isApproved
                + "          | only true or false");
        System.out.println("[boolean] isReserveMet     = " + isReserveMet);
        System.out.println("[boolean] isAutoProxy      = " + isAutoProxy);

        System.out.println();
    }

    // ================================================================
    //  SECTION 2 - 1-D Array (Bids for a Single Auction)
    // ================================================================
    static void demonstrateOneDimensionalArray() {
        printSectionHeader("2. 1-D Array - Bids for a Single Auction");

        /*
         * An ARRAY stores multiple values of the same type in a single variable.
         * Think of it like a row of numbered boxes.
         *
         * Declaration:  double[] weeklyBids;
         * Creation:     weeklyBids = new double[5];   <- 5 boxes, index 0..4
         * Shortcut:     double[] weeklyBids = {v1, v2, v3, v4, v5};
         *
         * Index starts at 0 (zero-based indexing).
         * weeklyBids[0] is the FIRST element.
         * weeklyBids[weeklyBids.length - 1] is the LAST element.
         */

        // Sample bids placed on Auction #100001 (a vintage car)
        double[] weeklyBids = {
            50_000.00,   // Bid 1 - opening bid
            52_500.00,   // Bid 2
            55_000.00,   // Bid 3
            58_000.00,   // Bid 4
            62_500.00    // Bid 5 - current highest bid
        };

        System.out.println("Auction #100001 - Vintage Car");
        System.out.println("Number of bids placed : " + weeklyBids.length);
        System.out.println("Bids placed:");

        // Loop through every element using its index
        for (int i = 0; i < weeklyBids.length; i++) {
            System.out.println("  Bid[" + i + "] = INR " + weeklyBids[i]);
        }

        // ---- Calculate Total ----
        double total = 0.0;
        for (int i = 0; i < weeklyBids.length; i++) {
            total = total + weeklyBids[i];   // accumulate each bid into total
        }

        // ---- Calculate Average ----
        double average = total / weeklyBids.length;

        // ---- Calculate Maximum ----
        double max = weeklyBids[0];   // start by assuming first element is biggest
        for (int i = 1; i < weeklyBids.length; i++) {
            if (weeklyBids[i] > max) {
                max = weeklyBids[i];  // found a bigger value - update max
            }
        }

        // ---- Calculate Minimum ----
        double min = weeklyBids[0];   // start by assuming first element is smallest
        for (int i = 1; i < weeklyBids.length; i++) {
            if (weeklyBids[i] < min) {
                min = weeklyBids[i];  // found a smaller value - update min
            }
        }

        System.out.println("------------------------------");
        System.out.printf("Total bid amount  : INR %.2f%n", total);
        System.out.printf("Average bid       : INR %.2f%n", average);
        System.out.printf("Highest bid (max) : INR %.2f%n", max);
        System.out.printf("Starting bid (min): INR %.2f%n", min);
        System.out.println();
    }

    // ================================================================
    //  SECTION 3 - 2-D Array (Bids Across Multiple Auctions)
    // ================================================================
    static void demonstrateTwoDimensionalArray() {
        printSectionHeader("3. 2-D Array - Bids for Multiple Auctions");

        /*
         * A 2-D ARRAY is like a table with rows and columns.
         * Think of it as an array of arrays.
         *
         * Declaration:  double[][] auctionBids;
         *
         * Here:
         *   Rows    -> each row is a different auction
         *   Columns -> each column is a bid placed in that auction
         *
         *               Bid1      Bid2      Bid3
         *   Auction 1: 50,000   52,000   55,000
         *   Auction 2: 30,000   32,000   35,000
         *   Auction 3: 70,000   72,000   75,000
         *
         * Access:  auctionBids[row][column]
         *          auctionBids[0][0] = 50,000 (Auction 1, Bid 1)
         *          auctionBids[2][2] = 75,000 (Auction 3, Bid 3)
         */

        double[][] auctionBids = {
            { 50_000.00,  52_000.00,  55_000.00 },   // Auction 1 - Vintage Camera
            { 30_000.00,  32_000.00,  35_000.00 },   // Auction 2 - Antique Watch
            { 70_000.00,  72_000.00,  75_000.00 }    // Auction 3 - Rare Coin Set
        };

        String[] auctionNames = {
            "Vintage Camera",
            "Antique Watch",
            "Rare Coin Set"
        };

        System.out.println("Multi-Auction Bid Summary:");
        System.out.println("--------------------------------------------------");
        System.out.printf("%-20s %-12s %-12s %-12s%n",
                          "Auction", "Start Bid", "Mid Bid", "Highest Bid");
        System.out.println("--------------------------------------------------");

        // Outer loop: iterate over rows (each auction)
        for (int auction = 0; auction < auctionBids.length; auction++) {

            // Inner loop: iterate over columns (each bid in this auction)
            double highestInAuction = 0.0;
            for (int bid = 0; bid < auctionBids[auction].length; bid++) {
                if (auctionBids[auction][bid] > highestInAuction) {
                    highestInAuction = auctionBids[auction][bid];
                }
            }

            System.out.printf("%-20s %-12.0f %-12.0f %-12.0f%n",
                auctionNames[auction],
                auctionBids[auction][0],                          // first bid
                auctionBids[auction][auctionBids[auction].length / 2], // middle bid
                highestInAuction                                  // highest bid
            );
        }

        System.out.println("--------------------------------------------------");
        System.out.println();
    }

    // ================================================================
    //  SECTION 4 - Operators
    // ================================================================
    static void demonstrateOperators() {
        printSectionHeader("4. Operators");

        System.out.println(">> Using AuctionConstants.MINIMUM_BID_INCREMENT = "
                + AuctionConstants.MINIMUM_BID_INCREMENT);
        System.out.println();

        // ---- Arithmetic Operators (+, -, *, /, %) ----
        System.out.println("-- Arithmetic Operators --");
        double currentBid    = 72_500.00;
        double nextBid       = currentBid + AuctionConstants.MINIMUM_BID_INCREMENT;
        double salePrice     = 75_000.00;
        double platformFee   = salePrice * (AuctionConstants.PLATFORM_FEE_PERCENTAGE / 100.0);
        double sellerPayout  = salePrice - platformFee;
        int    totalBidCount = 12;
        int    buyerCount    = 5;
        int    bidsPerBuyer  = totalBidCount / buyerCount;      // integer division
        int    remainderBids = totalBidCount % buyerCount;      // modulo (remainder)

        System.out.printf("  currentBid           = INR %.2f%n", currentBid);
        System.out.printf("  nextBid (+ increment): INR %.2f + %.2f = INR %.2f%n",
                          currentBid, AuctionConstants.MINIMUM_BID_INCREMENT, nextBid);
        System.out.printf("  platformFee (5%%)    : INR %.2f * 5/100 = INR %.2f%n",
                          salePrice, platformFee);
        System.out.printf("  sellerPayout (-)     : INR %.2f - %.2f = INR %.2f%n",
                          salePrice, platformFee, sellerPayout);
        System.out.printf("  bidsPerBuyer (//)    : %d / %d = %d%n",
                          totalBidCount, buyerCount, bidsPerBuyer);
        System.out.printf("  remainderBids (%%)    : %d %% %d = %d%n",
                          totalBidCount, buyerCount, remainderBids);

        // ---- Relational / Comparison Operators (==, !=, >, <, >=, <=) ----
        System.out.println();
        System.out.println("-- Relational Operators --");
        double reservePrice = 70_000.00;
        double highestBid   = 75_000.00;

        boolean reserveMet = highestBid >= reservePrice;   // is highest bid >= reserve?
        boolean bidValid   = nextBid > currentBid;         // new bid must be greater than current
        boolean isTie      = highestBid == 75_000.00;      // exact equality check

        System.out.println("  highestBid >= reservePrice  -> " + reserveMet
                           + " (reserve price met: " + reserveMet + ")");
        System.out.println("  nextBid > currentBid        -> " + bidValid);
        System.out.println("  highestBid == 75000.00      -> " + isTie);

        // ---- Logical Operators (&&, ||, !) ----
        System.out.println();
        System.out.println("-- Logical Operators --");
        boolean isActive        = true;
        boolean isAdminApproved = true;
        boolean isBanned        = false;

        // && (AND): BOTH conditions must be true
        boolean canBid = isActive && isAdminApproved && !isBanned;
        System.out.println("  canBid (active AND approved AND NOT banned) -> " + canBid);

        // || (OR): at least ONE condition must be true
        boolean requiresReview = !isAdminApproved || isBanned;
        System.out.println("  requiresReview (!approved OR banned)        -> " + requiresReview);

        // ---- Assignment Operators (=, +=, -=, *=, /=, %=) ----
        System.out.println();
        System.out.println("-- Assignment Operators --");
        double bidAmount = 50_000.00;
        System.out.println("  Initial bidAmount   = " + bidAmount);

        bidAmount += AuctionConstants.MINIMUM_BID_INCREMENT;  // same as: bidAmount = bidAmount + 500
        System.out.println("  After += increment  = " + bidAmount);

        bidAmount -= 200.0;   // subtract 200 (demo purpose)
        System.out.println("  After -= 200        = " + bidAmount);

        double fee = bidAmount;
        fee *= 0.05;          // same as: fee = fee * 0.05 (5% fee)
        System.out.printf("  Fee (* 0.05)        = %.2f%n", fee);

        // ---- Increment and Decrement Operators (++, --) ----
        System.out.println();
        System.out.println("-- Increment / Decrement Operators --");
        int bidCounter = 10;
        System.out.println("  bidCounter (start)  = " + bidCounter);

        bidCounter++;   // post-increment: first uses value, then increments
        System.out.println("  After bidCounter++  = " + bidCounter);

        ++bidCounter;   // pre-increment: increments first, then uses value
        System.out.println("  After ++bidCounter  = " + bidCounter);

        bidCounter--;   // post-decrement
        System.out.println("  After bidCounter--  = " + bidCounter);

        // ---- Ternary Operator (condition ? valueIfTrue : valueIfFalse) ----
        System.out.println();
        System.out.println("-- Ternary Operator --");
        double winningBid = 120_000.00;

        // Syntax: result = (condition) ? valueWhenTrue : valueWhenFalse;
        char auctionGrade = (winningBid >= AuctionConstants.GRADE_A_THRESHOLD) ? 'A' : 'B';
        System.out.println("  winningBid = " + winningBid);
        System.out.println("  GRADE_A_THRESHOLD = " + AuctionConstants.GRADE_A_THRESHOLD);
        System.out.printf("  auctionGrade = (bid >= threshold) ? 'A' : 'B' -> '%c'%n", auctionGrade);

        String statusLabel = reserveMet ? "SOLD" : "NOT SOLD (reserve not met)";
        System.out.println("  statusLabel = " + statusLabel);

        System.out.println();
    }

    // ================================================================
    //  SECTION 5 - Type Casting
    // ================================================================
    static void demonstrateTypeCasting() {
        printSectionHeader("5. Type Casting");

        /*
         * TYPE CASTING = converting a value from one data type to another.
         *
         * There are two kinds:
         *   A) WIDENING (automatic / implicit): smaller type -> larger type
         *      No data is lost. Java does this automatically.
         *
         *   B) NARROWING (manual / explicit): larger type -> smaller type
         *      Data MAY be lost. You must tell Java explicitly.
         *      Use syntax: (targetType) value
         */

        System.out.println("-- Widening (Automatic) --");
        System.out.println("   No cast needed. Java promotes smaller type to larger type.");

        int bidCount = 7;

        // int -> long   (widening: 4 bytes -> 8 bytes, no data loss)
        long bidCountLong = bidCount;   // Java does this automatically
        System.out.println("  int bidCount        = " + bidCount);
        System.out.println("  long bidCountLong   = " + bidCountLong + "  (int -> long, automatic)");

        // int -> double (widening: 4 bytes -> 8 bytes, no data loss)
        double bidCountDouble = bidCount;   // also automatic
        System.out.println("  double bidCountDouble = " + bidCountDouble
                           + "  (int -> double, automatic)");

        System.out.println();
        System.out.println("-- Narrowing (Manual / Explicit Cast) --");
        System.out.println("   You MUST add the cast in parentheses: (int) someDouble");

        double averageBid = 56_333.666;
        // double -> int: the decimal part is TRUNCATED (not rounded)
        int averageBidInt = (int) averageBid;   // you tell Java: "I know I may lose data"
        System.out.println("  double averageBid   = " + averageBid);
        System.out.println("  int averageBidInt   = (int) averageBid = " + averageBidInt
                           + "  (.666 is DROPPED, not rounded)");

        long largeAuctionId = 9_876_543_210L;
        // long -> int: may overflow if value is too large for int
        int smallId = (int) largeAuctionId;  // CAUTION: value may be wrong if > 2.1B
        System.out.println("  long largeAuctionId = " + largeAuctionId);
        System.out.println("  int smallId         = (int) largeAuctionId = " + smallId
                           + "  (may be WRONG if value > Integer.MAX_VALUE)");

        System.out.println();
        System.out.println("-- Correct way to calculate average with casting --");
        double[] bids  = { 50_000, 55_000, 60_000, 65_000, 70_000 };
        int count      = bids.length;
        double sumBids = 0;
        for (double b : bids) sumBids += b;

        // Without cast: if both total and count were int, result would be integer division
        // With cast   : we ensure floating-point division for accurate average
        double average = (double) sumBids / count;   // cast guarantees decimal result
        System.out.printf("  sumBids = %.0f, count = %d%n", sumBids, count);
        System.out.printf("  average = (double) sumBids / count = %.2f%n", average);

        System.out.println();
    }

    // ================================================================
    //  SECTION 6 - Integer Overflow
    // ================================================================
    static void demonstrateIntegerOverflow() {
        printSectionHeader("6. Integer Overflow - Problem and Fix");

        /*
         * INTEGER OVERFLOW happens when a calculation produces a value
         * that is LARGER than the maximum value an int can store.
         *
         * int range: -2,147,483,648 to 2,147,483,647 (about 2.1 billion)
         *
         * If you exceed this, Java does NOT throw an error.
         * Instead, the value "wraps around" and becomes a wrong (negative) number.
         * This is a SILENT BUG - very dangerous!
         *
         * FIX: Use long instead of int for large values.
         * long range: -9,223,372,036,854,775,808 to 9,223,372,036,854,775,807
         */

        System.out.println("-- Problem: int Overflow --");

        int bidsPerDay   = 50_000;
        int daysRunning  = 18_250;

        int overflow1 = bidsPerDay * daysRunning;              // 912,500,000 (fits)
        System.out.println("  bidsPerDay * daysRunning = " + overflow1 + "  (OK so far)");

        int overflow2 = bidsPerDay * daysRunning * 100;        // OVERFLOW!
        System.out.println("  bidsPerDay * daysRunning * 100 = " + overflow2
                           + "  <- WRONG! Should be 91,250,000,000");
        System.out.println("  (This is the silent overflow bug - Java gave no error!)");

        System.out.println();
        System.out.println("-- Fix: Use long --");

        // The 'L' suffix makes the literal a long, preventing overflow
        long fixedBidsPerDay  = 50_000L;
        long fixedDaysRunning = 18_250L;
        long fixedTotal       = fixedBidsPerDay * fixedDaysRunning * 100L;

        System.out.println("  (long) bidsPerDay * daysRunning * 100 = " + fixedTotal
                           + "  <- CORRECT!");
        System.out.println("  Always use 'long' for large counts, timestamps, money in paisa/cents.");
        System.out.println();
    }

    // ================================================================
    //  SECTION 7 - Floating-Point Precision
    // ================================================================
    static void demonstrateFloatingPointPrecision() {
        printSectionHeader("7. Floating-Point Precision - Known Limitation");

        /*
         * FLOATING-POINT NUMBERS (float and double) are stored in binary
         * (base 2) inside the computer. Some decimal fractions like 0.1,
         * 0.2, 0.3 CANNOT be represented exactly in binary.
         *
         * This is similar to how 1/3 = 0.333... never ends in decimal.
         *
         * Result: small rounding errors appear in double arithmetic.
         * This is NOT a Java bug - it is a fundamental limitation of
         * the IEEE 754 standard used by most programming languages.
         */

        System.out.println("-- The Classic 0.1 + 0.2 Problem --");
        double a = 0.1;
        double b = 0.2;
        double result = a + b;

        System.out.println("  double a = 0.1");
        System.out.println("  double b = 0.2");
        System.out.println("  a + b    = " + result
                           + "  <- Expected 0.3 but got a tiny rounding error!");
        System.out.println("  Is (a + b) == 0.3?  -> " + (result == 0.3)
                           + "  <- Do NOT use == to compare doubles!");

        System.out.println();
        System.out.println("-- Auction Price Precision Example --");
        double bidPrice1 = 72_500.10;
        double bidPrice2 = 72_500.20;
        double totalPrice = bidPrice1 + bidPrice2;

        System.out.println("  bidPrice1 + bidPrice2 = " + totalPrice);
        System.out.printf("  Formatted to 2 dp    : %.2f%n", totalPrice);
        System.out.println("  (Use String.format or printf for display - never == for comparison)");

        System.out.println();
        System.out.println("-- Safe Comparison Using a Tolerance (epsilon) --");
        double epsilon = 0.000001;   // tolerance (very small number)
        boolean isEqual = Math.abs(result - 0.3) < epsilon;
        System.out.println("  Math.abs((0.1+0.2) - 0.3) < 0.000001 -> " + isEqual
                           + "  <- Correct way to compare doubles!");

        System.out.println();
        System.out.println("-- Key Takeaway --");
        System.out.println("  For FINANCIAL calculations (money, prices, fees):");
        System.out.println("  Use java.math.BigDecimal instead of double for EXACT results.");
        System.out.println("  BigDecimal is covered in advanced Java topics.");
        System.out.println();
    }

    // ================================================================
    //  Helper method - prints a formatted section header (clean ASCII)
    // ================================================================
    static void printSectionHeader(String title) {
        System.out.println();
        System.out.println("+==========================================================+");
        System.out.printf( "|  %-56s|%n", title);
        System.out.println("+==========================================================+");
    }
}
