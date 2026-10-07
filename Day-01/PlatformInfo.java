/**
 * PlatformInfo.java
 * -----------------
 * Day 1 — Java Platform Basics
 *
 * This standalone program prints information about the Java platform at runtime.
 * Can be compiled and executed directly from terminal:
 *
 *   javac PlatformInfo.java
 *   java PlatformInfo
 *
 * Bytecode inspection:
 *   javap -c PlatformInfo
 *   java -verbose:class PlatformInfo
 */
public class PlatformInfo {

    public static void main(String[] args) {

        // 1. Read system properties
        String javaVersion     = System.getProperty("java.version");
        String javaVendor      = System.getProperty("java.vendor");
        String operatingSystem = System.getProperty("os.name");
        String osVersion       = System.getProperty("os.version");
        String osArch          = System.getProperty("os.arch");
        String userDir         = System.getProperty("user.dir");

        // 2. Read runtime information
        Runtime runtime = Runtime.getRuntime();

        int  processors  = runtime.availableProcessors();
        long maxMemory   = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory  = runtime.freeMemory();
        long usedMemory  = totalMemory - freeMemory;

        // Convert bytes to MB
        long maxMemoryMB  = maxMemory  / (1024 * 1024);
        long freeMemoryMB = freeMemory / (1024 * 1024);
        long usedMemoryMB = usedMemory / (1024 * 1024);

        // 3. Print the platform information
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

        // 4. Quick JVM execution flow reference
        System.out.println("--- JVM Execution Flow (Quick Reference) ---");
        System.out.println("  .java  -> javac -> .class (bytecode)");
        System.out.println("  .class -> JVM -> Class Loader -> Runtime Data Areas");
        System.out.println("  Runtime Data Areas -> Execution Engine");
        System.out.println("  Execution Engine -> Interpreter / JIT -> Machine Code");
        System.out.println("--------------------------------------------");
        System.out.println();
    }
}
