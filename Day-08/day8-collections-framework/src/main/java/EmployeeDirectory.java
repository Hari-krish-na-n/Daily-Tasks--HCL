import com.auction.model.BuggyEmployee;
import com.auction.model.Employee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Main demonstration class for Day 8 Collections Framework — Part 2.
 *
 * Demonstrates:
 * A. Map implementations:
 *    - HashMap for fast O(1) lookup
 *    - LinkedHashMap for insertion-order preservation
 *    - TreeMap for naturally sorted key order
 *    - Core map operations: put(), get(), containsKey(), remove(), getOrDefault()
 *    - Frequency counting using Map.merge()
 *
 * B. Comparable vs. Comparator:
 *    - Comparable<Employee> (natural order by ID)
 *    - Salary descending Comparator
 *    - Chained Comparator: salary descending then name ascending
 *    - Top-5 salary report handling fewer than five safely
 *
 * C. equals() and hashCode() contract:
 *    - Reproducible missing-hashCode() bug demonstration
 *    - Corrected implementation using both equals() and hashCode()
 */
public class EmployeeDirectory {

    public static void main(String[] args) {
        System.out.println("===============================================================================");
        System.out.println("         DAY 8: COLLECTIONS FRAMEWORK PART 2 - EMPLOYEE DIRECTORY              ");
        System.out.println("===============================================================================\n");

        demonstrateMapImplementations();
        demonstrateComparatorsAndReports();
        demonstrateEqualsAndHashCodeContract();
    }

    // =========================================================================
    // SECTION A: MAP IMPLEMENTATIONS
    // =========================================================================

    public static void demonstrateMapImplementations() {
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("1. MAP IMPLEMENTATIONS: HashMap, LinkedHashMap, and TreeMap");
        System.out.println("-------------------------------------------------------------------------------");

        // 1. HashMap for fast employee ID lookup (unordered, O(1) average time)
        Map<Integer, Employee> hashMap = new HashMap<>();
        // 2. LinkedHashMap for preserving insertion order
        Map<Integer, Employee> linkedMap = new LinkedHashMap<>();
        // 3. TreeMap for sorted key order (Red-Black tree, O(log N) lookup)
        Map<Integer, Employee> treeMap = new TreeMap<>();

        Employee[] sampleEmployees = {
                new Employee(104, "Vikram Malhotra", "Engineering", 95000.0),
                new Employee(101, "Aarav Sharma", "Engineering", 120000.0),
                new Employee(105, "Pooja Hegde", "Marketing", 85000.0),
                new Employee(102, "Meera Nair", "Finance", 110000.0),
                new Employee(103, "Rohan Das", "Finance", 95000.0)
        };

        for (Employee emp : sampleEmployees) {
            hashMap.put(emp.getEmployeeId(), emp);
            linkedMap.put(emp.getEmployeeId(), emp);
            treeMap.put(emp.getEmployeeId(), emp);
        }

        System.out.println("Insertion Order of IDs: [104, 101, 105, 102, 103]");
        System.out.println("a) HashMap KeySet (Unordered / Hash-bucket order): " + hashMap.keySet());
        System.out.println("b) LinkedHashMap KeySet (Preserves exact insertion order): " + linkedMap.keySet());
        System.out.println("c) TreeMap KeySet (Automatically sorted ascending by ID): " + treeMap.keySet());

        // Map core operations
        System.out.println("\n--- Core Map Methods Demonstration ---");
        int targetId = 101;
        System.out.println("Map.get(" + targetId + "): " + hashMap.get(targetId));
        System.out.println("Map.containsKey(" + targetId + "): " + hashMap.containsKey(targetId));
        System.out.println("Map.containsKey(999): " + hashMap.containsKey(999));

        Employee missingOrFallback = hashMap.getOrDefault(999, new Employee(0, "Guest/Temporary", "None", 0.0));
        System.out.println("Map.getOrDefault(999, fallback): " + missingOrFallback);

        Employee removed = hashMap.remove(105);
        System.out.println("Map.remove(105): Successfully removed " + (removed != null ? removed.getName() : "none"));
        System.out.println("Map.containsKey(105) after removal: " + hashMap.containsKey(105));

        // Frequency counting using Map.merge()
        System.out.println("\n--- Department Frequency Counting using Map.merge() ---");
        Map<String, Integer> deptCounts = countDepartmentFrequencies(List.of(sampleEmployees));
        deptCounts.forEach((dept, count) ->
                System.out.printf("   Department: %-15s | Count: %d%n", dept, count)
        );
        System.out.println();
    }

