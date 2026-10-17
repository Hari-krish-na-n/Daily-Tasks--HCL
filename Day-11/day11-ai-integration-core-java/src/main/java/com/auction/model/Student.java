package com.auction.model;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Student domain model for Day 11 AI Integration lab.
 *
 * Designed as a clean Java POJO following repository conventions:
 * - Private fields for studentId, name, email.
 * - Validation on constructor and setters (non-null, non-blank, email format).
 * - Full encapsulation.
 * - Value-based equals() and hashCode() on unique studentId.
 * - Meaningful toString() representation.
 */
public class Student {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private int studentId;
    private String name;
    private String email;

    public Student() {
    }

    public Student(int studentId, String name, String email) {
        setStudentId(studentId);
        setName(name);
        setEmail(email);
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        if (studentId <= 0) {
            throw new IllegalArgumentException("Student ID must be a strictly positive integer. Given: " + studentId);
        }
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be null or blank.");
        }
        this.name = name.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Student email cannot be null or blank.");
        }
        String cleanEmail = email.trim();
        if (!EMAIL_PATTERN.matcher(cleanEmail).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
        this.email = cleanEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return studentId == student.studentId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }

    @Override
    public String toString() {
        return String.format("Student[ID=%d, Name='%s', Email='%s']", studentId, name, email);
    }
}
