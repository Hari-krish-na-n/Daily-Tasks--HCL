package com.auction.service;

import com.auction.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Student POJO and StudentService Unit Tests")
public class StudentServiceTest {

    private StudentService service;

    @BeforeEach
    void setUp() {
        service = new StudentService();
    }

    @Test
    @DisplayName("POJO Validation - Valid student instantiation")
    void testValidStudentCreation() {
        Student student = new Student(101, "Hari", "hari@training.hcl.com");
        assertEquals(101, student.getStudentId());
        assertEquals("Hari", student.getName());
        assertEquals("hari@training.hcl.com", student.getEmail());
    }

    @Test
    @DisplayName("POJO Validation - Invalid IDs and invalid email throw IllegalArgumentException")
    void testInvalidStudentCreation() {
        assertThrows(IllegalArgumentException.class, () -> new Student(-1, "Hari", "hari@test.com"));
        assertThrows(IllegalArgumentException.class, () -> new Student(101, "", "hari@test.com"));
        assertThrows(IllegalArgumentException.class, () -> new Student(101, "Hari", "not-an-email"));
    }

    @Test
    @DisplayName("POJO Validation - equals and hashCode based on studentId")
    void testEqualsAndHashCode() {
        Student s1 = new Student(101, "Hari", "hari@test.com");
        Student s2 = new Student(101, "Hari Krishnan", "different@test.com");
        Student s3 = new Student(102, "Hari", "hari@test.com");

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
        assertNotEquals(s1, s3);
    }

    @Test
    @DisplayName("StudentService - Add student and find by ID")
    void testAddAndFindStudent() {
        Student student = new Student(1, "Ravi Kumar", "ravi@test.com");
        service.addStudent(student);

        Optional<Student> found = service.findStudentById(1);
        assertTrue(found.isPresent());
        assertEquals("Ravi Kumar", found.get().getName());
    }

    @Test
    @DisplayName("StudentService - Reject duplicate student IDs")
    void testDuplicateStudentIdRejected() {
        Student s1 = new Student(1, "Ravi", "ravi@test.com");
        Student s2 = new Student(1, "Ravi Duplicate", "ravi2@test.com");

        service.addStudent(s1);
        assertThrows(IllegalArgumentException.class, () -> service.addStudent(s2));
        assertEquals(1, service.getStudentCount());
    }

    @Test
    @DisplayName("StudentService - Safe collection access prevents internal state corruption")
    void testSafeCollectionAccess() {
        service.addStudent(new Student(1, "Ravi", "ravi@test.com"));
        List<Student> students = service.getAllStudents();

        assertEquals(1, students.size());
        assertThrows(UnsupportedOperationException.class, () -> students.add(new Student(2, "Hacker", "hacker@test.com")));
        assertEquals(1, service.getStudentCount());
    }

    @Test
    @DisplayName("StudentService - Update student success and missing record handling")
    void testUpdateStudent() {
        service.addStudent(new Student(1, "Original", "orig@test.com"));

        boolean updated = service.updateStudent(1, "Updated Name", "updated@test.com");
        assertTrue(updated);
        assertEquals("Updated Name", service.findStudentById(1).get().getName());

        boolean notFound = service.updateStudent(999, "Ghost", "ghost@test.com");
        assertFalse(notFound);
    }

    @Test
    @DisplayName("StudentService - Remove student")
    void testRemoveStudent() {
        service.addStudent(new Student(1, "Ravi", "ravi@test.com"));
        assertTrue(service.removeStudent(1));
        assertFalse(service.removeStudent(1));
        assertEquals(0, service.getStudentCount());
    }
}