    public static Map<String, Integer> countDepartmentFrequencies(List<Employee> employees) {
        Map<String, Integer> counts = new TreeMap<>(); // sorted by department name
        if (employees != null) {
            for (Employee emp : employees) {
                if (emp != null && emp.getDepartment() != null) {
                    // Map.merge(key, 1, Integer::sum):
                    // If key not present, associates with 1.
                    // If key present, invokes remapping function (oldVal, 1) -> oldVal + 1
                    counts.merge(emp.getDepartment(), 1, Integer::sum);
                }
            }
        }
        return counts;
    }

    // =========================================================================
    // SECTION B: COMPARABLE & COMPARATOR
    // =========================================================================

    public static void demonstrateComparatorsAndReports() {
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("2. COMPARABLE AND COMPARATOR DEMONSTRATIONS");
        System.out.println("-------------------------------------------------------------------------------");

        List<Employee> employees = new ArrayList<>(List.of(
                new Employee(105, "Ananya Roy", "Engineering", 95000.0),
                new Employee(102, "Meera Nair", "Finance", 125000.0),
                new Employee(108, "Amitabh Sen", "HR", 75000.0),
                new Employee(101, "Aarav Sharma", "Engineering", 125000.0), // Tie on salary with Meera
                new Employee(104, "Vikram Malhotra", "Engineering", 95000.0), // Tie on salary with Ananya
                new Employee(103, "Rohan Das", "Finance", 110000.0)
        ));

        // 1. Natural Ordering via Comparable<Employee> (sorted by employeeId ascending)
        List<Employee> naturalOrderList = new ArrayList<>(employees);
        Collections.sort(naturalOrderList);
        System.out.println("a) Natural Ordering via Comparable (by employeeId ascending):");
        naturalOrderList.forEach(e -> System.out.println("   " + e));

        // 2. Custom Comparator: Salary descending
        Comparator<Employee> salaryDescComparator = Comparator.comparingDouble(Employee::getSalary).reversed();

        // 3. Chained Comparator: Salary descending, then Name ascending on salary tie
        Comparator<Employee> chainedComparator = Comparator
                .comparingDouble(Employee::getSalary).reversed()
                .thenComparing(Employee::getName);

        List<Employee> chainedList = new ArrayList<>(employees);
        chainedList.sort(chainedComparator);
        System.out.println("\nb) Chained Sorting (Salary Descending -> Name Ascending tie-breaker):");
        chainedList.forEach(e -> System.out.println("   " + e));

        // 4. Top-Five Salary Report (handling lists with fewer or more than 5 employees safely)
        System.out.println("\nc) Top-5 Salary Report (Full list with 6 employees):");
        List<Employee> top5Full = getTopSalaryEmployees(employees, 5);
        for (int i = 0; i < top5Full.size(); i++) {
            System.out.printf("   Rank #%d: %s%n", i + 1, top5Full.get(i));
        }

        System.out.println("\nd) Top-5 Salary Report with Small List (Safely handles list of only 3 employees):");
        List<Employee> smallList = List.of(
                new Employee(201, "Neha Gupta", "Marketing", 80000.0),
                new Employee(202, "Karan Johar", "Media", 90000.0),
                new Employee(203, "Priya Paul", "HR", 70000.0)
        );
        List<Employee> top5Small = getTopSalaryEmployees(smallList, 5);
        for (int i = 0; i < top5Small.size(); i++) {
            System.out.printf("   Rank #%d: %s%n", i + 1, top5Small.get(i));
        }
        System.out.println();
    }

