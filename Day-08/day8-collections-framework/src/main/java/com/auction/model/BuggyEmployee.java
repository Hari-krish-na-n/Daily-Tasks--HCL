package com.auction.model;

import java.util.Objects;

/**
 * Employee entity with a deliberate bug:
 * Overrides equals() comparing employeeId, but FAILS to override hashCode()!
 *
 * It inherits hashCode() from java.lang.Object (memory/identity based hash).
 * When two distinct instances e1 and e2 have the exact same employeeId:
 * e1.equals(e2) == true
 * but e1.hashCode() != e2.hashCode() with near certainty!
 *
 * This contract violation causes Hash-based collections (HashMap, HashSet)
 * to place e1 and e2 into different hash buckets, resulting in:
 * - Duplicate elements in a HashSet (when set should contain unique elements)
 * - Map.get(e2) returning null even though e1 is in the map with the same logical key!
 */
public class BuggyEmployee {

    private int employeeId;
    private String name;
    private String department;
    private double salary;

    public BuggyEmployee(int employeeId, String name, String department, double salary) {
        this.employeeId = employeeId;
        this.name = name != null ? name.trim() : "";
        this.department = department != null ? department.trim() : "";
        this.salary = salary;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }

    /**
     * Overrides equals(): Checks only employeeId.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BuggyEmployee that = (BuggyEmployee) o;
        return employeeId == that.employeeId;
    }

    // MISSING hashCode()!
    // Leaves default Object.hashCode() which computes hash from internal memory address.

    @Override
    public String toString() {
        return String.format("BuggyEmployee[ID=%d, Name='%s', Hash=%d]",
                employeeId, name, System.identityHashCode(this));
    }
}
