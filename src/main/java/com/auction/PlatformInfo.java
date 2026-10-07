package com.auction;

/**
 * PlatformInfo.java
 * -----------------
 * Day 1 — Java Platform Basics
 *
 * This program prints information about the Java platform at runtime.
 * It uses two built-in Java classes:
 *   - System   : gives us access to JVM system properties
 *   - Runtime  : gives us access to the JVM runtime environment
 *
 * HOW TO RUN WITHOUT MAVEN (plain javac / java):
 *   1. Copy this file to any folder (remove the package declaration if needed)
 *   2. javac PlatformInfo.java
 *   3. java  PlatformInfo
 *
 * HOW TO INSPECT BYTECODE:
 *   javap -c PlatformInfo.class      ← shows disassembled bytecode
 *   java -verbose:class PlatformInfo ← shows every class loaded by the JVM
 *
 * HOW TO RUN INSIDE THE MAVEN PROJECT:
 *   mvn clean compile
 *   mvn exec:java -Dexec.mainClass="com.auction.PlatformInfo"
 */
public class PlatformInfo {

    public static void main(String[] args) {

        // ----------------------------------------------------------------
        // 1. Read system properties
        //    System.getProperty(key) asks the JVM for a named value that
        //    was set when the JVM started. These are "well-known" keys.
        // ----------------------------------------------------------------
        String javaVersion    = System.getProperty("java.version");   // e.g. "21.0.3"
        String javaVendor     = System.getProperty("java.vendor");    // e.g. "Oracle Corporation"
        String operatingSystem = System.getProperty("os.name");       // e.g. "Windows 11"
        String osVersion      = System.getProperty("os.version");     // e.g. "10.0"
        String osArch         = System.getProperty("os.arch");        // e.g. "amd64"
        String userDir        = System.getProperty("user.dir");       // current working directory

        // ----------------------------------------------------------------
        // 2. Read runtime information
        //    Runtime.getRuntime() returns the single Runtime object
        //    that represents the running JVM.
        //    Memory values are in BYTES — we convert to MB for readability.
        // ----------------------------------------------------------------
        Runtime runtime = Runtime.getRuntime();

        int    processors    = runtime.availableProcessors();  // number of CPU cores visible to JVM
        long   maxMemory     = runtime.maxMemory();            // maximum heap the JVM will ever use
        long   totalMemory   = runtime.totalMemory();          // current heap size allocated by JVM
        long   freeMemory    = runtime.freeMemory();           // free memory inside current heap
        long   usedMemory    = totalMemory - freeMemory;       // calculated: used = total - free

        // Convert bytes to megabytes (1 MB = 1,048,576 bytes = 1024 * 1024)
        long maxMemoryMB  = maxMemory  / (1024 * 1024);
        long freeMemoryMB = freeMemory / (1024 * 1024);
        long usedMemoryMB = usedMemory / (1024 * 1024);

        // ----------------------------------------------------------------
        // 3. Print the platform information in a readable format
        // ----------------------------------------------------------------
        System.out.println();
        System.out.println("===== Java Platform Information =====");
        System.out.println("Java Version       : " + javaVersion);
        System.out.println("Java Vendor        : " + javaVendor);
        System.out.println("Operating System   : " + operatingSystem + " (v" + osVersion + ")");
        System.out.println("OS Architecture    : " + osArch);
        System.out.println("Working Directory  : " + userDir);
        System.out.println("-------------------------------------");
        System.out.println("Processors (cores) : " + processors);
        System.out.println("Maximum Heap       : " + maxMemoryMB  + " MB");
        System.out.println("Used Heap          : " + usedMemoryMB + " MB");
        System.out.println("Free Heap          : " + freeMemoryMB + " MB");
        System.out.println("=====================================");
        System.out.println();

        // ----------------------------------------------------------------
        // 4. Quick JVM flow reminder (printed to console for awareness)
        // ----------------------------------------------------------------
        System.out.println("--- JVM Execution Flow (Quick Reference) ---");
        System.out.println("  .java  -> javac -> .class (bytecode)");
        System.out.println("  .class -> JVM -> Class Loader -> Runtime Data Areas");
        System.out.println("  Runtime Data Areas -> Execution Engine");
        System.out.println("  Execution Engine -> Interpreter / JIT -> Machine Code");
        System.out.println("--------------------------------------------");
        System.out.println();
    }
}