    /**
     * Safely returns the top N employees by salary descending (tie-breaker by name ascending).
     * Handles null or lists with fewer than N items safely without IndexOutOfBoundsException.
     */
    public static List<Employee> getTopSalaryEmployees(List<Employee> employees, int n) {
        if (employees == null || employees.isEmpty() || n <= 0) {
            return Collections.emptyList();
        }
        List<Employee> sorted = new ArrayList<>(employees);
        sorted.sort(Comparator.comparingDouble(Employee::getSalary).reversed()
                .thenComparing(Employee::getName));
        int limit = Math.min(n, sorted.size());
        return new ArrayList<>(sorted.subList(0, limit));
    }

    // =========================================================================
    // SECTION C: equals() AND hashCode() CONTRACT
    // =========================================================================

    public static void demonstrateEqualsAndHashCodeContract() {
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("3. equals() AND hashCode() CONTRACT DEMONSTRATION");
        System.out.println("-------------------------------------------------------------------------------");

        System.out.println("Contract Rule: If o1.equals(o2) is true, then o1.hashCode() MUST EQUAL o2.hashCode().");
        System.out.println("Why? Hash collections (HashMap, HashSet) first calculate bucket index via hashCode().");
        System.out.println("If two equal objects have different hashCodes, they go into different buckets, and");
        System.out.println("o1.equals(o2) will NEVER even be called during lookup!\n");

        // 1. REPRODUCIBLE BUG DEMO: Missing hashCode()
        System.out.println("--- [REPRODUCIBLE BUG] BuggyEmployee (Overrides equals, Missing hashCode) ---");
        BuggyEmployee b1 = new BuggyEmployee(1001, "Arun Kumar", "Sales", 60000.0);
        BuggyEmployee b2 = new BuggyEmployee(1001, "Arun Kumar", "Sales", 60000.0);

        System.out.println("b1: " + b1);
        System.out.println("b2: " + b2);
        System.out.println("b1.equals(b2): " + b1.equals(b2) + " (Logical equality holds)");
        System.out.println("b1.hashCode() == b2.hashCode(): " + (b1.hashCode() == b2.hashCode())
                + " (b1 hash=" + b1.hashCode() + ", b2 hash=" + b2.hashCode() + ") -> CONTRACT VIOLATED!");

        // Failure in HashSet:
        Set<BuggyEmployee> buggySet = new HashSet<>();
        buggySet.add(b1);
        buggySet.add(b2);
        System.out.println("HashSet size after adding b1 and b2 (Expected 1 for Set, Got): " + buggySet.size());
        System.out.println("HashSet contents: Duplicate equal elements present! -> " + buggySet.size());

        // Failure in HashMap:
        Map<BuggyEmployee, String> buggyMap = new HashMap<>();
        buggyMap.put(b1, "Sales Executive Record");
        System.out.println("HashMap.get(b1): " + buggyMap.get(b1));
        System.out.println("HashMap.get(b2) (Searching with equal key b2): " + buggyMap.get(b2) + " -> BUG: returned NULL!");

        // 2. CORRECTED IMPLEMENTATION DEMO:
        System.out.println("\n--- [CORRECTED] Employee (Overrides both equals and hashCode on ID) ---");
        Employee c1 = new Employee(1001, "Arun Kumar", "Sales", 60000.0);
        Employee c2 = new Employee(1001, "Arun Kumar", "Sales", 60000.0);

        System.out.println("c1.equals(c2): " + c1.equals(c2));
        System.out.println("c1.hashCode() == c2.hashCode(): " + (c1.hashCode() == c2.hashCode())
                + " (c1 hash=" + c1.hashCode() + ", c2 hash=" + c2.hashCode() + ") -> CONTRACT SATISFIED!");

        Set<Employee> correctSet = new HashSet<>();
        correctSet.add(c1);
        correctSet.add(c2);
        System.out.println("HashSet size after adding c1 and c2 (Expected 1): " + correctSet.size());

        Map<Employee, String> correctMap = new HashMap<>();
        correctMap.put(c1, "Sales Executive Record");
        System.out.println("HashMap.get(c2) (Searching with equal key c2): " + correctMap.get(c2) + " -> SUCCESS!");
        System.out.println("===============================================================================\n");
    }
}
