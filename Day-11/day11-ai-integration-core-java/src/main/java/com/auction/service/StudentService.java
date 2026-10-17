package com.auction.service;

import com.auction.model.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * StudentService manages student registration and records.
 *
 * Implemented and critically reviewed during Day 11 AI Integration Lab:
 * - Uses LinkedHashMap<Integer, Student> internally to provide O(1) lookups by ID
 *   while maintaining deterministic insertion order.
 * - Enforces rigorous input validations (null checks, positive IDs, non-blank fields).
 * - Detects and rejects duplicate student IDs on registration.
 * - Returns immutable unmodifiable collections via Collections.unmodifiableList() to protect internal state.
 * - Safely handles missing records and returns clear boolean or Optional results instead of throwing or swallowing nulls.
 */
public class StudentService {

    // Primary store: studentId -> Student (preserves insertion order and allows O(1) lookup)
    private final Map<Integer, Student> studentMap = new LinkedHashMap<>();

    /**
     * Adds a new student to the service.
     *
     * @param student the student to add, must not be null
     * @throws IllegalArgumentException if student is null or a student with the same ID already exists
     */
    public synchronized void addStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Cannot add a null student.");
        }
        int id = student.getStudentId();
        if (studentMap.containsKey(id)) {
            throw new IllegalArgumentException("Student with ID " + id + " already exists in the system.");
        }
        studentMap.put(id, student);
    }

    /**
     * Finds a student by ID.
     *
     * @param studentId the student's ID
     * @return Optional containing the Student if found, or Optional.empty()
     */
    public synchronized Optional<Student> findStudentById(int studentId) {
        if (studentId <= 0) {
            return Optional.empty();
        }
        return Optional.ofNullable(studentMap.get(studentId));
    }

    /**
     * Safely retrieves all registered students.
     * Protects internal state by returning an unmodifiable copy.
     *
     * @return unmodifiable List of students
     */
    public synchronized List<Student> getAllStudents() {
        return Collections.unmodifiableList(new ArrayList<>(studentMap.values()));
    }

    /**
     * Updates an existing student's name and email.
     *
     * @param studentId the ID of the student to update
     * @param newName   the updated name (must not be blank)
     * @param newEmail  the updated email (must be valid)
     * @return true if the student was found and updated, false if the student does not exist
     * @throws IllegalArgumentException if newName or newEmail are invalid
     */
    public synchronized boolean updateStudent(int studentId, String newName, String newEmail) {
        if (studentId <= 0) {
            throw new IllegalArgumentException("Invalid student ID: " + studentId);
        }
        Student existing = studentMap.get(studentId);
        if (existing == null) {
            return false;
        }
        // Validate and apply changes
        existing.setName(newName);
        existing.setEmail(newEmail);
        return true;
    }

    /**
     * Removes a student by ID.
     *
     * @param studentId ID of the student to remove
     * @return true if the student existed and was removed, false otherwise
     */
    public synchronized boolean removeStudent(int studentId) {
        if (studentId <= 0) {
            return false;
        }
        return studentMap.remove(studentId) != null;
    }

    /**
     * Returns the total number of students registered.
     */
    public synchronized int getStudentCount() {
        return studentMap.size();
    }

    /**
     * Clears all student records (useful for clean test setups).
     */
    public synchronized void clear() {
        studentMap.clear();
    }
}
