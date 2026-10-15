import com.auction.model.BuggyEmployee;
import com.auction.model.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests verifying Day 8 requirements for EmployeeDirectory:
 * - Map lookup (existing and missing ID)
 * - Frequency counting using merge()
 * - Employee ordering by ID (Comparable)
 * - Salary-descending sorting and tie-breaking by name
 * - Top-five output when there are fewer than five employees
 * - equals() / hashCode() contract demonstration
 */
public class EmployeeDirectoryTest {

    @Test
    @DisplayName("Map Lookup: Existing ID returns employee, missing ID returns null or default")
    void testMapLookupExistingAndMissing() {
        Map<Integer, Employee> directory = new HashMap<>();
        Employee e1 = new Employee(101, "Aarav", "Tech", 100000.0);
        directory.put(101, e1);

        assertTrue(directory.containsKey(101));
        assertEquals(e1, directory.get(101));

        assertFalse(directory.containsKey(999));
        assertNull(directory.get(999));

        Employee fallback = directory.getOrDefault(999, new Employee(0, "Fallback", "None", 0.0));
        assertEquals(0, fallback.getEmployeeId());
        assertEquals("Fallback", fallback.getName());
    }

    @Test
    @DisplayName("Department frequency counting using Map.merge()")
    void testDepartmentFrequencyCounting() {
        List<Employee> list = List.of(
                new Employee(1, "A", "IT", 50000),
                new Employee(2, "B", "HR", 45000),
                new Employee(3, "C", "IT", 60000),
                new Employee(4, "D", "Finance", 70000),
                new Employee(5, "E", "IT", 80000),
                new Employee(6, "F", "HR", 55000)
        );

        Map<String, Integer> counts = EmployeeDirectory.countDepartmentFrequencies(list);

        assertEquals(3, counts.get("IT"));
        assertEquals(2, counts.get("HR"));
        assertEquals(1, counts.get("Finance"));
        assertNull(counts.get("Marketing"));
    }

    @Test
    @DisplayName("Employee natural ordering by ID via Comparable<Employee>")
    void testEmployeeOrderingById() {
        List<Employee> list = new ArrayList<>(List.of(
                new Employee(105, "C", "IT", 50000),
                new Employee(101, "A", "HR", 45000),
                new Employee(103, "B", "IT", 60000)
        ));

        Collections.sort(list);

        assertEquals(101, list.get(0).getEmployeeId());
        assertEquals(103, list.get(1).getEmployeeId());
        assertEquals(105, list.get(2).getEmployeeId());
    }

    @Test
    @DisplayName("Salary descending and tie-breaking by name ascending")
    void testSalaryDescendingAndTieBreakingByName() {
        Employee e1 = new Employee(1, "Vikram", "IT", 90000);
        Employee e2 = new Employee(2, "Amit", "HR", 90000);    // Tie on salary, Amit comes first alphabetically
        Employee e3 = new Employee(3, "Zoya", "Finance", 120000); // Highest salary

        List<Employee> list = new ArrayList<>(List.of(e1, e2, e3));
        List<Employee> top = EmployeeDirectory.getTopSalaryEmployees(list, 3);

        assertEquals(3, top.get(0).getEmployeeId()); // Zoya (120k)
        assertEquals(2, top.get(1).getEmployeeId()); // Amit (90k, 'A' < 'V')
        assertEquals(1, top.get(2).getEmployeeId()); // Vikram (90k)
    }

    @Test
    @DisplayName("Top-five output safely handles fewer than 5 employees")
    void testTopFiveOutputWhenFewerThanFiveEmployees() {
        List<Employee> list = List.of(
                new Employee(1, "A", "IT", 50000),
                new Employee(2, "B", "HR", 60000),
                new Employee(3, "C", "Finance", 70000)
        );

        List<Employee> result = EmployeeDirectory.getTopSalaryEmployees(list, 5);

        assertEquals(3, result.size());
        assertEquals(3, result.get(0).getEmployeeId()); // 70k first
        assertEquals(2, result.get(1).getEmployeeId()); // 60k second
        assertEquals(1, result.get(2).getEmployeeId()); // 50k third
    }

    @Test
    @DisplayName("equals() and hashCode() contract: Bug reproduction and fix verification")
    void testEqualsAndHashCodeContract() {
        // 1. BuggyEmployee: equals() true, hashCode() different
        BuggyEmployee b1 = new BuggyEmployee(555, "Test", "QA", 40000);
        BuggyEmployee b2 = new BuggyEmployee(555, "Test", "QA", 40000);

        assertTrue(b1.equals(b2));
        assertNotEquals(b1.hashCode(), b2.hashCode(), "Missing hashCode results in unequal identity hash codes");

        Set<BuggyEmployee> buggySet = new HashSet<>();
        buggySet.add(b1);
        buggySet.add(b2);
        // Duplicate added because of different hash buckets!
        assertEquals(2, buggySet.size(), "Set contains 2 elements despite equal keys due to missing hashCode");

        Map<BuggyEmployee, String> buggyMap = new HashMap<>();
        buggyMap.put(b1, "Exists");
        assertNull(buggyMap.get(b2), "Lookup with equal object returns null due to missing hashCode");

        // 2. Correct Employee: equals() true, hashCode() identical
        Employee c1 = new Employee(555, "Test", "QA", 40000);
        Employee c2 = new Employee(555, "Test", "QA", 40000);

        assertTrue(c1.equals(c2));
        assertEquals(c1.hashCode(), c2.hashCode());

        Set<Employee> correctSet = new HashSet<>();
        correctSet.add(c1);
        correctSet.add(c2);
        assertEquals(1, correctSet.size(), "Set correctly de-duplicates equal objects");

        Map<Employee, String> correctMap = new HashMap<>();
        correctMap.put(c1, "Exists");
        assertEquals("Exists", correctMap.get(c2), "Lookup succeeds with equal object key");
    }
}
