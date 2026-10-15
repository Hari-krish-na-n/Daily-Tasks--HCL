package com.auction.model;

import java.util.Objects;

/**
 * Employee entity used in EmployeeDirectory to demonstrate:
 * 1. Comparable<Employee> (natural order by employeeId)
 * 2. Custom Comparators (by salary descending, chaining with name ascending)
 * 3. Contract between equals() and hashCode()
 */
public class Employee implements Comparable<Employee> {

    private int employeeId;
    private String name;
    private String department;
    private double salary;

    public Employee(int employeeId, String name, String department, double salary) {
        this.employeeId = employeeId;
        this.name = name != null ? name.trim() : "";
        this.department = department != null ? department.trim() : "";
        this.salary = salary;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name.trim() : "";
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department != null ? department.trim() : "";
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    /**
     * Natural ordering for Employee: Ascending order of employeeId.
     * Fulfills Comparable<Employee>.
     */
    @Override
    public int compareTo(Employee other) {
        if (other == null) {
            return 1;
        }
        return Integer.compare(this.employeeId, other.employeeId);
    }

    /**
     * Correct equals() implementation: compares by unique employeeId.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return employeeId == employee.employeeId;
    }

    /**
     * Correct hashCode() implementation: hashes the employeeId matching equals().
     */
    @Override
    public int hashCode() {
        return Objects.hash(employeeId);
    }

    @Override
    public String toString() {
        return String.format("Employee[ID=%d, Name='%s', Dept='%s', Salary=INR %,.2f]",
                employeeId, name, department, salary);
    }
}
